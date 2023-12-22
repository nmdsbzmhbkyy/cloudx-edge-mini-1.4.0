

package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备信息表
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:38:59
 */
@Data
@TableName("project_device_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备信息表")
public class ProjectDeviceInfoAbnormalVo extends ProjectDeviceInfo {

    /**
     * 异常记录ID
     */
    private Integer abnormalSeq;

    /**
     * 异常描述
     */
    private String abnormalDesc;

    /**
     * 设备添加时间
     */
    private LocalDateTime devAddTime;

    /**
     * 设备在线离线状态
     */
    @ApiModelProperty(value = "1 在线 2 离线 3 故障 4 未激活 9 未知")
    private String dStatus;
}
