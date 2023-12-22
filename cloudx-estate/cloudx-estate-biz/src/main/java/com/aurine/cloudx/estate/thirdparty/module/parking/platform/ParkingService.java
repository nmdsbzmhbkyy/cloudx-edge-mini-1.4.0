

package com.aurine.cloudx.estate.thirdparty.module.parking.platform;

import com.aurine.cloudx.estate.entity.ProjectEntryExitLane;
import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.aurine.cloudx.estate.entity.ProjectParkBillingRule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 统一停车场接入服务
 */
public interface ParkingService extends BaseService {


    /**
     * 添加车辆
     *
     * @param carRegister
     * @return
     */
    boolean addCar(ProjectParCarRegister carRegister);

    /**
     * 注销车辆
     *
     * @param carRegister 车辆注册信息
     * @return
     */
    boolean removeCar(ProjectParCarRegister carRegister);


    /**
     * 延期与充值
     *
     * @param plateNumber
     * @param chargeMoney
     * @param startDate
     * @param endDate
     * @return
     */
    boolean extraDate(ProjectParCarRegister carRegister, String plateNumber, BigDecimal chargeMoney, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取临时车缴费记录
     *
     * @param orderNo
     * @param parkingId
     * @return
     */
    boolean getTmpPayRecords(String parkingId, String orderNo);


    /**
     * 获取当前停车场的车道数据
     *
     * @param parkingId
     * @return
     */
    List<ProjectEntryExitLane> getLaneList(String parkingId);

    /**
     * 获取车场支付URL
     *
     * @param parkingId
     * @return
     */
    String getParkingPayUrl(String parkingId);

    /**
     *  获取支付类型列表
     * @param parkingId
     */
    List<ProjectParkBillingRule> getBillingRuleList(String parkingId,Integer projectId);


}
