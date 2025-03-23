package org.example.userservice.service;
import io.micrometer.common.util.StringUtils;
import org.example.userservice.constant.result.Result;
import org.example.userservice.mapper.UserMapper;
import org.example.userservice.model.dto.LoginDTO;
import org.example.userservice.model.dto.LoginResult;
import org.example.userservice.model.dto.RegisterDTO;
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

    public Result login(LoginDTO loginDTO) {
        // 校验用户名和密码是否为空
        if (StringUtils.isBlank(loginDTO.getUsername()) || StringUtils.isBlank(loginDTO.getPassword())) {
            return Result.error(401, "用户名或密码不能为空");
        }

        // 查询用户
        User user;
        try {
           user=userMapper.selectOne(loginDTO);
        } catch (Exception e) {
            log.error("Database query failed: {}", e.getMessage());
            return Result.error(500, "服务暂时不可用，请稍后重试");
        }

        // 处理查询结果
        if (user == null) {
            return Result.error(402, "用户不存在");
        }

        if (!user.getPassword().equals(loginDTO.getPassword())) {
            return Result.error(403, "密码错误");
        }

        // 生成token
        try {
            String token = jwtUtils.generateToken(user);
            Result result = Result.success(user);
            result.setToken(token);
            return result;
        } catch (Exception e) {
            log.error("Token generation failed: {}", e.getMessage());
            return Result.error(500, "Token生成失败");
        }
    }

    public Result register(RegisterDTO registerDTO) {
        // 校验用户名和密码是否为空
        if (StringUtils.isBlank(registerDTO.getUsername()) || StringUtils.isBlank(registerDTO.getPassword())) {
            return Result.error(401, "用户名或密码不能为空");
        }

        try {
            // 检查用户名是否已存在
            int count = userMapper.checkUsername(registerDTO.getUsername());
            if (count > 0) {
                return Result.error(409, "用户名已存在");
            }

            // 创建新用户
            User user = new User();
            user.setUsername(registerDTO.getUsername());
            user.setPassword(registerDTO.getPassword());

            // 插入用户数据
            int result = userMapper.insert(user);
            if (result > 0) {
                return Result.success(null);
            } else {
                return Result.error(500, "注册失败，请稍后重试");
            }
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            return Result.error(500, "服务暂时不可用，请稍后重试");
        }
    }

}