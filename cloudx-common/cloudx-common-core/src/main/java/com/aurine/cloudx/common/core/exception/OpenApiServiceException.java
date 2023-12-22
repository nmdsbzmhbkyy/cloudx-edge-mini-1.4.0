package com.aurine.cloudx.common.core.exception;

/**
 * @Author: wrm
 * @Date: 2022/07/11 10:51
 * @Package: com.aurine.cloudx.common.core.exception
 * @Version: 1.0
 * @Remarks:
 **/
public class OpenApiServiceException extends RuntimeException  {
    /**
     * 异常对应的错误类型
     */
    protected ErrorType errorType;

    /**
     * 默认是系统异常
     */
    public OpenApiServiceException() {
        this.errorType = SystemErrorType.SYSTEM_ERROR;
    }

    public OpenApiServiceException(String message) {
        super(message);
    }

    public OpenApiServiceException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

}
