package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * (PermissionProperty)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/15 9:39
 */
@Data
@ApiModel("微信端权限配置参数")
public class PermissionProperty {
    @ApiModelProperty("项目id")
    private Integer projectId;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("权限列表")
    private List<String> permissions;
}
