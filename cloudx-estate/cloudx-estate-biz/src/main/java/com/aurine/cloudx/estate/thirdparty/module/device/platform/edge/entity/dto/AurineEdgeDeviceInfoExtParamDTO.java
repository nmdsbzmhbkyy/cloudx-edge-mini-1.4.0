package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AurineEdgeDeviceInfoExtParamDTO {

    /**
     * 旧设备编号
     */
    private String deviceNoOld;
    /**
     * 设备Ip
     */
    private String deviceIp;

    /**
     * 设备端口
     */
    private Integer devicePort;

    /**
     * 硬件版本
     */
    private String hardwareVersion;

    /**
     * 软件版本
     */
    private String softwareVersion;

    /**
     * 是否自动注册	0：手动；1：自动
     */
    private String autoReg;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 事件上报间隔
     */
    private Integer interval;
    /**
     * 摄像头设备信息
     */
    private List<AurineEdgeChannelInfoDTO> channelInfoList;

}
