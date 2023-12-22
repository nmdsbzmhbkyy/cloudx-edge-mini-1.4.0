package com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums;

import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import lombok.AllArgsConstructor;

/**
 * @description: 4.0 告警事件类型枚举
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-07-16
 * @Copyright:
 */
@AllArgsConstructor
public enum EventTypeEnum {


    A002001("A002001", "3次密码错误告警", DeviceTypeEnum.LADDER_WAY_DEVICE),
    A002002("A002002", "长时间逗留报警", DeviceTypeEnum.LADDER_WAY_DEVICE),
    A002003("A002003", "3次刷卡错误告警", DeviceTypeEnum.LADDER_WAY_DEVICE),
    A002004("A002004", "挟持开门", DeviceTypeEnum.LADDER_WAY_DEVICE),
    A002005("A002005", "强行开门", DeviceTypeEnum.LADDER_WAY_DEVICE),
    A002006("A002006", "门未关报警", DeviceTypeEnum.LADDER_WAY_DEVICE),
    A002007("A002007", "防拆报警", DeviceTypeEnum.LADDER_WAY_DEVICE),
    A002008("A002008", "黑名单进门", DeviceTypeEnum.LADDER_WAY_DEVICE),
    A002009("A002009", "五次操作失败异常", DeviceTypeEnum.LADDER_WAY_DEVICE),
    A002010("A002010", "锁舌故障", DeviceTypeEnum.LADDER_WAY_DEVICE),
    A002011("A002011", "其他报警", DeviceTypeEnum.LADDER_WAY_DEVICE),

    A003001("A003001", "3次密码错误告警", DeviceTypeEnum.GATE_DEVICE),
    A003002("A003002", "长时间逗留报警", DeviceTypeEnum.GATE_DEVICE),
    A003003("A003003", "3次刷卡错误告警", DeviceTypeEnum.GATE_DEVICE),
    A003004("A003004", "挟持开门", DeviceTypeEnum.GATE_DEVICE),
    A003005("A003005", "强行开门", DeviceTypeEnum.GATE_DEVICE),
    A003006("A003006", "门未关报警", DeviceTypeEnum.GATE_DEVICE),
    A003007("A003007", "防拆报警", DeviceTypeEnum.GATE_DEVICE),
    A003008("A003008", "黑名单进门", DeviceTypeEnum.GATE_DEVICE),
    A003009("A003009", "五次操作失败异常", DeviceTypeEnum.GATE_DEVICE),
    A003010("A003010", "锁舌故障", DeviceTypeEnum.GATE_DEVICE),
    A003011("A003011", "其他报警", DeviceTypeEnum.GATE_DEVICE),


    /**
     * 室内终端
     */
    A001001("A001001","防区触发报警",DeviceTypeEnum.INDOOR_DEVICE),
    A001002("A001002","SOS报警",DeviceTypeEnum.INDOOR_DEVICE),
    A001003("A001003","防区故障报警",DeviceTypeEnum.INDOOR_DEVICE),
    A001004("A001004","防区破坏报警",DeviceTypeEnum.INDOOR_DEVICE),
    A001999("A001999","其他报警",DeviceTypeEnum.INDOOR_DEVICE),


    /**
     * 报警主机
     */
    A007001("A007001", "其他告警", DeviceTypeEnum.ALARM_DEVICE),
    A007002("A007002", "入侵告警", DeviceTypeEnum.ALARM_DEVICE),
    A007003("A007003", "触网告警", DeviceTypeEnum.ALARM_DEVICE),
    A007004("A007004", "短路告警", DeviceTypeEnum.ALARM_DEVICE),
    A007005("A007005", "断路告警", DeviceTypeEnum.ALARM_DEVICE),
    A007006("A007006", "防拆告警", DeviceTypeEnum.ALARM_DEVICE),
    A007007("A007007", "故障告警", DeviceTypeEnum.ALARM_DEVICE),

