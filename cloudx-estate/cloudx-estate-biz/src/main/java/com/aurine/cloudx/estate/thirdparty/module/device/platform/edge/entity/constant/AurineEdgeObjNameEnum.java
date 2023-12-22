package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant;

import lombok.AllArgsConstructor;

/**
 * <p>对象名称枚举</p>
 * @ClassName: AurineEdgeObjNameEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/27 15:34
 * @Copyright:
 */
@AllArgsConstructor
public enum AurineEdgeObjNameEnum {
    DeviceInfoObj("DeviceInfoObj", "设备信息对象"),
    DeviceParamInfo("DeviceParamInfo", "设备参数对象"),
    DeviceNoObj("DeviceNoObj", "设备编号对象"),
    DevRuleObj("DevRuleObj", "设备编号规则对象"),
    NetworkObj("NetworkObj", "网络参数对象"),
    VolumeObj("VolumeObj", "音量参数对象"),
    LockparamObj("LockparamObj", "锁属性对象"),
    DoorStateObj("DoorStateObj", "门状态对象"),
    FaceparamObj("FaceparamObj", "人脸参数对象"),
    SnapParamObj("SnapParamObj", "抓拍参数对象"),
    DevParamObj("DevParamObj", "设备参数"),
    ServerParamObj("ServerParamObj", "服务器参数对象"),
    FuncParamObj("FuncParamObj", "功能参数对象"),
    AccessParamObj("AccessParamObj", "门禁参数对象"),
    EventReport("EventReport", "事件服务对象"),
    OpendoorEventObj("OpendoorEventObj", "进门事件对象"),
    AlarmEventObj("AlarmEventObj", "报警事件对象"),
    RecordEventObj("RecordEventObj", "通话记录对象"),
    DeviceInfo("DeviceInfo", "删除设备对象"),
    PassObj("PassObj", "门禁通行对象");
    /**
     * 对象名称
     */
    public String code;
    /**
     * 描述
     */
    public String desc;

}
