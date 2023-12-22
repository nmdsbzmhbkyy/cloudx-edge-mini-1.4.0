package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectInspectCheckinDetail;
import com.aurine.cloudx.open.origin.entity.ProjectInspectTaskDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * (ProjectInspectVo)巡检签到Vo
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/10/28 8:56
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("巡检签到Vo")
@Data
public class ProjectInspectVo extends ProjectInspectTaskDetail {
    @ApiModelProperty("巡检设备状态列表")
    List<ProjectInspectDetailDeviceFormVo> devices;
    @ApiModelProperty("签到方式")
    List<ProjectInspectCheckinDetail> checkinDetails;
}
