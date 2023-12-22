

package com.aurine.cloudx.estate.entity;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.MediaResourceTypeConstant;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;

/**
 * 项目媒体资源库
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:35:57
 */
@Data
@TableName("project_media_repo")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目媒体资源库")
public class ProjectMediaRepo extends Model<ProjectMediaRepo> {
    private static final long serialVersionUID = 1L;

    /**
     * 资源自增id
     */
    @ApiModelProperty(value = "资源id")
    private Long seq;

    /**
     * 资源id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "资源id")
    private String repoId;
    /**
     * 第三方资源库的id
     */
    private String repoCode;
    /**
     * 资源类型resourceType
     */
    @ApiModelProperty(value = "资源类型resourceType:01出入口资源，02广播资源 03 数字标牌资源")
    private String resourceType;
    /**
     * 资源名称
     */
    @ApiModelProperty(value = "资源名称")
    @Length(max = 64,message = "输入的资源名称过长")
    private String repoName;
    /**
     * 资源类型 1 图片 2 视频
     */
    @ApiModelProperty(value = "资源类型 1 图片 2 视频")
    private String repoType;
    /**
     * 格式，1 JPG 2 FLV 3 MP4
     */
    @ApiModelProperty(value = "格式，1 JPG 2 FLV 3 MP4")
    private String repoFormat;
    /**
     * 时长（秒），仅针对视频类
     */
    @ApiModelProperty(value = "时长（秒），仅针对视频类")
    private Integer repoDuration;
    /**
     * 资源url地址
     */
    @ApiModelProperty(value = "资源url地址")
    private String repoUrl;
    /**
     * 数据来源
     */
    @ApiModelProperty(value = "数据来源")
    private String appId;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
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
    /**
     * 是否启用 1 启用 0 禁用
     */
    @ApiModelProperty(value = "是否启用 1 启用 0 禁用", required = true)
    @TableField("isVisible")
    private String visible;

    public String getRepoTypeByFormat() {
        if (StrUtil.isEmpty(repoType) && StrUtil.isNotEmpty(repoFormat)) {
            String format = this.repoFormat.toLowerCase().trim();
            switch (format) {
                case "mp3":
                    repoType = MediaResourceTypeConstant.SOUND;
                    break;
                case "mp4":
                    repoType = MediaResourceTypeConstant.VIDEO;
                    break;
                case "jpg":
                case "jpeg":
                case "png":
                    repoType = MediaResourceTypeConstant.IMAGE;
                    break;
                default:
                    repoType = "";
            }
        }
        return repoType;
    }
}
