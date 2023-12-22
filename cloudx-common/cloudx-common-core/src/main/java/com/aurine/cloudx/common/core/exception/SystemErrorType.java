package com.aurine.cloudx.common.core.exception;

import lombok.Getter;

@Getter
public enum SystemErrorType implements ErrorType {
    /**
     * 操作成功
     */
    OK(200, "请求成功"),
    PROCESSING(201, "请求提交成功，异步处理请求中"),

    /**
     * 入参异常
     */
    //请求接口失败
    MULTIPLE_CHOICES(300, "请求接口失败"),
    //请求参数校验不通过
    ARGUMENT_INVALID(301, "请求参数校验不通过"),
    //请求参数类型无效
    ARGUMENT_TYPE_INVALID(302, "请求参数类型无效"),
    //参数格式异常
    ARGUMENT_FORMAT_INVALID(303, "参数格式异常"),
    //参数取值范围异常
    ARGUMENT_SCOPE_INVALID(304, "参数取值范围异常"),
    //上传文件大小超过限制
    UPLOAD_FILE_SIZE_LIMIT(305, "上传文件大小超过限制"),
    //用户没有接口权限,拒绝访问
    UNAUTHORIZED(310, "用户没有接口权限,拒绝访问"),
    //未找到toke
    NOT_FOUND_TOKEN(311, "未找到toke"),
    //无效的token
    INVALID_TOKEN(312, "无效的token"),
    //token已失效
    OUTMODED_TOKEN(313, "token已失效"),
    //token存在，但权限校验失败
    NO_PERMISSION(314, "token存在，但权限校验失败"),
    /**
     * 请求异常
     */
    //请求异常
    REQUEST_ERROR(400, "请求异常"),
    //接口不存在
    NOT_FOUND(401, "接口不存在"),
    //接口已被禁用
    DISABLE_API(402, "接口已被禁用"),
    //接口已过期，提供新的接口提示
    OUTMODED_API(403, "接口已过期，提供新的接口提示"),
    /**
     * 系统异常
     */
    //系统异常
    SYSTEM_ERROR(500, "系统异常"),
    //服务组件异常未找到
    GATEWAY_NOT_FOUND_SERVICE(501, "服务组件异常未找到"),
    //数据库异常
    DATABASE_EXCEPTION(520, "数据库异常"),
    //唯一主键冲突
    DUPLICATE_PRIMARY_KEY(521, "唯一主键冲突"),
    //缓存异常
    CACHE_EXCEPTION(540, "缓存异常"),
    //设备异常
    UNIT_EXCEPTION(550, "设备异常"),
    //服务方设备异常
    TPOS_UNIT_EXCEPTION(551, "服务方设备异常"),
    //第三方服务异常
    TPOS_EXCEPTION(552, "第三方服务异常");

    /**
     * 错误类型码
     */
    private int code;
    /**
     * 错误类型描述信息
     */
    private String msg;

    SystemErrorType(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

}
