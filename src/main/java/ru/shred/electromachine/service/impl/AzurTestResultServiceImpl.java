package ru.shred.electromachine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shred.electromachine.dao.AzurTestResultDao;
import ru.shred.electromachine.model.AzurTestResult;
import ru.shred.electromachine.service.AzurTestResultService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AzurTestResultServiceImpl implements AzurTestResultService {

    private final AzurTestResultDao dao;

    @Override
    public List<AzurTestResult> getAllByProtocolId(Long protocolId) {
        return dao.getAllByProtocolAzurId(protocolId);
    }

    @Override
    public AzurTestResult getById(Long id) {
        return dao.getById(id);
    }

    @Override
    @Transactional
    public void update(AzurTestResult azurTestResult) {
        dao.update(azurTestResult);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        dao.delete(id);
    }

    @Override
    @Transactional
    public void save(AzurTestResult azurTestResult) {
        dao.save(azurTestResult);
    }
}
