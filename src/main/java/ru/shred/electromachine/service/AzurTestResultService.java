package ru.shred.electromachine.service;

import ru.shred.electromachine.model.AzurTestResult;

import java.util.List;

public interface AzurTestResultService {

    List<AzurTestResult> getAllByProtocolId(Long protocolId);

    AzurTestResult getById(Long id);
}
