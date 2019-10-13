package ru.shred.electromachine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shred.electromachine.dao.ProtocolAzurDao;
import ru.shred.electromachine.model.ProtocolAzur;
import ru.shred.electromachine.service.ProtocolAzurService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProtocolAzurServiceImpl implements ProtocolAzurService {

    private final ProtocolAzurDao protocolAzurDao;

    public List<ProtocolAzur> getAll() {
        return protocolAzurDao.getAll();
    }

    @Override
    public ProtocolAzur getById(Long id) {
        return protocolAzurDao.getById(id);
    }
}
