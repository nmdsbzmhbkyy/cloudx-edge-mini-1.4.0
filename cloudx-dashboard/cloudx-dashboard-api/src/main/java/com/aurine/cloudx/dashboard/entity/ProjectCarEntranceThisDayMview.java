package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 车辆进出统计-今日
 *
 * @ClassName: projectCarEntranceThisDayMview
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-30 14:37
 * @Copyright:
 */
@Data
@TableName("PROJECT_CAR_ENTRANCE_THISDAY_MVIEW")
public class ProjectCarEntranceThisDayMview extends BaseDashboardEntity {


    /**
     * 今日车辆数
     */
    @ApiModelProperty("今日车辆数")
    @TableField("CNT_CAR_EVENT")
    private Long total;


}
