package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 项目物业联系方式表(ProjectPropertyContact)表实体类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-10-27 15:38:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目物业联系方式表(ProjectPropertyContact)")
public class ProjectPropertyContact extends Model<ProjectPropertyContact> {

    private static final long serialVersionUID = 205407180602813024L;

    /**
     * 序列
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "序列")
    private Long seq;


    /**
     * 名称
     */
    @ApiModelProperty(value = "联系人名称")
    private String name;


    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String contactPhone;


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