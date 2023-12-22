package com.aurine.cloudx.estate.thirdparty.module.wr20.constant;

import lombok.AllArgsConstructor;

/**
 * WR20 服务 枚举
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-10-10
 * @Copyright:
 */
@AllArgsConstructor
public enum WR20ServiceEnum {

    CERT_CARD("CardManager", "卡管理服务","3"),
    CERT_PASSWORD("PasswordManager", "密码管理服务","4"),
    CERT_FACE("FaceManager", "人脸管理服务","2"),
    TENEMENT_MANAGER("TenementManager", "住户管理",""),
    PREMISSION("PREMISSION", "住户权限控制",""),
    FRAME_INFO_MANAGER("FrameInfoManager", "框架服务",""),
    WORKER_MANAGER("WorkerManager", "员工服务",""),
    VISITOR_MANAGER("VisitorManager", "访客服务",""),
    DEVICE_INFO_MANAGER("DeviceInfoManager", "设备同步服务",""),
    VILLAGE_INFO_MANAGER("VillageInfoManager", "社区网关管理",""),
    OTHER_MANAGER("OTHER", "无相关管理服务","");

    public String code;
    public String desc;
    public String cloudCode;


    public static WR20ServiceEnum getByCode(String code) {
        WR20ServiceEnum[] huaweiEnums = values();
        for (WR20ServiceEnum huaweitEnum : huaweiEnums) {
            if (huaweitEnum.code().equals(code)) {
                return huaweitEnum;
            }
        }
        return WR20ServiceEnum.OTHER_MANAGER;
    }

    public static WR20ServiceEnum getByCloudCode(String cloudCode) {
        WR20ServiceEnum[] huaweiEnums = values();
        for (WR20ServiceEnum huaweitEnum : huaweiEnums) {
            if (huaweitEnum.cloudCode.equals(cloudCode)) {
                return huaweitEnum;
            }
        }
        return null;
    }

    private String code() {
        return this.code;
    }
}
