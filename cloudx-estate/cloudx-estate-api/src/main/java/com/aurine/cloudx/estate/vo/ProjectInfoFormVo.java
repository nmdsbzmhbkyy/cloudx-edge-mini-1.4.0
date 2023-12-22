package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: ProjectInfoFormVo
 * Description: 项目管理 提交表单 用于新增删除操作
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/7 19:15
 */
@Data
@ApiModel("项目拓展属性Vo(用于新增查询操作)")
public class ProjectInfoFormVo extends ProjectInfo {
    @ApiModelProperty("人员Vo(用于新增)")
    private SysUserVo user;
    @ApiModelProperty("人员Vo列表(用于编辑查询回调)")
    private List<SysUserVo> userList;
}
