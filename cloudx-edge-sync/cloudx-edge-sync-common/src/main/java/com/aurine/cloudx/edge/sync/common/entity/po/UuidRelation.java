package com.aurine.cloudx.edge.sync.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author pigx code generator
 * @date 2022-01-05 16:09:50
 */
@Data
@TableName("uuid_relation")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "")
public class UuidRelation extends Model<UuidRelation> {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId
    @ApiModelProperty(value = "自增主键", hidden = true)
    private Integer seq;
    /**
     * 当前业务主键
     */
    @ApiModelProperty(value = "当前业务主键")
    private String uuid;
    /**
     * 第三方业务主键
     */
    @ApiModelProperty(value = "当前业务主键")
    private String thirdUuid;

    /**
     * 租户
     */
    @ApiModelProperty(value = "租户")
    private Integer tenantId;

    /**
     * 项目UUID
     */
    @ApiModelProperty(value = "项目UUID")
    private String projectUUID;

    /**
     * 服务名称
     */
    @ApiModelProperty(value = "服务名称")
    private String serviceName;

    /**
     * 旧的MD5
     */
    @ApiModelProperty(value = "旧的MD5")
    private String oldMd5;
}
