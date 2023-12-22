package com.aurine.cloudx.estate.openapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: wrm
 * @Date: 2021/12/15 20:12
 * @Package: com.aurine.cloudx.open.enums
 * @Version: 1.0
 * @Remarks:
 **/

@AllArgsConstructor
public enum OpenApiServiceNameEnum {

    CONFIG_SUBSCRIPTION_ADD("1", "ConfigSubscriptionAdd", "配置订阅新增"),
    CONFIG_SUBSCRIPTION_DELETE("2", "ConfigSubscriptionDelete", "配置订阅删除"),

    CASCADE_APPLY("11", "CascadeApply", "级联入云申请相关"),
    CASCADE_UNBIND("12", "CascadeUnbind", "级联入云解绑相关"),
    CASCADE_CLOUD_EDGE_REQUEST("31", "CascadeCloudEdgeRequest", "边缘网关入云申请（云平台）"),
    CASCADE_EDGE_CLOUD_REQUEST("32", "CascadeEdgeCloudRequest", "边缘网关入云申请（边缘侧）"),
    CASCADE_PROCESS_MASTER("41", "CascadeProcessMaster", "级联入云对接进度"),
    CASCADE_SYNC_LOG("51", "CascadeSyncLog", "级联入云同步日志"),

    PROJECT_INFO("101", "ProjectInfo", "项目信息"),
    PROJECT_REGION("102", "ProjectRegion", "项目区域"),
    ENTITY_LEVEL_CFG("103", "EntityLevelCfg", "组团配置"),
    HOUSE_DESIGN("104", "HouseDesign", "户型信息"),
    FRAME_INFO("105", "FrameInfo", "框架信息"),
    BUILDING_INFO("106", "BuildingInfo", "楼栋信息"),
    UNIT_INFO("107", "UnitInfo", "单元信息"),
    HOUSE_INFO("108", "HouseInfo", "房屋信息"),
    DEVICE_INFO("109", "DeviceInfo", "设备信息"),
    PASS_PLAN("110", "PassPlan", "通行方案"),
    PASS_PLAN_POLICY_REL("111", "PassPlanPolicyRel", "通行方案策略关系"),
    PHYSICAL_PASS_POLICY("112", "PhysicalPassPolicy", "物理策略"),
    LOGIC_PASS_POLICY("113", "LogicPassPolicy", "逻辑策略"),
    PERSON_PLAN_REL("114", "PersonPlanRel", "人员通行方案关系"),
    PERSON_DEVICE_REL("115", "PersonDeviceRel", "人员设备权限关系"),
    PERSON_INFO("116", "PersonInfo", "人员信息"),
    HOUSE_PERSON_INFO("117", "HousePersonInfo", "住户信息"),
    STAFF_INFO("118", "StaffInfo", "员工信息"),
    VISITOR_INFO("119", "VisitorInfo", "访客信息"),
    VISITOR_HIS("120", "VisitorHis", "来访记录"),
    FACE_INFO("121", "FaceInfo", "人脸信息"),
    CARD_INFO("122", "CardInfo", "卡信息"),
    PASSWORD_INFO("123", "PasswordInfo", "密码信息"),
    RIGHT_DEVICE_REL("124", "RightDeviceRel", "权限设备关系"),
    DEVICE_SUBSYSTEM("125", "DeviceSubSyStem", "设备子系统"),
    LABEL_CONFIG("126","LabelConfig","人员标签"),
    HOUSE_PERSON_CHANGE_HIS("127","HousePersonChangeHis","房屋人员变更日志"),
    DEVICE_REL("128","DeviceRel","设备关系"),
    LIFT_EVENT("129", "LiftEvent", "乘梯事件记录"),
    PERSON_LIFT_REL("130", "PersonLiftRel", "人员电梯权限关系表"),
    DEVICE_PARAM_INFO("131", "DeviceParamInfo", "设备参数表"),
    CARD_HIS("132", "CardHis", "卡操作记录"),
    PROJECT_CONFIG("133", "ProjectConfig", "项目参数设置"),
    DEVICE_ATTR("134", "DeviceAttr", "设备拓展属性"),


    PERSON_ENTRANCE("201", "PersonEntrance", "人行事件"),
    ALARM_EVENT("202", "AlarmEvent", "告警事件"),
    ALARM_HANDLE("203", "AlarmHandle", "报警事件处理"),

    SNAP_RECORD("301", "SnapRecord", "抓拍记录"),

    NULL("-1", "null", "空")

    ;

    public String code;
    public String name;
    public String desc;

    public static OpenApiServiceNameEnum getByCode(String code) {
        if (StringUtils.isEmpty(code)) return null;
        for (OpenApiServiceNameEnum value : OpenApiServiceNameEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static OpenApiServiceNameEnum getByName(String name) {
        if (StringUtils.isEmpty(name)) return null;
        for (OpenApiServiceNameEnum value : OpenApiServiceNameEnum.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }

    public static boolean existName(String name) {
        return getByName(name) != null;

    }
}
