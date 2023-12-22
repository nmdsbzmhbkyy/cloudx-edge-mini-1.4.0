package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.common.core.entity.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Title: ProjectDeviceSelectTreeVo
 * Description: 设备楼栋单元树形结构视图
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/25 10:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备楼栋单元树形结构视图")
public class ProjectDeviceSelectTreeVo extends TreeNode {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("类型")
    private String type;
}
