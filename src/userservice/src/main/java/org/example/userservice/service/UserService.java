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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    public LoginResult login(LoginDTO loginDTO) {
        // 校验用户名和密码是否为空
        if (StringUtils.isBlank(loginDTO.getUsername()) || StringUtils.isBlank(loginDTO.getPassword())) {
            return LoginResult.error(401, "用户名或密码不能为空");
        }

        // 查询用户
        User user;
        try {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getUsername, loginDTO.getUsername()));
        } catch (Exception e) {
            log.error("Database query failed: {}", e.getMessage());
            return LoginResult.error(500, "服务暂时不可用，请稍后重试");
        }

        // 处理查询结果
        if (user == null) {
            return LoginResult.error(402, "用户不存在");
        }

        if (!user.getPassword().equals(loginDTO.getPassword())) {
            return LoginResult.error(403, "密码错误");
        }

        // 生成token
        try {
            String token = jwtUtils.generateToken(user);
            LoginResult result = LoginResult.success(user);
            result.setToken(token);
            return result;
        } catch (Exception e) {
            log.error("Token generation failed: {}", e.getMessage());
            return LoginResult.error(500, "Token生成失败");
        }
    }
}