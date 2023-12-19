package by.clevertec.lobacevich.validator.impl;

import by.clevertec.lobacevich.data.UserDto;
import by.clevertec.lobacevich.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserDtoValidatorTest {

    private final UserDtoValidator validator = UserDtoValidator.getINSTANCE();
    private final UserDto userDto = ValidatorTestData.getUserDto();
    private final UserDto userDtoIdNull = ValidatorTestData.getUserDtoIdNull();

    @Test
    void validateToCreateShouldNotThrowException() {
        validator.validateToCreate(userDtoIdNull);
    }

    @Test
    void validateToCreateShouldThrowException() {
        assertThrows(ValidationException.class, () -> validator.validateToCreate(userDto));
    }

    @Test
    void validateToUpdateShouldNotThrowException() {
        validator.validateToUpdate(userDto);
    }

    @Test
    void validateToUpdateShouldThrowException() {
        assertThrows(ValidationException.class, () -> validator.validateToUpdate(userDtoIdNull));
    }

    @Test
    void validateToUpdateShouldThrowExceptionIncorrectEmail() {
        assertThrows(ValidationException.class, () ->
                validator.validateToUpdate(ValidatorTestData.getUserDtoIncorrectEmail()));
    }
}