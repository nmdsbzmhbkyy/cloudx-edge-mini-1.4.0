package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 员工管辖区域设置(ProjectStaffRegion)表实体类
 *
 * @author 王良俊
 * @since 2020-12-09 15:04:22
 */
@Data
@TableName("project_staff_region")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "员工管辖区域设置(ProjectStaffRegion)")
public class ProjectStaffRegion extends Model<ProjectStaffRegion> {

    private static final long serialVersionUID = 613835533127286336L;

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value = "序列")
    private Integer seq;


    /**
     * uid，可作为项目员工的唯一标识
     */
    @ApiModelProperty(value = "uid，可作为项目员工的唯一标识")
    private String staffId;


    /**
     * 区域id
     */
    @ApiModelProperty(value = "区域id")
    private String regionId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}