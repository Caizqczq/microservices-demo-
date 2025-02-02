package org.example.userservice.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import org.example.userservice.mapper.UserMapper;
import org.example.userservice.model.dto.LoginDTO;
import org.example.userservice.model.dto.LoginResult;
import org.example.userservice.model.entity.User;
import org.example.userservice.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    public LoginResult login(LoginDTO loginDTO) {
        if(StringUtils.isBlank(loginDTO.getUsername())
                || StringUtils.isBlank(loginDTO.getPassword())) {
            return LoginResult.error(401, "用户名或密码不能为空");
        }
        User user=userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginDTO.getUsername()));

        if(user==null) {
            return LoginResult.error(402,"用户不存在");
        }

        if(!user.getPassword().equals(loginDTO.getPassword())) {
            return LoginResult.error(403,"密码错误");
        }

        // 生成token
        String token = jwtUtils.generateToken(user);

        // 创建成功结果并设置token
        LoginResult result = LoginResult.success(user);
        result.setToken(token);

        return result;
    }

}
