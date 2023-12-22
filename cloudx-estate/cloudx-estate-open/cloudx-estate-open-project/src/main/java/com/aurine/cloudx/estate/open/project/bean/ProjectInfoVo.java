package com.aurine.cloudx.estate.open.project.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectInfoVo {
    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private int projectId;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String projectName;
}
