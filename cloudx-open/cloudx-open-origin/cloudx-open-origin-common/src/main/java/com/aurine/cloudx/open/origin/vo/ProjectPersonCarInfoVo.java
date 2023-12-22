package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectParCarRegister;
import com.aurine.cloudx.open.origin.entity.ProjectParkingPlace;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("人员车辆信息表")
public class ProjectPersonCarInfoVo {
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
