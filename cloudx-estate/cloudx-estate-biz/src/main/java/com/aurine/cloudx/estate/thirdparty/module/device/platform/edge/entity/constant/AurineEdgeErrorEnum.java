package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiFaceResultCodeEnum;
import lombok.AllArgsConstructor;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/14 17:03
 * @Copyright:
 */
@AllArgsConstructor
public enum AurineEdgeErrorEnum {

    SUCCESS("0","操作成功"),
    FAIL("1","操作失败"),
    LOST_PUBLIC_PARAM("1000","缺少公共参数"),
    WRONG_PARAM_NUMBER("1001","公共参数个数错误"),
    LOST_NECESSARY_PARAM("1004","缺少必须参数"),
    WRONG_TOKEN("1005","验证失败"),
    WRONG_APP("1006","应用id失效，请联系管理员"),
    WRONG_SIGN("1008","授权码错误"),
    SIGN_OUTTIME("1009","授权码已过期"),
    NO_RIGHT("1010","应用权限不足"),
    NO_DEVICE("2001","未找到指定设备"),
    NO_GROUP("4002","请求对象不存在"),
    NO_SUBSCRIBE("3001","订阅不存在"),
    SERVER_CONNECTION_FAILED(HuaweiFaceResultCodeEnum.SERVER_CONNECTION_FAILED.resultCode, HuaweiFaceResultCodeEnum.SERVER_CONNECTION_FAILED.desc),
    SERVER_FILE_DOES_NOT_EXIST(HuaweiFaceResultCodeEnum.SERVER_FILE_DOES_NOT_EXIST.resultCode, HuaweiFaceResultCodeEnum.SERVER_FILE_DOES_NOT_EXIST.desc),
    SERVER_FILE_LENGTH_IS_ZERO(HuaweiFaceResultCodeEnum.SERVER_FILE_LENGTH_IS_ZERO.resultCode, HuaweiFaceResultCodeEnum.SERVER_FILE_LENGTH_IS_ZERO.desc),
    FACE_FEATURE_IS_NOT_ENABLED(HuaweiFaceResultCodeEnum.FACE_FEATURE_IS_NOT_ENABLED.resultCode, HuaweiFaceResultCodeEnum.FACE_FEATURE_IS_NOT_ENABLED.desc),
    DEVICE_UNAUTHORIZED_FACE_DATABASE(HuaweiFaceResultCodeEnum.DEVICE_UNAUTHORIZED_FACE_DATABASE.resultCode, HuaweiFaceResultCodeEnum.DEVICE_UNAUTHORIZED_FACE_DATABASE.desc),
    REGISTER_FAIL_WHEN_BUSY(HuaweiFaceResultCodeEnum.REGISTER_FAIL_WHEN_BUSY.resultCode, HuaweiFaceResultCodeEnum.REGISTER_FAIL_WHEN_BUSY.desc),
    PHOTO_CANNOT_REGISTERED(HuaweiFaceResultCodeEnum.PHOTO_CANNOT_REGISTERED.resultCode, HuaweiFaceResultCodeEnum.PHOTO_CANNOT_REGISTERED.desc),
    UNRECOGNIZED_PHOTO(HuaweiFaceResultCodeEnum.UNRECOGNIZED_PHOTO.resultCode, HuaweiFaceResultCodeEnum.UNRECOGNIZED_PHOTO.desc),
    NOT_ENOUGH_SPACE(HuaweiFaceResultCodeEnum.NOT_ENOUGH_SPACE.resultCode, HuaweiFaceResultCodeEnum.NOT_ENOUGH_SPACE.desc),
    RESTART_IN_THE_MIDDLE_OF_THE_NIGHT(HuaweiFaceResultCodeEnum.RESTART_IN_THE_MIDDLE_OF_THE_NIGHT.resultCode, HuaweiFaceResultCodeEnum.RESTART_IN_THE_MIDDLE_OF_THE_NIGHT.desc),
    REGISTERING_LOCAL_FACE(HuaweiFaceResultCodeEnum.REGISTERING_LOCAL_FACE.resultCode, HuaweiFaceResultCodeEnum.REGISTERING_LOCAL_FACE.desc);

    public String code;
    public String desc;



    public static AurineEdgeErrorEnum getByCode(String code) {
        AurineEdgeErrorEnum[] huaweiEventEnums = values();
        for (AurineEdgeErrorEnum huaweiEventEnum : huaweiEventEnums) {
            if (huaweiEventEnum.code().equals(code)) {
                return huaweiEventEnum;
            }
        }
        return AurineEdgeErrorEnum.FAIL;
    }

    private String code() {
        return this.code;
    }
}
