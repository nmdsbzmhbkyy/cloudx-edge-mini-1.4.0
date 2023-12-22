package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant;

import lombok.AllArgsConstructor;

/**物联网报警类型状态
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-07-09 16:28
 */
@AllArgsConstructor
public enum HuaweiAlarmStateEnum {
    STATE_NORMAL("0","正常"),
    STATE_ABNORMAL("1","异常");


    public String code;
    public String state;
}
