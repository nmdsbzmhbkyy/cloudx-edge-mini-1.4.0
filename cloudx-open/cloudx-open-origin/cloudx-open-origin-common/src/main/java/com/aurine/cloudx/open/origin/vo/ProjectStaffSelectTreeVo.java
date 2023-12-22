package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.common.core.entity.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Title: ProjectStaffSelectTreeVo
 * Description: 员工树形结构视图
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/7/06 16:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "员工树形结构视图")
public class ProjectStaffSelectTreeVo extends TreeNode {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("是否禁用")
    private boolean disabled = false;
}
