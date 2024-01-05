package by.clevertec.lobacevich.pdf.impl;

import by.clevertec.lobacevich.dto.UserDto;
import by.clevertec.lobacevich.exception.PdfException;
import by.clevertec.lobacevich.pdf.PdfGenerator;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Component
public class UserPdfGenerator implements PdfGenerator {

    private static final String BACKGROUND_PATH = "/pdf/Clevertec_Template.pdf";
    private static final String OUTPUT_PATH_PART = "src/main/resources/pdf/";

    @Override
    public void createPdf(UserDto userDto) {
        if (userDto == null) {
            return;
        }
        createDirectoryIfNotExists();
        String outputPath = OUTPUT_PATH_PART + LocalDate.now() + "/user" + userDto.getId() + ".pdf";
        try (PdfWriter writer = new PdfWriter(outputPath);
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document document = new Document(pdfDocument);
             InputStream backgroundStream = getClass().getResourceAsStream(BACKGROUND_PATH);
             PdfDocument backgroundPdf = new PdfDocument(new PdfReader(backgroundStream))) {
            document.setTopMargin(130f);
            pdfDocument.addPage(backgroundPdf.getFirstPage().copyTo(pdfDocument));
            fillDocument(document, userDto);
        } catch (IOException e) {
            throw new PdfException("Can't create pdf" + e);
        }
    }

    private void createDirectoryIfNotExists() {
        Path path = Paths.get(OUTPUT_PATH_PART + LocalDate.now());
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new PdfException("Can't create directory" + e);
            }
        }
    }

    private void fillDocument(Document document, UserDto userDto) {
        document.add(new Paragraph("information about user with id " + userDto.getId())
                .setTextAlignment(TextAlignment.CENTER));
        List list = new List();
        list.add("firstname: " + userDto.getFirstname());
        list.add("lastname: " + userDto.getLastname());
        list.add("email: " + userDto.getEmail());
        list.add("date of birth: " + userDto.getDateOfBirth());
        document.add(list);
    }
}
