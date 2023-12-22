package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *  巡检点设备列表
 * </p>
 *
 * @author 王良俊
 * @param
 * @return
 * @throws
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectPointDeviceVo extends ProjectDeviceInfo {

    /**
    * 设备id
    */
    private String deviceId;

    /**
    * 设备所在位置
    */
    private String address;

    /**
    * 设备名
    */
    private String deviceName;

    /**
    * 设备类型名
    */
    private String deviceTypeName;

    /**
     * 区域id
     */
    private String regionId;

    /**
     * 区域名
     */
    private String regionName;

    /**
    * 检查项数量
    */
    private String checkItemNum;
}
