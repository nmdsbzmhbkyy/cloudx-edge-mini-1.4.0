package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("DevParamObj")
public class AurineEdgePerimeterDeviceParamsDTO {
    @JSONField(name="channelList")
    private List<String> channelNameList;

    private String paramDevId;

    private String passwd;
}
