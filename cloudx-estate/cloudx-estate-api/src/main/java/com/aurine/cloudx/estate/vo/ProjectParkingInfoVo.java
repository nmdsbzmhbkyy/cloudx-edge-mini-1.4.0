
package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 停车区域
 *
 * @author 王伟
 * @date 2020-05-07 09:13:25
 */
@Data
public class ProjectParkingInfoVo extends ProjectParkingInfo {


    /**
     * 停车场ID
     */
    @ApiModelProperty(value = "停车场ID")
    private String parkId;
    /**
     * 停车场名称
     */
    @ApiModelProperty(value = "停车场名称、车位区域")
    private String parkName;

    /**
     * 停车场编码，可用于第三方编码
     */
    @ApiModelProperty(value = "停车场编码，可用于第三方编码")
    private String parkCode;
    /**
     * 停车位总数
     */
    @ApiModelProperty(value = "停车位总数")
    private Integer parkNum;
    /**
     * 车辆数
     */
    @ApiModelProperty(value = "车辆数")
    private Integer carNum;

    /**
    * 已经被使用的车位数
    */
    @ApiModelProperty(value = "已使用车位数")
    private Integer usedPark;
    /**
     * 对接状态 1 已连接 0 未连接
     */
    @ApiModelProperty(value = "对接状态 1 已连接 0 未连接")
    private char status;

    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层")
    private String floor;

    /**
     * 临时车收费规则id，关联project_park_billing_rule
     */
    @ApiModelProperty(value = "临时车收费规则id，关联project_park_billing_rule")
    private String ruleId;

    /**
     * 对接第三方厂商
     */
    @ApiModelProperty(value = "对接第三方厂商")
    private String company;
    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    @TableField(value = "tenant_id")
    private Integer tenantId;
    /**
     * 对接厂商名字
     */
    @ApiModelProperty(value = "对接第三方厂商")
    private String manufacturer;
}
