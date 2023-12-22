package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectMediaRepo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Title: ProjectMediaRepoVo
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/6/8 19:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("项目媒体资源库Vo")
public class ProjectMediaRepoVo extends ProjectMediaRepo {
    @ApiModelProperty("开始时间字符串")
    private String beginTimeString;
    @ApiModelProperty("结束时间字符串")
    private String endTimeString;
}
