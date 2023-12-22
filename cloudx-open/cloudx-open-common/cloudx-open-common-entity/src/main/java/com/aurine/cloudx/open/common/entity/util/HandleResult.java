package com.aurine.cloudx.open.common.entity.util;

import com.aurine.cloudx.open.common.entity.base.OpenBaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 处理结果类
 *
 * @author : Qiu
 * @date : 2021 12 20 17:02
 */

@Data
@AllArgsConstructor
public class HandleResult<T extends OpenBaseModel> {

    /**
     * 处理结果
     */
    private Boolean result;

    /**
     * 处理对象对象
     */
    private T data;

    /**
     * 处理错误信息
     */
    private String message;

    /**
     * 处理结果优先级
     * 值越小优先级越大（默认为0）
     */
    private Integer priority;

    /**
     * 构造函数（初始化处理结果）
     */
    public HandleResult() {
        this.result = true;
        this.priority = 0;
    }

    /**
     * 构造函数（初始化处理结果）
     *
     * @param data
     */
    public HandleResult(T data) {
        this.result = true;
        this.data = data;
        this.priority = 0;
    }

    /**
     * 返回结果
     */
    public HandleResult<T> result(boolean result) {
        this.result = result;
        return this;
    }

    /**
     * 成功
     */
    public HandleResult<T> success() {
        return success(null);
    }

    /**
     * 成功
     *
     * @param data
     */
    public HandleResult<T> success(T data) {
        this.result = true;
        this.message = null;
        this.data = data;
        return this;
    }

    /**
     * 失败
     */
    public HandleResult<T> failed() {
        return failed(null);
    }

    /**
     * 失败
     *
     * @param message
     */
    public HandleResult<T> failed(String message) {
        this.result = false;
        this.message = message;
        return this;
    }

    /**
     * 失败
     *
     * @param format
     * @param args
     */
    public HandleResult<T> failed(String format, Object... args) {
        this.result = false;
        this.message = String.format(format, args);
        return this;
    }
}
