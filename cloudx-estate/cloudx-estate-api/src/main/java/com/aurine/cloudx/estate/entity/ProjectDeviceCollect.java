
package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

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
public class ProjectDeviceCollect extends Model<ProjectDeviceCollect> {
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

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
