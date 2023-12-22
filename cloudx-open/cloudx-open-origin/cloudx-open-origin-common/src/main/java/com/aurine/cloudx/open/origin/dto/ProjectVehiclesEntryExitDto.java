package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.entity.ProjectVehiclesEntryExit;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ProjectVehiclesEntryExitDto
 * @Description 车道信息
 * @Author linlx
 * @Date 2022/5/20 15:11
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectVehiclesEntryExitDto extends ProjectVehiclesEntryExit {

    private String entryId;

    /**
     * 第三方编码
     */
    private String entryCode;

    /**
     * 出入口名称
     */
    private String entryName;

    /**
     * 经度
     */
    private Double lon;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 坐标，面、多点、线
     */
    private String gisArea;

    /**
     * 坐标系代码
     */
    private String gisType;

    private String entryType;

    /**
     * 使用状态 1 在用 0 禁用
     */
    private String status;

    /**
     * 行进方向
     */
    private String direction;

    /**
     * 车道数
     */
    private Integer laneNumber;

    /**
     * 停车场id
     */
    private String parkId;

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
     * 车道ID列表
     * */
    @ApiModelProperty("车道ID列表")
    String[] laneIdArr;


}
