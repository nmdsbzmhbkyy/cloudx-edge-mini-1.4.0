package com.aurine.cloudx.estate.entity;

import com.aurine.cloudx.common.data.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@TableName("project_qr_passcode_use_flow")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "通行二维码使用记录",description = "")
public class ProjectQrPasscodeUseFlow extends BaseEntity {
    /** 通行人员名称 */
    @ApiModelProperty(name = "通行人员名称",notes = "")
    private String passenger ;
    /** 通行人员电话 */
    @ApiModelProperty(name = "通行人员电话",notes = "")
    private String phone ;
    /** 通行人员电话号码 */
    @ApiModelProperty(name = "通行人员电话号码",notes = "")
    private String credentialNo ;
    /** 放行开始时间 */
    @ApiModelProperty(name = "放行开始时间",notes = "")
    private Long startTime ;
    /** 放行结束时间 */
    @ApiModelProperty(name = "放行结束时间",notes = "")
    private Long endTime ;
    /** 可通行次数 */
    @ApiModelProperty(name = "可通行次数",notes = "")
    private Integer times ;
    /** 二维码校验字符串 */
    @ApiModelProperty(name = "二维码校验字符串",notes = "")
    private String uniqueCode ;


    /** 二维码记录ID */
    @ApiModelProperty(name = "二维码记录ID",notes = "")
    private Long recordId ;
    /** 通行设备编号 */
    @ApiModelProperty(name = "通行设备编号",notes = "")
    private String deviceNo ;
    /** 通行设备ID */
    @ApiModelProperty(name = "通行设备ID",notes = "")
    private String deviceId ;
    /** 通行时间 */
    @ApiModelProperty(name = "通行时间",notes = "")
    private Long passTime ;
    /** 识别结果;0 识别成功 1 无效二维码 2 过期二维码 */
    @ApiModelProperty(name = "识别结果",notes = "1 识别成功 2 无效二维码 3 过期二维码")
    /**
     * @see RemoteOpenDoorResultModel.ResultType
     */
    private Integer result ;
}
