package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.common.core.entity.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Title: ProjectDeviceSubsystemVo
 * Description:子系统树
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/20 17:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "子系统树")
public class ProjectDeviceSubsystemTreeVo extends TreeNode {
    @ApiModelProperty("子系统名称")
    private String name;
    @ApiModelProperty("子系统编码")
    private String code;
    @ApiModelProperty("子系统类型")
    private String level;
    @ApiModelProperty("之系统树名称")
    private String treeName;
}
