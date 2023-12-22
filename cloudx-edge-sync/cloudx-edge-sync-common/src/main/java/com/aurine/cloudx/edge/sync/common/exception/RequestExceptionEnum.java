package com.aurine.cloudx.edge.sync.common.exception;

import lombok.AllArgsConstructor;

/**
 * @Author: wrm
 * @Date: 2022/03/10 9:35
 * @Package: com.aurine.cloudx.edge.sync.biz.exception
 * @Version: 1.0
 * @Remarks: 错误信息枚举类
 **/
@AllArgsConstructor
public enum RequestExceptionEnum {
    /**
     * 异常类型
     */
    // 未定义服务名
    UNDEFINED_SERVICE_NAME("未定义的serviceName"),
    // 未定义类型
    UNDEFINED_TYPE("serviceType未定义此type类型"),
    ;
    public String name;
}
