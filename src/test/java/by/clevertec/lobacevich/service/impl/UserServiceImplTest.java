package by.clevertec.lobacevich.service.impl;

import by.clevertec.lobacevich.dao.UserDao;
import by.clevertec.lobacevich.data.UserDto;
import by.clevertec.lobacevich.entity.User;
import by.clevertec.lobacevich.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper mapper;

    @Mock
    private UserDao dao;

    @Mock
    private Connection connection;

    @InjectMocks
    private UserServiceImpl userService;
    private final User user = UserServiceTestData.getUser();
    private final UserDto userDto = UserServiceTestData.getUserDto();
    private final User userIdNull = UserServiceTestData.getUserIdNull();
    private final UserDto userDtoIdNull = UserServiceTestData.getUserDtoIdNull();

    @Test
    void createUserShouldCreateUser() {
        when(mapper.toUser(userDtoIdNull)).thenReturn(userIdNull);
        when(dao.createUser(userIdNull, connection)).thenReturn(user);
        when(mapper.toUserDto(user)).thenReturn(userDto);

        UserDto actual = userService.createUser(UserServiceTestData.getUserDtoIdNull());

        assertEquals(userDto, actual);
    }

    @Test
    void updateUserShouldUpdateUser() {
        when(mapper.toUser(userDto)).thenReturn(user);
        when(dao.updateUser(user, connection)).thenReturn(true);

        boolean actual = userService.updateUser(userDto);

        assertTrue(actual);
    }

    @Test
    void updateUserShouldNotUpdateUser() {
        when(mapper.toUser(userDto)).thenReturn(user);
        when(dao.updateUser(user, connection)).thenReturn(false);

        boolean actual = userService.updateUser(userDto);

        assertFalse(actual);
    }

    @Test
    void deleteUserShouldDeleteUser() {
        when(dao.findUserById(1L, connection)).thenReturn(Optional.of(user));

        boolean actual = userService.deleteUser(1L);

        assertTrue(actual);
    }

    @Test
    void deleteUserShouldNotDeleteUser() {
        when(dao.findUserById(1L, connection)).thenReturn(Optional.empty());

        boolean actual = userService.deleteUser(1L);

        assertFalse(actual);
    }

    @Test
    void findUserByIdShouldFindUser() {
        when(dao.findUserById(1L, connection)).thenReturn(Optional.of(user));
        when(mapper.toUserDto(user)).thenReturn(userDto);

        UserDto actual = userService.findUserById(1L);

        assertEquals(userDto, actual);
    }

    @Test
    void findUserByIdShouldNotFindUser() {
        when(dao.findUserById(1L, connection)).thenReturn(Optional.empty());

        UserDto actual = userService.findUserById(1L);

        assertNull(actual);
    }

    @Test
    void getAllShouldReturnListUserDto() {
        when(dao.findAllUsers(connection)).thenReturn(List.of(user));
        when(mapper.toUserDto(user)).thenReturn(userDto);

        List<UserDto> result = userService.getAll();

        assertEquals(1, result.size());
        assertEquals(userDto, result.get(0));
    }
}