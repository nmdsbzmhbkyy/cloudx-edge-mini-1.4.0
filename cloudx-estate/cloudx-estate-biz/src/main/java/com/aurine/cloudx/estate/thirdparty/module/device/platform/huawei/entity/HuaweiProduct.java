package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;

/**
 * 华为中台 产品对象
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-11
 * @Copyright:
 */
@Data
public class HuaweiProduct {

    private String productId; //产品ID	string	（这是自增序列系统中要使用productKey）
    private String productType;//	产品品类	string	是	见产品品类定义
    private String name;//	产品名称	string	是
    private String productModel;//	产品型号	string	否
    private String modelId;//	产品型号ＩＤ	string	是	产品型号ID
    private String manufacturer;//	厂商名称	string	否
    private String desc;//	产品描述	string	否
    private String protocol;//	协议类型	string	否
    private String dataFormat;//	数据格式	string	否	设备上报的数据格式
    private String industry;//	所属行业	string	否
    @JacksonInject("capabilities")
    private ArrayNode capabilities;//	设备能力属性	对象数组	否	设备的服务能力列表
    private String adaptionId;//	接口适配ID	string	否	设备接入接口适配ID，接口接入分发时，需知道使用哪个接口适配
    private String capability;//能力集
    private String productKey;//产品key
    private String modelType;//产品key

}
