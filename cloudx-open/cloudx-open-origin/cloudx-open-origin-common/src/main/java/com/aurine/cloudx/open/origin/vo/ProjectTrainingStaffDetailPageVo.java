package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectTrainingStaffDetailPageVo)培训分析详情分页vo
 *
 * @author guhl
 * @since 2021-01-14 08:37:39
 */
@Data
@ApiModel(value = "")
public class ProjectTrainingStaffDetailPageVo {
    /**
     * 员工id
     */
    @ApiModelProperty("员工id")
    String staffId;

    /**
     * 员工姓名
     */
    @ApiModelProperty("员工姓名")
    String staffName;

    /**
     * 需要阅读的资料总数
     */
    @ApiModelProperty("需要阅读的资料总数")
    Integer total;

    /**
     * 已完成阅读的资料数
     */
    @ApiModelProperty("已完成阅读的资料数")
    Integer doneCount;

    /**
     * 完成时间（降序排列）
     */
    @ApiModelProperty("完成时间（降序排列）")
    String completeTime;
}
