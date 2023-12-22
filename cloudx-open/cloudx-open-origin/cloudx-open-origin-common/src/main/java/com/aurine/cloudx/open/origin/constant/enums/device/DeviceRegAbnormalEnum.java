package com.aurine.cloudx.open.origin.constant.enums.device;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>设备自动注册错误代码</p>
 * @author : 王良俊
 * @date : 2021-09-24 16:22:02
 */
@AllArgsConstructor
public enum DeviceRegAbnormalEnum {

    /**
     * 这里不需要再进行判断，只要设备端设备编号重复，就会再报一次注册失败（原来注册成功的也会）
     * 编号重复注册（就是设备编号重复）
     */
    /**
     * IPV4地址相同
     */
    AURINE_IPV4_REPEAT("IPV4地址相同", "", "1", DeviceRegParamEnum.IPV4.code),

    /**
     * 系统：编号重复注册 第三方：已存在(设备编号重复）
     */
    DEVICE_NO_REPEAT("编号重复注册", "7", "2", DeviceRegParamEnum.DEVICE_NO.code),

    /**
     * MAC相同
     */
    AURINE_MAC_REPEAT("MAC相同", "", "3", DeviceRegParamEnum.MAC.code),

    /**
     * 默认失败事件
     */
    OTHER("其他", "OTHER", "999", ""),
    ;

    /**
     * 描述
     */
    public String desc;

    /**
     * 错误代码 (这个有值的说明是中台文档中定义的异常)
     */
    public String thirdCode;

    /**
     * 错误代码（系统的）
     */
    public String systemCode;

    /**
     * 异常的目标参数
     */
    public String targetParam;

    public static DeviceRegAbnormalEnum getEnumByThirdCode(String code) {
        for (DeviceRegAbnormalEnum value : values()) {
            if (value.thirdCode.equals(code)) {
                return value;
            }
        }
        return DeviceRegAbnormalEnum.OTHER;
    }


    /**
     * <p>获取异常原因（使用','分隔）</p>
     *
     * @param failCodeSet 异常code Set集合
     * @return 异常原因
     * @author: 王良俊
     */
    public static String getDesc(Set<String> failCodeSet) {
        List<String> descList = new ArrayList<>();
        for (DeviceRegAbnormalEnum item : values()) {
            if (failCodeSet.contains(item.systemCode)) {
                descList.add(item.desc);
            }
        }
        return String.join(",", descList);
    }
}
