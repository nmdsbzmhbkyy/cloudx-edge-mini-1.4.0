package com.tuya.api.common;

/**
 * 结果类
 */
public class TuyaResult<T> {

    /**
     * 结果码
     */
    private Integer code = 0;

    /**
     * 请求失败信息，成功为空
     */
    private String msg;

    /**
     * 请求是否成功
     */
    private Boolean success;

    /**
     * 当前时间戳
     */
    private Long t;

    /**
     * 结果集
     */
    private T result;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Long getT() {
        return t;
    }

    public void setT(Long t) {
        this.t = t;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }


    public TuyaResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public TuyaResult(Boolean success, T result) {
        this.success = success;
        this.result = result;
    }

    public TuyaResult(Boolean success, Long t, T result) {
        this.success = success;
        this.t = t;
        this.result = result;
    }

    public TuyaResult() {

    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code +
                ", \"msg\":" + msg +
                ", \"success\":" + success +
                ", \"t\":" + t +
                ", \"result\":" + result +
                '}';
    }
}
