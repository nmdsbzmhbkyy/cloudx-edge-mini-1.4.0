package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 车辆持有统计
 * @ClassName: ProjectComplaintRecordMview
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-29 14:37
 * @Copyright:
 */
@Data
@TableName("PROJECT_PERSON_CAR_MVIEW")
public class ProjectPersonCarMview extends BaseDashboardEntity {


    /**
     * 总人数
     */
    @ApiModelProperty("总人数")
    @TableField("CNT_TOTAL")
    private Long total;
    /**
     * 有车人数
     */
    @ApiModelProperty("有车人数")
    @TableField("CNT_HAVE_CAR")
    private Long haveCar;
    /**
     * 无车人数
     */
    @ApiModelProperty("无车人数")
    @TableField("CNT_NOT_HAVE_CAR")
    private Long notHaveCar;
}
