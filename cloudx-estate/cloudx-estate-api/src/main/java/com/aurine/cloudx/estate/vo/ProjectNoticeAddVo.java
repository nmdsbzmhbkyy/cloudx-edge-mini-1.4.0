package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectNotice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Title: ProjectNoticeAddVo
 * Description: 新增信息发布
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/5/20 16:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("新增信息发布Vo")
public class ProjectNoticeAddVo extends ProjectNotice {
    /**
     * 设备id列表
     */
    @ApiModelProperty("设备id列表")
    List<String> deviceId;

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

}
