package com.aurine.cloudx.common.core.exception;

/**
 * Description:
 * 自定义服务端处理异常用于系统异常时调用
 *
 * @author xull <xull@aurine.cn>
 * @version 1.0
 * @date 2020-02-21 10:23
 */
public class SysException extends BaseException {

    public SysException(String msg){
        this.errorType=new SysErrorType(msg);
    }

    private class SysErrorType implements ErrorType{
        private String msg;

        public SysErrorType(String msg){
            this.msg=msg;
        }

        @Override
        public int getCode() {
            return SystemErrorType.SYSTEM_ERROR.getCode();
        }

        @Override
        public String getMsg() {
            return msg;
        }
    }
}
