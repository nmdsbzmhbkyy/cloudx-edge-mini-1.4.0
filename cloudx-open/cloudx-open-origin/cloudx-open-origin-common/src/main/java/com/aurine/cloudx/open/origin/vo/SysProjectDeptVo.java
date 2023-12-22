package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.SysProjectDept;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysProjectDeptVo extends SysProjectDept {
    /**
     * 部门员工人数
     */
    @ApiModelProperty(value = "部门员工人数")
    private Integer staffCount;

    @ApiModelProperty("项目ID")
    private Integer projectId;
    /**
     * 租户ID
     */
    @ApiModelProperty("租户ID")
    private Integer tenantId;
}
