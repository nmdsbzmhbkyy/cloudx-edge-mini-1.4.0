package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant;

import lombok.AllArgsConstructor;

/**
 *  异常信息 字典枚举
 *
 * @ClassName: AurineMessageTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-01-19
 * @Copyright:
 */
@AllArgsConstructor
public enum AurineErrorCodeEnum {

    SUCCESS("0", "成功", "linux",true),
    FTP_CONN_FAIL("4002", "FTP服务器无法连接", "linux",false),
    FTP_FILE_NO_EXITS("4003", "FTP文件不存在", "linux",true),
    FTP_FILE_LENGTH_ERROR("4004", "FTP文件长度为0", "linux",true),
    FACE_NO_POWER("4005", "人脸功能未启用", "linux",true),
    DEVICE_NO_FACE_LAB("4006", "设备未授权人脸库", "linux",true),
    BUSY("4010", "繁忙无法注册（呼叫中或识别中）", "linux",false),
    FACE_REG_ERROR("4011", "照片无法注册", "linux",true),
    NO_FACE_ERROR("4012", "检测不到人脸", "linux",true),
    TOO_MANY_FACE("4013", "检测到多张人脸", "linux",true),
    BlACK_PHOTO("4014", "黑白照片", "linux",true),
    FACE_SIZE_ERROR("4015", "人脸尺寸不合格", "linux",true),
    PHOTO_BAD_ERRORB("4016", "照片模糊", "linux",true),
    PHOTO_TO_BIG("4017", "照片文件太大", "linux",true),
    FULL("4018", "存储空间满", "linux",true),
    BACKING("4019", "人脸库损坏获取备份中", "linux",false),
    RESTARTING("4020", "即将半夜重启（重启前30秒）", "linux",false),
    FACE_REGING("4021", "本地人脸注册中", "linux",false),
    OTHER("-1", "未知错误", "linux",true);

    public String code;
    public String desc;
    public String system;
    public boolean submitFlag;//是否提交操作，false的时候，将忽略本次回调

    public static AurineErrorCodeEnum getByCode(String code) {
        AurineErrorCodeEnum[] entityEnums = values();
        for (AurineErrorCodeEnum entity : entityEnums) {
            if (entity.code.equals(code)) {
                return entity;
            }
        }
        return AurineErrorCodeEnum.OTHER;
    }
}
