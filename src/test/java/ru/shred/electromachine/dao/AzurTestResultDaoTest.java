package ru.shred.electromachine.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shred.electromachine.BaseTest;
import ru.shred.electromachine.model.AzurTestResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AzurTestResultDaoTest extends BaseTest {

    @Autowired
    private AzurTestResultDao dao;

    @Test
    void getAllByProtocolAzurId() {
        List<AzurTestResult> allByProtocolAzurId = dao.getAllByProtocolAzurId(100000L);

        assertEquals(3, allByProtocolAzurId.size());
    }

    @Test
    void save() {
        var azurTestResult = getAzurTestResult();

        var result = dao.save(azurTestResult);

        assertEquals(azurTestResult.getTestType(), result.getTestType());
        assertEquals(azurTestResult.getParameters(), result.getParameters());
        assertEquals(azurTestResult.getNorm(), result.getNorm());
    }

    @Test
    void update() {
        var azurTestResult = getAzurTestResult();
        azurTestResult.setId(100003L);

        var result = dao.update(azurTestResult);

        assertEquals(100003L, result.getId());
        assertEquals(azurTestResult.getTestType(), result.getTestType());
        assertEquals(azurTestResult.getParameters(), result.getParameters());
        assertEquals(azurTestResult.getNorm(), result.getNorm());
    }

    private AzurTestResult getAzurTestResult() {
        return AzurTestResult.builder()
                .protocolAzurId(100001L)
                .testType("Время")
                .parameters("23м")
                .norm(230.0)
                .result(225.5)
                .conclusion("Passed")
                .notation("Within acceptable limits")
                .build();
    }
}