package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectHouseFeeItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 新增房屋费用设置视图
 *
 * @author xull@aurine
 * @date 2020-07-29 13:27:59
 */
@Data
@ApiModel(value = "新增房屋费用设置视图")
@NoArgsConstructor
@AllArgsConstructor
public class ProjectHouseFeeItemUpdateVo  {
    @ApiModelProperty("房屋id")
    private String houseId;
    @ApiModelProperty("删除费用id")
    private List<String> deleteIds;
    @ApiModelProperty("房屋费用对象列表")
    private List<ProjectHouseFeeItem> projectHouseFeeItems;
}
