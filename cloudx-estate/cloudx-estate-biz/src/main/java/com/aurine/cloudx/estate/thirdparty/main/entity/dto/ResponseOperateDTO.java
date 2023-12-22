package com.aurine.cloudx.estate.thirdparty.main.entity.dto;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.vo.DeviceParamJsonVo;
import lombok.Data;

import java.util.List;

/**
 * @description: 指令下发结果 DTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06
 * @Copyright:
 */

@Data
public class ResponseOperateDTO extends BaseDTO {

    /**
     * 设备编号
     */
    private String thirdPartyCode;

    /**
     * 设备SN
     */
    private String deviceSn;


    /**
     * 设备框架号
     */
    private String deviceFrameNo;

    /**
     * 要更新的设备对象数据
     */
    private ProjectDeviceInfo deviceInfo;

    /**
     * 要更新的通行凭证
     */
    private ProjectRightDevice rightDevice;

    /**
     * 设备额外参数使用的json数据对象
     */
    private DeviceParamJsonVo deviceParamJsonVo;

    /**
     * 要更新的通行凭证
     */
    private List<ProjectRightDevice> rightDeviceList;

    /**
     * 响应状态，具体要更新的状态（成功、失败、空间不足等）
     */
    private String respondStatus;

    /**
     * 事件流水Id
     */
    private Integer eventCode;

    /**
     * 报警状态
     */
    private String alarmStatus;



/*    public boolean checkRespond() {
        return respondStatus.equalsIgnoreCase("1") ? true : false;
    }*/

}
