package ru.shred.electromachine.dao;

import org.apache.ibatis.annotations.Mapper;
import ru.shred.electromachine.model.AzurTestResult;

import java.util.List;

@Mapper
public interface AzurTestResultDao {

    List<AzurTestResult> getAllByProtocolAzurId(Long protocolAzurId);
}
