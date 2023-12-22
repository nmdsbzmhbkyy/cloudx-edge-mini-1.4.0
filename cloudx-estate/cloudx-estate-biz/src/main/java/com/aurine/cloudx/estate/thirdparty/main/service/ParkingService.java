

package com.aurine.cloudx.estate.thirdparty.main.service;

import com.aurine.cloudx.estate.entity.ProjectEntryExitLane;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceParkingPassDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceParkingPayDTO;

import java.util.List;

/**
 * <p>车场统一回调业务接口</p>
 *
 * @ClassName: ParkingService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06 11:52
 * @Copyright:
 */
public interface ParkingService {


    /**
     * 回调添加车辆入场纪录
     *
     * @param deviceParkingPassDTO
     * @return
     */
    boolean enterCar(EventDeviceParkingPassDTO deviceParkingPassDTO);

    /**
     * 回调添加车辆出场纪录
     *
     * @param deviceParkingPassDTO
     * @return
     */
    boolean outerCar(EventDeviceParkingPassDTO deviceParkingPassDTO);

    /**
     * 回调添加车辆入场图片
     *
     * @param deviceParkingPassDTO
     * @return
     */
    boolean enterImg(EventDeviceParkingPassDTO deviceParkingPassDTO);

    /**
     * 回调添加车辆出场图片
     *
     * @param deviceParkingPassDTO
     * @return
     */
    boolean outerImg(EventDeviceParkingPassDTO deviceParkingPassDTO);

    /**
     * 回调添加临时车缴费记录
     *
     * @param deviceParkingPayDTO
     * @return
     */
    boolean pay(EventDeviceParkingPayDTO deviceParkingPayDTO);

    /**
     * 获取车道信息
     *
     * @param parkingId
     * @return
     */
    List<ProjectEntryExitLane> getLaneList(String parkingId);

    /**
     * 设置车场在线状态
     *
     * @param eventDeviceParkingPassDTO
     */
    void parkingStatus(EventDeviceParkingPassDTO eventDeviceParkingPassDTO);

}
