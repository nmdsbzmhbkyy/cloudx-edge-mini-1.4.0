

package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.entity.ProjectMediaAdPlaylist;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
