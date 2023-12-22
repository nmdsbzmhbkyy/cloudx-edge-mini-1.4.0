package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 *  设备车牌号下发记录Vo对象
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/26 14:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPlateNumberDeviceVo {

    /*
     * 自增序列
     **/
    private Integer seq;

    /*
     * UUID
     **/
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
     * 车牌号状态 0 正常 1 禁止通行
     **/
    private String plateNumberStatus;

    /*
     * 下载状态 0 下载成功 1 下载失败
     **/
    private String dlStatus;

    /*
     * 车牌号状态中文描述
     **/
    private String plateNumberStatusCh;

    /*
     * 下载状态中文描述
     **/
    private String dlStatusCh;

    /*
     * 失败原因
     **/
    private String errMsg;

    /*
     * 下发时间
     **/
    private LocalDateTime sendTime;

    /*
     * 下发时间
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

}
