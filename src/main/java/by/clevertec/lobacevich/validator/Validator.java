package by.clevertec.lobacevich.validator;

import by.clevertec.lobacevich.dto.UserDto;

public interface Validator {

    void validateToCreate(UserDto userDto);

    void validateToUpdate(UserDto userDto);
}
