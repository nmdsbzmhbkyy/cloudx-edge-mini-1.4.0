package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import lombok.Data;

@Data
public class ProjectPersonRightStatusVo extends ProjectRightDevice {

    /**
     * 介质下载失败数
     * */
    private Integer failedNum;
}
