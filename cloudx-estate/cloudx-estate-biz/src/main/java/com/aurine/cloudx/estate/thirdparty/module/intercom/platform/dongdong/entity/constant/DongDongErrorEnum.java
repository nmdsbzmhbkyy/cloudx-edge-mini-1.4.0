package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.entity.constant;

import lombok.AllArgsConstructor;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-18
 * @Copyright:
 */
@AllArgsConstructor
public enum DongDongErrorEnum {
    SUCCESS("0", "成功"),
    NO_LOGIN("1", "尚未登录"),
    EMAIL_EXISTS("2", "Email 地址已存在"),
    PHONE_EXISTS("3", "手机号码已存在"),
    BlANK_UPDATE("4", "修改信息为空"),
    WRONG_ACCESS("5", "无效的账号或者密码"),
    DATABASE_ERROR("6", "数据库执行失败"),
    WRONG_PARAMS("7", "无效的参数"),// *
    WRONG_ID("8", "无效的厂商 ID"),//*
    OUT_OF_LIMIT("9", "超出上限"),//*
    WRONG_ROOM_ID("10", "无效的房号 ID"),//
    WRONG_VILLAGE_ID("11", "无效的小区 ID"),//
    WRONG_SERIAL("12", "无效的序列号"),//
    ALREADY_ADD("13", "已经被添加"),//*
    WRONG_DEVICE_ID("14", "无效的设备 ID"),//*
    WRONG_PHONE("15", "无效的手机号码"),//*
    WRONG_ROOM_USER("16", "无效的房号用户"),
    WRONG_ROOM_CARD("17", "无效的房号卡"),
    CAN_NOT_UPDATE_ROOM_NO("18", "暂不允许修改房号"),
    UNSUPPORTED("1000", "服务不支持"),
    AUTH_FAILED("1001", "参数加密错误"),
    PARAMS_INVALID("1002", "参数无效"),
    UNKNOW("1003", "未知异常");

    public String code;
    public String value;


    /**
     * @param code
     * @return
     */
    public static DongDongErrorEnum getByCode(String code) {
        for (DongDongErrorEnum value : DongDongErrorEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
    }
