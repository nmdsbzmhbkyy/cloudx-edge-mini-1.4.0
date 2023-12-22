package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: ServiceProjectSaveVo
 * Description:项目新增增值服务时，用于接收前端传来的参数
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/6/5 11:54
 */
@Data
@ApiModel(value = "新增项目增值服务表单对象")
public class ServiceProjectSaveVo {
    @ApiModelProperty("增值服务id列表")
    private List<String> serviceIds;
    @ApiModelProperty("项目id")
    private Integer projectId;
    @ApiModelProperty("截止时间")
    private String expTime;
}
