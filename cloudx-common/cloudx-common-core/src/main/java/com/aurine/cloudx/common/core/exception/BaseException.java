package com.aurine.cloudx.common.core.exception;

import lombok.Getter;

/**
 * Description:自定义系统异常
 *
 * @author xull <xull@aurine.cn>
 * @version 1.0.0
 * @date 2019-11-26 11:22
 */
@Getter
public class BaseException extends RuntimeException  {
    /**
     * 异常对应的错误类型
     */
    protected ErrorType errorType;

    /**
     * 默认是系统异常
     */
    public BaseException() {
        this.errorType = SystemErrorType.SYSTEM_ERROR;
    }

    public BaseException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

}
