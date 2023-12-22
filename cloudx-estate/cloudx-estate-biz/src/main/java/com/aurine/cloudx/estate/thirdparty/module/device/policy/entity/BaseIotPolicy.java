package com.aurine.cloudx.estate.thirdparty.module.device.policy.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 物联网策略基类
 * </p>
 * @author : 王良俊
 * @date : 2021-08-19 17:24:37
 */
@Data
public abstract class BaseIotPolicy {

    /**
     * <p>
     * 获取用于策略指令下发的params json
     * </p>
     *
     */
    public abstract String getParams();

    /**
    * <p>
    * 返回任意一台设备的sn
    * </p>
    *
    */
    public abstract String getSn();

}
