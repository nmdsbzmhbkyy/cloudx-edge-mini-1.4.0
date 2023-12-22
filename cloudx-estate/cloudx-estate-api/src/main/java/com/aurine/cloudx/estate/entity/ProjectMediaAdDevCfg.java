

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 项目媒体广告设备配置表
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:36:26
 */
@Data
@TableName("project_media_ad_dev_cfg")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目媒体广告设备配置表")
public class ProjectMediaAdDevCfg extends Model<ProjectMediaAdDevCfg> {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("物理主键")
    private Integer seq;

    /**
     * 广告id，关联project_media_ad.adId
     */
    @ApiModelProperty(value = "广告id，关联project_media_ad.adId")
    private String adId;
    /**
     * 设备id，关联project_device_info.deviceId
     */
    @ApiModelProperty(value = "设备id，关联project_device_info.deviceId")
    private String deviceId;
    /**
     * 下载状态 1 正在下载 2 已下载 3 空间满 99 其他
     */
    @ApiModelProperty(value = "下载状态 1 正在下载 2 已下载 3 空间满 99 其他")
    private String dlStatus;
    /**
     * 数据来源，第三方应用id
     */
    @ApiModelProperty(value = "数据来源，第三方应用id")
    private String appId;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    /**
     * 楼栋
     */
    @ApiModelProperty(value = "楼栋")
    private String buildingName;
    /**
     * 单元
     */
    @ApiModelProperty(value = "单元")
    private String unitName;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
