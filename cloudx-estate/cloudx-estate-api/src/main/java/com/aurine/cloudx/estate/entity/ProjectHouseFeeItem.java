package com.aurine.cloudx.estate.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 房屋费用设置(ProjectHouseFeeItem)表实体类
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "房屋费用设置(ProjectHouseFeeItem)")
public class ProjectHouseFeeItem extends Model<ProjectHouseFeeItem> {

    private static final long serialVersionUID = -45590449487607214L;


    /**
     * 记录id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "记录id")
    private String recordId;


    /**
     * 房间id
     */
    @ApiModelProperty(value = "房间id")
    private String houseId;


    /**
     * 费用id
     */
    @ApiModelProperty(value = "费用id")
    private String feeId;

    /**
     * 账单月份
     */
    @ApiModelProperty("账单月份")
    private String billMonth;


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