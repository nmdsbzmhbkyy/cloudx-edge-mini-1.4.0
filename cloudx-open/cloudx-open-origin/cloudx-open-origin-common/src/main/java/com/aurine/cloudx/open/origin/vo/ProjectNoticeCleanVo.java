package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: ProjectNoticeCleanVo
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/22 9:29
 */
@Data
@ApiModel("消息清除Vo")
public class ProjectNoticeCleanVo {
    /**
     * 设备id列表
     */
    @ApiModelProperty("设备id列表")
    private List<String> deviceIds;
}
