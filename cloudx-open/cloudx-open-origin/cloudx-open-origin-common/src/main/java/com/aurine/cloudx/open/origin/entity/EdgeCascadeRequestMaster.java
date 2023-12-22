package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>边缘网关级联申请管理（主）</p>
 * @author : 王良俊
 * @date : 2021-12-17 09:25:12
 */
@Data
@TableName("edge_cascade_request_master")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "边缘网关入云级联配置对象")
public class EdgeCascadeRequestMaster extends OpenBasePo<EdgeCascadeRequestMaster> {

    /**
     * 序列
     */
    @ApiModelProperty(value = "序列")
    private Integer seq;
    /**
     * 申请ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "申请ID")
    private String requestId;
    /**
     * 从项目名称
     */
    @ApiModelProperty(value = "从项目名称")
    private String slaveProjectName;
    /**
     * 从联系人姓名
     */
    @ApiModelProperty(value = "从联系人姓名")
    private String slaveContactPerson;
    /**
     * 从联系人手机号
     */
    @ApiModelProperty(value = "从联系人手机号")
    private String slaveContactPhone;
    /**
     * 从联系人性别
     */
    @ApiModelProperty(value = "从联系人性别")
    private String slaveGender;
    /**
     * 从边缘网关设备编号
     */
    @ApiModelProperty(value = "从边缘网关设备编号")
    private String slaveEdgeDeviceId;
    /**
     * 级联状态
     */
    @ApiModelProperty(value = "级联状态")
    private Character cascadeStatus;
    /**
     * 设备状态
     */
    @ApiModelProperty(value = "设备状态")
    private Character deviceStatus;
    /**
     * 第三方项目ID
     */
    @ApiModelProperty(value = "第三方项目ID")
    private String projectCode;
    /**
     * 配置json
     */
    @ApiModelProperty(value = "配置json")
    private String configJson;
    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    private LocalDateTime requestTime;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Integer projectId;


}
