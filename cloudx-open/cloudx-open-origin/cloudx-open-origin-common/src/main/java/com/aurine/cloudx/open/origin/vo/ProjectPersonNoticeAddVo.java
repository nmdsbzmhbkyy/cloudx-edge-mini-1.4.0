package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectNotice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * (ProjectPersonNoticeAddVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/10/23 15:53
 */
@Data
@ApiModel("业主信息新增Vo")
public class ProjectPersonNoticeAddVo extends ProjectNotice {
    /**
     * 用户列表
     */
    @ApiModelProperty("用户id列表")
    List<String> userIds;
}
