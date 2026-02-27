package dev.jgregorio.demo.demo_template.service;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfServiceTest {

    @Test
    void testMergePdf() throws IOException {
        // Arrange
        PdfService pdfService = new PdfService();
        final String pdfsDir = "templates/pdfs";

        // Act
        try (FileOutputStream fos = new FileOutputStream("test.pdf")) {
            pdfService.mergePdf(pdfsDir, fos);
        }
        File file = new File("test.pdf");

        // Assert
        assertTrue(file.exists(), "El archivo PDF debería existir");
    }
}
