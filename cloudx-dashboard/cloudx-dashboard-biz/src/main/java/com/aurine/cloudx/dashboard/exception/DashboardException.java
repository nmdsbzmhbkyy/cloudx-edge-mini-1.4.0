package com.aurine.cloudx.dashboard.exception;

import com.aurine.cloudx.common.core.exception.BaseException;
import com.aurine.cloudx.common.core.exception.ErrorType;
import com.aurine.cloudx.common.core.exception.SystemErrorType;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
public class DashboardException extends BaseException {

    /**
     * 默认是系统异常
     */
    public DashboardException() {
        this.errorType = SystemErrorType.SYSTEM_ERROR;
    }

    public DashboardException(ErrorType errorType) {
        super(errorType, errorType.getMsg());
    }

    public DashboardException(ErrorType errorType, String message) {
        super(errorType, message);
    }

    public DashboardException(ErrorType errorType, String message, Throwable cause) {
        super(errorType, message, cause);
    }
}
