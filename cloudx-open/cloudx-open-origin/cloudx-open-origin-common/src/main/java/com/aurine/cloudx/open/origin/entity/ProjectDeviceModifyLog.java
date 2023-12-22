package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备修改记录表
 *
 * @author 邹宇
 * @date 2021-9-26 15:34:03
 */
@Data
@TableName("project_device_modify_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备修改记录表")
public class ProjectDeviceModifyLog extends OpenBasePo<ProjectDeviceModifyLog> {
    private static final long serialVersionUID = 1L;

    /**
     * 序列，自增长
     */
    @ApiModelProperty(value = "序列，自增长")
    @TableId
    private Integer seq;

    /**
     * uuid，主键
     */
    @ApiModelProperty(value = "uuid，主键")
    private String id;

    /**
     * 设备Id
     */
    @ApiModelProperty(value = "设备Id")
    private String deviceId;
    /**
     * 修改后的值
     */
    @ApiModelProperty(value = "修改后的值")
    private String valueAfter;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime modifyTime;

    /**
     * 修改人，有关联的需求才使用该字段
     */
    @ApiModelProperty(value = "修改人，有关联的需求才使用该字段")
    @TableField(fill = FieldFill.INSERT)
    private Integer operatorUserId;


    /**
     * 修改人名称，如需求为流水记录取此字段即可
     */
    @ApiModelProperty(value = "修改人名称，如需求为流水记录取此字段即可")
    private String operatorName;
}
