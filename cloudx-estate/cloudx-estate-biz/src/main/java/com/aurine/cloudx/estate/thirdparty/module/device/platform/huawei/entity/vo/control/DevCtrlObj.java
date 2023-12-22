package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.control;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 设备远程控制对象
 * </p>
 *
 * @ClassName: remoteControl
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:31:04
 * @Copyright:
 */
@Data
@AllArgsConstructor
public class DevCtrlObj {

    /**
     * 用户ID
     *操作人员对应的用户ID
     * */
    @NotNull
    String userId;

    /**
     * 操作密码
     * 操作人员操作密码
     * */
    @NotNull
    String password;

}
