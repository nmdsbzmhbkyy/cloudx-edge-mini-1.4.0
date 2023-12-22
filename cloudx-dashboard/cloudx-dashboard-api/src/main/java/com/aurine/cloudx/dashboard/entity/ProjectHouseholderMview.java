package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 住户类型信息
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-24
 * @Copyright:
 */
@Data
@TableName("PROJECT_HOUSEHOLDER_MVIEW")
public class ProjectHouseholderMview extends BaseDashboardEntity {


    /**
     * 住户总数
     */
    @ApiModelProperty("住户总数")
    @TableField("CNT_TOTAL")
    private Long cntTotal;

    /**
     * 业主数量
     */
    @ApiModelProperty("业主数量")
    @TableField("CNT_OWNER")
    private Long cntOwner;

    /**
     * 家属数量
     */
    @ApiModelProperty("家属数量")
    @TableField("CNT_FAMILY")
    private Long cntFamily;

    /**
     * 租客数量
     */
    @ApiModelProperty("租客数量")
    @TableField("CNT_RENT")
    private Long cntRent;


}
