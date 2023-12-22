package com.aurine.cloudx.estate.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *  设备参数设置结果对象
 * </p>
 * @ClassName: ProjectDeviceParamSetResultVo
 * @author: 王良俊 <>
 * @date:  2020年12月25日 上午09:38:02
 * @Copyright:
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDeviceParamSetResultVo {

    /**
     * 设备ID
     * */
    String deviceId;

    /**
     * 设备第三方编码
     * */
    String thirdpartyCode;

    /**
     * 本次设置的服务ID
     * */
    String serviceId;

    /**
     * 本服务是否设置成功
     * */
    boolean success;
}
