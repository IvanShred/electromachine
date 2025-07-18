package ru.shred.electromachine.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.shred.electromachine.model.User;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    void getByEmail() {
        User user = userDao.getByEmail("admin@gmail.com");

        assertNotNull(user);
    }
}