package com.aurine.cloudx.open.origin.vo;

import com.pig4cloud.pigx.admin.api.dto.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Title: ProjectGroupTreeVo
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/13 16:34
 */
@Data
@ApiModel(value = "项目组树")
@EqualsAndHashCode(callSuper = true)
public class ProjectGroupTreeVo extends TreeNode {
    @ApiModelProperty(value = "项目组名称")
    private String name;
    @ApiModelProperty(value = "部门类型")
    private String deptTypeId;
}
