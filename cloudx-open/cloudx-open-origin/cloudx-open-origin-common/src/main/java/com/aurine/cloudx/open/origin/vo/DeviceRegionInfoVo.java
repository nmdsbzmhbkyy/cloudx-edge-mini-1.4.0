package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>设备区域vo对象</p>
 * @author : 王良俊
 * @date : 2021-09-02 11:36:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRegionInfoVo {

    /**
     * 包含了父级区域 使用','分隔
     * */
    private String regionName;

    /**
     * 区域ID
     */
    private String regionId;
}
