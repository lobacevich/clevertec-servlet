package by.clevertec.lobacevich.validator;

import by.clevertec.lobacevich.data.UserDto;

public interface Validator {

    void validateToCreate(UserDto userDto);

    void validateToUpdate(UserDto userDto);
}
