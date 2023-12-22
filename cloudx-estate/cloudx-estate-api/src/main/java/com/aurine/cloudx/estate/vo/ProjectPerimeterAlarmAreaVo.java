package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 周界报警防区信息
 *
 * @author 邹宇
 * @date 2021-6-15 14:45:18
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPerimeterAlarmAreaVo {


    /**
     * 信息uuid
     */
    private String infoUid;

    /**
     * 报警主机设备id
     */
    private String deviceId;


    /**
     * 设备号
     */
    private String moduleNo;


    /**
     * 防区号
     */
    private String channelNo;

    /**
     * 防区名称
     */
    private String channelName;

    /**
     * 防区类型 1 周界 9 其他
     */
    private String channelType;

    /**
     * 布防状态 0 撤防 1 布防 2 旁路
     */
    private String armedStatus;


    /*
        区域名称
     */
    private String regionName;
    /**
     * 区域id
     */
    private String regionId;


    /**
     * 区域Id
     */
    private String deviceRegionId;
}
