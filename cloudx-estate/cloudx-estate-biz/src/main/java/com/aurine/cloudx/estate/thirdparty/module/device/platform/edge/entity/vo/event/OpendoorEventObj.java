package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 开门事件对象
 * </p>
 *
 * @ClassName: OpendoorEventObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午03:27:40
 * @Copyright:
 */
@Data
@AllArgsConstructor
public class OpendoorEventObj {

    /**
     * 开门方式:
     * 1：卡号,
     * 2：密码,
     * 3：二维码,
     * 4：人脸识别,
     * 5：指纹，
     * 14：人证核验，
     * 21：远程开门
     */
    @NotNull
    String openMode;

    /**
     * 门禁设备ID
     * 第三方ID
     */
    @NotNull
    String devId;

    /**
     * 第三方门禁ID
     */
    @NotNull
    String thirdDeviceId;

    /**
     * 事件描述
     */
    @NotNull
    String eventDesc;

    /**
     * 用户ID
     * 当InOutStatus为异常，该值为空
     * 第三方ID
     */
    @NotNull
    String userId;
    /**
     * 相似度
     */
    String similarity;
    /**
     * 体温
     * OpenMode值为4．人脸识别开门时才可能有该值
     */
    String temperature;

    /**
     * 用户名称
     * 当InOutStatus为异常，该值为空
     */
    @NotNull
    String userName;

    /**
     * 用户描述
     * 住户框架或地址
     */
    @NotNull
    String userDesc;

    /**
     * 用户类型描述
     * 详见【成员类别定义】
     */
    @NotNull
    String userType;

    /**
     * 通行标识
     * 卡号/身份证号
     */
    @NotNull
    String passId;

    /**
     * 住户第三方ID
     */
    String userThirdId;

    /**
     * 记录时间
     * yyyy-MM-dd HH:mm:ss
     */
    @NotNull
    String recordTime;

    /**
     * 进出状态
     * 0:进，
     * 1：出，
     * 2：异常
     */
    @NotNull
    String inOutStatus;

    /**
     * 场景抓拍图片
     * 图片ＵＲＬ地址
     */
    String snapPic;
    /**
     * 抓拍地址2
     */
    String imageUrl;

    /**
     * 人脸抓拍图片
     * 图片ＵＲＬ地址
     */
    String facePic;

    /**
     * 二维码code*
     */
    String qrCode;

//    /**
//     * 体温
//     * OpenMode值为4．人脸识别开门时才可能有该值
//     */
//    float Temperature;
//
//    /**
//     * 相似度
//     */
//    Double similarity;

    /**
     * 获取抓拍路径
     */
    public String getSnapImg() {
        if (StringUtils.isNotEmpty(this.snapPic)) {
            return this.snapPic;
        } else {
            return this.imageUrl;
        }
    }
}
