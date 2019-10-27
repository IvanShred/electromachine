package ru.shred.electromachine.service;

import ru.shred.electromachine.model.AzurTestResult;

import java.util.List;

public interface AzurTestResultService {

    List<AzurTestResult> getAllByProtocolId(Long protocolId);

    AzurTestResult getById(Long id);

    void update(AzurTestResult azurTestResult);

    void delete(Long id);

    void deleteAllByProtocolId(Long protocolId);

    void save(AzurTestResult azurTestResult);
}
