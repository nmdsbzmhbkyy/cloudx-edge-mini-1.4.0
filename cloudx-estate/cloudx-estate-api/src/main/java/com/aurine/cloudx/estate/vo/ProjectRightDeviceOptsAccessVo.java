

package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备维护-门禁查看-列表
 *
 * @author 王良俊
 * @date 2020-05-21 09:52:28
 */
@Data
@ApiModel(value = "设备维护-门禁查看-用户列表")
public class ProjectRightDeviceOptsAccessVo {


    /**
     * 主键，自增
     */
    @ApiModelProperty(value="主键，自增")
    private Integer seq;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String deviceId;

    /**
     * 认证介质 1 指纹 2 人脸 3 卡
     */
    @ApiModelProperty(value = "通讯方式 1 指纹 2 人脸 3 卡")
    private String certMedia;

    /**
     * 介质对应的数据，如人脸是图片url 卡片是卡号
     */
    @ApiModelProperty(value = "介质对应的数据，如人脸是图片url 卡片是卡号")
    private String certData;

    /**
     * 认证介质id
     */
    @ApiModelProperty(value = "认证介质id")
    private String certMediaId;

    /**
     * 下载状态 0 未下载 1 已下载 2 下载失败
     */
    @ApiModelProperty(value = "下载状态 0 未下载 1 已下载 2 下载失败")
    private String dlStatus;

    /**
     * 人员ID
     */
    @ApiModelProperty(value = "人员ID")
    private String personId;

    /**
     * 人员姓名
     */
    @ApiModelProperty(value = "人员姓名")
    private String personName;

    /**
     * 人员类型
     */
    @ApiModelProperty(value = "人员类型")
    private String personType;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String telephone;

    /**
     * 下载（下发）状态
     */
    @ApiModelProperty(value = "设备名")
    private String deviceName;

}
