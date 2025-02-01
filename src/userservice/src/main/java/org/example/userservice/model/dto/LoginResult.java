package org.example.userservice.model.dto;

import org.example.userservice.entity.User;

public class LoginResult {
    private Integer code;
    private String message;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private User data;

    public LoginResult(Integer code, String message, User data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static LoginResult success(User data) {
        return new LoginResult(200, "登录成功", data);
    }

    public static LoginResult error(int code, String message) {
        return new LoginResult(code, message, null);
    }
}
