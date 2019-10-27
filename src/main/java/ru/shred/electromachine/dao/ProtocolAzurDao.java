package ru.shred.electromachine.dao;

import org.apache.ibatis.annotations.Mapper;
import ru.shred.electromachine.model.ProtocolAzur;

import java.util.List;

@Mapper
public interface ProtocolAzurDao {

    List<ProtocolAzur> getAll();

    ProtocolAzur getById(Long id);

    void delete(Long id);

    void save(ProtocolAzur protocolAzur);

    void update(ProtocolAzur protocolAzur);
}
