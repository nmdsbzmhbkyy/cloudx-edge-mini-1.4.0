package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 通话记录对象
 * </p>
 *
 * @ClassName: RecordEventObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午03:57:41
 * @Copyright:
 */
@Data
@AllArgsConstructor
public class RecordEventObj {

    /**
     * 记录类型
     * 0：未接
     * 1：已接
     * 2：呼叫抓拍
     */
    @NotNull
    String recordMode;

    /**
     * 记录描述
     */
    String recordDesc;

    /**
     * 通话类型
     * 0:呼叫室内
     * 1:呼叫中心
     * 2:呼叫APP
     * 3:呼叫转移（云电话）
     */
    @NotNull
    String callType;

    /**
     * 门禁设备ID
     * 第三方ID
     */
    @NotNull
    String deviceId;

    /**
     * 用户ID
     * 呼叫目标住户编号
     */
    @NotNull
    String userDevno;

    /**
     * 通话时长
     */
    Integer duration;

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
