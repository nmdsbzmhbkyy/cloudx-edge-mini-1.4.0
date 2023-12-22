package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo;

import lombok.Data;

/**
 * @author:zouyu
 * @data:2022/4/24 10:32 上午
 */
@Data
public class ObjInfoVo {

    /**
     * 呼叫目标
     */
    private String callTarget;

    /**
     * 呼叫方式
     */
    private String callMode;

    /**
     * 呼叫目标小区
     */
    private String callTargetProject;

    /**
     * 呼叫时间
     */
    private String callTime;

    /**
     * 没啥用 不考虑
     */
    private String sysVideoCallStatus;

    /**
     * 接听时间
     */
    private Integer talkTime;

    /**
     * 接听用户
     */
    private String talkUser;
}
