package com.aurine.cloudx.estate.vo;

import com.pig4cloud.pigx.admin.api.dto.TreeNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("菜单权限")
public class SysMenuMobileTreeVo extends TreeNode {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("权限")
    private String permission;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("菜单类型：0 模块 1 模块功能 2 按钮权限")
    private String menuType;
    @ApiModelProperty("区分小程序和app特有功能 0或者空 小程序或app 1 小程序 2 app 9 其他")
    private String type;
}
