package com.aurine.cloudx.open.origin.dto;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>入云申请类</p>
 * @author : 王良俊
 * @date : 2021-12-10 10:04:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntoCloudRequestInfoDTO {


    /**
     * 入云码
     */
    private String connectCode;

    /**
     * 设备SN
     */
    private String deviceSn;

    /**
     * 申请入云项目Code
     */
    @ApiModelProperty("申请入云项目Code")
    private String projectCode;


    /**
     * 项目名
     */
    @ApiModelProperty("项目名")
    private String projectName;


    /**
     * 边缘侧联系人姓名
     */
    @ApiModelProperty("联系人姓名")
    private String contactPersonName;

    /**
     * 边缘侧联系人手机号
     */
    @ApiModelProperty("联系人手机号")
    private String contactPhone;


    /**
     * 联系人性别 使用字典gender_type
     */
    @ApiModelProperty("联系人性别")
    private String contactGender;


    /**
     * 联系人照片文件Base64数据
     */
    @ApiModelProperty("联系人照片文件Base64数据")
    private String contactPicBase64;


    /**
     * 联系人身份证号
     */
    @ApiModelProperty("联系人身份证号")
    private String contactIdNumber;

    /**
     * 申请时间 时间戳 秒
     */
    private Long requestTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}