package com.aurine.cloudx.estate.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *  更新区域的管辖人员
 * </p>
 *
 * @ClassName: ProjectStaffRegionVo
 * @author: 王良俊 <>
 * @date: 2020年12月10日 下午02:22:45
 * @Copyright:
 */
@Data
public class ProjectStaffRegionVo {

    // 员工ID列表
    private List<String> staffIdList;

    // 区域ID
    private String regionId;
}
