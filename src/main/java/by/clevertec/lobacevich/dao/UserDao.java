package by.clevertec.lobacevich.dao;

import by.clevertec.lobacevich.entity.User;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    User createUser(User user, Connection connection);

    boolean updateUser(User user, Connection connection);

    void deleteUser(User user, Connection connection);

    Optional<User> findUserById(Long id, Connection connection);

    List<User> findAllUsers(Connection connection);
}
