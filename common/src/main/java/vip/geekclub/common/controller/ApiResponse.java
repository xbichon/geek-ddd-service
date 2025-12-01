package vip.geekclub.common.controller;

import lombok.*;
import vip.geekclub.common.command.CommandResult;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    // 静态方法简化成功/失败响应
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("success");
        apiResponse.setData(data);
        return apiResponse;
    }

    public static ApiResponse<Void> success() {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("success");
        return apiResponse;
    }

    // 静态方法简化成功/失败响应
    public static <T> ApiResponse<T> success(CommandResult<T> result) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage(result.message());
        apiResponse.setData(result.primaryKey());
        return apiResponse;
    }

    // 静态方法简化成功/失败响应
    public static ApiResponse<Void> message(String message) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage(message);
        return apiResponse;
    }

    public static ApiResponse<?> fail(int code, String message) {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setCode(code);
        apiResponse.setMessage(message);
        return apiResponse;
    }
}