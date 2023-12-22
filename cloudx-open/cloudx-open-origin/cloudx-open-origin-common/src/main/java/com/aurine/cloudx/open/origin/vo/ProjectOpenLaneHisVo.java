package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * 开关闸记录对象
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/27 10:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectOpenLaneHisVo {

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
    private String openPersonName;

    /*
     * 降杠人员姓名（关闸）
     **/
    @ApiModelProperty("降杠人员姓名（关闸）")
    private String closePersonName;

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

    /*
     * 车道名
     **/
    @ApiModelProperty("车道名")
    private String laneName;

    /*
     * 车场名
     **/
    @ApiModelProperty("车场名")
    private String parkName;

    /*
     * 行进方向
     **/
    @ApiModelProperty("行进方向")
    private String direction;


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
