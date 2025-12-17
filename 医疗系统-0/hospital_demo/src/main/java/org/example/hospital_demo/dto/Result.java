package org.example.hospital_demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 统一响应结果封装类
 * 用于标准化API响应格式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    
    private Integer code;      // 响应状态码
    private String message;    // 响应消息
    private T data;           // 响应数据
    private Long timestamp;   // 时间戳
    
    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null, System.currentTimeMillis());
    }
    
    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data, System.currentTimeMillis());
    }
    
    /**
     * 成功响应（自定义消息）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data, System.currentTimeMillis());
    }
    
    /**
     * 失败响应
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null, System.currentTimeMillis());
    }
    
    /**
     * 失败响应（自定义状态码）
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null, System.currentTimeMillis());
    }
    
    /**
     * 参数错误响应
     */
    public static <T> Result<T> badRequest(String message) {
        return new Result<>(400, message, null, System.currentTimeMillis());
    }
    
    /**
     * 未找到资源响应
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null, System.currentTimeMillis());
    }
}