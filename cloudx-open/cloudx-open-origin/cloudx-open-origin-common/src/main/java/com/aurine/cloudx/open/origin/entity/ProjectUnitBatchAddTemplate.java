

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 房屋模板
 *
 * @author 王伟
 * @date 2020-06-04 15:37:03
 */
@Data
@TableName("project_unit_batch_add_template")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "房屋模板")
public class ProjectUnitBatchAddTemplate extends OpenBasePo<ProjectUnitBatchAddTemplate> {
private static final long serialVersionUID = 1L;


    /**
     * 模板id,关联楼栋模板id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="模板id,关联楼栋模板id")
    private String buildingTemplateId;
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
    }
