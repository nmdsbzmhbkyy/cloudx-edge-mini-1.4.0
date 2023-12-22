package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectStaffShiftDetail;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * (ProjectStaffShiftDetailUpdateVo) 用于排班计划详情更新
 *
 * @author guhl
 * @since 2021/3/24 10:16
 */
@Data
@ApiModel(value = "用于排班计划详情更新")
public class ProjectStaffShiftDetailUpdateVo extends ProjectStaffShiftDetail {
    /**
     * 真正需要更新的字段名称
     */
    private String column;
}
