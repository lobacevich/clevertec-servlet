package by.clevertec.lobacevich.pdf;

import by.clevertec.lobacevich.data.UserDto;

public interface PdfGenerator {

    void createPdf(UserDto userDto);
}
