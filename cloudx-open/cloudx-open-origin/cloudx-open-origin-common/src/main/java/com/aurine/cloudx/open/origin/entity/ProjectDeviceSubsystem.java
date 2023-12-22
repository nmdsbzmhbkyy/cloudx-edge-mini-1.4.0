
package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备子系统
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:39:47
 */
@Data
@TableName("project_device_subsystem")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备子系统")
public class ProjectDeviceSubsystem extends OpenBasePo<ProjectDeviceSubsystem> {
    private static final long serialVersionUID = 1L;

    /**
     * uuid,按层级划分 1级：子系统编码 2级：子模块编码 3级：设备类型
     */
    @ApiModelProperty(value = "uuid,按层级划分 1级：子系统编码 2级：子模块编码 3级：设备类型")
    @TableId(type = IdType.ASSIGN_UUID)
    private String subsystemId;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    /**
     * 子系统编码
     */
    @ApiModelProperty(value = "子系统编码")
    private String subsystemCode;
    /**
     * 子系统名称，跟随id层级
     */
    @ApiModelProperty(value = "子系统名称，跟随id层级")
    private String subsystemName;
    /**
     * 上级id
     */
    @ApiModelProperty(value = "上级id")
    private String pid;
 
    /**
     * 1级：子系统id 2级：子模块id 3级：设备类型
     */
    @ApiModelProperty(value = "1级：子系统id 2级：子模块id 3级：设备类型")
    @TableField("rLevel")
    private String level;
}
