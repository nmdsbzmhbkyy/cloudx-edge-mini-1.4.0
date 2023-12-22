package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectRepairRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Title: ProjectRepairRecordFromVo
 * Description: 报修服务表单提交Vo
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/7/24 11:49
 */
@Data
@ApiModel(value = "报修服务表单对象")
public class ProjectRepairRecordFromVo extends ProjectRepairRecord {
    /**
     * 预约时间范围:开始时间
     * <p>
     * 字符串类型 用于接收前端传值
     */
    @ApiModelProperty("预约时间范围:开始时间")
    private String reserveTimeBeginString;
    /**
     * 预约时间范围:结束时间
     */
    @ApiModelProperty("预约时间范围:开始时间")
    private String reserveTimeEndString;
}
