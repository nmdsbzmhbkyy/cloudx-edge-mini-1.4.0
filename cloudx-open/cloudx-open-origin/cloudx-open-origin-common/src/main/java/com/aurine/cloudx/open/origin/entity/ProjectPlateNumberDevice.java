package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 设备车牌号下发情况表
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/26 14:58
 */
@Data
@Builder
@TableName(value = "project_plate_number_device", schema = "aurine_parking")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPlateNumberDevice extends OpenBasePo<ProjectPlateNumberDevice> {

    /*
     * 自增序列
     **/
    private Integer seq;

    /*
     * UUID
     **/
    @TableId(type = IdType.ASSIGN_UUID)
    private String uid;

    /*
     * 设备ID
     **/
    private String deviceId;

    /*
     * 车辆登记ID
     **/
    private String parCarRegisterId;

    /*
     * 车牌号
     **/
    private String plateNumber;

    /*
     * 车牌号状态 0 正常(白名单) 1 禁止通行(黑名单)
     **/
    private String plateNumberStatus;

    /*
     * 下载状态 0 下载成功 1 下载失败 2 下载中 3 删除中
     **/
    private String dlStatus;

    /*
     * 失败原因
     **/
    private String errMsg;

    /*
     * 下发时间
     **/
    private LocalDateTime sendTime;

    /*
     * 项目ID
     **/
    private Integer projectId;

    /*
     * 租户ID
     **/
    @TableField(value = "tenant_id")
    private Integer tenantId;

    /*
     * 操作人
     **/
    private Integer operator;

    /*
     * 创建时间
     **/
    private LocalDateTime createTime;

    /*
     * 修改时间
     **/
    private LocalDateTime updateTime;

    /**
     * 设备对接id
     */
    private String thirdpartyCode;
}
