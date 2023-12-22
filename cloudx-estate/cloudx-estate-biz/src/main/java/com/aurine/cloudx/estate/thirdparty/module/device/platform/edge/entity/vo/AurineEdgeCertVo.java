package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo;

import lombok.Data;

/**
 * @ClassName: HuaweiCertVo
 * @author: 王良俊 <>
 * @date: 2020年11月23日 下午04:22:36
 * @Copyright:
 */
@Data
public class AurineEdgeCertVo {



    /**
     * 必填：否
     * 设备ID
     */
    String devId;

    /**
     * 必填：是
     * 钥匙校验号码
     * 对于卡：为卡号
     * 对于密码：为密码
     * 对于人脸：为人脸ID
     */
    String passNo;

    /**
     * 必填：否
     * 人脸类型时为图片url地址
     */
    String extul;

    /**
     * 必填：否
     * 钥匙属性
     * 默认为管理员进门
     * 0：管理员/可进门
     * 1：普通进门 以下扩展功能：
     * 10：黑名单
     * 11：挟持
     * 12：特殊人群
     * 13：巡更
     * 100：其他
     */
    int attri;

    /**
     * 必填：否
     * 生效时间 时间戳
     */
    String starttime;

    /**
     * 必填：否
     * 失效时间 时间戳
     */
    String endtime;

    /**
     * 必填：否
     * 可用次数
     * 默认无限次
     * 当>0时，时间参数不生效，使用一次减一，减到0，则不再使用。
     * 当=-1时，限制使用时间
     */
    String lifecycle;

    /**
     * 必填：否
     * 关联房号
     */
    String roomno;

    /**
     * 必填：否
     * 关联人员ID
     */
    String personId;

    // 人员对象 personInfo
}
