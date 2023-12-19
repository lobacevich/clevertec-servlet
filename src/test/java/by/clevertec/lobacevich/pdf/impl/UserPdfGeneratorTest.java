package by.clevertec.lobacevich.pdf.impl;

import by.clevertec.lobacevich.pdf.PdfGenerator;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static by.clevertec.lobacevich.pdf.impl.PdfTestData.OUTPUT_PATH;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserPdfGeneratorTest {

    private final PdfGenerator pdfGenerator = UserPdfGenerator.getInstance();

    @Test
    void createPdfShouldCreateDirectory() {
        PdfTestData.deleteDirectoryIfExists();

        pdfGenerator.createPdf(PdfTestData.getUserDto());

        assertTrue(new File(OUTPUT_PATH).exists());
    }

    @Test
    void createPdfShouldComparePdfInformation() throws IOException {
        String[] expected = PdfTestData.getData();

        pdfGenerator.createPdf(PdfTestData.getUserDto());
        try (PdfReader reader = new PdfReader(OUTPUT_PATH + "/user1.pdf");
             PdfDocument pdfDocument = new PdfDocument(reader)) {
            String pdfText = PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage());
            String[] actual = Arrays.stream(pdfText.split("\n"))
                    .map(String::strip)
                    .filter(x -> x.length() > 2)
                    .toArray(String[]::new);

            assertArrayEquals(expected, actual);
        }
    }
}