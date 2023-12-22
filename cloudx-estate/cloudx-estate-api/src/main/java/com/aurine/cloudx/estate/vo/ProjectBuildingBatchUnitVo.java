

package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p> 单元批量添加VO </p>
 *
 * @ClassName: ProjectBuildingInfoBatchVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/10 10:47
 * @Copyright:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "单元批量添加VO")
public class ProjectBuildingBatchUnitVo extends Model<ProjectBuildingBatchUnitVo> {
    private static final long serialVersionUID = 1L;
    /**
     * 房屋列表
     */
    @ApiModelProperty(value = "房屋列表")
    private List<ProjectHouseInfoVo> houseList;
    /**
     * 单元名称
     */
    @ApiModelProperty(value = "单元名称")
    private String unitName;
    /**
     * 单元id
     */
    @ApiModelProperty("单元id")
    private String unitId;

    @ApiModelProperty(value = "单元编号")
    private String unitCode;
}
