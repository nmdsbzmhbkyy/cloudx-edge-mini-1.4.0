package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Title: ProjectInfoPageVo
 * Description: 项目管理分页查询视图
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/9 10:17
 */
@Data
@ApiModel("项目管理分页查询视图")
public class ProjectInfoPageVo extends ProjectInfo {
    /**
     * 所属集团名称
     */
    @ApiModelProperty("所属集团名称")
    private String companyName;
    /**
     * 所属项目组
     */
    @ApiModelProperty("所属项目组")
    private String projectGroupName;
    @ApiModelProperty("公安是否启用")
    private String policeEnable;

}
