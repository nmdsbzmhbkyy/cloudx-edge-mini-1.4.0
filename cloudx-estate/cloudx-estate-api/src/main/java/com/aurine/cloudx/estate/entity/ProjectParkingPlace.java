

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 车位
 *
 * @author 王伟
 * @date 2020-05-08 10:24:42
 */
@Data
@TableName("project_parking_place")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车位")
public class ProjectParkingPlace extends Model<ProjectParkingPlace> {
    private static final long serialVersionUID = 1L;


    /**
     * 自增序列
     */
    @ApiModelProperty(value = "自增序列")
    private Integer seq;

    /**
     * 车位ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "车位ID")
    private String placeId;
    /**
     * 车位编号，如A-2201。可用于第三方对接
     */
    @ApiModelProperty(value = "车位编号，如A-2201。可用于第三方对接")
    private String placeCode;

    /**
     * 第三方人员编码
     */
    @ApiModelProperty(value = "第三方人员编码")
    private String personCode;
    /**
     * 车位号
     */
    @ApiModelProperty(value = "车位号")
    private String placeName;
    /**
     * 月租车收费规则id，关联project_park_billing_rule
     */
    @ApiModelProperty(value = "月租车收费规则id，关联project_park_billing_rule")
    private String ruleId;
    /**
     * 归属停车场ID
     */
    @ApiModelProperty(value = "归属停车场ID")
    private String parkId;
    /**
     * 归属停车场ID
     */
    @ApiModelProperty(value = "车位区域id")
    private String parkRegionId;
    /**
     * 归属房屋ID
     */
    @ApiModelProperty(value = "归属房屋")
    private String houseId;

    /**
     * 归属人
     */
    @ApiModelProperty(value = "归属人")
    private String personId;
    /**
     * 归属人姓名
     */
    @ApiModelProperty(value = "归属人姓名")
    private String personName;
    /**
     * 车位类型
     */
    @ApiModelProperty(value = "车位类型")
    private String placeType;
    /**
     * 关系类型 0 闲置 1 产权 2 租赁
     */
    @ApiModelProperty(value = "关系类型 0 闲置 1 产权 2 租赁")
    private String relType;
    /**
     * 启用时间
     */
    @ApiModelProperty(value = "启用时间")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime checkInTime;
    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effTime;
    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    private LocalDateTime expTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 委托代理人姓名
     */
    @ApiModelProperty(value = "委托代理人姓名")
    private String wtdlrxm;

    /**
     * 代理人联系电话
     */
    @ApiModelProperty(value = "代理人联系电话")
    private String dlrlxdh;

    /**
     * 代理人证件类型
     */
    @ApiModelProperty(value = "代理人证件类型 111: 居民身份证 414  普通护照 554 外国人居留证 990 其他  字典credential_type")
    private String dlrzjlx;

    /**
     * 代理人证件号码
     */
    @ApiModelProperty(value = "代理人证件号码")
    private String dlrzjhm;

}
