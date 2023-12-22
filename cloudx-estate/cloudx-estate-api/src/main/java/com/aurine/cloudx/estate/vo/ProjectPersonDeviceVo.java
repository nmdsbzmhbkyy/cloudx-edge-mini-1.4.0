

package com.aurine.cloudx.estate.vo;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.time.DateUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>住户/员工 权限基础VO</p>
 *
 * @ClassName: ProjectPersonDeviceVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 11:43
 * @Copyright:
 */
@Data
@ApiModel(value = "人员设备权限")
public class ProjectPersonDeviceVo {
    private static final long serialVersionUID = 1L;

    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    private String personType;
    /**
     * 人员id，根据人员类型取相应表id，注意访客应取当前的来访id
     */
    @NotNull(message = "人员ID不能为空")
    @ApiModelProperty(value = "人员id，根据人员类型取相应表id，注意访客应取当前的来访id")
    private String personId;
    /**
     * 通行方案id，为空则为自选权限
     */
    @NotNull(message = "方案不能为空")
    @NotEmpty(message = "请选择通行方案")
    @ApiModelProperty(value = "通行方案id")
    private String planId;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String deviceId;
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
     * 设备状态列表
     */
    @ApiModelProperty(value = "设备状态列表")
    private List<ProjectPassDeviceVo> deviceList;

    /**
     * 是否有效
     */
    @ApiModelProperty(value = "状态  是否启用 1 启用 0 禁用")
    private String isActive;

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

    public void setExpTime(LocalDateTime expTime){
        /**
         * 基于需求原型 1.6 2020-07-03
         * 用户不选择过期时间时，自动设置过期时间未2199-01-01
         * 在界面上不回显
         * @author: 王伟 2020-07-06
         */

        if(expTime != null && expTime.getYear() == 2199 && expTime.getMonthValue() == 1 && expTime.getDayOfMonth() == 1){
            expTime = null;
        }

        this.expTime = expTime;
    }

    public LocalDateTime getExpTime(){
        return this.expTime;
    }
}
