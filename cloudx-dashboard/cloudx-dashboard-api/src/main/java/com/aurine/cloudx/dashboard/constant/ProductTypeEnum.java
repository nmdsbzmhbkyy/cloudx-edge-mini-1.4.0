package com.aurine.cloudx.dashboard.constant;

import lombok.AllArgsConstructor;

/**
 * @description: 产品类型枚举 基于华为中台3.0
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-09
 * @Copyright:
 */
@AllArgsConstructor
public enum ProductTypeEnum {

    P020101("020101","DW_DEV_DATA_020101", "烟感", "0201", "安全报警类", "02", "安全防范"),

    P020201("020201","", "IPC摄像头", "0202", "视频监控类", "02", "安全防范"),
    P020202("020202", "","带云台摄像头", "0202", "视频监控类", "02", "安全防范"),

    P020301("020301", "","室内机", "0203", "可视对讲类", "02", "安全防范"),
    P020302("020302", "","大门公共终端", "0203", "可视对讲类", "02", "安全防范"),
    P020307("020307", "","门前机", "0203", "可视对讲类", "02", "安全防范"),
    P020399("020399", "","管理员机", "0203", "可视对讲类", "02", "安全防范"),
    P020320("020320", "","WR20接入网关", "0203", "可视对讲类", "02", "安全防范"),

    P020401("020401", "","车辆道闸", "0204", "车辆通行类", "02", "安全防范"),

    P030001("030001", "DW_DEV_DATA_030001","垃圾分类箱", "0300", "公共服务杂类", "03", "物联传感"),
    P030010("030010", "DW_DEV_DATA_030010","充电桩", "0300", "公共服务杂类", "03", "物联传感"),
    P030020("030020", "DW_DEV_DATA_030020","智能水表", "0300", "公共服务杂类", "03", "物联传感"),
    P030201("030201", "DW_DEV_DATA_030201","消防栓", "0300", "公共服务杂类", "03", "物联传感"),
    P030301("030301", "DW_DEV_DATA_030301","智慧路灯", "0300", "公共服务杂类", "03", "物联传感"),


    P030101("030101", "DW_DEV_DATA_030101","环境监测", "0301", "监测类", "03", "物联传感"),
    P030102("030102", "DW_DEV_DATA_030102","井下液位计", "0301", "监测类", "03", "物联传感"),
    P030103("030103", "DW_DEV_DATA_030103","路面积水仪", "0301", "监测类", "03", "物联传感"),
    P030104("030104", "","液压检测", "0301", "监测类", "03", "物联传感"),
    P030105("030105", "DW_DEV_DATA_030105","井盖监控器", "0301", "监测类", "03", "物联传感"),
    P030106("030106", "","地磁", "0301", "监测类", "03", "物联传感");


    /**
     * 产品代码
     */
    public String productCode;

    /**
     * 数据表
     */
    public String tableName;


    /**
     * 产品名称
     */
    public String productName;


    /**
     * 产品中类
     */
    public String productMiddleType;

    /**
     * 产品中类
     */
    public String productMiddleTypeName;

    /**
     * 产品大类
     */
    public String productType;
    /**
     * 产品大类
     */
    public String productTypeName;


    /**
     * @param productCode
     * @return
     */
    public static ProductTypeEnum getProductCode(String productCode) {
        for (ProductTypeEnum value : ProductTypeEnum.values()) {
            if (value.productCode.equals(productCode)) {
                return value;
            }
        }
        return null;
    }


}
