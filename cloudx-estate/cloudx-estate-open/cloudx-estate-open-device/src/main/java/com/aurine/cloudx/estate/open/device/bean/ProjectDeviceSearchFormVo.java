package com.aurine.cloudx.estate.open.device.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: ProjectDeviceInfoPageFormVo
 * Description: 设备管理分页查询表单
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/20 15:13
 */
@Data
@ApiModel("区口设备管理分页查询表单")
public class ProjectDeviceSearchFormVo extends ProjectDeviceInfoPageFormVoPage {
//    /**
//     * 设备类型id
//     */
//    @ApiModelProperty("设备类型id")
//    private String deviceTypeId = "3";
    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称、描述")
    private String deviceName;
    /**
     * 设备sn
     */
    @ApiModelProperty(value = "设备sn")
    private String sn;
    /**
     * 设备描述
     */
    @ApiModelProperty(value = "设备描述")
    private String deviceDesc;

    /**
     * 1 在线 2 离线 3 故障 4 未激活 9 未知
     */
    @ApiModelProperty(value = "1 在线 2 离线 3 故障 4 未激活 9 未知")
    private String status;


    /**
     * 没有异常 1 ，有异常 0
     */
    @ApiModelProperty(value = "配置状态 没有异常 1 ，有异常 0")
    private String configurationStatus;

}
