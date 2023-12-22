package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目设备楼栋单元名称vo
 *
 * @author 邹宇
 * @date 2021-9-26 11:41:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDeviceBuildingUnitNameVo {

    /**
     * 楼栋名称
     */
    private String buildingName;


    /**
     * 单元名称
     */
    private String unitName;

    /**
     * 房屋名称
     */
    private String houseName;


}
