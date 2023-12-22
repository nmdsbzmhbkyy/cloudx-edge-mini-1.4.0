

package com.aurine.cloudx.estate.dto;

import com.aurine.cloudx.estate.entity.ProjectMediaAd;
import com.aurine.cloudx.estate.entity.ProjectMediaAdPlaylist;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 媒体广告传输对象,中台对接
 * @ClassName: ProjectMediaAdDTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/22 15:50
 * @Copyright:
 */
@Data
@ApiModel(value = "媒体广告传输对象")
public class ProjectMediaAdPlaylistDTO extends ProjectMediaAdPlaylist {

    /**
     * 资源URL
     */
    @ApiModelProperty(value = "资源URL")
    private String repoURL;

}
