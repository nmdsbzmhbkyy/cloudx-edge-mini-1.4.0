package com.aurine.cloudx.estate.entity;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>项目入云配置（云端）</p>
 * @author : 王良俊
 * @date : 2021-12-10 15:31:17
 */
@Data
public class EdgeCascadeConf {

    /**
     * 序列
     */
    private Integer seq;

    /**
     * 配置ID
     */
    @ApiModelProperty(value = "配置ID UUID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String confId;

    /**
     * 开启级联 （云端无用）
     */
    private Character isCascade;

    /**
     * 开启入云 （云端无用）
     */
    private Character isSyncCloud;

    /**
     * 入云码
     */
    private String connectCode;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID,关联pgixx.sys_dept.dept_id")
    private Integer projectId;

    /**
     * 租户ID
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "操作人")
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
/*    public String getConnectCode() {
        if (StrUtil.isEmpty(connectCode) && projectId != null) {
            connectCode = Integer.toUnsignedString(projectId, 36);
        }
        return connectCode;
    }
    */
}