    /**
     * 智能水表
     */
    A008001("A008001", "故障", DeviceTypeEnum.WATER_METER_DEVICE),
    A008002("A008002", "超磁报警", DeviceTypeEnum.WATER_METER_DEVICE),
    A008003("A008003", "过流报警", DeviceTypeEnum.WATER_METER_DEVICE),
    A008004("A008004", "漏流报警", DeviceTypeEnum.WATER_METER_DEVICE),
    A008005("A008005", "开盖报警", DeviceTypeEnum.WATER_METER_DEVICE),

    /**
     * 液位计
     */
    A009001("A009001", "故障", DeviceTypeEnum.LEVEL_GAUGE_DEVICE),
    A009002("A009002", "超磁报警", DeviceTypeEnum.LEVEL_GAUGE_DEVICE),
    A009003("A009003", "液位低下限报警", DeviceTypeEnum.LEVEL_GAUGE_DEVICE),
    A009004("A009004", "液位超阈值报警", DeviceTypeEnum.LEVEL_GAUGE_DEVICE),
    A009005("A009005", "无法进入低功耗报警", DeviceTypeEnum.LEVEL_GAUGE_DEVICE),
    A009006("A009006", "电池电量低报警", DeviceTypeEnum.LEVEL_GAUGE_DEVICE),
    /**
     * 烟感
     */
    A010001("A010001", "烟雾报警", DeviceTypeEnum.SMOKE_DEVICE),
    A010002("A010002", "低电报警", DeviceTypeEnum.SMOKE_DEVICE),
//    A010003("A010003", "烟雾报警解除", DeviceTypeEnum.SMOKE_DEVICE),
    /**
     * 智能井盖
     */
    A011001("A011001", "故障", DeviceTypeEnum.WELL_DEVICE),
    A011002("A011002", "超磁报警", DeviceTypeEnum.WELL_DEVICE),
    A011003("A011003", "开盖报警", DeviceTypeEnum.WELL_DEVICE),
    A011004("A011004", "强烈撞击报警", DeviceTypeEnum.WELL_DEVICE),
    A011005("A011005", "水位报警", DeviceTypeEnum.WELL_DEVICE),
    A011006("A011006", "盗窃报警", DeviceTypeEnum.WELL_DEVICE),
    /**
     * 智能路灯
     */
    A012001("A012001", "设备离线", DeviceTypeEnum.STREET_LAMP_DEVICE),
    A012002("A012002", "异常开灯", DeviceTypeEnum.STREET_LAMP_DEVICE),
    A012003("A012003", "异常关灯", DeviceTypeEnum.STREET_LAMP_DEVICE),
    A012004("A012004", "设备过压", DeviceTypeEnum.STREET_LAMP_DEVICE),
    A012005("A012005", "设备过流", DeviceTypeEnum.STREET_LAMP_DEVICE),
    A012006("A012006", "设备欠压", DeviceTypeEnum.STREET_LAMP_DEVICE),
    A012007("A012007", "灯泡或线路异常", DeviceTypeEnum.STREET_LAMP_DEVICE),
    A012008("A012008", "接触器或回路异常", DeviceTypeEnum.STREET_LAMP_DEVICE),
    A012009("A012009", "电源驱动器异常", DeviceTypeEnum.STREET_LAMP_DEVICE),
    /**
     * AI盒子
     */
    A019001("A019001", "人员聚集报警", DeviceTypeEnum.AI_BOX_DEVICE),

    /**
     * 监控设备
     */
    A006001("A006001", "报警", DeviceTypeEnum.MONITOR_DEVICE),
    A006002("A006002", "人脸抓拍", DeviceTypeEnum.MONITOR_DEVICE),

    OTHER("", "未知事件类型", null);


    public String eventTypeId;
    public String eventTypeName;
    public DeviceTypeEnum deviceTypeEnum;

    public static EventTypeEnum getByEventType(String eventTypeId) {
        for (EventTypeEnum eventTypeEnum : EventTypeEnum.values()) {
            if (eventTypeEnum.eventTypeId.equals(eventTypeId)) {
                return eventTypeEnum;
            }
        }
        return OTHER;
    }

}
