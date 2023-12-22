package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Author: wrm
 * @Date: 2022/10/19 16:45
 * @Package: com.aurine.cloudx.estate.entity
 * @Version: 1.0
 * @Remarks:
 **/
@Data
@TableName("project_card_issue_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "发卡记录(ProjectCardIssueRecord)")
public class ProjectCardIssueRecord extends Model<ProjectCardIssueRecord> {
    private static final long serialVersionUID = -44252101955053642L;

    /**
     * 序列，自增
     */
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;

    /**
     * 记录id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "记录id，uuid")
    private String recordId;

    /**
     * 卡号
     */
    @ApiModelProperty(value = "卡号")
    private String cardNo;

    /**
     * 卡类型
     */
    @ApiModelProperty(value = "卡类型")
    private Integer cardType;

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    @TableField(value = "tenant_id")
    private Integer tenantId;

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

}
