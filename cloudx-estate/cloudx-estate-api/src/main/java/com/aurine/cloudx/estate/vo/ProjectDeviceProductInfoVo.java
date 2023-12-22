package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 * @ClassName: ProjectDeviceProductInfoVo
 * @author: 王良俊 <>
 * @date:  2020年12月22日 上午09:49:31
 * @Copyright:
*/
@Data
public class ProjectDeviceProductInfoVo extends ProjectDeviceInfo {

    /**
     * 设备所属区域名
     * */
    private String regionName;

    /**
     * 产品能力集
     * */
    private String capability;

    /**
     * 设备厂商名
     * */
    private String manufacture;

    /**
     * 设备资源类型暂无
     * */
    private String devResType;

    /**
     * 产品型号ID
     * */
    private String modelId;

}
