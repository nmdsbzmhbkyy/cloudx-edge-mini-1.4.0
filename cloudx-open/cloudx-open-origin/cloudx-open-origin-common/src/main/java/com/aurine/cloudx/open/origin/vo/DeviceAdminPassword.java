package com.aurine.cloudx.open.origin.vo;

import cn.hutool.core.codec.Base64;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 设备管理员密码和设备ID
 * </p>
 * @author : 王良俊
 * @date : 2021-06-09 17:26:46
 */
@NoArgsConstructor
@AllArgsConstructor
public class DeviceAdminPassword {

    /**
     * 设备ID
     * */
    @ApiModelProperty("设备ID")
    private String deviceId;

    /**
     * 管理员密码
     * */
    @ApiModelProperty("管理员密码")
    private String password;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPassword() {
        return Base64.decodeStr(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
