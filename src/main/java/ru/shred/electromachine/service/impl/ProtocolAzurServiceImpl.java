package ru.shred.electromachine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shred.electromachine.dao.AzurTestResultDao;
import ru.shred.electromachine.dao.ProtocolAzurDao;
import ru.shred.electromachine.model.ProtocolAzur;
import ru.shred.electromachine.service.ProtocolAzurService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProtocolAzurServiceImpl implements ProtocolAzurService {

    private final ProtocolAzurDao protocolAzurDao;
    private final AzurTestResultDao azurTestResultDao;

    @Override
    @Transactional(readOnly = true)
    public List<ProtocolAzur> getAll() {
        return protocolAzurDao.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ProtocolAzur getById(Long id) {
        return protocolAzurDao.getById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        azurTestResultDao.deleteAllByProtocolAzurId(id);
        protocolAzurDao.delete(id);
    }


    @Override
    @Transactional
    public ProtocolAzur save(ProtocolAzur protocolAzur) {
        return protocolAzurDao.save(protocolAzur);
    }

    @Override
    @Transactional
    public ProtocolAzur update(ProtocolAzur protocolAzur) {
        return protocolAzurDao.update(protocolAzur);
    }
}
