package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectNoticeDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Title: ProjectNoticeDeviceVo
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/26 9:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("设备消息Vo")
public class ProjectNoticeDeviceVo extends ProjectNoticeDevice {
    @ApiModelProperty("楼栋Id")
    private String buildingId;
    @ApiModelProperty("单元id")
    private String unitId;

}
