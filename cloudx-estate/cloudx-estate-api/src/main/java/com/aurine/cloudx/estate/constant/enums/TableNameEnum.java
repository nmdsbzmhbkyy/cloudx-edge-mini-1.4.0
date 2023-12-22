package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:zouyu
 * @data:2022/8/17 4:29 下午
 */
@AllArgsConstructor
public enum TableNameEnum {

    //---------------------aurine库----------------------
    /**
     * 配置项目框架层级
     */
    ENTITY_LEVEL_CFG("project_entity_level_cfg"),
    /**
     * 户型信息
     */
    HOUSE_DESIGN("project_house_design"),
    /**
     * 通行方案
     */
    PASS_PLAN("project_pass_plan"),
    /**
     * 楼栋信息
     */
    BUILDING_INFO("project_building_info"),
    /**
     * 单元信息
     */
    UNIT_INFO("project_unit_info"),
    /**
     * 房屋信息
     */
    HOUSE_INFO("project_house_info"),
    /**
     * 框架信息
     */
    FRAME_INFO("project_frame_info"),
    /**
     * 项目区域
     */
    PROJECT_REGION("project_device_region"),
    /**
     * 设备信息
     */
    DEVICE_INFO("project_device_info"),
    /**
     * 人员信息
     */
    PERSON_INFO("project_person_info"),
    /**
     * 住户信息
     */
    HOUSE_PERSON_INFO("project_house_person_rel"),
    /**
     * 人脸信息
     */
    FACE_INFO("project_face_resources"),
    /**
     * 卡信息
     */
    CARD_INFO("project_card"),
    /**
     * 密码信息
     */
    PASSWORD_INFO("project_passwd"),
    /**
     * 人员通行方案关系
     */
    PERSON_PLAN_REL("project_person_plan_rel"),
    /**
     * 通行方案策略关系
     */
    PASS_PLAN_POLICY_REL("project_pass_plan_policy_rel"),
    /**
     * 物理策略
     */
    PHYSICAL_PASS_POLICY("project_physical_pass_policy"),
    /**
     * 逻辑策略
     */
    LOGIC_PASS_POLICY("project_logic_pass_policy"),
    /**
     * 人员设备权限关系
     */
    PERSON_DEVICE_REL("project_person_device"),
    /**
     * 权限设备关系
     */
    RIGHT_DEVICE_REL("project_right_device"),
    /**
     * 告警事件
     */
    ALARM_EVENT("project_entrance_alarm_event"),
    /**
     * 报警事件处理
     */
    ALARM_HANDLE("project_alarm_handle"),
    /**
     * 人行事件
     */
    PERSON_ENTRANCE("project_entrance_event"),
    /**
     * 来访记录
     */
    VISITOR_HIS("project_visitor_his"),
    /**
     * 访客信息
     */
    VISITOR_INFO("project_visitor"),
    /**
     * 人员标签
     */
    LABEL_CONFIG("project_label_config"),
    /**
     * 房屋人员变更日志
     */
    HOUSE_PERSON_CHANGE_HIS("project_house_person_change_his"),
    /**
     * 员工信息
     */
    STAFF_INFO("project_staff"),
    /**
     * 设备关系
     */
    DEVICE_REL("project_device_rel"),
    /**
     * 乘梯事件记录
     */
    LIFT_EVENT("project_lift_event"),
    /**
     * 人员电梯权限关系表
     */
    PERSON_LIFT_REL("project_person_lift_rel"),
    /**
     * 设备参数表
     */
    DEVICE_PARAM_INFO("project_device_param_info"),

    /**
     * 卡操作记录表
     */
    CARD_HIS("project_card_his"),

    /**
     * 项目参数设置
     */
    PROJECT_CONFIG("project_config"),

    /**
     * 设备拓展属性
     */
    DEVICE_ATTR("project_device_attr"),
    //---------------------aurine_parking库----------------------

    /**
     * 黑名单
     */
    BLACKLIST("project_blacklist"),
    /**
     * 车辆信息
     */
    CAR_INFO("project_car_info"),
    /**
     * 出入口车道
     */
    ENTRY_EXIT_LANE("project_entry_exit_lane"),
    /**
     * 开关闸记录
     */
    OPEN_LANE_HIS("project_open_lane_his"),
    /**
     * 车辆登记
     */
    PAR_CAR_REGISTER("project_par_car_register"),
    /**
     * 缴费记录
     */
    PARK_BILLING_INFO("project_park_billing_info"),
    /**
     * 车场计费规则
     */
    PARK_BILLING_RULE("project_park_billing_rule"),
    /**
     * 车辆类型
     */
    PARK_CAR_TYPE("project_park_car_type"),
    /**
     * 车行记录
     */
    PARK_ENTRANCE_HIS("project_park_entrance_his"),
    /**
     * 车位区域
     */
    PARK_REGION("project_park_region"),
    /**
     * 车场信息
     */
    PARKING_INFO("project_parking_info"),
    /**
     * 车位
     */
    PARKING_PLACE("project_parking_place"),
    /**
     * 车位变动记录
     */
    PARKING_PLACE_HIS("project_parking_place_his"),
    /**
     * 设备车牌号下发情况
     */
    PLATE_NUMBER_DEVICE("project_plate_number_device"),
    /**
     * 车辆出入口信息
     */
    VEHICLES_ENTRY_EXIT("project_vehicles_entry_exit"),
    /**
     * 车辆预登记
     */
    CAR_PRE_REGISTER("project_car_pre_register");

    public String code;

    public static List<String> getServiceName() {
        List<String> list = new ArrayList<>();
        for (TableNameEnum value : TableNameEnum.values()) {
            if (value.equals(BLACKLIST)) {
                break;
            }
            list.add(value.code);
        }
        return list;
    }

    public static List<String> getParkingServiceName() {
        List<String> list = new ArrayList<>();
        boolean flag = false;
        for (TableNameEnum value : TableNameEnum.values()) {
            if (!value.equals(BLACKLIST)) {
                if (flag) {
                    list.add(value.code);
                }
            } else {
                list.add(value.code);
                flag = true;
            }

        }
        return list;
    }
}
