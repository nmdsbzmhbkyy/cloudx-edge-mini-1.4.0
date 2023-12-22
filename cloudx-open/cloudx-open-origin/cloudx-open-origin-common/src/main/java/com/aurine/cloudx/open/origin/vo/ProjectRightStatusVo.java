package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectRightDevice;
import lombok.Data;

@Data
public class ProjectRightStatusVo extends ProjectRightDevice {

    /**
     * 介质所在的设备名
     * */
    private String deviceName;

}
