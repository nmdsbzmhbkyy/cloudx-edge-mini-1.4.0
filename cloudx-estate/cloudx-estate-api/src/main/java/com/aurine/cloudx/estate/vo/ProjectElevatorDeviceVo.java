package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>电梯设备信息vo对象</p>
 *
 * @author 王良俊
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectElevatorDeviceVo extends ProjectDeviceInfo {

    /*
     * 电梯状态 0 正常 1 异常 （后面两种可能没有）| 2 控制器故障 3 识别设备故障
     * */
    private String elevatorStatus;

    /*
     * 楼栋名
     * */
    private String buildingName;

    /*
     * 区域名
     * */
    private String regionName;

}
