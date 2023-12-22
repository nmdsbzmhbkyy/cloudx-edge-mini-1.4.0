package com.aurine.cloudx.edge.sync.common.enums;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 图片枚举
 *
 * @author:zy
 * @data:2022/12/16 11:59 上午
 */
@AllArgsConstructor
public enum ImagesEnum {


    // ------------------aurine库------------------

    //楼栋
    BUILDING_INFO("BuildingInfo", ListUtil.toList("picUrl1","picUrl2","picUrl3")),

    //单元
    UNIT_INFO("UnitInfo", ListUtil.toList("picUrl1","picUrl2","picUrl3")),

    //区域
    PROJECT_REGION("ProjectRegion", ListUtil.toList("picUrl")),

    //设备
    DEVICE_INFO("DeviceInfo", ListUtil.toList("picUrl")),

    //人员
    PERSON_INFO("PersonInfo", ListUtil.toList("picUrl")),

    //人脸
    FACE_INFO("FaceInfo", ListUtil.toList("picUrl")),

    //人行事件
    PERSON_ENTRANCE("PersonEntrance", ListUtil.toList("picUrl","smallPicUrl")),

    //告警事件
    ALARM_EVENT("AlarmEvent", ListUtil.toList("picUrl")),

    //报警事件处理
    ALARM_HANDLE("AlarmHandle", ListUtil.toList("picUrl")),

    //访客
    VISITOR_INFO("VisitorInfo", ListUtil.toList("credentialPicFront","credentialPicBack")),

    //员工
    STAFF_INFO("StaffInfo", ListUtil.toList("picUrl","credentialPicFront","credentialPicBack")),

    //乘梯事件记录
    LIFT_EVENT("LiftEvent", ListUtil.toList("picUrl")),

    //抓拍记录
    SNAP_RECORD("SnapRecord", ListUtil.toList("snapSmalImage","snapBigImage")),


    // ------------------aurine_parking库------------------

    //开关闸记录
    OPEN_LANE_HIS("OpenLaneHis", ListUtil.toList("picUrl")),

    //车行记录
    PARK_ENTRANCE_HIS("ParkEntranceHis", ListUtil.toList("enterPicUrl","outPicUrl")),

    //车辆出入口信息
    VEHICLES_ENTRY_EXIT("VehiclesEntryExit", ListUtil.toList("picUrl")),

    //出入口车道
    ENTRY_EXIT_LANE("EntryExitLane", ListUtil.toList("picUrl"));

    public String serviceName;

    public List<String> field;

    public static List<String> getFieId(String serviceName){
        for (ImagesEnum value : ImagesEnum.values()) {
            if(serviceName.equals(value.serviceName)){
                return value.field;
            }
        }
        return null;
    }
}
