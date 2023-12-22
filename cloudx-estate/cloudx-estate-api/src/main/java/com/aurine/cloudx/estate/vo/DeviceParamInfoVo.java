package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 *  存放设备参数json和对应的设备信息
 * </p>
 *
 * @ClassName: DeviceParamInfoVo
 * @author: 王良俊 <>
 * @date: 2020年12月17日 上午10:26:31
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceParamInfoVo {

    /**
     * 被修改过的服务ID集合
     * */
    private List<String> changeServiceIdList;

    /**
     * 设备ID
     * */
    private String deviceId;

    /**
     * 设备第三方编码
     * */
    private String thirdpartyCode;


    /**
     * 要进行设置的参数服务ID集合（多台设备时就是要同步的参数服务ID，单台设备就是这台设备所有的参数服务ID） 必填项
     * */
    private List<String> serviceIdList;

    /**
     * 一次性设置多台设备使用
     * */
    private List<ProjectDeviceInfo> deviceInfoList;

    /**
     * 所要批量设置的设备ID
     * */
    private List<String> deviceIdList;

    /**
     * 所要设置的设备参数json数据
     * */
    private String paramJson;

    /**
     * 是否是重配操作
     * */
    private boolean reset;
}
