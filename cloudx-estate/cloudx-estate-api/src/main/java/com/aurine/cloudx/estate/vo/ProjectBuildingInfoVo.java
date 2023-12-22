

package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectBuildingInfo;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 楼栋
 *
 * @author 王伟
 * @date 2020-05-07 16:52:22
 */
@Data
@ApiModel(value = "楼栋")
public class ProjectBuildingInfoVo extends ProjectBuildingInfo {
    private static final long serialVersionUID = 1L;


    /**
     * 父类所有的框架
     */
    private List<ProjectFrameInfo> parentFrameList;

    /**
     * 上级框架UID
     */
    private String puid;

    /**
     * 图片列表
     * */
    List<ProjectUnitFileVo> fileList;
    /**
     * 序列
     */
    @ApiModelProperty(value = "序列")
    private Integer seq;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Integer projectId;

    /**
     * 租户号
     */
    private Integer tenantId;

    /**
     * 楼层数
     */
    private Integer floorTotal;
}
