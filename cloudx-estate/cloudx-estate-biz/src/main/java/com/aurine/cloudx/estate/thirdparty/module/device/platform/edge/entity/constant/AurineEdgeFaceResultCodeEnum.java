package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant;

import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import lombok.AllArgsConstructor;

/**
 * <p>
 *  3.0设备人脸下发回调结果码
 * </p>
 * @author : 王良俊
 * @date : 2021-06-08 10:58:54
 */
@AllArgsConstructor
public enum AurineEdgeFaceResultCodeEnum {

    SUCCESS("0","下载/删除 成功", PassRightCertDownloadStatusEnum.SUCCESS.code),
    FAIL("1","下载/删除 失败", PassRightCertDownloadStatusEnum.FAIL.code),
    SERVER_CONNECTION_FAILED("4002","服务器无法连接", PassRightCertDownloadStatusEnum.FAIL.code),
    SERVER_FILE_DOES_NOT_EXIST("4003","服务器文件不存在", PassRightCertDownloadStatusEnum.FAIL.code),
    SERVER_FILE_LENGTH_IS_ZERO("4004","服务器文件长度为0", PassRightCertDownloadStatusEnum.FAIL.code),
    FACE_FEATURE_IS_NOT_ENABLED("4005","人脸功能未启用", PassRightCertDownloadStatusEnum.FAIL.code),
    DEVICE_UNAUTHORIZED_FACE_DATABASE("4006","设备未授权人脸库", PassRightCertDownloadStatusEnum.FAIL.code),
    REGISTER_FAIL_WHEN_BUSY("4010","繁忙无法注册（呼叫中）", PassRightCertDownloadStatusEnum.RE_DOWNLOAD.code),
    PHOTO_CANNOT_REGISTERED("4011","照片无法注册（通用）", PassRightCertDownloadStatusEnum.FAIL.code),
    UNRECOGNIZED_PHOTO("4016","照片模糊", PassRightCertDownloadStatusEnum.FAIL.code),
    NOT_ENOUGH_SPACE("4018","存储空间满", PassRightCertDownloadStatusEnum.OUT_OF_MEMORY.code),
    RESTART_IN_THE_MIDDLE_OF_THE_NIGHT("4020","即将半夜重启（半夜重启前30S）", PassRightCertDownloadStatusEnum.RE_DOWNLOAD.code),
    REGISTERING_LOCAL_FACE("4021","本地人脸注册中", PassRightCertDownloadStatusEnum.RE_DOWNLOAD.code);


    // 设备回调的结果码
    public String resultCode;
    // 暂时只是作为意思解释
    public String desc;
    // 介质要更新的下载状态（下载中->下载失败/....）
    public String cetDownloadStatus;

    /**
    * <p>
    * 根据设备回调的结果码获取到对应的下载状态变更
    * </p>
    *
    * @param resultCode 设备介质结果码 result
    */
    public static AurineEdgeFaceResultCodeEnum getCertStatusByResult(String resultCode) {
        AurineEdgeFaceResultCodeEnum[] huaweiFaceResultCodeEnums = values();
        for (AurineEdgeFaceResultCodeEnum resultCodeEnum : huaweiFaceResultCodeEnums) {
            if (resultCodeEnum.resultCode.equals(resultCode)) {
                return resultCodeEnum;
            }
        }
        // 如果找不到则默认返回失败
        return AurineEdgeFaceResultCodeEnum.FAIL;
    }

}
