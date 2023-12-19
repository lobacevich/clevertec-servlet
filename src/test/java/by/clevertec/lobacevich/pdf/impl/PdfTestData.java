package by.clevertec.lobacevich.pdf.impl;

import by.clevertec.lobacevich.data.UserDto;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;

public class PdfTestData {

    public static final String OUTPUT_PATH = "src/main/resources/pdf/" + LocalDate.now();

    public static UserDto getUserDto() {
        return UserDto.builder()
                .id(1)
                .firstname("Alex")
                .lastname("Murfhy")
                .dateOfBirth(LocalDate.of(1997, 05, 17))
                .email("1234@gmail.com")
                .build();
    }

    public static String[] getData() {
        return new String[]{"information about user with id 1",
                "- firstname: Alex",
                "- lastname: Murfhy",
                "- email: 1234@gmail.com",
                "- date of birth: 1997-05-17"};
    }

    public static void deleteDirectoryIfExists() {
        File dir = new File(OUTPUT_PATH);
        if (dir.exists()) {
            File[] files = dir.getAbsoluteFile().listFiles();
            if (files != null) {
                Arrays.stream(files)
                        .forEach(File::delete);
            }
            dir.delete();
        }
    }
}
