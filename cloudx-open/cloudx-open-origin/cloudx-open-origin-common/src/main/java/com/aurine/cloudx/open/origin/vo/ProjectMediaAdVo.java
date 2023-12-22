package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectMediaAd;
import com.aurine.cloudx.open.origin.entity.ProjectMediaAdPlaylist;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Title: ProjectMediaAdVo
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/6/4 16:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("媒体广告表Vo")
public class ProjectMediaAdVo extends ProjectMediaAd {
    /**
     * 下发失败数
     */
    @ApiModelProperty("下发失败数")
    private Integer countFailNum;
    /**
     * 播放频率解析
     */
    @ApiModelProperty("播放频率解析")
    private String frequencyString;

    @ApiModelProperty("媒体对象列表")
    private List<ProjectMediaAdPlaylist> projectMediaAdPlaylistList;


}
