package com.aurine.cloudx.open.common.core.exception;

import com.aurine.cloudx.common.core.exception.ErrorType;

import com.aurine.cloudx.open.common.core.constant.enums.VersionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * CloudxOpen错误类型
 *
 * @author : Qiu
 * @date : 2021 12 13 18:55
 */

@Getter
@AllArgsConstructor
public enum CloudxOpenErrorEnum implements ErrorType {
    /**
     * 操作成功
     */
    SUCCESS(0, "请求成功"),

    /**
     * 参数异常
     */
    // 缺少必要参数
    MISSING_REQUIRED_PARAMETERS(29999, "缺少必要参数"),
    //请求接口失败
    MULTIPLE_CHOICES(30000, "请求接口失败"),
    //请求参数校验不通过
    ARGUMENT_INVALID(30100, "请求参数校验不通过"),
    //请求参数类型无效
    ARGUMENT_TYPE_INVALID(30200, "请求参数类型无效"),
    //参数格式异常
    ARGUMENT_FORMAT_INVALID(30300, "参数格式异常"),
    //参数取值范围异常
    ARGUMENT_SCOPE_INVALID(30400, "参数取值范围异常"),
    //上传文件大小超过限制
    UPLOAD_FILE_SIZE_LIMIT(30500, "上传文件大小超过限制"),
    //未找到token
    NOT_FOUND_TOKEN(31100, "未找到token"),
    //无效的token
    INVALID_TOKEN(31200, "无效的token"),
    //token已失效
    OUTMODED_TOKEN(31300, "token已失效"),
    //token存在，但权限校验失败
    NO_PERMISSION(31400, "token存在，但权限校验失败"),

    /**
     * 请求异常
     */
    //请求异常
    REQUEST_ERROR(40000, "请求异常"),
    //授权失败
    UNAUTHORIZED(40100, "用户没有接口权限,拒绝访问"),
    //接口不存在
    NOT_FOUND(40400, "接口或方法不存在"),
    NOT_FOUND_PRODUCER(40401, "未找到接口服务商"),
    NOT_FOUND_METHOD(40402, "未找到接口方法"),
    NOT_FOUND_VERSION(40403, "未找到对应版本"),

    NOT_FOUND_CALLBACK(40499, "未找到回调地址"),

    FORBIDDEN(40500, "当前方法被禁用"),
    OUT_OF_TIME(40800, "请求超时,请稍后再试"),

    REPEATING_DATA(40900,"存在重复数据"),
    /**
     * 系统异常
     */
    SYSTEM_ERROR(50000, "系统异常"),

    /**
     * 回调地址异常
     */
    CALLBACK_ERROR(50099, "回调地址异常"),

    /**
     * 数据异常，用于非正常情况下找不到数据
     */
    EMPTY_RESULT(60000, "未找到数据"),

    /**
     * 业务异常，用于业务处理异常
     */
    SERVICE_ERROR(60100, "业务异常"),


    ;

    /**
     * 错误类型码
     */
    private int code;
    /**
     * 错误类型描述信息
     */
    private String msg;

    public static CloudxOpenErrorEnum getByCode(int code) {
        for (CloudxOpenErrorEnum value : CloudxOpenErrorEnum.values()) {
            if (value.code==code) {
                return value;
            }
        }
        return null;
    }

    public static List<Integer> getCodeList() {
        List<Integer> codeList=new ArrayList<>();
        for (CloudxOpenErrorEnum value : CloudxOpenErrorEnum.values()) {
            codeList.add(value.code);
        }
        return codeList;
    }

}
