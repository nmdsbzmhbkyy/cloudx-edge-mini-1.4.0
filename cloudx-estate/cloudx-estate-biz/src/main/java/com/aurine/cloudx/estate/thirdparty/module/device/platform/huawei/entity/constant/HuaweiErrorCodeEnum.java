package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant;

import lombok.AllArgsConstructor;

/**
 *  异常信息 字典枚举
 *
 * @ClassName: HuaweiErrorCodeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-01-19
 * @Copyright:
 */
@AllArgsConstructor
public enum HuaweiErrorCodeEnum {

    SUCCESS("0", "成功", true),
    FTP_CONN_FAIL("4002", "服务器无法连接", true),
    FTP_FILE_NO_EXITS("4003", "服务器文件不存在", true),
    FTP_FILE_LENGTH_ERROR("4004", "服务器文件长度为0", true),
    FACE_NO_POWER("4005", "人脸功能未启用", true),
    DEVICE_NO_FACE_LAB("4006", "设备未授权人脸库", true),
    BUSY("4010", "繁忙无法注册（呼叫中）", false),
    FACE_REG_ERROR("4011", "照片无法注册（通用）", true),
    NO_FACE_ERROR("4012", "检测不到人脸", true),
    TOO_MANY_FACE("4013", "检测到多张人脸", true),
    BlACK_PHOTO("4014", "黑白照片", true),
    FACE_SIZE_ERROR("4015", "人脸尺寸不合格", true),
    PHOTO_BAD_ERRORB("4016", "照片模糊", true),
    PHOTO_TO_BIG("4017", "照片文件太大", true),
    FULL("4018", "存储空间满", true),
    BACKING("4019", "人脸库损坏获取备份中", false),
    RESTARTING("4020", "即将半夜重启（重启前30秒）", false),
    FACE_REGING("4021", "本地人脸注册中", false),
    OTHER("-1", "未知错误", true);

    public String code;
    public String desc;
    public boolean submitFlag;//是否提交操作，false的时候，将忽略本次回调

    public static HuaweiErrorCodeEnum getByCode(String code) {
        HuaweiErrorCodeEnum[] entityEnums = values();
        for (HuaweiErrorCodeEnum entity : entityEnums) {
            if (entity.code.equals(code)) {
                return entity;
            }
        }
        return HuaweiErrorCodeEnum.OTHER;
    }
}
