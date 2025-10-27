package com.bbs.common;

public class Result<T> {
    private boolean success;
    private String message;
    private T data;
    private int code;
    
    private Result() {}
    
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.success = true;
        result.code = 200;
        result.message = "操作成功";
        return result;
    }
    
    public static <T> Result<T> success(T data) {
        Result<T> result = success();
        result.data = data;
        return result;
    }
    
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = success(data);
        result.message = message;
        return result;
    }
    
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.success = false;
        result.code = 500;
        result.message = message;
        return result;
    }
    
    public static <T> Result<T> error(int code, String message) {
        Result<T> result = error(message);
        result.code = code;
        return result;
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
}