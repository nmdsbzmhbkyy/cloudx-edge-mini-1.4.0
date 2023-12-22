package com.aurine.cloudx.estate.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectElevatorFormVo {

    /**
    * 设备ID
    */
    private String deviceId;

    /**
    * 设备名
    */
    private String deviceName;

    /**
    * 设备区域ID
    */
    private String deviceRegionId;

    /**
    * 设备区域ID
    */
    private String deviceRegionName;

    /**
    * 楼栋ID
    */
    private String buildingId;

    /**
    * 楼栋ID
    */
    private String buildingName;

    /**
    * 单元ID
    */
    private String unitId;

    /**
    * 单元ID
    */
    private String unitName;

    /**
    * 分层控制器设备ID列表
    */
    private List<String> layerControlDeviceIdList;

    /**
    * 乘梯识别终端设备ID列表
    */
    private List<String> recognizerDeviceIdList;

}
