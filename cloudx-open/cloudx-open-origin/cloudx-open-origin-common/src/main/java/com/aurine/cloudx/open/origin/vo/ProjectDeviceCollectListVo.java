package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Title: ProjectDeviceCollectListVo
 * Description: 设备采集参数key-value视图
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/6/12 14:23
 */
@Data
@ApiModel("设备采集参数key-value视图")
public class ProjectDeviceCollectListVo {

    /**
     * 参数id
     */
    @ApiModelProperty(value = "参数id")
    private String attrId;
    /**
     * 设备类型 1 人脸采集设备 2 身份证采集设备
     */
    @ApiModelProperty(value = "设备类型 1 人脸采集设备 2 身份证采集设备")
    private String deviceType;
    /**
     * 参数编码,页面配置填写，可填写拼音串
     */
    @ApiModelProperty(value = "参数编码,页面配置填写，可填写拼音串")
    private String attrCode;
    /**
     * 参数名称
     */
    @ApiModelProperty(value = "参数名称")
    private String attrName;
    /**
     * 值描述
     */
    @ApiModelProperty(value = "值描述")
    private String remark;
    /**
     * 参数值
     */
    @ApiModelProperty(value = "参数值")
    private String attrValue;

}
