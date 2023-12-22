package com.aurine.cloudx.edge.sync.common.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wrm
 * @Date: 2021/12/15 16:38
 * @Package: com.aurine.cloudx.open.entity
 * @Version: 1.0
 * @Remarks:
 **/
@Data
public class TaskInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 随机生成的32位主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "唯一主键", notes = "随机生成的32位唯一主键")
    private String taskId;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id", notes = "租户id")
    private Integer tenantId;

    /**
     * 项目Uuid
     */
    @ApiModelProperty(value = "项目Uuid", notes = "项目Uuid")
    private String projectUUID;

    /**
     * 业务主键
     */
    @ApiModelProperty(value = "业务主键", notes = "云平台或者边缘网关变动的表的主键")
    private String uuid;

    /**
     * 操作，级联，事件，命令及其他类型的类型值
     */
    @ApiModelProperty(value = "类型", notes = "操作，级联，事件，命令及其他类型的类型值")
    private String type;
    /**
     * 服务类型
     * 级联入云：cascade
     * 操作：operate
     * 事件：event
     * 命令：command
     * 其他：ohter
     */
    @ApiModelProperty(value = "服务类型", notes = "级联入云：cascade，操作：operate，事件：event，命令：command，其他：ohter")
    private String serviceType;
    /**
     * 服务名称
     */
    @ApiModelProperty(value = "服务名称", notes = "服务名称")
    private String serviceName;

    /**
     * 重试次数
     */
    @ApiModelProperty(value = "重试次数")
    private Integer retriesCount;

    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String errMsg;

    /**
     * 新的MD5
     */
    @ApiModelProperty(value = "新的MD5")
    private String newMd5;
    /**
     * 操作的Json对象字符串
     */
    @ApiModelProperty(value = "操作的Json对象字符串")
    private String data;

    /**
     * 数据来源
     * 云端：platform
     * 主边缘网关：master
     * 子边缘网关：slave
     */
    @ApiModelProperty(value = "数据来源")
    private String source;

    //---------------以下为新变量--------------------------
    /**
     * 旧的MD5
     */
    @ApiModelProperty(value = "旧的MD5")
    private String oldMd5;

    /** 第三方uuid，用于新增操作返回给同步发起者入库使用 */
    private String thirdUuid;

    /** 第三方uuid，用于新增操作返回给同步发起者入库使用 */
    private String projectCode;

    /**
     * 消息id，保存在redis中，用于过滤重复请求
     * 调度线程存入redis中生成，
     * 接收端第一次收到后保存到redis中
     * 业务处理完成后接收端延时删除redis中此msgId
     */
    private String msgId;
}
