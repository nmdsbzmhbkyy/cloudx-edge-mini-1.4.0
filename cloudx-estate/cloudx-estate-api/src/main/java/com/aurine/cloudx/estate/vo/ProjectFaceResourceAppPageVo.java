package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectFaceResourceAppPageVo)小程序人脸授权物业端分页查询结果集
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/11/12 10:59
 */
@Data
@ApiModel("小程序人脸授权物业端分页查询结果集")
public class ProjectFaceResourceAppPageVo {
    @ApiModelProperty("房屋名称")
    private String houseName;
    @ApiModelProperty("房屋id")
    private String houseId;
    @ApiModelProperty("人员id")
    private String personId;
    @ApiModelProperty("人员名称")
    private String personName;
    @ApiModelProperty("人脸id")
    private String faceId;
    @ApiModelProperty("人脸图片地址")
    private String picUrl;
    @ApiModelProperty("授权状态 0全部下载失败 1：下载成功  2：下载中 3： 下载中的数据超过12小时 4： 部分下载失败")
    private String dlStatus;
    @ApiModelProperty("人屋关系id")
    private String relaId;
    @ApiModelProperty(value = "住户类型 1 业主（产权人） 2 家属 3 租客 见通用字典household_type")
    private String householdType;
}
