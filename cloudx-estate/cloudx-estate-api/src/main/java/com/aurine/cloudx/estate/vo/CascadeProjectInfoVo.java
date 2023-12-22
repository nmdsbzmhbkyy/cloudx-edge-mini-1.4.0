package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>级联项目管理分页VO对象</p>
 * @author : 王良俊
 * @date : 2021-12-30 17:05:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CascadeProjectInfoVo {

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Integer projectId;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    /**
     * 项目负责人姓名（边缘网关负责人）
     */
    @ApiModelProperty(value = "项目负责人姓名（边缘网关负责人）")
    private String slaveContactPerson;

    /**
     * 项目负责人手机号（边缘网关负责人）
     */
    @ApiModelProperty(value = "项目负责人手机号（边缘网关负责人）")
    private String slaveContactPhone;

    /**
     * 边缘网关状态（设备状态）
     */
    @ApiModelProperty(value = "边缘网关状态（设备状态）")
    private String deviceStatus;

    /**
     * 原型删了
     * 是否允许入云 0不允许 1允许
     */
//    @ApiModelProperty(value = "是否允许入云 0不允许 1允许")
//    private char accessToCloud;

    /**
     * 云端项目名称
     */
    @ApiModelProperty(value = "云端项目名称")
    private String cloudProjectName;

    /**
     * 入云申请状态 0 未入云 1 待审核 2  已拒绝 3 已入云 4 解绑中
     */
    @ApiModelProperty("入云申请状态 0 未入云 1 待审核 2  已拒绝 3 已入云 4 解绑中")
    private char cloudStatus;

    /**
     * 入云申请申请ID
     */
    @ApiModelProperty("入云申请申请ID")
    private String cloudRequestId;

    /**
     * 主边缘网关项目ID
     */
    @ApiModelProperty("主边缘网关项目ID")
    private Integer masterProjectId;

    /**
     * 主边缘网关联网开关状态 1 开启 0 关闭
     */
    @ApiModelProperty("主边缘网关联网开关状态")
    private Character masterIsSyncCloud;

}
