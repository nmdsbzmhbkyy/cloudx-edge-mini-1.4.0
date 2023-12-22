package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 房屋费用设置(ProjectHouseFeeItem)表实体类
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "房屋费用设置(ProjectHouseFeeItem)")
public class ProjectHouseFeeItem extends OpenBasePo<ProjectHouseFeeItem> {

    private static final long serialVersionUID = -45590449487607214L;


    /**
     * 记录id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "记录id")
    private String recordId;


    /**
     * 房间id
     */
    @ApiModelProperty(value = "房间id")
    private String houseId;


    /**
     * 费用id
     */
    @ApiModelProperty(value = "费用id")
    private String feeId;

    /**
     * 账单月份
     */
    @ApiModelProperty("账单月份")
    private String billMonth;


}