

package com.aurine.cloudx.estate.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>框架统计与入住人员统计 DTO</p>
 *
 * @ClassName: ProjectFrameCountDTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/7/3 11:35
 * @Copyright:
 */
@Data
public class ProjectFrameCountDTO {
    private static final long serialVersionUID = 1L;
    /**
     * 框架数量
     */
    @ApiModelProperty(value = "框架数量")
    private int entityCount;
    /**
     * 框架层级
     */
    @ApiModelProperty(value = "框架层级")
    private int level;
    /**
     * 入住人数
     */
    @ApiModelProperty(value = "入住人数")
    private int personCount;
}
