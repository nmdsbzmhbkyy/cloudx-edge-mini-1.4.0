package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant;

import lombok.AllArgsConstructor;

/**
 * <p>冠林中台 通行凭证类型枚举</p>
 *
 * @ClassName: AurineEventEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-19 18:00
 * @Copyright:
 */
@AllArgsConstructor
public enum AurineCertEnum {
    /**
     * 卡片
     */
    CARD("0"),
    /**
     * 面部
     */
    FACE("3"),
    /**
     * 密码
     */
    PASSWORD("2");


    public String code;

}
