package com.aurine.cloudx.common.uniview.model;

import lombok.Data;

/**
 * 宇视API响应对象
 */
@Data
public abstract class VcsResponse<T> {
    // 执行成功
    public final static int SUCCESS = 200;
    // 服务异常 - 一般参数异常也会报这个问题
    public final static int ERROR_SERVER = 1000;
    // TOEKN过期了，需要重新登录一下
    public final static int ERROR_TOKEN = 1001;
    // 用户名或者密码错误
    public final static int INCORRECT_PASSWORD_OR_USERNAME = 2004;

    private Integer code;
    private String message;
    private T data;
}
