package org.example.userservice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.userservice.model.dto.LoginDTO;
import org.example.userservice.model.entity.User;


@Mapper
public interface UserMapper {
    User selectOne(LoginDTO loginDTO);

    int insert(User user);

    int checkUsername(String username);
}
