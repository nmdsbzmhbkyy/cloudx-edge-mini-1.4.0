package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceLoadLog;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceLoadLogVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备导入日志(ProjectDeviceLoadLog)表数据库访问层
 *
 * @author 王良俊
 * @since 2021-06-03 10:49:37
 */
@Mapper
public interface ProjectDeviceLoadLogMapper extends BaseMapper<ProjectDeviceLoadLog> {

    Page<ProjectDeviceLoadLogVo> fetchList(Page<ProjectDeviceLoadLogVo> page, @Param("deviceType") String deviceType);

}
