package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.SysEventTypeConf;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: cloudx
 * @description: 用于预案关联展示
 * @author: 谢泽毅
 * @create: 2021-07-16 11:56
 **/
@Data
@ApiModel("预案关联展示")
public class SysEventTypeConfVo extends SysEventTypeConf {
    @ApiModelProperty("预案id")
    private String planId;
}
