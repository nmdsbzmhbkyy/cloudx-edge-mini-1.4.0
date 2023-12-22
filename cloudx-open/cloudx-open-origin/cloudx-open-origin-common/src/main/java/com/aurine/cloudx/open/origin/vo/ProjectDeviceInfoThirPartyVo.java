

package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备信息表
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:38:59
 */
@Data
@TableName("project_device_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备信息表")
public class ProjectDeviceInfoThirPartyVo extends ProjectDeviceInfo {

    /**
     * 设备是否可以重启 0 不可以 ， 1 可以
     */
    @ApiModelProperty(value = "设备是否可以重启 0 不可以 , 1 可以")
    private Integer isResetDevice;

}
