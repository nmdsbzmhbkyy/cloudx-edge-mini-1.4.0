
package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目采集设备参数配置
 *
 * @author xull@aurine.cn
 * @date 2020-06-12 11:43:55
 */
@Data
@TableName("project_device_collect_cfg")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目采集设备参数配置")
public class ProjectDeviceCollectCfg extends OpenBasePo<ProjectDeviceCollectCfg> {
    private static final long serialVersionUID = 1L;


    /**
     * 参数id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "参数id")
    private String attrId;
    /**
     * 设备类型  1 人脸采集设备 2 身份证采集设备 3 车场 4 视频 5公安 6 WR20 7中台
     */
    @ApiModelProperty(value = "设备类型 1 人脸采集设备 2 身份证采集设备 3 车场 4 视频 5公安 6 WR20 7中台")
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
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer projectId;
}
