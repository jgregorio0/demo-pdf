package dev.jgregorio.demo.demo_template.service;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

@Service
public class PdfService {

    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);

    public void mergePdf(String pdfsDir, OutputStream os) throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource(pdfsDir);
        if (resource == null) {
            logger.error("Directory not found: {}", pdfsDir);
            throw new FileNotFoundException("Directory not found: " + pdfsDir);
        }
        try {
            PDFMergerUtility merger = new PDFMergerUtility();
            merger.setDestinationStream(os);
            final File folder = new File(resource.toURI());
            final File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

            if (files != null) {
                for (File file : files) {
                    logger.info("Adding file to PDF: {}", file.getName());
                    merger.addSource(file);
                }
                merger.mergeDocuments(createTempFileOnlyStreamCache());
            }
        } catch (Exception e) {
            logger.error("Error creating merged PDF", e);
            throw new IOException("Error creating merged PDF", e);
        }
    }
}
