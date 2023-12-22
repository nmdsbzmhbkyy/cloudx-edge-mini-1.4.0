package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 报警状态变更数据DTO
 * @author : 邱家标 <qiujb@miligc.com>
 * @date : 2021 11 17 11:15
 * @Copyright:
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AurineEdgeAlarmStateChangeDTO {

    /**
     * 设备编号
     */
    private String devId;

    /**
     * 报警流水ID
     */
    private Integer flowId;

    /**
     * 防拆报警
     */
    private Integer destroyAlarm;

    /**
     * 三次密码错误报警
     */
    private Integer errorPwdAlarm;

    /**
     * 挟持密码报警
     */
    private Integer holdPwdAlarm;

    /**
     * 强行开门报警
     */
    private Integer foreOpendoorAlarm;

    /**
     * 防拆报警
     */
    private Integer doorOpenTimeoutAlarm;
}

