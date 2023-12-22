package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * 楼栋与公共楼层
 *
 * @author 陈喆
 * @date 2022-2-18
 */
@Data
@ApiModel(value = "楼栋与公共楼层")
public class BuildingPublicFloorVo {
    private List<ProjectBuildingInfoVo> list;

    /**
     * 公共楼层
     */
    private String publicFloors;

    /**
     * 楼层编号关系
     */
    private String floorNumber;
}
