package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author : 王良俊
 * @date : 2021-12-17 09:25:12
 */
@Data
@TableName("cloud_edge_request")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "边缘网关连接申请")
public class CloudEdgeRequest extends OpenBasePo<CloudEdgeRequest> {

    /**
     * 自增序列
     */
    private Integer seq;


    /**
     * 边缘网关连接申请UUID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("边缘网关连接申请UUID")
    private String requestId;

    @ApiModelProperty("第三方项目ID")
    private String projectCode;


    /**
     * 边缘侧项目名
     */
    @ApiModelProperty("边缘侧项目名")
    private String edgeProjectName;


    /**
     * 边缘侧联系人姓名
     */
    @ApiModelProperty("边缘侧联系人姓名")
    private String edgeContactPerson;

    /**
     * 边缘侧联系人手机号
     */
    @ApiModelProperty("边缘侧联系人手机号")
    private String edgeContactPhone;


    /**
     * 边缘侧联系人性别 使用字典gender_type
     */
    @ApiModelProperty("边缘侧联系人性别")
    private String edgeGender;


    /**
     * 照片URL
     */
    @ApiModelProperty("照片URL")
    private String edgePicUrl;


    /**
     * 身份证号
     */
    @ApiModelProperty("身份证号")
    private String edgeIdNumber;


    /**
     * 边缘网关的设备编号
     */
    @ApiModelProperty("边缘网关的设备编号")
    private String edgeDeviceId;

    /**
     * 同步方式 1 使用边缘网关数据 2 使用云端数据
     */
    @ApiModelProperty("同步方式")
    private Character syncType;


    /**
     * 当syncType字段为1时启用，0未删除，1已删除
     */
    @ApiModelProperty("当syncType字段为1时启用，0未删除，1已删除，2删除中")
    private String delStatus;


    /**
     * 入云状态 0 未入云 1 待审核 2  已拒绝 3 已入云 4 解绑中
     */
    @ApiModelProperty("入云状态")
    private Character cloudStatus;


    @ApiModelProperty("配置信息 JSON")
    private String configJson;


    /**
     * 申请时间
     */
    @ApiModelProperty("申请时间")
    private LocalDateTime requestTime;

    /**
     * 0 未同步 1 已同步
     */
    @ApiModelProperty("同步状态")
    private Character isSync;


    /**
     * 项目ID
     */
    @ApiModelProperty("项目ID")
    private Integer projectId;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
