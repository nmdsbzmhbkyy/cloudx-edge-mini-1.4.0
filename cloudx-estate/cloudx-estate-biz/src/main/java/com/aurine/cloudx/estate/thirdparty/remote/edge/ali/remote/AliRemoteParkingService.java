

package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.remote;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.remote.BaseRemote;

import java.util.ArrayList;
import java.util.List;

/**
 * 阿里停车场接入服务
 */
public interface AliRemoteParkingService extends BaseRemote {
    /**
     * 添加车辆
     * @param areaId 车牌号
     * @param parkingLotId 停车场id
     * @param projectId 项目id
     * @param userId 车主证件号
     * @param userName 车主名称
     * @param userPhone 车主手机号
     * @return
     */
    Boolean addCar(ArrayList<String> areaId, String parkingLotId, String projectId, String userId, String userName, String userPhone);

    /**
     *  删除车辆
     * @param parkingLotId 停车场id
     * @param plateNumber 车牌号
     * @param projectId  项目id
     * @return
     */
    JSONObject removeCar(String parkingLotId, String plateNumber, String projectId);

    /**
     * 更新车辆
     * @param areaId 车牌号
     * @param parkingLotId 停车场id
     * @param projectId 项目id
     * @param userId 车主证件号
     * @param userName 车主名称
     * @param userPhone 车主手机号
     * @return
     */
    JSONObject updateCar(ArrayList<String> areaId, String parkingLotId, String projectId, String userId, String userName, String userPhone);
    /**
     * 获取车场信息
     *
     *
     * @return
     */
    JSONObject getParkInfo(String projectId);

    /**
     * 获取停车场区域信息
     * @param areaId  区域id
     * @param parkingLotId  停车场id
     * @param projectId
     * @return
     */
    JSONObject getParkingArea(String areaId,String parkingLotId,String projectId);

    /**
     *  停车场区域下道闸列表信息查询
     * @param areaId 区域id
     * @param parkingLotId 停车场id
     * @param projectId
     * @return
     */
    JSONObject getParkingAreaBarrier(String areaId,String parkingLotId,String projectId);

    /**
     * 停车场区域列表信息查询
     * @param parkingLotId
     * @param projectId
     * @return
     */
    JSONObject getParkingAreaList(String parkingLotId,String projectId);

    /**
     * 查询区域车位列表
     * @param areaId
     * @param parkingLotId
     * @param projectId
     * @return
     */
    JSONObject getParkingAreaParkSpace(String areaId,String parkingLotId,String projectId);

    /**
     * 停车场道闸列表信息查询
     * @param parkingLotId 停车场id
     * @param projectId
     * @return
     */
    JSONArray getParkingBarrierList(String parkingLotId, String projectId);
    /**
     * 车辆对道闸授权
     * @param barrierList 道闸列表
     * @param effectiveDate 开始时间 yyyy-MM-dd HH:mm:ss 或 yyyy/MM/dd HH:mm:ss
     * @param expiryDate 终止时间 yyyy-MM-dd HH:mm:ss 或 yyyy/MM/dd HH:mm:ss
     * @param parkingLotId  停车场id
     * @param plateNumber 车牌号
     * @param projectId
     * @return
     */
    JSONObject permissionBarrier(List<String> barrierList,
                                 String effectiveDate,
                                 String expiryDate,
                                 String parkingLotId,
                                 String plateNumber,
                                 String projectId
                                 );
    /**
     * 车辆对道闸授权更新
     * @param barrierList 道闸列表
     * @param effectiveDate 开始时间 yyyy-MM-dd HH:mm:ss 或 yyyy/MM/dd HH:mm:ss
     * @param expiryDate 终止时间 yyyy-MM-dd HH:mm:ss 或 yyyy/MM/dd HH:mm:ss
     * @param parkingLotId  停车场id
     * @param plateNumber 车牌号
     * @param projectId
     * @return
     */
    JSONObject updatePermissionBarrier(List<String> barrierList,
                                 String effectiveDate,
                                 String expiryDate,
                                 String parkingLotId,
                                 String plateNumber,
                                 String projectId
    );

}
