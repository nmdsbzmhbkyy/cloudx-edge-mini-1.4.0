package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * (ProjectHouseParkPlaceInfo)业主车位和车辆信息表
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/11/11 9:29
 */
@Data
@ApiModel("业主车位和车辆信息表")
public class ProjectHouseParkPlaceInfoVo   {
    /**
     * 人员ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "人员ID")
    private String personId;


    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String personName;

    /**
     * 性别 1 男 2 女
     */
    @ApiModelProperty(value = "性别 1 男 2 女")
    private String gender;
    /**
     * 人员类型 1 业主 2 访客 9 其他
     */
    @ApiModelProperty(value = "人员类型 1 业主 2 访客 9 其他")
    private String peopleTypeCode;

    /**
     * 住户类型 1 业主（产权人） 2 家属 3 租客
     */
    @ApiModelProperty(value = "住户类型 1 业主（产权人） 2 家属 3 租客 见通用字典household_type")
    private String householdType;
    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码")
    private String telephone;


    /**
     * 车位信息列表
     */
    @ApiModelProperty(value = "车位信息列表")
    private List<ProjectParkingPlace> parkingPlaces;


    /**
     * 车辆信息列表
     */
    @ApiModelProperty(value = "车辆信息列表")
    private List<ProjectParCarRegister> parCarRegisters;
}
