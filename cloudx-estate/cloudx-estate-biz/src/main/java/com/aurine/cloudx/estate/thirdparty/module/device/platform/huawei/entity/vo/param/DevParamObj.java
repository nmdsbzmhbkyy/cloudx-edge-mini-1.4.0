package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 设备参数 json名：devParam
 * </p>
 *
 * @ClassName: DevParamObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:22:28
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("devParam")
public class DevParamObj {

    /**
     * 密码模式
     * 0：简易密码 1：高级密码
     */
    Integer pwdMode;

    /**
     * 动态密码
     * 0：关闭 1：启用
     */
    Integer dynamicPwd;

    /**
     * 省电模式
     * 0：关闭 1：启用
     */
    Integer powerMode;

    /**
     * 屏保设置
     * 0：关闭 1：启用
     */
    Integer screenPro;

    /**
     * 人体感应类型
     * 0：触发开屏
     * 1：人脸识别
     * 2：扫码开门
     */
    Integer bodyInduction;

    /**
     * 卡号长度
     * 6:6位卡号  8:8位卡号
     */
    Integer cardNoLen;
}
