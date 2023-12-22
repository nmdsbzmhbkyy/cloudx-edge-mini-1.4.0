package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectNotice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * (ProjectPersonNoticeVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/10/23 15:51
 */
@Data
@ApiModel("人员信息Vo")
public class ProjectPersonNoticeVo extends ProjectNotice {

    @ApiModelProperty("消息的状态（0未读 1已读）")
    private String status;
    private String userId;
}
