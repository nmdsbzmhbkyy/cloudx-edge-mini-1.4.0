package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * 设备巡检路线设置(ProjectInspectRouteConfVo)对象
 *
 * @author 王良俊
 * @since 2020-07-24 17:16:18
 */
@Data
@ApiModel(value = "设备巡检路线查询条件vo对象")
public class ProjectInspectRouteConfSearchConditionVo {

    /**
     * 制定时间范围
     */
    private String[] dateRange;

    /**
     * 路线名
     */
    private String inspectRouteName;

    /**
     * 执行顺序
     */
    private String isSort;
}