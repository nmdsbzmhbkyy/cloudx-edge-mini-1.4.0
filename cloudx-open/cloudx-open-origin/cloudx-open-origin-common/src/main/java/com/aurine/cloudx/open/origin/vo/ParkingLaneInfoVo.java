package com.aurine.cloudx.open.origin.vo;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 车场车道信息对象
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/23 9:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLaneInfoVo {

    /*
     * 车场ID
     **/
    private String parkId;

    /*
     * 车道ID列表
     **/
    private List<String> laneIdList = new ArrayList<>();

    /*
     * 车道绑定的设备ID列表
     **/
    private List<String> deviceIdList = new ArrayList<>();

    public void addDevice(String deviceId) {
        if (StrUtil.isNotEmpty(deviceId)) {
            this.deviceIdList.add(deviceId);
        }
    }

    public void addLane(String laneId) {
        if (StrUtil.isNotEmpty(laneId)) {
            this.laneIdList.add(laneId);
        }
    }

}
