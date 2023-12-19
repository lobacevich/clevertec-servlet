package by.clevertec.lobacevich.service.impl;

import by.clevertec.lobacevich.data.UserDto;
import by.clevertec.lobacevich.entity.User;

import java.time.LocalDate;

public class UserServiceTestData {

    public static User getUser() {
        return User.builder()
                .id(1L)
                .firstname("Alex")
                .lastname("Murfhy")
                .dateOfBirth(LocalDate.of(1997, 05, 17))
                .email("1234@gmail.com")
                .build();
    }

    public static UserDto getUserDto() {
        return UserDto.builder()
                .id(1)
                .firstname("Alex")
                .lastname("Murfhy")
                .dateOfBirth(LocalDate.of(1997, 05, 17))
                .email("1234@gmail.com")
                .build();
    }

    public static User getUserIdNull() {
        return User.builder()
                .id(null)
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
}
