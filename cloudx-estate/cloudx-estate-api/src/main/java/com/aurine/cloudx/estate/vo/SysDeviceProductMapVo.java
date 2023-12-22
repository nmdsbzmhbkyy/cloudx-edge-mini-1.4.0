package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.SysDeviceProductMap;
import lombok.Data;

@Data
public class SysDeviceProductMapVo extends SysDeviceProductMap {

    /**
    * 设备类型ID
    **/
    private String deviceType;

}
