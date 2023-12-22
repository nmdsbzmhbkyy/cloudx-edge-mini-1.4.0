package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 车道一体机设备维护列表对象
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/17 11:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectVehicleBarrierDeviceInfoVo extends ProjectDeviceInfo {

    /*
     * 车牌下载成功数
     **/
    int successNum;

    /*
     * 车牌下载成功数
     **/
    int downloadingNum;

    /*
     * 车牌下载失败数
     **/
    int failedNum;

}
