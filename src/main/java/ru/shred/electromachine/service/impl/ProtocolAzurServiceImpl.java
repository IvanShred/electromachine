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

    public List<ProtocolAzur> getAll() {
        return protocolAzurDao.getAll();
    }

    @Override
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
    public void save(ProtocolAzur protocolAzur) {
        protocolAzurDao.save(protocolAzur);
    }

    @Override
    @Transactional
    public void update(ProtocolAzur protocolAzur) {
        protocolAzurDao.update(protocolAzur);
    }
}
