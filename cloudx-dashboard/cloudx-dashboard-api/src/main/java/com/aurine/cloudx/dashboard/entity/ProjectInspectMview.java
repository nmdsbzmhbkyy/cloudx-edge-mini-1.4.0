

package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 巡检
 *
 * @author 黄阳光 code generator
 * @date 2020-05-20 13:27:59
 */
@Data
@TableName("PROJECT_INSPECT_MVIEW")
public class ProjectInspectMview extends BaseDashboardEntity {

    /**
     * 待巡检
     */
    @ApiModelProperty(value = "待巡检")
    @TableField("CNT_TO_BE")
    private Long toDo;
    /**
     * 巡检中
     */
    @ApiModelProperty(value = "巡检中")
    @TableField("CNT_IN")
    private Long doing;

    /**
     * 已巡检
     */
    @ApiModelProperty(value = "已巡检")
    @TableField("CNT_COMPLETE")
    private Long complete;
    /**
     * 未巡检
     */
    @ApiModelProperty(value = "未巡检")
    @TableField("CNT_CANCELED")
    private Long canceled;
    /**
     * 已过期
     */
    @ApiModelProperty(value = "已过期")
    @TableField("CNT_EXPIRED")
    private Long expired;

}
