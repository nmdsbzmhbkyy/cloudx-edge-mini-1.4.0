package com.aurine.cloudx.open.origin.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * <p>
 * 开关闸记录对象
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/27 10:28
 */
public class ProjectOpenLaneHisDto {

    /*
     * 自增序列
     **/
    @ApiModelProperty("自增序列 主键")
    private Integer seq;

    /*
     * 开闸记录ID uid
     **/
    @ApiModelProperty("开闸记录ID uid")
    private String recordId;

    /*
     * 开闸时间
     **/
    @ApiModelProperty("开闸时间")
    private LocalDateTime openTime;

    /*
     * 关闸时间
     **/
    @ApiModelProperty("关闸时间")
    private LocalDateTime closeTime;

    /*
     * 抬杠人员姓名（开闸）
     **/
    @ApiModelProperty("抬杠人员姓名（开闸）")
    private LocalDateTime openPersonName;

    /*
     * 降杠人员姓名（关闸）
     **/
    @ApiModelProperty("降杠人员姓名（关闸）")
    private LocalDateTime closePersonName;

    /*
     * 车道ID
     **/
    @ApiModelProperty("车道ID")
    private String laneId;

    /*
     * 车场ID
     **/
    @ApiModelProperty("车场ID")
    private String parkId;

    /*
     * 开闸原因 字典:vehicle_pass_reason
     **/
    @ApiModelProperty("开闸原因 字典:vehicle_pass_reason")
    private Character passReason;

    /*
     * 图片url（抓拍图片）
     **/
    @ApiModelProperty("图片url（抓拍图片）")
    private String picUrl;

    /**
     * 项目id
     */
    @ApiModelProperty("项目ID")
    private Integer projectId;

    /**
     *
     */
    @TableField(value = "tenant_id")
    @ApiModelProperty("租户ID")
    private Integer tenantId;

    /**
     * 操作时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
