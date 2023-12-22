package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.common.core.entity.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (ProjectDeviceCameraVo)
 * 监控设备
 *
 * @author xull
 * @since 2020/7/8 10:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectMonitorNodeVo extends TreeNode {
    public static final String TYPE_REGION = "1";
    public static final String TYPE_MONITOR = "2";
    
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;
    /**
     * 层级
     */
    @ApiModelProperty("层级")
    private int level;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private int sort;
    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String code;
    /**
     * 监控类型
     */
    @ApiModelProperty("监控类型")
    private String monitorType;
    /**
     * 类型，1，表示区域 2，表示监控设备
     */
    @ApiModelProperty("类型，1，表示区域 2，表示监控设备")
    private String type;
    /**
     * 空间ID
     */
    @ApiModelProperty("名称")
    private String corpId;
    /**
     * 国标ID
     */
    @ApiModelProperty("空间ID")
    private String gbId;
    /**
     * 服务区ID
     */
    @ApiModelProperty("服务区ID")
    private String regionId;
    /**
     * 上级服务区ID
     */
    @ApiModelProperty("上级服务区ID")
    private String parRegionId;
    /**
     * 设备数量
     */
    @ApiModelProperty("设备数量")
    private Integer num;
}
