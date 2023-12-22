package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectStaffRota;
import com.aurine.cloudx.open.origin.entity.ProjectStaffRotaDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: ProjectStaffRotaPageVo
 * Description: 员工值班查询Vo
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/8/03 14:49
 */
@Data
@ApiModel(value = "员工值班列表")
public class ProjectStaffRotaPageVo extends ProjectStaffRota {
    @ApiModelProperty("员工值班详细列表")
    List<ProjectStaffRotaDetail> projectStaffRotaDetails;
}
