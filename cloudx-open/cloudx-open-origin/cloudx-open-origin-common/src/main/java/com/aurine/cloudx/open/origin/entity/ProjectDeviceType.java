

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备信息表
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:39:33
 */
@Data
@TableName("project_device_type")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备类型表")
public class ProjectDeviceType extends OpenBasePo<ProjectDeviceType> {
    private static final long serialVersionUID = 1L;
    /**
     * 设备类型id
     */
    @ApiModelProperty(value = "设备类型id")
    @TableId(type = IdType.INPUT)
    private String deviceTypeId;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    /**
     * 设备类型编码
     */
    @ApiModelProperty(value = "设备类型编码")
    private String deviceTypeCode;
    /**
     * 设备类型名称
     */
    @ApiModelProperty(value = "设备类型名称")
    private String deviceTypeName;
}
