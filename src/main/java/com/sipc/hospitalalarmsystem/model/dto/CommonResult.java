package com.sipc.hospitalalarmsystem.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sipc.hospitalalarmsystem.model.dto.enumeration.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CommonResult<T> implements Serializable {
    private String code;
    private String message;
    private T data;

    public static <T> CommonResult<T> success() {
        return new CommonResult<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), null);
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data);
    }

    public static <T> CommonResult<T> success(String message, T data) {
        return new CommonResult<>(ResultEnum.SUCCESS.getCode(), message, data);
    }

    public static <T> CommonResult<T> success(String message) {
        return new CommonResult<>(ResultEnum.SUCCESS.getCode(), message, null);
    }

    public static <T> CommonResult<T> fail() {
        return new CommonResult<>(ResultEnum.FAILED.getCode(), ResultEnum.FAILED.getMessage(), null);
    }

    public static <T> CommonResult<T> fail(String message) {
        return new CommonResult<>(ResultEnum.FAILED.getCode(), message, null);
    }

    public static <T> CommonResult<T> fail(String message, T data){
        return new CommonResult<>(ResultEnum.FAILED.getCode(), message, data);
    }

    public static <T> CommonResult<T> tokenWrong() {
        return new CommonResult<>(ResultEnum.TOKEN_WRONG.getCode(), ResultEnum.TOKEN_WRONG.getMessage(), null);
    }

    public static <T> CommonResult<T> tokenNull() {
        return new CommonResult<>(ResultEnum.TOKEN_NULL.getCode(), ResultEnum.TOKEN_NULL.getMessage(), null);
    }
    public static <T> CommonResult<T> loginErr(){
        return new CommonResult<>(ResultEnum.LOGIN_ERR.getCode(),ResultEnum.LOGIN_ERR.getMessage(), null);
    }

}