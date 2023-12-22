package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import lombok.Data;

/**
 * @description: 请求对象
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-03
 * @Copyright:
 */
@Data
public class HuaweiRequestObject {

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 连接地址
     */
    private String uri;

    /**
     * 版本
     */
    private String version;

    /**
     * 请求方法：post/get
     */
    private String method;

    /**
     * 请求内容
     */
    private JSONObject requestJson;
    /**
     * 请求内容
     */
    private JSONArray requestJsonArray;

    /**
     * 原请求关联数据主键，用于异步数据回传时确认原数据
     */
    private String uid;

    /**
     * 华为配置数据对象
     */
    private HuaweiConfigDTO config;
    /**
     * 数据来源：华为直连设备，或WR20
     */
    private String source;

    /**
     * 数据来源的版本号
     */
    private String sourceVersion;
    /**
     * WR20的设备类型
     */
    private String wr20DeviceType;
    /**
     * 第三方主键
     */
    private String uuid;

    /**
     * 项目ID
     */
    private int projectId;

}
