package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 报警事件对象
 * </P>
 *
 * @ClassName: AlarmEventObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午03:33:08
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmStateChangeObj {


    /**
     * 门禁设备ID
     * 第三方ID
     */

    String devId;
    /**
     * 流水Id
     */
    String flowId;

    /**
     * 防拆报警
     */
    int destoryAlarm;




    /**
     * 强行开门报警
     */
    int foreOpendoorAlarm;

    /**
     * 门开超时报警
     */
    int doorOpenTimeoutAlarm;

    /**
     * 三次密码错误报警
     */
    int errorPwdAlarm;

    /**
     * 	挟持密码报警
     */
    int holdPwdAlarm;





}
