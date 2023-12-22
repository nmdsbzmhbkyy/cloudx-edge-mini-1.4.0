package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeErrorEnum;
import lombok.Data;

/**
 * 华为中台 响应数据 DTO
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-25
 * @Copyright:
 */
@Data
public class AurineEdgeRespondDTO {

    /**
     * 异常结果
     */
    private AurineEdgeErrorEnum errorEnum;


    /**
     * 异常信息
     */
    private String errorMsg;


    private JSONArray bodyArray;

    private JSONObject bodyObj;


}
