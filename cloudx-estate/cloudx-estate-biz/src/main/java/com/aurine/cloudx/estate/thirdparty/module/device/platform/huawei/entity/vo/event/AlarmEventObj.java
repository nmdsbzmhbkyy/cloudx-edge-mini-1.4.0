package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.event;

import lombok.AllArgsConstructor;
import lombok.Data;

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
public class AlarmEventObj {

    /**
     * 报警类型：
     * 1：无效卡刷卡，
     * 2：挟持开门，
     * 3：强行开门报警，
     * 4：门未关报警，
     * 5：防拆报警,
     * 6:三次输入密码错误报警
     */
    @NotNull
    String alarmType;

    /**
     * 门禁设备ID
     * 第三方ID
     */
    @NotNull
    String devId;

    /**
     * 事件描述
     */
    @NotNull
    String alarmDesc;

    /**
     * 通行标识
     * 卡号/房号
     */
    String passID;

    /**
     * 记录时间
     * yyyy-MM-dd HH:mm:ss
     */
    @NotNull
    String recordTime;

    /**
     * 场景抓拍图片
     * 图片ＵＲＬ地址
     */
    String snapPic;
}
