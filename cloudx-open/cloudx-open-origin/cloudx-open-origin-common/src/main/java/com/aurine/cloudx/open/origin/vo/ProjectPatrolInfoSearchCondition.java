

package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目巡更记录(ProjectPatrolInfo)表实体类
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-09 08:48:43
 */
@Data
@ApiModel(value = "项目巡更记录")
public class ProjectPatrolInfoSearchCondition {
private static final long serialVersionUID = 1L;

    /*
     巡更日期
    */
    @ApiModelProperty(value="巡更日期")
    private String patrolDate;
    /*
    巡更状态 参考字典类型 patrol_status
     */
    @ApiModelProperty(value="巡更状态 0.待巡更 1.巡更中 2.已完成 3.已过期 参考字典类型 patrol_status")
    private String status;
    /*
    巡更结果 1 正常 2 异常
     */
    @ApiModelProperty(value="巡更结果 1 正常 2 异常")
    private String result;
    /*
    签到情况 1 正常 2 异常
     */
    @ApiModelProperty(value="签到情况 1 正常 2 异常")
    private String checkInStatus;
    /*
    巡更路线名称
     */
    @ApiModelProperty(value = "巡更路线名称")
    private String patrolRouteName;
    /**
     *  巡更年月
     */
    @ApiModelProperty(value = "巡更年月")
    private String date;

}
