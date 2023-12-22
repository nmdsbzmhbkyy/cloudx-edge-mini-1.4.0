

package com.aurine.cloudx.estate.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 楼层信息表
 *
 * @author huangjj
 * @date 2022-03-08 10:04:10
 */
@Data
@EqualsAndHashCode()
@ApiModel(value = "楼层信息表")
public class ProjectFloorVo{
    @ApiModelProperty(value = "楼层框架号")
    private String floorNo;
    @ApiModelProperty(value = "是否为公共楼层，预留")
    private String isPublic;
    @ApiModelProperty(value = "是否禁止用户改变,1为选中不可变更")
    private String isDisable;
}
