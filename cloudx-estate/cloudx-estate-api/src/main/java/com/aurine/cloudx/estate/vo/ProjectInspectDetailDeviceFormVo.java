package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectInspectDetailCheckItem;
import com.aurine.cloudx.estate.entity.ProjectInspectDetailDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * (ProjectInspectDetailDeviceFormVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/11/11 16:47
 */
@Data
@ApiModel(value = "巡检任务明细设备列表表单视图")
public class ProjectInspectDetailDeviceFormVo extends ProjectInspectDetailDevice {

    /**
     * 检查项
     */
    @ApiModelProperty(value = "检查项")
    private List<ProjectInspectDetailCheckItem> detailCheckItems;

}