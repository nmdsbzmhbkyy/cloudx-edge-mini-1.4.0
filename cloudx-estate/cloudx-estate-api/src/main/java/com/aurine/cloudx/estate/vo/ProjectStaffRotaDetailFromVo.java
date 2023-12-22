package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectStaffRotaDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Title: ProjectStaffRotaDetailFromVo
 * Description: 员工值班详情表单Vo
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/7/31 14:50
 */
@Data
@ApiModel(value = "值班安排详细表单对象")
public class ProjectStaffRotaDetailFromVo extends ProjectStaffRotaDetail {
    @ApiModelProperty("开始时间（字符串）")
    private String startTimeString;

    @ApiModelProperty("结束时间（字符串）")
    private String endTimeString;
}
