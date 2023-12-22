package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * 记录一台设备参数设置结果
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/24 15:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceParamConfResultVo {

    private String deviceId;

    private List<String> failedServiceId;

}
