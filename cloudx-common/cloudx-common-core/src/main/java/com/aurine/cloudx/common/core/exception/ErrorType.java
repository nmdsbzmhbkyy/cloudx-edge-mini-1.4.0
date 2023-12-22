package com.aurine.cloudx.common.core.exception;

/**
 * Description:
 * 错误响应接口
 *
 * @author xull <xull@aurine.cn>
 * @version 1.0.0
 * @date 2019-11-14 15:45
 */
public interface ErrorType {
    int  getCode();
    String getMsg();
}
