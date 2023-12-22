

package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>住户 权限VO</p>
 *
 * @ClassName: ProjectPersonDeviceVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 11:43
 * @Copyright:
 */
@Data
@ApiModel(value = "住户 权限VO")
public class ProjectProprietorDeviceVo extends ProjectPersonDeviceVo {
    private static final long serialVersionUID = 1L;

    /**
     * 电梯通行方案id
     */
    //@NotNull(message = "电梯方案不能为空")
    //@NotEmpty(message = "请选择电梯通行方案")
    @ApiModelProperty(value = "电梯通行方案id")
    private String liftPlanId;

    /**
     * 电梯集合
     */
    @ApiModelProperty(value = "电梯集合")
    private List<ProjectDeviceLiftVo> lifts;
    /**
     * 设备id 数组
     */
    @ApiModelProperty(value="设备id 数组")
    private String[] deviceIdArray;
}
