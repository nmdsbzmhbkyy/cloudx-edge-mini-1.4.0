package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

/**
 * 值班明细(ProjectStaffRotaDetail)表实体类
 *
 * @author guhl@aurine.cn
 * @since 2020-08-03 11:49:09
 */
@Data
@TableName("project_staff_rota_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "值班明细(ProjectStaffRotaDetail)")
public class ProjectStaffRotaDetail extends OpenBasePo<ProjectStaffRotaDetail> {

    private static final long serialVersionUID = -30263876504240804L;

    /**
     * 值班明细id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "值班明细id，uuid")
    private String rotaDetailId;

    /**
     * 值班表id
     */
    @ApiModelProperty(value = "值班表id")
    private String rotaId;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalTime endTime;

    /**
     * 值班人员列表，填姓名，逗号分隔
     */
    @ApiModelProperty(value = "值班人员列表，填姓名，逗号分隔")
    private String rotaStaffList;

    /**
     * 接收电话开门人员
     */
    @ApiModelProperty(value = "接收电话开门人员")
    private String staffName;

}