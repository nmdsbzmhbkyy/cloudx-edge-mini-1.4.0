package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectHouseFeeItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 批量添加房屋费用设置视图
 *
 * @author xull@aurine
 * @date 2020-07-29 13:27:59
 */
@Data
@ApiModel(value = "批量添加房屋费用设置视图")
public class ProjectHouseFeeItemUpdateBatchVo {
    @ApiModelProperty("房屋id列表")
    private List<String> houseIds;
    @ApiModelProperty("房屋费用对象列表")
    private List<ProjectHouseFeeItem> projectHouseFeeItems;
}
