

package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>通行设备VO,存储通信设备被勾选、被禁用状态</p>
 * 用于配置通行权相，与前端的表单交互用
 *
 * @ClassName: ProjectPassPlan
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/20 15:17
 * @Copyright:
 */
@Data
@ApiModel(value = "可通行设备")
public class ProjectPassDeviceVo {
    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID")
    private String deviceId;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    /**
     * 设备别名
     */
    @ApiModelProperty(value = "设备别名")
    private String deviceAlias;
    /**
     * 设备描述
     */
    @ApiModelProperty(value = "设备描述")
    private String deviceDesc;
    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private String deviceType;
    /**
     * 设备状态
     */
    @ApiModelProperty(value = "设备状态 1 在线 2 离线 4 未激活")
    private String status;

    /**
     * 是否可取消选择
     */
    @ApiModelProperty(value = "是否可取消选择")
    private boolean disabled;

    public boolean getDisable(){
        return this.disabled;
    }


    /****************逻辑策略*******************/

    /**
     * 楼栋id
     */
    @ApiModelProperty(value = "楼栋id")
    private String buildingId;
    /**
     * 单元id
     */
    @ApiModelProperty(value = "单元id")
    private String unitId;

}
