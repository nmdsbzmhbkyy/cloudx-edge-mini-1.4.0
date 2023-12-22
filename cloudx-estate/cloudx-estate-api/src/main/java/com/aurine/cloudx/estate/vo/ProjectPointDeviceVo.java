package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceLocation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

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
