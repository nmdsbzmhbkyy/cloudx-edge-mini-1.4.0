package com.aurine.cloudx.estate.constant;

/**
 * Title: PersonConstant
 * Description: 业主信息相关常量
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/4/21 17:18
 */
public interface PersonConstant {
    /**
     * 是产权人
     */
    String PROPERTY_OWNER = "0";
    /**
     * 非产权人
     */
    String NOT_PROPERTY_OWNER = "1";
    /**
     * 数据状态:冻结
     */
    String FREEZE="0";
    /**
     * 数据状态:启用
     */
    String USING="1";
    /**
     * 新增用户默认密码
     */
    String PASSWORD="123456";
    /**
     * 默认密码，AES加密 123456 => rKu1/348LvKp0rsVC06eCA==
     */
    String AES_PASSWORD="rKu1/348LvKp0rsVC06eCA==";
}
