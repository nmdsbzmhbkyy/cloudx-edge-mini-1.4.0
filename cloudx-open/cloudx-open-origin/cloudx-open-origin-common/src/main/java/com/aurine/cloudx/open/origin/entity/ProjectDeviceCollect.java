
package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目设备采集参数
 *
 * @author xull@aurine.cn
 * @date 2020-06-12 11:43:43
 */
@Data
@TableName("project_device_collect")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目设备采集参数")
public class ProjectDeviceCollect extends OpenBasePo<ProjectDeviceCollect> {
    private static final long serialVersionUID = 1L;

    /**
     * 设备类型 1 人脸采集设备 2 身份证采集设备
     */
    @ApiModelProperty(value = "设备类型 1 人脸采集设备 2 身份证采集设备")
    private String deviceType;
    /**
     * 参数id
     */
    @ApiModelProperty(value = "参数id")
    private String attrId;
    /**
     * 参数值
     */
    @ApiModelProperty(value = "参数值")
    private String attrValue;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
}
