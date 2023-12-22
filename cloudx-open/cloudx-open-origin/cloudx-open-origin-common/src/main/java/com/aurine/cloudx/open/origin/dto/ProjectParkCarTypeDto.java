package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.entity.ProjectParkCarType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Classname ProjectParkCarTypeDto
 * @Description 车辆类型
 * @Date 2022/5/12 15:21
 * @Created by linlx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectParkCarTypeDto extends ProjectParkCarType {
    private static final long serialVersionUID = 1L;

    private String typeId;

    /**
     * 车辆类型 0 免费车 1月租车
     */
    private String carType;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 车场名称
     */
    private String parkName;

    /**
     * 是否禁用 0 启用 1 禁用
     */
    private String isDisable;

    /**
     * 是否默认
     */
    private Integer isDefault;

    /**
     * 车场ID
     */
    private String parkId;

    /**
     * 项目id
     */
    private Integer projectId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 操作人
     */
    private Integer operator;
    /**
     * 操作时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
