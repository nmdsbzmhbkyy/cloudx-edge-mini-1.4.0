package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("财务配置表单")
public class ProjectFinanceConfigFormVo {
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Integer projectId;

    @ApiModelProperty("厂商类型")
    private String type;
}
