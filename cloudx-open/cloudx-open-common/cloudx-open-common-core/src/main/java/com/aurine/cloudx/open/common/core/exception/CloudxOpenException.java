package com.aurine.cloudx.open.common.core.exception;

import com.aurine.cloudx.common.core.exception.BaseException;
import com.aurine.cloudx.common.core.exception.ErrorType;
import com.aurine.cloudx.common.core.exception.SystemErrorType;

/**
 * CloudxOpen异常类
 *
 * @author : Qiu
 * @date : 2021 12 13 18:53
 */

public class CloudxOpenException extends BaseException {

    /**
     * 默认是系统异常
     */
    public CloudxOpenException() {
        this.errorType = SystemErrorType.SYSTEM_ERROR;
    }

    public CloudxOpenException(ErrorType errorType) {
        super(errorType, errorType.getMsg());
    }

    public CloudxOpenException(String message) {
        super(SystemErrorType.REQUEST_ERROR, message);
    }

    public CloudxOpenException(ErrorType errorType, String message) {
        super(errorType, message);
    }

    public CloudxOpenException(ErrorType errorType, String message, Throwable cause) {
        super(errorType, message, cause);
    }
}
