package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectInspectPointConf;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备巡检点配置Vo对象
 *
 * @author 王良俊
 * @date 2020-07-23 16:26:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备巡检点vo对象")
public class ProjectInspectPointConfVo extends ProjectInspectPointConf {


    /**
     * 创建人姓名
     */
    @ApiModelProperty(value = "创建人姓名")
    private String operatorName;

    /**
     * 全部区域地址
     */
    @ApiModelProperty(value = "全部区域地址")
    private String address;

    /**
     * 检查项数量
     */
    @ApiModelProperty(value = "检查项数量")
    private Integer checkItemNum;

    /**
     * 关联设备数量
     */
    @ApiModelProperty(value = "关联设备数量")
    private Integer associatedDeviceNum;

    /**
     * 关联设备Id数组
     */
    @ApiModelProperty(value = "关联设备Id数组")
    private String[] deviceIdArr;

    /**
     * 制定人员
     */
    @ApiModelProperty(value = "制定人员")
    private String operateName;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private String sort;

}
