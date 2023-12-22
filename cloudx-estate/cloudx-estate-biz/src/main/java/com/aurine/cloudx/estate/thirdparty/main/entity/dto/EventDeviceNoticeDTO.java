package com.aurine.cloudx.estate.thirdparty.main.entity.dto;

import com.aurine.cloudx.estate.entity.ProjectDeviceCallEvent;
import lombok.Data;

/**
 * @description: 设备事件通知 DTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06
 * @Copyright:
 */
@Data
public class EventDeviceNoticeDTO extends BaseDTO {
    /**
     * 设备状态变化
     */
    private String status;

    /**
     * 设备的第三方编码
     */
    private String thirdPartyCode;
    /**
     * 设备的SN编码
     */
    private String deviceSn;

    /**
     * 通话记录
     * */
    private ProjectDeviceCallEvent deviceCallEvent;

}
