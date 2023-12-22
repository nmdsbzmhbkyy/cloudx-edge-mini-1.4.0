

package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceAttr;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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


    /**
     * 设备额外拓展属性
     */
    @ApiModelProperty(value="设备额外拓展属性")
    private List<ProjectDeviceAttrVo> deviceAttrList;

}
