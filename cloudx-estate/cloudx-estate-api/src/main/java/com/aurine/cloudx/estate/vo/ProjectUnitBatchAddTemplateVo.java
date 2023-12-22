

package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectHouseBatchAddTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 房屋模板
 *
 * @author 王伟
 * @date 2020-06-04 15:37:03
 */
@Data
@ApiModel(value = "房屋模板")
public class ProjectUnitBatchAddTemplateVo {
    private static final long serialVersionUID = 1L;

    /**
     * 模板id,关联楼栋模板id
     */
    @ApiModelProperty(value = "模板id,关联楼栋模板id")
    private String buildingTemplateId;

    /**
     * 房屋列表
     */
    @ApiModelProperty(value = "房屋列表")
    List<ProjectHouseBatchAddTemplate> houseList;

    /**
     * 单元模板id，uuid
     */
    @ApiModelProperty(value = "单元模板id，uuid")
    private String unitTemplateId;
    /**
     * 模板中楼栋的单元号 ，如 1 2 3 或者A B C
     */
    @ApiModelProperty(value = "模板中楼栋的单元号 ，如 1 2 3 或者A B C")
    private String unitNo;
    /**
     * 所在单元号的房间数
     */
    @ApiModelProperty(value = "所在单元号的房间数")
    private Integer houseCount;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
