

package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.entity.ProjectMediaAd;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 媒体广告传输对象，中台对接
 * @ClassName: ProjectMediaAdDTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/22 15:50
 * @Copyright:
 */
@Data
@ApiModel(value = "媒体广告传输对象")
public class ProjectMediaAdDTO extends ProjectMediaAd {

    @ApiModelProperty(value = "媒体对象列表")
    private List<ProjectMediaAdPlaylistDTO> projectMediaAdPlaylistDTOList;

}
