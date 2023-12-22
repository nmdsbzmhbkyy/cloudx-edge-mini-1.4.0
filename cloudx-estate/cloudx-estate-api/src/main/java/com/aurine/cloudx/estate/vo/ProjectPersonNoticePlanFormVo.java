package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectPersonNoticePlan;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * (ProjectPersonNoticePlanFormVo)
 *
 * @author guhl
 * @version 1.0.0
 * @date 2020/12/15 15:51
 */
@Data
@ApiModel("住户通知计划表单vo")
public class ProjectPersonNoticePlanFormVo extends ProjectPersonNoticePlan {
    /**
     * 设备id列表
     */
    @ApiModelProperty("房屋id列表")
    List<String> houseId;

    /**
     * 发送目标列表
     */
    @ApiModelProperty("发送目标列表")
    List<String> checkTargetType;

    @ApiModelProperty("房屋名称列表")
    List<String> houseName;
}
