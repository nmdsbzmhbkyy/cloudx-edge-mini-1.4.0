

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备区域表
 *
 * @author xull@aurine.cn
 * @date 2020-05-25 08:50:24
 */
@Data
@TableName("project_device_region")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备区域表")
public class ProjectDeviceRegion extends OpenBasePo<ProjectDeviceRegion> {
    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    /**
     * 区域id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "区域id")
    private String regionId;
    /**
     * 区域编码, 第三方
     */
    @ApiModelProperty(value = "区域编码, 第三方")
    private String regionCode;
    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String regionName;
    /**
     * 上级区域
     */
    @ApiModelProperty(value = "上级区域")
    private String parRegionId;
    /**
     * 平面图名称
     */
    @ApiModelProperty(value = "平面图名称")
    private String picName;
    /**
     * 平面图url
     */
    @ApiModelProperty(value = "平面图url")
    private String picUrl;
    /**
     * 平面图url
     */
    @ApiModelProperty(value = "是否默认， 1是，0否")
    private String isDefault;
    /**
     * 上传时间
     */
    @ApiModelProperty(value = "上传时间")
    private LocalDateTime uploadTime;
    /**
     * 上传人
     */
    @ApiModelProperty(value = "上传人")
    private Integer uploadBy;
    /**
     * 区域层级
     */
    @ApiModelProperty(value = "区域层级")
    private Integer regionLevel;
}
