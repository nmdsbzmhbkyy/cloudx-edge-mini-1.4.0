

package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备信息表代理对象，额外多出Project,tenantId
 * @ClassName: ProjectDeviceInfoProxy
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-11 14:21
 * @Copyright:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备信息表")
public class ProjectDeviceInfoProxyVo extends ProjectDeviceInfo {

    /**
     * 项目id
     */
    @ApiModelProperty(value="项目id")
    private Integer projectId;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    @TableField(value = "tenant_id")
    private Integer tenantId;
}
