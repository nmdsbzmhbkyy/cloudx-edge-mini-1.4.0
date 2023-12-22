package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

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
public class ProjectStaffRota extends OpenBasePo<ProjectStaffRota> {

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

}