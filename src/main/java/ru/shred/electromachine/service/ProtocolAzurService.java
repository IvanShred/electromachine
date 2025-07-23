package ru.shred.electromachine.service;

import ru.shred.electromachine.model.ProtocolAzur;

import java.util.List;

public interface ProtocolAzurService {

    List<ProtocolAzur> getAll();

    ProtocolAzur getById(Long id);

    void delete(Long id);

    ProtocolAzur save(ProtocolAzur protocolAzur);

    ProtocolAzur update(ProtocolAzur protocolAzur);
}
