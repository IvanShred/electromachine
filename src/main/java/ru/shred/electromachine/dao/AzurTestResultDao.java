package ru.shred.electromachine.dao;

import org.apache.ibatis.annotations.Mapper;
import ru.shred.electromachine.model.AzurTestResult;

import java.util.List;

@Mapper
public interface AzurTestResultDao {

    List<AzurTestResult> getAllByProtocolAzurId(Long protocolAzurId);

    AzurTestResult getById(Long id);

    void update(AzurTestResult azurTestResult);

    void delete(Long id);

    void deleteAllByProtocolAzurId(Long protocolAzurId);

    void save(AzurTestResult azurTestResult);
}
