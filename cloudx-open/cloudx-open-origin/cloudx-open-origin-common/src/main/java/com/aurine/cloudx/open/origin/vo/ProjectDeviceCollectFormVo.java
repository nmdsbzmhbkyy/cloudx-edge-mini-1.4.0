package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceCollect;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: ProjectDeviceCollectFormVo
 * Description: 设备采集参数添加表达
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/6/12 14:32
 */
@Data
@ApiModel("设备采集参数添加表单对象")
public class ProjectDeviceCollectFormVo {
    /**
     * 设备采集参数key-value视图列表
     */
    @ApiModelProperty(value = "设备采集参数key-value视图列表")
    List<ProjectDeviceCollect> projectDeviceCollectList = new ArrayList<>();
    /**
     * 设备采集类型
     */
    @ApiModelProperty(value = "设备采集类型")
    private String type;
    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Integer projectId;
}
