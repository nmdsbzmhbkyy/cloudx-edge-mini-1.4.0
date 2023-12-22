

package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 住户 权限  查询结果 VO
 * </p>
 *
 * @author: 王良俊
 * @date: 2020/5/22 15:22
 * @Copyright:
 */
@Data
@ApiModel(value = "住户 权限VO")
public class ProjectProprietorDeviceRecordVo {
    private static final long serialVersionUID = 1L;

    /**
     * 住户姓名
     */
    @ApiModelProperty(value="住户姓名")
    private String personName;
    /**
     * 住户姓名
     */
    @ApiModelProperty(value="住户姓名")
    private String personId;

    /**
     * 住户地址（非数组使用","分隔）
     */
    @ApiModelProperty(value="住户地址")
    private String groupAddress;

    /**
     * 住户地址（数组结构）
     * 可能要用如果不是前端进行处理的话
     */
    @ApiModelProperty(value="住户地址(数组)")
    private String groupAddressArray;

    /**
     * 住户性别
     */
    @ApiModelProperty(value="住户性别")
    private String gender;

    /**
     * 手机号
     */
    @ApiModelProperty(value="手机号")
    private String telephone;

    /**
     * 通行状态
     */
    @ApiModelProperty(value="通行状态")
    private String rightStatus;
    /**
     * 启用状态
     */
    @ApiModelProperty(value="启用状态")
    private String isActive;

}
