package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceGatePassConstant;
import lombok.AllArgsConstructor;

/**
 * @author:zy
 * @data:2022/10/9 3:31 下午
 */

@AllArgsConstructor
public enum AurineOpenModeEnum {

    /**
     * 健康码开门  展示没啥用
     */
    HEALTH_CODE_MODE(null,"1"),

    /**
     * 密码开门
     */
    PASSWORD_CODE_MODE(EventDeviceGatePassConstant.ACTION_PASSWORD,"2"),

    /**
     * 二维码开门
     */
    QRCODE_OPEN_MODE(EventDeviceGatePassConstant.ACTION_QR_CODE_WITH_PERSON,"3"),

    /**
     * 人脸开门
     */
    FACE_OPEN_MODE(EventDeviceGatePassConstant.ACTION_FACE,"4"),

    /**
     * 门禁卡开门
     */
    CARD_OPEN_MODE(EventDeviceGatePassConstant.ACTION_CAED,"5"),

    /**
     * 远程开门
     */
    LONG_RANGE_OPEN_MODE(EventDeviceGatePassConstant.ACTION_REMOTE_WITH_PERSON,"6"),
    /**
     * @description 远程校验开门
     * @author cyw
     * @time 2023/6/12 16:40
     */
    REMOTE_VALID_OPEN_MODE(EventDeviceGatePassConstant.ACTION_REMOTE_VALID_WITH_PERSON,"7");


    ;

    public String action;

    public String openMode;

}
