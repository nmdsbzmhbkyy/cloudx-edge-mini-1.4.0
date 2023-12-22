

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备拓展属性配置表
 *
 * @author xull@aurine.cn
 * @date 2020-07-03 15:19:06
 */
@Data
@TableName("project_device_attr_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备拓展属性配置表")
public class ProjectDeviceAttrConf extends OpenBasePo<ProjectDeviceAttrConf> {
    private static final long serialVersionUID = 1L;

    /**
     * 属性id, uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "属性id, uuid")
    private String attrId;
    /**
     * 设备类型id
     */
    @TableId
    @ApiModelProperty(value = "设备类型id")
    private String deviceTypeId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer projectId;

    /**
     * 属性编码, 如xxxType
     */
    @ApiModelProperty(value = "属性编码, 如xxxType")
    private String attrCode;
    /**
     * 属性名称
     */
    @ApiModelProperty(value = "属性名称")
    private String attrName;
    /**
     * 值描述
     */
    @ApiModelProperty(value = "值描述")
    private String remark;

    /**
     * 值类型，int 整型 float 浮点型 bool 布尔型 char 字符型
     */
    @ApiModelProperty(value = "值类型，int 整型 float 浮点型 bool 布尔型 char 字符型")
    private String valueType;
    /**
     * 最小值
     */
    @ApiModelProperty(value = "最小值")
    private Float valuemin;
    /**
     * 最大值
     */
    @ApiModelProperty(value = "最大值")
    private Float valuemax;
    /**
     * 值精度
     */
    @ApiModelProperty(value = "值精度")
    private Float valuePrecision;
    /**
     * 状态描述，适用于布尔型
     */
    @ApiModelProperty(value = "状态描述，适用于布尔型")
    private String valueDesc;
    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;
}
