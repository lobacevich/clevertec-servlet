package by.clevertec.lobacevich.validator.impl;

import by.clevertec.lobacevich.data.UserDto;

import java.time.LocalDate;

public class ValidatorTestData {

    public static UserDto getUserDto() {
        return UserDto.builder()
                .id(1)
                .firstname("Alex")
                .lastname("Murfhy")
                .dateOfBirth(LocalDate.of(1997, 05, 17))
                .email("1234@gmail.com")
                .build();
    }

    public static UserDto getUserDtoIdNull() {
        return UserDto.builder()
                .id(null)
                .firstname("Alex")
                .lastname("Murfhy")
                .dateOfBirth(LocalDate.of(1997, 05, 17))
                .email("1234@gmail.com")
                .build();
    }

    public static UserDto getUserDtoIncorrectEmail() {
        return UserDto.builder()
                .id(1)
                .firstname("Alex")
                .lastname("Murfhy")
                .dateOfBirth(LocalDate.of(1997, 05, 17))
                .email("1234@g@mail.com")
                .build();
    }
}
