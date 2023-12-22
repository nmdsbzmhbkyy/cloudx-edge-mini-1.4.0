package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectVehiclesEntryExit;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 车辆出入口信息表(ProjectVehiclesEntryExit)表实体类
 *
 * @author 王良俊
 * @since 2020-08-17 10:08:52
 */
@Data
@ApiModel(value = "车辆出入口信息表Vo对象")
public class ProjectVehiclesEntryExitVo extends ProjectVehiclesEntryExit {

    /**
     * 车道ID列表
     * */
    @ApiModelProperty("车道ID列表")
    String[] laneIdArr;

    /**
     * 图片文件列表
     * */
    @ApiModelProperty("图片url对象集合")
    List<ProjectUnitFileVo> fileList;

    /**
     * 图片url数组
     * */
    @ApiModelProperty("图片url数组")
    String[] picUrlArr;
}