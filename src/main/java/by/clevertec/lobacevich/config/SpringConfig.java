package by.clevertec.lobacevich.config;

import by.clevertec.lobacevich.cache.Cache;
import by.clevertec.lobacevich.cache.factory.CacheFactory;
import by.clevertec.lobacevich.cache.factory.impl.LFUCacheFactory;
import by.clevertec.lobacevich.cache.factory.impl.LRUCacheFactory;
import by.clevertec.lobacevich.mapper.UserMapperImpl;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@ComponentScan("by.clevertec.lobacevich")
@PropertySource("classpath:application.yml")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SpringConfig {

    @Value("${database.driver}")
    private String driverClassName;

    @Value("${database.url}")
    private String jdbcUrl;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        PropertySourcesPlaceholderConfigurer propertyConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        Properties yamlObject = Objects.requireNonNull(yaml.getObject(), "Property not found");
        propertyConfigurer.setProperties(yamlObject);

        return propertyConfigurer;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public UserMapperImpl userMapperImpl() {
        return new UserMapperImpl();
    }

    @Bean
    public Cache cache(@Value("${cache.algorithm}") String algorithm,
                       @Value("${cache.capacity}") int capacity) {
        CacheFactory cacheFactory;
        if (algorithm.equals("LRU")) {
            cacheFactory = new LRUCacheFactory();
        } else {
            cacheFactory = new LFUCacheFactory();
        }

        return cacheFactory.createCache(capacity);
    }

    @Bean
    public SpringLiquibase liquibase(@Value("${liquibase.should_run}") boolean shouldRun,
                                     @Value("${liquibase.changelog_path}") String changelogPath) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setShouldRun(shouldRun);
        liquibase.setDataSource(dataSource());
        liquibase.setDropFirst(true);
        liquibase.setChangeLog(changelogPath);
        liquibase.setContexts("development");

        return liquibase;
    }
}
