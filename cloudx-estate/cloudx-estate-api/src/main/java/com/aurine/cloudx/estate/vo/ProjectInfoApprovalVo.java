package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Title: ProjectInfoApprovalVo
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/9 10:55
 */
@Data
@ApiModel("项目审核Vo")
public class ProjectInfoApprovalVo {
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    /**
     * 审核不通过原因
     */
    @ApiModelProperty(value = "审核不通过原因")
    private String auditReason;

    /**
     * 审批是否通过
     */
    @ApiModelProperty(value="审批是否通过")
    private Boolean pass;

    /**
     * 项目到期时间
     */
    @ApiModelProperty(value="项目到期时间")
    private LocalDate expTime;
}
