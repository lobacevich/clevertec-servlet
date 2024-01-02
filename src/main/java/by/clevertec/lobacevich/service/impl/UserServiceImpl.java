package by.clevertec.lobacevich.service.impl;

import by.clevertec.lobacevich.dao.UserDao;
import by.clevertec.lobacevich.dao.impl.UserDaoImpl;
import by.clevertec.lobacevich.db.ConnectionPool;
import by.clevertec.lobacevich.dto.UserDto;
import by.clevertec.lobacevich.entity.User;
import by.clevertec.lobacevich.exception.ConnectionException;
import by.clevertec.lobacevich.mapper.UserMapper;
import by.clevertec.lobacevich.mapper.UserMapperImpl;
import by.clevertec.lobacevich.service.UserService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    public static final UserServiceImpl INSTANCE = new UserServiceImpl();
    private DataSource dataSource = ConnectionPool.getDataSource();
    private UserDao dao = UserDaoImpl.getInstance();
    private UserMapper mapper = new UserMapperImpl();
    public static final String CONNECTION_FAILED = "Connection failed:";

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return INSTANCE;
    }

    /***
     * Преобразует userDto в User и передает в слой dao для записи в базу данных
     * @param userDto дто сущности User
     * @return возвращает дто сущности User, полученной из слоя dao
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        try (Connection connection = dataSource.getConnection()) {
            return mapper.toUserDto(dao.createUser(mapper.toUser(userDto), connection));
        } catch (SQLException e) {
            throw new ConnectionException(CONNECTION_FAILED + e.getMessage());
        }
    }

    /***
     * Преобразует userDto в User и передает в слой dao для обновления записи в базе данных
     * @param userDto дто сущности User
     * @return возвращает true в случае успешного обновления записи в базе данных, иначе false
     */
    @Override
    public boolean updateUser(UserDto userDto) {
        try (Connection connection = dataSource.getConnection()) {
            return dao.updateUser(mapper.toUser(userDto), connection);
        } catch (SQLException e) {
            throw new ConnectionException(CONNECTION_FAILED + e.getMessage());
        }
    }

    /***
     * обращается к методу слоя dao для удаления сущности с указанным id
     * @param id получает id сущности, которую надо удалить из базы данных
     * @return возвращает значение boolean true, если объект удален, и false, если нет
     */
    @Override
    public boolean deleteUser(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            return dao.deleteUser(id, connection);
        } catch (SQLException e) {
            throw new ConnectionException(CONNECTION_FAILED + e.getMessage());
        }
    }

    /***
     * вызывает в слое dao метод для поиска сущности с данным id, получает Optional данной сущности
     * @param id получает id сущности для поиска
     * @return возвращае dto сущности с указанным id либо null, если сущность не найдена
     */
    @Override
    public UserDto findUserById(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            Optional<User> userOptional = dao.findUserById(id, connection);
            return userOptional.map(mapper::toUserDto).orElse(null);
        } catch (SQLException e) {
            throw new ConnectionException(CONNECTION_FAILED + e.getMessage());
        }
    }

    /***
     * вызывает метод в слое dao для получения всех сущностей из базы данных
     * @return возвращает List dto всех сущностей из базы данных
     */
    @Override
    public List<UserDto> getAll() {
        try (Connection connection = dataSource.getConnection()) {
            return dao.findAllUsers(connection).stream()
                    .map(mapper::toUserDto)
                    .toList();
        } catch (SQLException e) {
            throw new ConnectionException(CONNECTION_FAILED + e.getMessage());
        }
    }
}
