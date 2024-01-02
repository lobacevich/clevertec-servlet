package by.clevertec.lobacevich.db;

import by.clevertec.lobacevich.util.YamlReader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import javax.sql.DataSource;
import java.util.Map;

public class ConnectionPool {

    @Getter
    private static final DataSource dataSource;

    static {
        Map<String, String> data = YamlReader.getData();
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(data.get("Connection.driver"));
        config.setJdbcUrl(data.get("Connection.url"));
        config.setUsername(data.get("Connection.username"));
        config.setPassword(data.get("Connection.password"));
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);

        dataSource = new HikariDataSource(config);
    }

    private ConnectionPool() {
    }
}
