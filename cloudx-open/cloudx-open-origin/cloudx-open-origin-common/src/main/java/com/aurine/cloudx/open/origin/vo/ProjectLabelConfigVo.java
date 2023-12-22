package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectLabelConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("标签管理")
public class ProjectLabelConfigVo extends ProjectLabelConfig {
    /**
     * 创建者姓名
     * */
    @ApiModelProperty("操作者")
    private String operateName;
    /**
     * 项目ID
     */
    @ApiModelProperty("项目ID")
    private Integer projectId;
    /**
     * 租户ID
     */
    @ApiModelProperty("租户ID")
    private Integer tenantId;
    /**
     * 创建者姓名
     */
    @ApiModelProperty("创建者姓名")
    private String creatorName;

}
