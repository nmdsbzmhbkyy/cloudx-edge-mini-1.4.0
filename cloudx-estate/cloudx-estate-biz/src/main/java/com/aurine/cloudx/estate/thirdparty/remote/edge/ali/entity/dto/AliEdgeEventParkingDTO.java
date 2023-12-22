package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.dto;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 阿里边缘网关 DTO对象
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-22
 * @Copyright:
 */
@Data
public class AliEdgeEventParkingDTO {

    @ApiModelProperty(value = "主要信息")
    JSONObject data;

    @ApiModelProperty(value = "数据模型ID:如 iot_park_pass_record")
    String modelId;


}
