package by.clevertec.lobacevich.db;

import by.clevertec.lobacevich.exception.ConnectionException;
import by.clevertec.lobacevich.util.YamlReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class Connect {

    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;
    private static final Connection CONNECTION;

    static {
        Map<String, String> data = YamlReader.getData();
        URL = data.get("Connect.url");
        USERNAME = data.get("Connect.username");
        PASSWORD = data.get("Connect.password");
        try {
            CONNECTION = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new ConnectionException("Connection failed");
        }
    }

    private Connect() {
    }

    public static Connection getConnection() {
        return CONNECTION;
    }
}
