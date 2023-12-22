package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.common.core.entity.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Title: ProjectDeviceRegionTreeVo
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/25 10:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备区域树")
public class ProjectDeviceRegionTreeVo extends TreeNode {
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 层级
     */
    @ApiModelProperty(value = "层级")
    private int level;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private int sort;
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String code;
    /**
     * 是否默认
     */
    @ApiModelProperty(value = "是否默认")
    private String isDefault;


}
