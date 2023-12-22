
package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p> 楼栋批量增加VO </p>
 *
 * @ClassName: ProjectBuildingInfoBatchVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/10 10:47
 * @Copyright:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "楼栋")
public class ProjectBuildingBatchVo extends Model<ProjectBuildingBatchVo> {
    private static final long serialVersionUID = 1L;

    /**
     * 樓棟列表
     */
    @ApiModelProperty(value = "樓棟列表")
    private List<ProjectBuildingBatchBuildingVo> buildingList;

}
