package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.TreeNode;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 车辆出入口信息表(ProjectVehiclesEntryExit)表实体类
 *
 * @author 王良俊
 * @since 2020-08-17 10:08:52
 */
@Data
@ApiModel(value = "车辆出入口信息表树")
public class ProjectVehiclesEntryExitTreeVo extends TreeNode {
    String name;
    String id;
    String level;
}
