package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 人员地区分布
 * @ClassName: ProjectPersonRegionMview
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-29 14:37
 * @Copyright:
 */
@Data
@TableName("PROJECT_PERSON_REGION_MVIEW")
public class ProjectPersonRegionMview extends BaseDashboardEntity {


    /**
     * 外省人数
     */
    @ApiModelProperty("外省人数")
    @TableField("CNT_OUT_PROVINCE")
    private Long outProvince;

    /**
     * 本省人数
     */
    @ApiModelProperty("本省人数")
    @TableField("CNT_IN_PROVINCE")
    private Long inProvince;

    /**
     * 本市人数
     */
    @ApiModelProperty("本市人数")
    @TableField("CNT_IN_CITY")
    private Long inCity;

    /**
     * 未知地区人数
     */
    @ApiModelProperty("未知地区人数")
    @TableField("CNT_IN_UNKNOWN")
    private Long unknown;



}
