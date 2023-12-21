package by.clevertec.lobacevich.validator.impl;

import by.clevertec.lobacevich.dto.UserDto;

import java.time.LocalDate;

public class ValidatorTestData {

    public static UserDto getUserDto() {
        return new UserDto(1, "Alex", "Murfhy",
                LocalDate.of(1997, 05, 17), "1234@gmail.com");
    }

    public static UserDto getUserDtoIdNull() {
        return new UserDto(null, "Alex", "Murfhy",
                LocalDate.of(1997, 05, 17), "1234@gmail.com");
    }

    public static UserDto getUserDtoIncorrectEmail() {
        return new UserDto(1, "Alex", "Murfhy",
                LocalDate.of(1997, 05, 17), "1234@g@mail.com");
    }
}
