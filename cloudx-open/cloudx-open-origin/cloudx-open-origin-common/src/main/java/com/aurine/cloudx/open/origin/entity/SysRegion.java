package com.aurine.cloudx.open.origin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("sys_region")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "城市信息")
public class SysRegion extends Model<SysRegion> {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="id")
    private String uid;
    /**
     * 城市名称
     */
    @ApiModelProperty(value="城市名称")
    private String regionName;
    /**
     * 城市code
     */
    @ApiModelProperty(value="城市code")
    private String parRegionCode;
    /**
     * 城市codeList
     */
    @ApiModelProperty(value="城市codeList")
    private String parRegionList;

    /**
     * 城市级别
     */
    @ApiModelProperty(value="城市级别")
    private Integer rLevel;

    /**
     * 状态
     */
    @ApiModelProperty(value="状态")
    private String status;

    /**
     * 描述
     */
    @ApiModelProperty(value="描述")
    private String remark;

    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;
}
