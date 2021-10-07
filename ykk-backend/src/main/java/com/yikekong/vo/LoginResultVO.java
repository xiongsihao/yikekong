package com.yikekong.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginResultVO implements Serializable{
    /**
     * 登录结果
     */
    private Boolean loginSuccess;
    /**
     * 管理员Id
     */
    private Integer adminId;
    /**
     * jwt token
     */
    private String token;
}
