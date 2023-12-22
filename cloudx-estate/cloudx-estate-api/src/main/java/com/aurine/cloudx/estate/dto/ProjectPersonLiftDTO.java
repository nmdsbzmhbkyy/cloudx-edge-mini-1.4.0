
package com.aurine.cloudx.estate.dto;

import com.aurine.cloudx.estate.vo.ProjectDeviceLiftVo;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>住户/员工 权限 传递DTO</p>
 *
 * @ClassName: ProjectPersonDeviceDTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 11:43
 * @Copyright:
 */
@Data
public class ProjectPersonLiftDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    private String personType;
    /**
     * 人员id，根据人员类型取相应表id，注意访客应取当前的来访id
     */
    @ApiModelProperty(value = "人员id，根据人员类型取相应表id，注意访客应取当前的来访id")
    private String personId;
    /**
     * 通行方案id，为空则为自选权限
     */
    @ApiModelProperty(value = "通行方案id，为空则为自选权限")
    private String LiftPlanId;

    /**
     * 电梯集合
     */
    @ApiModelProperty(value = "电梯集合")
    private List<ProjectDeviceLiftVo> lifts;
    /**

    /**
     * 是否有效
     */
    @ApiModelProperty(value = "状态  是否启用 1 启用 0 禁用")
    private String isActive;
    /**
     * 状态 1 正常 2 失效
     */
    @ApiModelProperty(value = "状态 1 正常 2 失效")
    private String status;
    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effTime;
    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    private LocalDateTime expTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Integer operator;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    public LocalDateTime getEffTime() {
        if (this.effTime == null) {
            this.effTime = LocalDateTime.now();
        }
        return this.effTime;
    }
}
