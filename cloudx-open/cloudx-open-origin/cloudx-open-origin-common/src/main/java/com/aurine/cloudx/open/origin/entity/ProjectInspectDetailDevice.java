package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡检任务明细设备列表(ProjectInspectDetailDevice)表实体类
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:31
 */
@Data
@TableName("project_inspect_detail_device")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "巡检任务明细设备列表(ProjectInspectDetailDevice)")
public class ProjectInspectDetailDevice extends OpenBasePo<ProjectInspectDetailDevice> {

    private static final long serialVersionUID = 301440241353201214L;


    /**
     * 明细设备id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "明细设备id")
    private String deviceDetailId;

    /**
     * 明细id，uuid
     */
    @ApiModelProperty(value = "明细id，uuid")
    private String detailId;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String deviceId;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    /**
     * 设备类别
     */
    @ApiModelProperty(value = "设备类别")
    private String deviceTypeId;

    /**
     * 设备类别名称
     */
    @ApiModelProperty(value = "设备类别名称")
    private String deviceTypeName;

    /**
     * 所在位置
     */
    @ApiModelProperty(value = "所在位置")
    private String regionId;

    /**
     * 所在位置名称
     */
    @ApiModelProperty(value = "所在位置名称")
    private String regionName;

    /**
     * 巡检结果 0 未巡检 1 正常 2 异常
     */
    @ApiModelProperty(value = "巡检结果 0 未巡检 1 正常 2 异常")
    private String result;
    /**
     * 巡检结果说明
     */
    @ApiModelProperty(value = "巡检说明")
    private String resultDesc;
    /**
     * 图片1
     */
    @ApiModelProperty(value = "图片1")
    private String picUrl;

    /**
     * 图片2
     */
    @ApiModelProperty(value = "图片2")
    private String picUrl2;

    /**
     * 图片3
     */
    @ApiModelProperty(value = "图片3")
    private String picUrl3;

    /**
     * 图片4
     */
    @ApiModelProperty(value = "图片4")
    private String picUrl4;


}