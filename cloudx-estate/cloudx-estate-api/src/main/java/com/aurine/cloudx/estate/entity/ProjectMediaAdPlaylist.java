

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
 * 项目媒体广告播放列表配置
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:36:13
 */
@Data
@TableName("project_media_ad_playlist")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目媒体广告播放列表配置")
public class ProjectMediaAdPlaylist extends Model<ProjectMediaAdPlaylist> {
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
     * 资源id，关联project_media_repo.repoId
     */
    @ApiModelProperty(value = "资源id，关联project_media_repo.repoId")
    private String repoId;
    /**
     * 播放时长（秒）
     */
    @ApiModelProperty(value = "播放时长（秒）")
    private Integer duration;
    /**
     * 间隔时长（秒）
     */
    @ApiModelProperty(value = "间隔时长（秒）")
    private Integer gapTime;
    /**
     * 数据来源，第三方应用id
     */
    @ApiModelProperty(value = "数据来源，第三方应用id")
    private String appId;
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
