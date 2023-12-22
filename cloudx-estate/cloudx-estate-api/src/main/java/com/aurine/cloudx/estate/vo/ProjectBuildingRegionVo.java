package com.aurine.cloudx.estate.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *     用于移动楼栋时使用
 * </p>
 * @ClassName: ProjectBuildingRegionVo
 * @author: 王良俊 <>
 * @date:  2020年12月09日 上午11:16:11
 * @Copyright:
*/
@Data
public class ProjectBuildingRegionVo {

    // 楼栋ID数组
    private List<String> buildingIdList;

    // 区域ID
    private String regionId;
}
