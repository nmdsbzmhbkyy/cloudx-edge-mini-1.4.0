package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import lombok.Data;

@Data
public class ProjectRightStatusVo extends ProjectRightDevice {

    /**
     * 介质所在的设备名
     * */
    private String deviceName;


    /**
     * 卡状态
     */
    private Integer cardStatus;

}
