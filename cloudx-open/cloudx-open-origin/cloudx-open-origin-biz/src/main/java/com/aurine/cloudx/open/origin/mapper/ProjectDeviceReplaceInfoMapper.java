package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceReplaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备变更历史表，用于存储被替换掉的设备信息(ProjectDeviceReplaceInfo)表数据库访问层
 *
 * @author 王良俊
 * @since 2021-01-11 11:18:43
 */
@Mapper
public interface ProjectDeviceReplaceInfoMapper extends BaseMapper<ProjectDeviceReplaceInfo> {

}