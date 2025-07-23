package ru.shred.electromachine.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shred.electromachine.BaseTest;
import ru.shred.electromachine.model.ProtocolAzur;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by KuhtaIA on 22.07.2025
 */
class ProtocolAzurDaoTest extends BaseTest {

    @Autowired
    private ProtocolAzurDao protocolAzurDao;

    @Test
    void save() {
        var protocolAzur = getProtocolAzur();

        var result = protocolAzurDao.save(protocolAzur);

        assertEquals("protocolNumber", result.getProtocolNumber());
        assertEquals("measurementPurpose", result.getMeasurementPurpose());
    }

    @Test
    void update() {
        var protocolAzur = getProtocolAzur();
        protocolAzur.setId(100001L);

        var result = protocolAzurDao.update(protocolAzur);

        assertEquals(100001L, result.getId());
        assertEquals("protocolNumber", result.getProtocolNumber());
        assertEquals("measurementPurpose", result.getMeasurementPurpose());
    }

    private ProtocolAzur getProtocolAzur() {
        return ProtocolAzur.builder()
                .protocolNumber("protocolNumber")
                .measurementPurpose("measurementPurpose")
                .documentsNumber("documentsNumber")
                .resultVisualInspection("resultVisualInspection")
                .climateData("climateData")
                .build();
    }
}
