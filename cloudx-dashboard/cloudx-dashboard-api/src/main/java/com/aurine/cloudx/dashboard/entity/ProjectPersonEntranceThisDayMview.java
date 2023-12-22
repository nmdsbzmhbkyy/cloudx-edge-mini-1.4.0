package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 人员进出统计-今日
 *
 * @ClassName: ProjectPersonEntranceThisDayMview
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-30 14:37
 * @Copyright:
 */
@Data
@TableName("PROJECT_PERSON_ENTRANCE_THISDAY_MVIEW")
public class ProjectPersonEntranceThisDayMview extends BaseDashboardEntity {


    /**
     * 今日人员总数
     */
    @ApiModelProperty("今日人员总数")
    @TableField("CNT_TOTAL")
    private Long total;
    /**
     * 业主数
     */
    @ApiModelProperty("业主数")
    @TableField("CNT_OWNER")
    private Long owner;
    /**
     * 家属数
     */
    @ApiModelProperty("家属数")
    @TableField("CNT_FAMILY")
    private Long family;
    /**
     * 租客数
     */
    @ApiModelProperty("租客数")
    @TableField("CNT_RENT")
    private Long rent;
    /**
     * 员工数
     */
    @ApiModelProperty("员工数")
    @TableField("CNT_STAFF")
    private Long staff;
    /**
     * 访客数
     */
    @ApiModelProperty("访客数")
    @TableField("CNT_VISITOR")
    private Long visitor;

}
