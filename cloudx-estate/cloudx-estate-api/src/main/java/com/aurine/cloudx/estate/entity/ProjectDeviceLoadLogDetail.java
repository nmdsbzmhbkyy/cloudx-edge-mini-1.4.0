package com.aurine.cloudx.estate.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 设备导入日志明细(ProjectDeviceLoadLogDetail)表实体类
 *
 * @author 王良俊
 * @since 2021-06-03 10:49:52
 */
@Data
@NoArgsConstructor
@TableName("project_device_load_log_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备导入日志明细(ProjectDeviceLoadLogDetail)")
public class ProjectDeviceLoadLogDetail extends Model<ProjectDeviceLoadLogDetail> {

    private static final long serialVersionUID = 226083475655263916L;

    /**
     * 序列
     */
    @ApiModelProperty(value = "序列")
    private Integer seq;


    /**
     * 明细id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "明细id")
    private String batchDetailId;


    /**
     * 导入批次
     */
    @ApiModelProperty(value = "导入批次")
    private String batchId;


    /**
     * 行号
     */
    @ApiModelProperty(value = "行号")
    private Integer rowNo;


    /**
     * 设备SN
     */
    @ApiModelProperty(value = "设备SN")
    private String SN;


    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;


    /**
     * 导入状态 0 未导入 1 导入中 2 导入成功 9 导入失败
     */
    @ApiModelProperty(value = "导入状态 0 未导入 1 导入中 2 导入成功 9 导入失败")
    private String loadStatus;


    /**
     * 返回码
     */
    @ApiModelProperty(value = "返回码")
    private String errorCode;


    /**
     * 返回信息
     */
    @ApiModelProperty(value = "返回信息")
    private String errorMsg;


    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;


    @ApiModelProperty(value = "", hidden = true)
    @TableField(value = "tenant_id")
    private Integer tenantId;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String operator;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    public ProjectDeviceLoadLogDetail(String batchDetailId, String batchId, Integer rowNo, String SN, String deviceName, String loadStatus, String errorCode, String errorMsg) {
        this.batchDetailId = batchDetailId;
        this.batchId = batchId;
        this.rowNo = rowNo;
        this.SN = SN;
        this.deviceName = deviceName;
        this.loadStatus = loadStatus;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    // 避免这两个长度过长导致设备导入失败

    public void setSN(String SN) {
        this.SN = SN != null && SN.length() > 64 ? SN.substring(0, 64) : SN;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName != null && deviceName.length() > 64 ? deviceName.substring(0, 64) : deviceName;
    }

}
