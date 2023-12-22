

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 记录设备平面图位置打点信息
 *
 * @author lingang
 * @date 2020-06-15 16:07:41
 */
@Data
@TableName("project_device_location")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "记录设备平面图位置打点信息")
public class ProjectDeviceLocation extends OpenBasePo<ProjectDeviceLocation> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id,自增序列
     */
    @ApiModelProperty(value = "主键id,自增序列")
    private String seq;

    /**
     * 设备id, 关联project_device_info.deviceId
     */
    @ApiModelProperty(value = "设备id, 关联project_device_info.deviceId")
    private String deviceId;
    /**
     * 关联project_floor_pic.picId
     */
    @ApiModelProperty(value = "关联project_floor_pic.picId")
    private String picId;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private BigDecimal lon;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;
    /**
     * 缩放比例
     */
    @ApiModelProperty(value = "缩放比例")
    private Integer zoom;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
}
