package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class ProjectStaffRegion extends OpenBasePo<ProjectStaffRegion> {

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


}