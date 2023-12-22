

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备拓展属性表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 15:18:23
 */
@Data
@TableName("project_device_attr")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备拓展属性表")
public class ProjectDeviceAttr extends Model<ProjectDeviceAttr> {
    private static final long serialVersionUID = 1L;

    /**
     * 属性拓展表主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "属性拓展表主键")
    private Integer seq;

    /**
     * 属性id
     */
    @ApiModelProperty(value = "属性id")
    private String attrId;
    /**
     * 设备id, 32位uuid
     */
    @ApiModelProperty(value = "设备id, 32位uuid")
    private String deviceId;
    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private String deviceType;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    /**
     * 属性编码
     */
    @ApiModelProperty(value = "属性编码")
    private String attrCode;

    /**
     * 属性值
     */
    @ApiModelProperty(value = "属性值")
    private String attrValue;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
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
