package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 电梯卡类型枚举
 * </p>
 * @author : 黄健杰
 * @date : 2022-04-01
 */
@AllArgsConstructor
public enum LiftCardTypeEnum {

    /**
     * 时段卡
     */
    durationCard(0x01),
    /**
     * 收费卡
     */
    chargeCard(0x02),
    /**
     * 管理人员卡
     */
    staffCard(0x03),
    /**
     * 屏蔽房间卡
     */
    shieldRoomCard(0x04),
    /**
     * 设定按钮响应时间卡
     */
    setButtonRespTimeCard(0x05),
    /**
     * 设定控制器时钟卡
     */
    setCtrlClockCard(0x06),
    /**
     * 读取用户数据卡
     */
    readUserDataCard(0x07),
    /**
     * 访客按钮响应时间卡
     */
    setVisitorButtonRespTimeCard(0x08),
    /**
     * 电梯运行时段卡
     */
    setRunDurationCard(0x09),
    /**
     * 分层系统开关卡
     */
    setLayerSystemSwitchCard(0x0A),
    /**
     * 批量屏蔽卡
     */
    setBatchShieldCard(0x0B),
    /**
     * 设置丽人时段卡
     */
    setLRDurationCard(0x0C),
    /**
     * 系统开关卡
     */
    setSystemSwitchCard(0x0D),
    /**
     * 重启设备卡
     */
    restartCard(0x0E),
    /**
     * 楼层调试卡
     */
    floorDebugCard(0x0F),
    /**
     * 设置电梯编号
     */
    setCodeCard(0x10),
    /**
     * 电梯编号加卡
     */
    setCodeAddCard(0x11),
    /**
     * 电梯编号减卡
     */
    setCodeReduceCard(0x12),
    /**
     * 恢复出厂卡
     */
    restoreCard(0x13),
    /**
     * 读取故障代码卡
     */
    readFaultCodeCard(0x14),
    /**
     * 读取控制器状态
     */
    readCtrlStatusCard(0x15),
    /**
     * 读取版本信息
     */
    readVersionInfoCard(0x16),
    /**
     * 电梯禁用时段卡
     */
    setDisableDurationCard(0x17),
    /**
     * 网络参数配置卡
     */
    setNetworkCard(0x18),
    ;

    public Integer code;

}
