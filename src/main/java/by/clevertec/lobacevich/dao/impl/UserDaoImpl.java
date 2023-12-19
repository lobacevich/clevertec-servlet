package by.clevertec.lobacevich.dao.impl;

import by.clevertec.lobacevich.dao.UserDao;
import by.clevertec.lobacevich.entity.User;
import by.clevertec.lobacevich.exception.DataBaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private static final UserDaoImpl INSTANSE = new UserDaoImpl();
    private static final String CREATE_USER = "INSERT INTO users(firstname, lastname, date_of_birth, email) " +
            "VALUES(?, ?, ?, ?);";
    private static final String UPDATE_USER = "UPDATE users SET firstname=?, lastname=?, date_of_birth=?, " +
            "email=? WHERE id=?;";
    private static final String DELETE_USER = "DELETE FROM users WHERE id=?;";
    private static final String GET_BY_ID = "SELECT * FROM users WHERE id=?";
    private static final String GET_ALL = "SELECT * FROM users ORDER BY id";

    private UserDaoImpl() {
    }

    public static UserDaoImpl getInstance() {
        return INSTANSE;
    }

    @Override
    public User createUser(User user, Connection connection) {
        try (PreparedStatement ps = connection.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFirstname());
            ps.setString(2, user.getLastname());
            ps.setObject(3, user.getDateOfBirth());
            ps.setString(4, user.getEmail());
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                    return user;
                } else {
                    throw new DataBaseException("DB failed: Can't get generated key");
                }
            }
        } catch (SQLException e) {
            throw new DataBaseException("DB failed: Can't create user");
        }
    }

    @Override
    public boolean updateUser(User user, Connection connection) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_USER)) {
            ps.setString(1, user.getFirstname());
            ps.setString(2, user.getLastname());
            ps.setObject(3, user.getDateOfBirth());
            ps.setString(4, user.getEmail());
            ps.setLong(5, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataBaseException("DB failed: Can't update user");
        }
    }

    @Override
    public void deleteUser(User user, Connection connection) {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_USER)) {
            ps.setLong(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseException("DB failed: Can't delete user");
        }
    }

    @Override
    public Optional<User> findUserById(Long id, Connection connection) {
        try (PreparedStatement ps = connection.prepareStatement(GET_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(resultSetToUser(rs));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataBaseException("DB failed: Can't get user by id");
        }
    }

    @Override
    public List<User> findAllUsers(Connection connection) {
        List<User> users = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(GET_ALL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = resultSetToUser(rs);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new DataBaseException("DB failed: Can't load users");
        }
    }

    private User resultSetToUser(ResultSet rs) {
        try {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setFirstname(rs.getString("firstname"));
            user.setLastname(rs.getString("lastname"));
            user.setDateOfBirth(rs.getObject("date_of_birth", LocalDate.class));
            user.setEmail(rs.getString("email"));
            return user;
        } catch (SQLException e) {
            throw new DataBaseException("DB failed: Can't load user");
        }
    }
}
