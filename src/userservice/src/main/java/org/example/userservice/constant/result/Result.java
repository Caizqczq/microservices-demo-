package org.example.userservice.constant.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    private String token;

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功的响应结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 失败响应
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败的响应结果
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 自定义响应
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     * @param <T> 数据类型
     * @return 自定义的响应结果
     */
    public static <T> Result<T> build(int code, String message, T data) {
        return new Result<>(code, message, data);
    }
}
