package com.aurine.cloudx.open.origin.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @program: cloudx
 * @description: 设备下的用户和房屋传输对象
 * @author: 谢泽毅
 * @create: 2021-08-13 15:10
 **/
@Data
@ApiModel(value = "设备下用户和房屋信息DTO")
public class ProjectUserHouseDTO {

    /**
     * 房屋id
     */
    private String houseId;

    /**
     * 房屋名称
     */
    private String houseName;

    /**
     * 房屋编码
     */
    private String houseCode;

    /**
     * 用户名称
     */
    private String personName;

    /**
     * 用户id
     */
    private String personId;

    /**
     * 用户状态
     */
    private String pStatus;

    /**
     * 用户类型
     */
    private Integer peopleTypeCode;

    /**
     * 用户手机号
     */
    private String telephone;

    /**
     * 项目id
     */
    private String projectId;

    /**
     *
     */
    private Integer tenant_Id;

    /**
     * 用户头像
     */
    private String picUrl;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备sn
     */
    private String sn;
}
