package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备关系表
 *
 * @author 黄健杰
 * @date 2022-01-27 09:13:10
 */
@Data
@TableName("project_device_rel")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备关系表")
public class ProjectDeviceRel extends OpenBasePo<ProjectDeviceRel> {
    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;

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
}
