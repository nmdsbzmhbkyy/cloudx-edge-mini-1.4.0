package com.aurine.cloudx.estate.vo;

import lombok.Data;

/**
 * <p>
 *  员工和设备区域-用于区域观管辖分配功能
 * </p>
 *
 * @ClassName: StaffRegionVo
 * @author: 王良俊 <>
 * @date: 2020年12月10日 上午09:10:18
 * @Copyright:
 */
@Data
public class StaffRegionVo {

    // 员工ID
    private String staffId;

    // 员工姓名
    private String staffName;

    // 员工管辖区域数量
    private Integer regionNum;

    // 员工管辖区域数量
    private Integer departmentId;

}
