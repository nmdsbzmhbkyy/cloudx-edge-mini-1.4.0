package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectStaffShiftDetail;
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
