package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>异常设备管理分页查询条件</p>
 * @author : 王良俊
 * @date : 2021-09-26 13:57:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDeviceAbnormalSearchCondition {

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备sn
     */
    private String sn;

    /**
     * 设备状态 1 在线 2 离线 3 故障 4 未激活 9 未知
     */
    private String dStatus;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 异常原因字典值
     */
    private String abnormalCode;

    /**
     * 接入方式
     */
    private String accessMethod;
}
