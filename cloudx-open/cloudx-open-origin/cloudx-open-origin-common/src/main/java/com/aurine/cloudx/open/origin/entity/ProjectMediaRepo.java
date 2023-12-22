

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.open.origin.constant.MediaResourceTypeConstant;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class ProjectMediaRepo extends OpenBasePo<ProjectMediaRepo> {
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
     * 资源名称
     */
    @ApiModelProperty(value = "资源名称")
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
