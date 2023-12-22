package com.aurine.cloudx.open.origin.vo;

import lombok.Data;

/**
 * <p>
 * 区域管理楼栋分配页的数据展示
 * </p>
 *
 * @ClassName: ProjectBuildingRegionInfoVo
 * @author: 王良俊 <>
 * @date: 2020年12月08日 下午03:59:28
 * @Copyright:
 */
@Data
public class ProjectBuildingRegionInfoVo {

    // 楼栋ID
    private String buildingId;

    // 楼栋名
    private String buildingName;

    // 区域ID
    private String regionId;

    // 区域名
    private String regionName;
}
