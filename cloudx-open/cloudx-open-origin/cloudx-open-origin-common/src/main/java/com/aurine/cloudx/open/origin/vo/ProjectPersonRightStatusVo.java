package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectRightDevice;
import lombok.Data;

@Data
public class ProjectPersonRightStatusVo extends ProjectRightDevice {

    /**
     * 介质下载失败数
     * */
    private Integer failedNum;
}
