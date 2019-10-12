package ru.shred.electromachine.dao;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shred.electromachine.model.AzurTestResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AzurTestResultDaoTest {

    @Autowired
    private AzurTestResultDao dao;

    @Test
    void getAllByProtocolAzurId() {
        List<AzurTestResult> allByProtocolAzurId = dao.getAllByProtocolAzurId(100000L);

        assertEquals(3, allByProtocolAzurId.size());
    }
}