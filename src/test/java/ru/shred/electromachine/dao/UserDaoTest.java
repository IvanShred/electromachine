package ru.shred.electromachine.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.shred.electromachine.model.User;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserDaoTest {

    @Container
    protected static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:16.1")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.liquibase.url", container::getJdbcUrl);
        registry.add("spring.liquibase.user", container::getUsername);
        registry.add("spring.liquibase.password", container::getPassword);
    }

    @Autowired
    private UserDao userDao;

    @Test
    void getByEmail() {
        User user = userDao.getByEmail("admin@gmail.com");

        assertNotNull(user);
    }
}