package com.aurine.cloudx.open.origin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户操作日志
 * 
 * @ClassName: SysOperationLog
 * @author: 林港 <ling@aurine.cn>
 * @date: 2020年6月22日 上午11:16:20
 * @Copyright:
 */
@Data
@TableName("sys_operation_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户操作日志")
public class SysOperationLog extends Model<SysOperationLog> {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userid;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String type;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String title;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    @TableField(value = "service_id")
    private String serviceId;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    @TableField(value = "remote_addr")
    private String remoteAddr;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String params;
    /**
     * 执行时间
     */
    @ApiModelProperty(value = "执行时间")
    private String time;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String exception;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    @TableField(value = "user_agent")
    private String userAgent;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    @TableField(value = "request_uri")
    private String requestUri;
    /**
     * 
     */
    @ApiModelProperty(value = "")
    private String method;
    /**
     * 
     */
    @ApiModelProperty(value = "", hidden = true)
    @TableField(value = "tenant_id")
    private Integer tenantId;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createtime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatetime;
}
