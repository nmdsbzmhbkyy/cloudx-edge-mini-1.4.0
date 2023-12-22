package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("消息模板查询表单")
public class ProjectNoticeTemplateForm {
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("是否启用")
    private String isActive;
    @ApiModelProperty("消息类别")
    private String typeId;
    @ApiModelProperty("发布查询开始时间")
    private LocalDateTime effTime;
    @ApiModelProperty("发布查询结束时间")
    private LocalDateTime expTime;
    @ApiModelProperty("消息内容")
    private String content;
    @ApiModelProperty("项目id")
    private Integer projectId;
}
