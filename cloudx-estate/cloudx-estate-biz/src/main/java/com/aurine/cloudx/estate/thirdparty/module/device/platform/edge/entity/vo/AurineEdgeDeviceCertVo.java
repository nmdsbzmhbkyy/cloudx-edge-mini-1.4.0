package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo;


import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import lombok.Data;

import java.util.List;

/**
 * 下发凭证数据vo
 *
 * @ClassName: AurineEdgeDeviceCertVo
 * @author: 邹宇
 * @date: 2022-2-22 11:32:58
 * @Copyright:
 */
@Data
public class AurineEdgeDeviceCertVo {

    //冠林边缘网关配置信息
    AurineEdgeConfigDTO config;

    //设备对象
    ProjectDeviceInfo deviceInfo;

    // 人脸/卡片
    String certType;

    //请求类型
    String action;

    //JSON
    JSONObject paramsJsonArray;

    //id
    List<String> doorKeyIdList;

    //优先级
    Integer priotity;
}
