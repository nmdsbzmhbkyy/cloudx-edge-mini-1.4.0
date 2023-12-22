package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.entity.ProjectEntryExitLane;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ProjectEntryExitLaneDto
 * @Description 车道信息
 * @Author linlx
 * @Date 2022/5/20 15:07
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectEntryExitLaneDto extends ProjectEntryExitLane {

    private static final long serialVersionUID = 1L;

    /**
     * 车道id
     */
    private String laneId;

    private String laneCode;

    /**
     * 车道名称
     */
    private String laneName;

    /**
     * 关联project_vehicles_entry_exit.entryId
     */
    private String entryId;

    /**
     * 使用状态 1 在用 0 禁用
     */
    private String status;

    /**
     * 行进方向
     */
    private String direction;
    /**
     * 控制机机号
     */
    private String machineNo;

    /**
     * 车辆道闸一体机ID
     */
    private String deviceId;

    /**
     * 车辆道闸一体机
     */
    private String deviceName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 图片路径
     */
    private String picUrl;

    /**
     * 楼层号
     */
    private String floor;

    /**
     * 车场ID
     */
    private String parkId;

    /**
     * 设备第三方编码
     */
    private String thirdpartyCode;

    private String videoUrl;
}
