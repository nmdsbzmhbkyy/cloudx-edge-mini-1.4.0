

package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>员工 权限VO</p>
 *
 * @ClassName: ProjectPersonDeviceVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 11:43
 * @Copyright:
 */
@Data
@ApiModel(value = "员工 权限VO")
public class ProjectStaffDeviceVo extends ProjectPersonDeviceVo {
    private static final long serialVersionUID = 1L;

    /**
     * 设备id 数组
     */
    @ApiModelProperty(value="设备id 数组")
    private String[] deviceIdArray;
}
