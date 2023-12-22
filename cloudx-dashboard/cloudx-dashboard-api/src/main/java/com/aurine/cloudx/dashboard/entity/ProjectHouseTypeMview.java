package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 房屋类型
 * @ClassName: ProjectHouseTypeMview
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25 15:13
 * @Copyright:
 */
@Data
@TableName("PROJECT_HOUSE_TYPE_MVIEW")
public class ProjectHouseTypeMview extends BaseDashboardEntity {


    /**
     * 房屋总数
     */
    @ApiModelProperty("房屋总数")
    @TableField("CNT_TOTAL")
    private Long cntTotal;

    /**
     * 自住房数
     */
    @ApiModelProperty("自住房数")
    @TableField("CNT_OWNER")
    private Long cntOwner;

    /**
     * 空置房数
     */
    @ApiModelProperty("空置房数")
    @TableField("CNT_IDLE")
    private Long cntIdle;

    /**
     * 出租屋数
     */
    @ApiModelProperty("出租屋数")
    @TableField("CNT_RENT")
    private Long cntRent;


}
