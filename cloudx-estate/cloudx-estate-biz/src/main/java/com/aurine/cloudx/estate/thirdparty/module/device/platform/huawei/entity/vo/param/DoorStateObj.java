package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 门状态对象 json名：doorParam
 * </p>
 *
 * @ClassName: DoorStateObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:11:24
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("doorParam")
public class DoorStateObj {

    /**
     * 是否启用门状态检测
     * 0：不启用 1:启用
     */
    @NotNull
    Integer enable;

    /**
     * 是否响报警声
     * 0：不响1：响
     */
    @NotNull
    Integer alarmSound;

    /**
     * 是否上报
     * 0：不上报 1：上报
     */
    @NotNull
    Integer reportEnable;

    /**
     * 是否启用强行开门报警
     * 0：不启用 1：启用
     */
    @NotNull
    Integer foreOpenAlarm;

    /**
     * 是否启用强行开门报警
     * 0：不启用 1：启用
     */
    Integer OpenTimeoutAlarm;

}
