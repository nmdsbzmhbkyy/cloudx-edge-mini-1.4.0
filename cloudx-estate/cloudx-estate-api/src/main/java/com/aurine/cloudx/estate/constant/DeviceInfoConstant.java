package com.aurine.cloudx.estate.constant;

/**
 * Title: DeviceInfoContatt
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/25 13:09
 */
public interface DeviceInfoConstant {
    /**
     * 设备查询结构树:梯口终端分类id
     */
    String TREE_BUILD_ID = "buildId";
    /**
     * 梯口终端
     */
    String TREE_BUILD_NAME = "梯口终端";
    /**
     * 设备查询结构树:区口分类id
     */
    String TREE_GATE_ID = "gateId";
    /**
     * 区口终端
     */
    String TREE_GATE_NAME = "区口终端";
    /**
     * 是否启用: 是
     */
    String IS_ACTIVE = "1";
    /**
     * 是否启用: 否
     */
    String NOT_ACTIVE = "0";
    /**
     * 设备查询结构树: 是否设备 是
     */
    String IS_DEVICE = "1";
    /**
     * 设备查询结构树: 是否设备 否
     */
    String IS_NOT_DEVICE = "0";
    /**
     * 在线
     */
    String ONLINE_STATUS = "1";

    /**
     * 离线
     */
    String OUTLINE_STATUS = "2";
    /**
     * 故障
     */
    String EXCEPTION_STATUS = "3";

    /**
     * 未激活
     */
    String UNACTIVATED_STATUS = "4";
    /**
     * 未知
     */
    String UNKNOWN_STATUS = "5";
}
