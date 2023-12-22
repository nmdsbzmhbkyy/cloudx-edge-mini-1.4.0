package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 抓拍参数对象 json名：snapParam
 * </p>
 *
 * @ClassName: SnapParamObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:18:08
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("snapParam")
public class SnapParamObj {

    /**
     * 访客呼叫拍照
     * 0：不启用 1：启用
     */
    @NotNull
    Integer callVisitor;

    /**
     * 呼叫中心拍照
     * 0：不启用 1：启用
     */
    @NotNull
    Integer callCenter;

    /**
     * 错误密码开门拍照
     * 0：不启用 1：启用
     */
    @NotNull
    Integer errPwdOpendoor;

    /**
     * 挟持密码开门拍照
     * 0：不启用 1：启用
     */
    @NotNull
    Integer holdPwdOpendoor;

    /**
     * 人脸开门抓拍
     * 0：不启用 1：启用
     */
    Integer faceOpendoor;

    /**
     * 指纹开门抓拍
     * 0：不启用 1：启用
     */
    Integer fingerOpendoor;

    /**
     * 刷卡开门抓拍
     * 0：不启用 1：启用
     */
    @NotNull
    Integer cardOpendoor;

    /**
     * 密码开门抓拍
     * 0：不启用 1：启用
     */
    @NotNull
    Integer pwdOpendoor;

    /**
     * 扫码开门抓拍
     * 0：不启用 1：启用
     */
    Integer qrcodeOpendoor;

    /**
     * 陌生人脸抓拍
     * 0：不启用 1：启用
     */
    Integer strangerFace;
    /**
     * 临时密码开门抓拍
     * 0：不启用 1：启用
     */
    Integer tempPwdOpen;
    /**
     * 防拆报警抓拍
     * 0：不启用 1：启用
     */
    Integer tamperAlarm;
}
