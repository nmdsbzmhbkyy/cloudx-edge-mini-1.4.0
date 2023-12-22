package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectNoticeTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("消息模板视图")
public class ProjectNoticeTemplateVo extends ProjectNoticeTemplate {
    @ApiModelProperty("创建人名称")
    private String createName;
    @ApiModelProperty("更新人名称")
    private String updateName;
    @ApiModelProperty("项目id")
    private Integer projectId;
}
