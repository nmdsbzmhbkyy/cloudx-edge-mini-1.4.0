package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目员工值班表信息(ProjectStaffRota)表实体类
 *
 * @author guhl@aurine.cn
 * @since 2020-08-03 11:48:49
 */
@Data
@TableName("project_staff_rota")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目员工值班表信息(ProjectStaffRota)")
public class ProjectStaffRota extends Model<ProjectStaffRota> {

    private static final long serialVersionUID = 358818169627554611L;

    /**
     * 值班表id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "值班表id，uuid")
    private String rotaId;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

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