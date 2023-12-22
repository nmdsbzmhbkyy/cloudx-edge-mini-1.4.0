package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.common.core.entity.TreeNode;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

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