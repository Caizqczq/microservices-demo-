package org.example.userservice.constant.enums;

public enum LoginStatusEnum {
    SUCCESS(200, "登录成功"),
    PARAMS_ERROR(401, "用户名或密码不能为空"),
    USER_NOT_FOUND(402, "用户名不存在"),
    PASSWORD_ERROR(403, "密码错误");

    private final Integer code;
    private final String message;

    LoginStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
