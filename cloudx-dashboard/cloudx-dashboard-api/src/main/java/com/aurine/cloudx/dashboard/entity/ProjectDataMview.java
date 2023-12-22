package com.aurine.cloudx.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 项目统计信息
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-24
 * @Copyright:
 */
@Data
@TableName("PROJECT_DATA_MVIEW")
public class ProjectDataMview extends BaseDashboardEntity {


    /**
     * 楼栋数量
     */
    @ApiModelProperty("楼栋数量")
    @TableField("CNT_BUILDING")
    private Long buildingNum;
    /**
     * 房屋数量
     */
    @ApiModelProperty("房屋数量")
    @TableField("CNT_HOUSE")
    private Long houseNum;
    /**
     * 房屋空闲数
     */
    @ApiModelProperty("房屋空闲数")
    @TableField("CNT_HOUSE_IDLE")
    private Long houseIdleNum;
    /**
     * 住户数量
     */
    @ApiModelProperty("住户数量")
    @TableField("CNT_PERSON")
    private Long personNum;
    /**
     * 车数量
     */
    @ApiModelProperty("车数量")
    @TableField("CNT_CAR")
    private Long carNum;
    /**
     * 停车位数量
     */
    @ApiModelProperty("停车位数量")
    @TableField("CNT_PLACE")
    private Long placeNum;
    /**
     * 设备数量
     */
    @ApiModelProperty("设备数量")
    @TableField("CNT_DEVICE")
    private Long deviceNum;

}
