package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineConfigDTO;
import lombok.Data;

/**
 * @description: 请求对象
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-11
 * @Copyright:
 */
@Data
public class AurineRequestObject {

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
     * 数据请求类型：form/json
     */
    private String dataType;

    /**
     * 请求内容
     */
    private JSONObject requestJson;

    /**
     * 原始请求数据
     */
    private JSONObject requestObjectJson;

    /**
     * 原请求关联数据主键，用于异步数据回传时确认原数据
     */
    private String uid;

    /**
     * 连接配置
     */
    private AurineConfigDTO config;

    public int getProjectId(){
        return this.config.getProjectId();
    }

    /**
     * 是否为项目级存储
     * 如果是项目级，每个项目独立存储token
     * @return
     */
    public boolean isProject(){
        return this.config.getUseByProject();
    }

}
