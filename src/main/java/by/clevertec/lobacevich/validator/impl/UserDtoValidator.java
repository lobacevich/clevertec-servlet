package by.clevertec.lobacevich.validator.impl;

import by.clevertec.lobacevich.data.UserDto;
import by.clevertec.lobacevich.exception.ValidationException;
import by.clevertec.lobacevich.validator.Validator;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserDtoValidator implements Validator {

    private static final UserDtoValidator INSTANCE = new UserDtoValidator();
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private UserDtoValidator() {
    }

    public static UserDtoValidator getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void validateToCreate(UserDto userDto) {
        if (userDto.getId() != null) {
            throw new ValidationException("Validation error: id must be null");
        }
        validateFields(userDto);
    }

    @Override
    public void validateToUpdate(UserDto userDto) {
        if (userDto.getId() == null) {
            throw new ValidationException("Validation error: id must be null");
        }
        validateFields(userDto);
    }

    private void validateFields(UserDto userDto) {
        String firstName = userDto.getFirstname();
        if (firstName == null || firstName.length() < 2) {
            throw new ValidationException("Validation error: incorrect first name");
        }
        String lastName = userDto.getLastname();
        if (lastName == null || lastName.length() < 2) {
            throw new ValidationException("Validation error: field id must be filled");
        }
        int yearOfBirth = userDto.getDateOfBirth().getYear();
        int yearNow = LocalDate.now().getYear();
        if (userDto.getDateOfBirth() == null || yearNow - yearOfBirth > 130 ||
                yearOfBirth > yearNow) {
            throw new ValidationException("Validation error: incorrect date of birth");
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(userDto.getEmail());
        if (!matcher.matches()) {
            throw new ValidationException("Validation error: incorrect e-mail");
        }
    }
}
