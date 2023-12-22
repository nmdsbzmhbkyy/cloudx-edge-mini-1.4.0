package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: ProjectInfoByAdminFormVo
 * Description: 项目审批查询表单
 *
 * @author xull@aurine.cn , ghl@aurine.cn
 * @version 1.0.0
 * @date 2020/5/9 19:15
 */
@Data
@ApiModel("项目审批查询表单")
public class ProjectInfoByAdminFormVo extends ProjectInfo {
    /**
     * 平台管理员id
     */
    @ApiModelProperty("平台管理员id")
    private Integer platformId;

    /**
     * 集团企业名称
     */
    @ApiModelProperty("集团企业名称")
    private String companyName;

    /**
     * 项目组名称
     */
    @ApiModelProperty("项目组名称")
    private String projectGroupName;


    @ApiModelProperty("项目状态列表")
    private List<String> auditStatusList;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String projectName;


}
