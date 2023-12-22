package com.aurine.cloudx.estate.entity;

import com.aurine.cloudx.common.data.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@TableName("project_qr_passcode_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "通行二维码注册记录表",description = "")
public class ProjectQrPasscodeRecord extends BaseEntity {
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
    @NotBlank(message = "二维码校验字符串不可为空")
    private String uniqueCode ;
}
