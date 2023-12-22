package com.aurine.cloudx.common.core.exception;

/**
 * 第三方服务异常
 *
 * @ClassName: ThirdPartyServiceException
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-11 9:15
 * @Copyright:
 */
public class ThirdPartyServiceException extends BaseException {


    /**
     * 默认是系统异常
     */
    public ThirdPartyServiceException() {
        this.errorType = SystemErrorType.SYSTEM_ERROR;
    }

    public ThirdPartyServiceException(ErrorType errorType) {
        super(errorType,errorType.getMsg());
    }

    public ThirdPartyServiceException(ErrorType errorType, String message) {
        super(errorType, message);
    }

    public ThirdPartyServiceException(ErrorType errorType, String message, Throwable cause) {
        super(errorType,message, cause);
    }
}
