package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *  员工工作情况-用于员工查看-工作情况数据展示
 * </p>
 *
 * @ClassName: ProjectWorkConditionVo
 * @author: 王良俊 <>
 * @date: 2020年12月11日 下午04:44:24
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStaffAllWorkVo {

    /**
     * 员工ID
     *
     */
    @ApiModelProperty("员工ID")
    private String staffId;

    /**
     * 投诉统计
     * */
    @ApiModelProperty("投诉统计")
    private ProjectStaffWorkVo complainRecord;

    /**
     * 报修统计
     * */
    @ApiModelProperty("报修统计")
    private ProjectStaffWorkVo RepairRecord;

    /**
     * 巡检统计
     * */
    @ApiModelProperty("巡检统计")
    private ProjectStaffWorkVo inspectTask;
    /**
     * 巡更统计
     * */
    @ApiModelProperty("巡更统计")
    private ProjectStaffWorkVo patrolInfo;

}
