package com.aurine.cloudx.dashboard.constant;

import lombok.AllArgsConstructor;

/**
 * @description: 版本号枚举
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-22
 * @Copyright:
 */
@AllArgsConstructor
public enum ServiceNameEnum {

    FRAME_TOTAL("frameTotal", "统计某个项目下的框架，房间、单元、楼栋总数统计"),
    HOUSE_TYPE("houseType", "统计房屋类型与数量"),
    HOUSEHOLDER("householder", "统计住户信息，如：住户总数，租客、家属、业主人数"),
    HOUSEHOLDER_TOTAL("householderTotal", "统计住户信息，如：住户总数，租客、家属、业主人数"),
    PERSON_AGE_GENDER("personAgeGender", "统计人员性别与年龄分布"),
    PERSON_AGE("personAge", "统计人员年龄分布"),
    PERSON_REGION("personRegion", "人员来源分布（本省，外省）"),
    PERSON_FACE("personFace", "统计面部识别持有情况"),
    PERSON_FOCUS("personFocus", "关注人群"),
    PERSON_CAR("personCar", "统计车辆持有情况"),
    PERSON_ENTRANCE("personEntrance", "住户每小时/每天通行量"),
    PERSON_CAR_ENTRANCE("personCarEntrance", "统计某个项目人员、车辆出入情况"),
    CAR_ENTRANCE("carEntrance", "车行情况"),
    PARK_PLACE("parkPlace", "每小时车位使用趋势"),
    COMPLAINT_RECORD("complaintRecord", "统计投诉量"),
    REPAIR_RECORD("repairRecord", "每日故障报修数量"),
    ENTRANCE_EVENT("entranceEvent", "人行抓拍"),
    PARK_ENTRANCE("parkEntrance", "车行抓拍"),
    INSPECT_RECORD("inspectRecord", "巡检统计"),
    PATROL_RECORD("patrolRecord", "巡更统计"),
    PERSON_ABNORMAL("personAbnormal", "人员异常行为记录"),
    PERSON_CONTROL("personControl", "防控人员记录"),
    ALARM_RECODE("alarmRecode", "报警事件"),

    BUILDING_PERSON_TOTAL("buildingPersonTotal", "统计本楼栋人口类型分布（访客、住户）、性别分布、年龄分布（儿童<14、青壮年14<65<、老人>65）"),
    BUILDING_PERSON_ENTRANCE("buildingPersonEntrance", "统计本楼栋以小时为节点统计进出人员数量"),
    BUILDING_HOUSE_INFO("buildingHouseInfo", "本楼栋面积、楼层数、单元数、房屋数、电梯数等信息"),
    BUILDING_HOUSE_TOTAL("buildingHouseTotal", "计算本楼栋自住、出租、空置房屋占比以及业主、家属、租户数量"),
    BUILDING_EMPLOYER_TOTAL("buildingEmployerTotal", "统计本楼栋所有单位总数量以及各个经济类型的数量（个体工商、公司、社会团队）"),
    BUILDING_ALARM_TOTAL("buildingAlarmTotal", "统计本楼栋所有今日/本周/本月/今年事件数量及比例（未处理、处理中、已处理）"),
    BUILDING_DEVICE_TOTAL("buildingDeviceTotal", "统计本楼栋所有设备的在线率、离线率、告警率及七天设备异常数"),
    BUILDING_DEVICE_LIST("buildingDeviceList", "统计本楼栋所有设备名称、设备类型、设备ID、设备状态、设备地址"),
//    BUILDING_PERSON_TYPE("buildingPersonType", "统计本楼栋人口类型分布（访客、住户）"),
//    BUILDING_PERSON_GENDER("buildingPersonGender", "统计本楼栋人口性别分布"),
//    BUILDING_PERSON_AGE("buildingPersonAge", "统计本楼栋人口年龄分布（儿童<14、青壮年14<65<、老人>65）"),
//    BUILDING_PERSON_ENTRANCE("buildingPersonEntrance", "统计本楼栋以小时为节点统计进出人员数量"),
//    BUILDING_HOUSE_INFO("buildingHouseInfo", "本楼栋面积、楼层数、单元数、房屋数、电梯数等信息"),
//    BUILDING_HOUSE_STATE("buildingHouseState", "计算本楼栋自住、出租、空置房屋占比"),
//    BUILDING_HOUSE_HOUSEHOLD("buildingHouseHousehold", "统计本楼栋业主、家属、租户数量"),
//    BUILDING_COMPANY_TOTAL("buildingCompanyTotal", "统计本楼栋所有单位总数量"),
//    BUILDING_COMPANY_TYPE("buildingCompanyType", "统计本楼栋各个经济类型的数量（个体工商、公司、社会团队）"),
//    BUILDING_ALARM_TOTAL("buildingAlarmTotal", "统计本楼栋所有今日/本周/本月/今年事件数量及比例（未处理、处理中、已处理）"),
//    BUILDING_DEVICE_TOTAL("buildingDeviceTotal", "统计本楼栋所有设备的在线率、离线率、告警率及七天设备异常数"),
//    BUILDING_DEVICE_LIST("buildingDeviceList", "统计本楼栋所有设备的在线率、离线率、告警率及七天设备异常数"),

    DEVICE_DATA("deviceData", "设备数据"),
    DEVICE_DATA_LIST("deviceDataList", "多条设备数据，默认7日"),
    DEVICE_OVERVIEW("deviceOverview", "设备总览"),
    DEVICE_STREAM("deviceStream", "设备媒体流"),
    TEST_PARKING_IN("testParkingIn", "设备媒体流"),
    TEST_PARKING_OUT("testParkingOut", "设备媒体流"),
    TEST_ENTRANCE_ALARM("testEntranceAlarm", ""),
    TEST_ENTRANCE("testEntrance", "设备媒体流"),
    OTHER("other", "");

    /**
     * 服务名称
     */
    public String serviceName;

    /**
     * 描述
     */
    public String desc;


    /**
     * @param serviceName
     * @return
     */
    public static ServiceNameEnum getServiceName(String serviceName) {
        for (ServiceNameEnum value : ServiceNameEnum.values()) {
            if (value.serviceName.equals(serviceName)) {
                return value;
            }
        }
        return null;
    }


}
