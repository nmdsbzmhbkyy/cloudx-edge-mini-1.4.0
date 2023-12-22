package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("小区房屋状态")
public class ProjectHouseStatusVo {
    /**
     * 小区列表
     */
    @ApiModelProperty(value = "小区Id")
    private String projectId;

    /**
     * 是否绑定房屋状态
     */
    @ApiModelProperty(value = "是否绑定房屋状态 0 1 2 9")
    private String auditStatus;
}
