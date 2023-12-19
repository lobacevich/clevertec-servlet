package by.clevertec.lobacevich.service;

import by.clevertec.lobacevich.data.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    boolean updateUser(UserDto userDto);

    boolean deleteUser(Long id);

    UserDto findUserById(Long id);

    List<UserDto> getAll();
}
