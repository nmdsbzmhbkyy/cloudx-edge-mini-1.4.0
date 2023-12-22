package com.aurine.cloudx.open.common.core.constant.enums;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 指令类型
 * 
 * @author : Qiu
 * @date : 2021 12 23 11:15
 */

@AllArgsConstructor
public enum CommandTypeEnum {

    CLOSE("0", "close", "关闭指令"),
    OPEN("1", "open", "打开指令"),
    CHANGE("2", "change", "改变指令"),
    EMPTY("4","empty","清空指令"),
    DOWN("11", "down", "下发指令"),
    DELETE("12", "delete", "删除指令"),

    DELETE_DEVICE("101", "deleteDevice", "删除设备指令"),
    DELETE_HOUSE_PERSON("111", "deleteHousePerson", "删除住户指令"),

    BLACK_LIST_INSERT("201", "BlacklistInsert", "黑名单添加"),
    BLACK_LIST_UPDATE("202", "BlacklistUpdate", "黑名单修改"),
    BLACK_LIST_DELETE("203", "BlacklistDelete", "黑名单删除"),
    PARKING_INFO_INSERT("211", "ParkingInfoInsert", "车场信息添加"),
    PARKING_INFO_UPDATE("212", "ParkingInfoUpdate", "车场信息修改"),
    PARKING_INFO_DELETE("213", "ParkingInfoDelete", "车场信息删除"),
    PARKING_GLOBAL("214", "ParkingGlobal", "车场全局设置"),
    ENTRY_EXIT_LANE_INSERT("221", "EntryExitLaneInsert", "出入口车道添加"),
    ENTRY_EXIT_LANE_UPDATE("222", "EntryExitLaneUpdate", "出入口车道修改"),
    ENTRY_EXIT_LANE_DELETE("223", "EntryExitLaneDelete", "出入口车道删除"),
    LANE_ASSOCIATED_DEVICE("224", "LaneAssociatedDevice", "车道关联设备"),
    DELETE_LANE_ASSOCIATED_DEVICE("225", "DeleteLaneAssociatedDevice", "车道取消关联设备"),
    PARKING_PLACE_INSERT("231", "ParkingPlaceInsert", "车位添加"),
    PARKING_PLACE_UPDATE("232", "ParkingPlaceUpdate", "车位修改"),
    PARKING_PLACE_DELETE("233", "ParkingPlaceDelete", "车位删除"),
    CAR_INFO_INSERT("241", "CarInfoInsert", "车辆信息添加"),
    CAR_INFO_UPDATE("242", "CarInfoUpdate", "车辆信息修改"),
    CAR_INFO_DELETE("243", "CarInfoDelete", "车辆信息删除"),
    FAIL_CAR_RESEND("251", "failCarResend", "失败车辆重发"),

    NULL("-1", null, "空")
    ;

    public String code;
    public String name;
    public String desc;

    public static CommandTypeEnum getByCode(String code) {
        if (StringUtils.isEmpty(code)) return null;
        for (CommandTypeEnum value : CommandTypeEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static CommandTypeEnum getByName(String name) {
        if (StringUtils.isEmpty(name)) return null;
        for (CommandTypeEnum value : CommandTypeEnum.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }
}
