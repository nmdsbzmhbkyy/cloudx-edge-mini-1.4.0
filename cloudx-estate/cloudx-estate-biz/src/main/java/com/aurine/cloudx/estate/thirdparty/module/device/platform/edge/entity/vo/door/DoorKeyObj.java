package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.door;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 进门钥匙对象(DoorKeyObj)
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-25
 * @Copyright:
 */
@Data
public class DoorKeyObj {
    /**
     * 钥匙序号
     * 为该设备下的用户钥匙ID；
     */
    private String keyId;


    /**
     * 钥匙校验号码
     * 对于卡：为卡号 对于密码：为密码 对于人脸：为人脸ＩＤ
     */
    @NotNull
    private String passNo;

    /**
     * 扩展URL地址
     * 当为人脸类型时，此项作为人脸照片的URL地址
     */
    private String extUrl;


    /**
     * 钥匙属性
     * 默认为管理员进门
     * 0：管理员/可进门
     * 1：普通进门
     * <p>
     * 以下扩展功能：
     * 10：黑名单
     * 11：挟持
     * 12：特殊人群
     * 13：巡更
     * 100：其他
     */
    private Integer attri;

    /**
     * 生效时间
     * 无则不限制，标准时间戳
     * 秒？毫秒？
     */
    private String startTime;

    /**
     * 失效时间
     * 无则不限制，标准时间戳
     */
    private String endTime;

    /**
     * 可用次数
     * 默认为无限次数（该项不填） 当>0时，时间参数不生效，使用一次减一，减到0，则不再使用。 当=-1时，限制使用时间
     */
    private String lifecycle;


    /**
     * 关联房号 第三方
     */
    private String roomNo;

    /**
     * 关联人员ID 第三方
     */
    private String personId;

    /**
     * 人员对象信息
     */
    private JSONObject personInfo;

    /**
     * 类型
     * 	0：人脸
     * 1；卡
     */
    private Integer type;

    /**
     * 操作结果
     *
     */
    private Integer result;

    /**
     * 访问周期
     */
    private String period;
}
