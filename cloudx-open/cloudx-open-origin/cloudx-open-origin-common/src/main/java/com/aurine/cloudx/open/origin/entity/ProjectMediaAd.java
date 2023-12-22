

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
 * 媒体广告表
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:37:46
 */
@Data
@TableName("project_media_ad")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "媒体广告表")
public class ProjectMediaAd extends OpenBasePo<ProjectMediaAd> {
    private static final long serialVersionUID = 1L;

    /**
     * 广告自增序列
     */
    @ApiModelProperty(value = "广告自增序列")
    private Long seq;
    /**
     * 广告id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "广告id")
    private String adId;
    /**
     * 广告名称
     */
    @ApiModelProperty(value = "广告名称")
    private String adName;
    /**
     * 播放频率
     */
    @ApiModelProperty(value = "播放频率")
    private String frequency;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;
    /**
     * 播放次数
     */
    @ApiModelProperty("播放次数")
    private Integer times;
    /**
     * 媒体类型
     */
    @ApiModelProperty("媒体类型")
    private String type;
    /**
     * 数据来源，第三方应用id
     */
    @ApiModelProperty(value = "数据来源，第三方应用id")
    private String appId;
}
