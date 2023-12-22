package com.aurine.cloudx.estate.thirdparty.main.entity.constant;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-07
 * @Copyright:
 */
public class EventDeviceGatePassConstant {

    /**
     * 使用卡片通过门禁
     */
    public static final String ACTION_CAED = "CARD";

    /**
     * 使用指纹通过门禁
     */
    public static final String ACTION_FINGER = "FINGER";

    /**
     * 使用面部识别通过门禁
     */
    public static final String ACTION_FACE = "FACE";

    /**
     * 使用密码识别通过门禁
     */
    public static final String ACTION_PASSWORD = "PASSWORD";
    /**
     * 使用二维码通过门禁
     */
    public static final String ACTION_QR_CODE = "QR_CODE";
    /**
     * 使用二维码通过门禁
     */
    public static final String ACTION_QR_CODE_WITH_PERSON = "QR_CODE_WITH_PERSON";
    /**
     * @description 远程校验开门
     * @author cyw
     * @time 2023/6/12 16:40
     */
    public static final String ACTION_REMOTE_VALID_WITH_PERSON = "REMOTE_VALID_WITH_PERSON";
    /**
     * 使用系统远程开门
     */
    public static final String ACTION_REMOTE = "REMOTE";
    /**
     * 使用系统远程开门
     */
    public static final String ACTION_REMOTE_WITH_PERSON = "REMOTE_WITH_PERSON";
    /**
     * 通过其他方式过门
     */
    public static final String OTHER = "OTHER";

    /**
     * 异常通行
     */
    public static final String ACTION_ERROR_PASS = "ERROR_PASS";

}
