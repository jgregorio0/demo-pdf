package dev.jgregorio.demo.demo_template.controller;

import dev.jgregorio.demo.demo_template.service.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/resources")
public class ResourcesController {

    private static final Logger logger = LoggerFactory.getLogger(ResourcesController.class);
    private static final String DEFAULT_TEMPLATE_PATH = "/templates/test.odt";
    private static final String ZIP_FILENAME = "generated-pdfs.zip";

    private final PdfService pdfService;

    public ResourcesController(final PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/pdf")
    public ResponseEntity<StreamingResponseBody> generatePdf() {
        logger.info("Request to generate PDF");
        final StreamingResponseBody stream = outputStream -> {
            try {
                final String pdfsDir = "templates/pdfs";
                pdfService.mergePdf(pdfsDir, outputStream);
                logger.info("Successfully streamed PDF file");
            } catch (final Exception e) {
                logger.error("Error generating PDF file", e);
                throw new RuntimeException("Error generating PDF file", e);
            }
        };

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", ZIP_FILENAME);

        return ResponseEntity.ok()
                .headers(headers)
                .body(stream);
    }


    @GetMapping("/test")
    public ResponseEntity<Void> test() {
        return ResponseEntity.ok().build();
    }
}
