package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 作为设备参数的字典使用（需要在参数类型和服务ID之间进行转换）
 * </p>
 *
 * @ClassName: DeviceParamEnum
 * @author: 王良俊 <>
 * @date: 2021年01月21日 上午09:30:54
 * @Copyright:
 */
@Getter
@AllArgsConstructor
public enum DeviceParamEnum {

    // 设备编号对象
    DEVICE_NO_OBJ("deviceNo", "DeviceNoObj"),
    // 设备编号对象
    EDGE_DEVICE_NO_OBJ("DeviceNoObj", "DeviceNoObj"),
    // 设备编号规则对象
    DEV_RULE_OBJ("devNoRule", "DevRuleObj"),
    // 网络参数对象
    NETWORK_OBJ("netparam", "NetworkObj"),
    // 住户参数对象
//    RESIDENT_OBJ("residentParam", "ResidentObj"),
    // 音量参数对象
    VOLUME_OBJ("volumeParam", "VolumeObj"),
    // 存储卡信息对象
//    SDCARD_OBJ("sdcardInfo", "SdcardObj"),
    // 锁属性对象
    LOCK_PARAM_OBJ("lockParam", "LockparamObj"),
    // 门状态对象
    DOOR_STATE_OBJ("doorParam", "DoorStateObj"),
    // 人脸参数对象
    FACE_PARAM_OBJ("faceParam", "FaceparamObj"),
    // 二维码参数对象
//    QRCODE_OBJ("qrcodeParam", "QrcodeObj"),
    // 抓拍参数对象
    SNAP_PARAM_OBJ("snapParam", "SnapParamObj"),
    // 功能对象
    FUNC_PARAM_OBJ("funcParam", "FuncParamObj"),
    // 门禁参数对象
    ACCESS_PARAM_OBJ("accessParam", "AccessParamObj"),
    // 服务器参数对象
    SERVER_PARAM_OBJ("serverParam", "ServerParamObj"),

    // 边缘网关特有
    // 边缘网关-系统参数
    EDGE_SYS_PARAM_OBJ("EdgeSysParamObj", "EdgeSysParamObj"),


    // 版本信息对象
//    VERSION_OBJ("version", "VersionObj"),
    // 额外参数（暂无参数对象名）
//    EXT_PARAM_OBJ("extParam", "ExtParamObj"),
    // 设备参数
    DEV_PARAM_OBJ("devParam", "DevParamObj"),
    // 设备状态服务（门状态、云对讲状态等，不是设备参数，和上面那些没关系。）
    DEVICE_STATE_CHANGE("DeviceStateChange", "DeviceStateChange"),
    /*
    * 第三方物联网设备的服务
    * */
    // 电池电量
    BATTERY("Battery", "Battery"),
    // 网络信息服务
    CONNECTIVITY("Connectivity", "Connectivity"),
    // 采集数据内容
    DATA_ATTRIB("DataAttrib", "DataAttrib"),
    // 报警事件信息
    WARNING_ALARM("WarningAlarm", "WarningAlarm"),
    // 设备事件数据服务
    EVENT_DATA("EventData", "EventData"),

    // 这是设备设置参数 IOT设备的参数
    // 设备设置
    DEVICE_SETTING( "DeviceSetting", "DeviceSetting"),
    ;

    /**
     * 系统中的serviceId(门禁设备是产品同步时的参数服务下的property_name)
     * */
    public String serviceId;

    /**
     * 这里的objName是用于获取门禁设备设备参数时使用的（而paramsTypeId是同步产品时能获取的，同时也作为系统的服务ID使用两者是1对1的关系）
     * */
    public String objName;

    /**
    * <p>
    * 只限于获取在获取设备参数时所需要的参数对象名，根据serviceId
    * </p>
    *
    * @param serviceId 服务ID
    */
    public static String getObjName(String serviceId) {
        DeviceParamEnum[] values = DeviceParamEnum.values();
        for (DeviceParamEnum value : values) {
            // 这里是防止传入的是参数对象名但是要获取的也是参数对象名（serviceId），两种都能获取到参数对象名
            if (value.getServiceId().equals(serviceId)) {
                return value.getObjName();
            }
        }
        return serviceId;
    }
/*
    public static String getParamType(String objName) {
        DeviceParamEnum[] values = DeviceParamEnum.values();
        for (DeviceParamEnum value : values) {
            // 这里是防止传入的是参数类型ID但是要获取的也是参数类型ID，两种都能获取到参数类型ID
            if (value.getObjName().equals(objName) || value.getServiceId().equals(objName)) {
                return value.getServiceId();
            }
        }
        return null;
    }*/

    public static DeviceParamEnum getEnum(String objectName) {
        DeviceParamEnum[] values = DeviceParamEnum.values();
        for (DeviceParamEnum value : values) {
            // 这里是防止传入的是参数类型ID但是要获取的也是参数类型ID，两种都能获取到参数类型ID
            if (value.getObjName().equals(objectName) || value.getServiceId().equals(objectName)) {
                return value;
            }
        }
        return null;
    }

    public static List<String> getIsNotADeviceParameterServiceID() {
        List<String> serviceIdList = new ArrayList<>();
        serviceIdList.add(DeviceParamEnum.DEVICE_STATE_CHANGE.serviceId);
        return serviceIdList;
    }

    public static List<String> getIotDeviceAlarmServiceIdList() {
        List<String> serviceIdList = new ArrayList<>();
        serviceIdList.add(DeviceParamEnum.WARNING_ALARM.serviceId);
        return serviceIdList;
    }

}
