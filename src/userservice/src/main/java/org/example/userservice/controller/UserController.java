package org.example.userservice.controller;



import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.userservice.model.dto.LoginDTO;
import org.example.userservice.model.dto.LoginResult;
import org.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User接口")
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "登录请求")
    @PostMapping("/login")
    public LoginResult login(@RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }
}
