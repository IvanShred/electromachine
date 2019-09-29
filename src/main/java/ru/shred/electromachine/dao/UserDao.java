package ru.shred.electromachine.dao;

import org.apache.ibatis.annotations.Mapper;
import ru.shred.electromachine.model.User;

@Mapper
public interface UserDao {

    User getByEmail(String email);
}
