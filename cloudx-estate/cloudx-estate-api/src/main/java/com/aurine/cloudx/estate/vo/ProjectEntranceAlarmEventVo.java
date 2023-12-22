package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报警事件
 *
 * @author 黄阳光
 * @date 2020-06-04 08:30:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "报警事件")
public class ProjectEntranceAlarmEventVo extends Model<ProjectEntranceAlarmEventVo> {
    private static final long serialVersionUID = 1L;
    
    /**
     * 事件id，uuid
     */
    @TableId
    @ApiModelProperty(value = "事件id，uuid")
    private String eventId;
    /**
     * 事件处理记录seq
     */
    @ApiModelProperty(value = "事件处理记录seq")
    private Integer handleSeq;
    /**
     * 人员类型
     */
    @ApiModelProperty(value = "人员类型")
    private String personType;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String personName;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String deviceId;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    /**
     * 设备区域id
     */
    @ApiModelProperty(value = "设备区域id")
    private String deviceRegionId;
    /**
     * 设备区域名称
     */
    @ApiModelProperty(value = "设备区域名称")
    private String regionName;
    /**
     * 事件描述
     */
    @ApiModelProperty(value = "事件描述")
    private String eventDesc;
    /**
     * 事件时间
     */
    @ApiModelProperty(value = "事件时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventTime;
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 事件开始时间
     */
    @ApiModelProperty(value = "事件开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    /**
     * 事件结束时间
     */
    @ApiModelProperty(value = "事件结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    /**
     * 开始处理时间
     */
    @ApiModelProperty(value = "开始处理时间")
    private LocalDateTime handleBeginTime;
    /**
     * 结束处理时间
     */
    @ApiModelProperty(value = "结束处理时间")
    private LocalDateTime handleEndTime;
    /**
     * 处理时长
     */
    @ApiModelProperty(value = "处理时长")
    private String dealDuration;
    /**
     * 处理结果
     */
    @ApiModelProperty(value = "处理结果")
    private String result;
    /**
     * 当月事件总量
     */
    @ApiModelProperty(value = "当月事件总量")
    private String monthNum;
    /**
     * 当月超限处理事件总量
     */
    @ApiModelProperty(value = "当月超限处理事件总量")
    private String timeLeaveNum;
    /**
     *  时限
     */
    @ApiModelProperty(value = "时限")
    private String timeLeave;
    /**
     *  处理时限长度
     */
    @ApiModelProperty(value = "处理时限长度")
    private String alarmTimeLimit;
    /**
     * 抓拍图片
     */
    @ApiModelProperty(value = "抓拍图片")
    private String picUrl;
    /**
     * 处理图片
     */
    @ApiModelProperty(value = "处理图片")
    private String livePic;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 不查询的状态
     */
    @ApiModelProperty(value = "不查询的状态")
    private String notStatus;
    /**
     * evenId集合
     */
    @ApiModelProperty(value = "evenId集合")
    private List<String> eventIdList;
    
    /**
     * 查询处理时长起始
     */
    @ApiModelProperty(value = "查询处理时长起始")
    private Long effTime;
    /**
     * 查询处理时长结束
     */
    @ApiModelProperty(value = "查询处理时长结束")
    private Long expTime;
    
    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;
    
    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    @TableField(value = "tenant_id")
    private Integer tenantId;

    /**
     * 事件类型ID
     */
    @ApiModelProperty(value = "事件类型ID")
    private String eventTypeId;

    /**
     * 设备类型
     */
    @ApiModelProperty(value = "事件类型ID")
    private String deviceType;

    /**
     * 警告信息
     */
    @ApiModelProperty(value = "警告信息")
    private String eventTypeName;


    /**
     * 报警级别
     */
    @ApiModelProperty(value = "报警级别 1 一般  2严重")
    private String eventLevel;

    /**
     * 设备类型名称
     */
    @ApiModelProperty(value = "设备类型名称")
    private String deviceTypeName;


    /**
     * 设备类型  1 确认  2 处理
     */
    @ApiModelProperty(value = "设备类型 1 确认  2 处理")
    private String eventCategory;

    /**
     * 创建人Id
     */
    @ApiModelProperty(value = "处理事件的创建人Id")
    private Integer handleOperator;


    /**
     * 事件Id 由中台传入
     */
    @ApiModelProperty(value = "事件流水ID")
    private Integer eventCode;

    /**
     * 事件Id 由中台传入
     */
    @ApiModelProperty(value = "防区号")
    private Integer areaNo;

}
