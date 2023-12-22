package com.aurine.cloudx.open.origin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *  存放json的key和key对应的数据
 * </p>
 *
 * @ClassName: DeviceParamJsonVo
 * @author: 王良俊 <>
 * @date: 2020年12月16日 上午10:14:27
 * @Copyright:
 */
@Data
@NoArgsConstructor
public class DeviceParamJsonVo {

    /**
     * 设备参数信息表中的serviceId
     * */
    String serviceId;

    /**
     * json的key值
     * */
    String key;

    /**
     * 对应的数据
     * */
    String json;

    public DeviceParamJsonVo(String serviceId, String key, String json) {
        this.serviceId = serviceId;
        this.key = key;
        this.json = json;
    }

    /**
    * <p>
    * serviceId默认就是json键值
    * </p>
    *
    * @param
    * @return
    * @author: 王良俊
    */
    public DeviceParamJsonVo(String key, String json) {
        this.serviceId = key;
        this.key = key;
        this.json = json;
    }
}
