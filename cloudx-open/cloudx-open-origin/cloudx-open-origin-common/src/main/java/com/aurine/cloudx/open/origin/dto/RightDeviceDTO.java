package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.entity.ProjectRightDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 设备凭证数据传输对象
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-09-24
 * @Copyright:
 */
@Data
@ApiModel(value = "设备凭证数据传输对象")
public class RightDeviceDTO {

    @ApiModelProperty(value = "操作结果")
    private boolean result;

    @ApiModelProperty(value = "需要添加的数据列表")
    private List<ProjectRightDevice> addList;

    @ApiModelProperty(value = "需要删除的数据列表")
    private List<ProjectRightDevice> delList;
}
