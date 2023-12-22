

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 权限设备关系表，记录权限（认证介质）的下发状态
 *
 * @author 王良俊
 * @date 2020-05-21 09:52:28
 */
@Data
@TableName("project_right_device")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "权限设备关系表，记录权限（认证介质）的下发状态")
public class ProjectRightDevice extends Model<ProjectRightDevice> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增
     */
    @ApiModelProperty(value = "主键，自增")
    private Integer seq;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String deviceId;
    /**
     * uid,主要用于回调识别
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "uid")
    private String uid;
    /**
     * 认证介质 1 指纹 2 人脸 3 卡  4人脸黑名单 7云电话
     */
    @ApiModelProperty(value = "认证介质 1 指纹 2 人脸 3 卡 4人脸黑名单 7云电话")
    private String certMedia;
    /**
     * 认证介质id
     */
    @ApiModelProperty(value = "认证介质id")
    private String certMediaId;
    /**
     * personId
     */

//    @ApiModelProperty(value = "personId")
////    @TableField(exist = false)
//    private String personId;
    /**
     * 下载状态 0 未下载 1 已下载 2 下载失败
     */
    @ApiModelProperty(value = "下载状态 0 未下载 1 已下载 2 下载失败")
    private String dlStatus;
    /**
     * 接口请求状态（适用于异步接口） 1 请求中 2 请求失败 3 请求成功
     */
    @ApiModelProperty(value = "接口请求状态（适用于异步接口） 1 请求中 2 请求失败 3 请求成功")
    private String reqStatus;
    /**
     * 接口返回信息
     */
    @ApiModelProperty(value = "接口返回信息")
    private String errMsg;

    /**
     * 凭证具体信息，如人脸地址url、卡号等
     */
    @ApiModelProperty(value = "凭证具体信息，如人脸地址url、卡号等")
    private String certMediaInfo;

    /**
     * 凭证编码，第三方编码
     */
    @ApiModelProperty(value = "凭证编码，第三方编码")
    private String certMediaCode;

    /**
     * 人员类型  1 住户 2 员工 3 访客  10 黑名单
     */
    @ApiModelProperty(value = "人员类型  1 住户 2 员工 3 访客 10 黑名单")
    private String personType;

    /**
     * 人员id
     */
    @ApiModelProperty(value = "人员id")
    private String personId;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String personName;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String mobileNo;


    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
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
}