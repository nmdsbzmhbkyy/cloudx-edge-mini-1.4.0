package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectStaffRota;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Title: ProjectStaffRotaFromVo
 * Description: 员工值班信息表单Vo
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/7/31 14:50
 */
@Data
@ApiModel(value = "值班安排表单对象")
public class ProjectStaffRotaFromVo extends ProjectStaffRota {
    @ApiModelProperty("开始日期（字符串）")
    private String startDateString;

    @ApiModelProperty("结束日期（字符串）")
    private String endDateString;
}
