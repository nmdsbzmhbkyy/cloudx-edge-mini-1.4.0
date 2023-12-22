

package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 巡更记录</p>
 * @ClassName: ProjectPatrolMview
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-12 15:14
 * @Copyright:
 */
@Data
@TableName("PROJECT_PATROL_MVIEW")
public class ProjectPatrolMview extends BaseDashboardEntity {

    /**
     * 待巡更
     */
    @ApiModelProperty(value = "待巡更")
    @TableField("CNT_TO_BE")
    private Long toDo;
    /**
     * 巡更中
     */
    @ApiModelProperty(value = "巡更中")
    @TableField("CNT_IN")
    private Long doing;

    /**
     * 已巡更
     */
    @ApiModelProperty(value = "已巡更")
    @TableField("CNT_COMPLETE")
    private Long complete;

    /**
     * 已过期
     */
    @ApiModelProperty(value = "已过期")
    @TableField("CNT_EXPIRED")
    private Long expired;

}
