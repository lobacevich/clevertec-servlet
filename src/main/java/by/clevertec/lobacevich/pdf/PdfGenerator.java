package by.clevertec.lobacevich.pdf;

import by.clevertec.lobacevich.dto.UserDto;

public interface PdfGenerator {

    void createPdf(UserDto userDto);
}
