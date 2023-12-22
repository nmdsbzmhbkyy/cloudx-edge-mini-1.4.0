

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备关系表
 *
 * @author 黄健杰
 * @date 2022-01-27 09:13:10
 */
@Data
@NoArgsConstructor
@TableName("project_device_rel")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备关系表")
public class ProjectDeviceRel extends Model<ProjectDeviceRel> {
    private static final long serialVersionUID = 1L;

    public ProjectDeviceRel(String deviceId, String parDeviceId) {
        this.deviceId = deviceId;
        this.parDeviceId = parDeviceId;
    }

    /**
     * 设备id, 32位uuid
     */
    @ApiModelProperty(value = "关系id, 32位uuid")
    @TableId(type = IdType.ASSIGN_UUID)
    private String relId;

    /**
     * 设备id, 32位uuid
     */
    @ApiModelProperty(value = "设备id, 32位uuid")
    private String deviceId;

    /**
     * 设备id, 32位uuid
     */
    @ApiModelProperty(value = "父设备id, 32位uuid")
    private String parDeviceId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
