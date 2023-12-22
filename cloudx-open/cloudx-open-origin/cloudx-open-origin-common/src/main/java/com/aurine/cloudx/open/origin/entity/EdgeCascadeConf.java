package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>项目入云配置（云端）</p>
 * @author : 王良俊
 * @date : 2021-12-10 15:31:17
 */
@Data
@TableName("edge_cascade_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目入云配置")
public class EdgeCascadeConf extends OpenBasePo<CloudEdgeRequest> {

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
     * 连接码
     */
    private String connectCode;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID,关联pigxx.sys_dept.dept_id")
    private Integer projectId;

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