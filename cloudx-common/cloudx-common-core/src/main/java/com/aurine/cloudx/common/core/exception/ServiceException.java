package com.aurine.cloudx.common.core.exception;

/**
 * Description:
 * 自定义业务处理异常用于service层业务处理时候调用
 *
 * @author xull <xull@aurine.cn>
 * @version 1.0
 * @date 2020-02-21 10:23
 */
public class ServiceException extends BaseException  {

    public ServiceException(String msg){
        this.errorType=new BizErrorType(msg);
    }

    private class BizErrorType implements ErrorType{
        private String msg;

        public BizErrorType(String msg){
            this.msg=msg;
        }

        @Override
        public int getCode() {
            return SystemErrorType.MULTIPLE_CHOICES.getCode();
        }

        @Override
        public String getMsg() {
            return msg;
        }
    }
}
