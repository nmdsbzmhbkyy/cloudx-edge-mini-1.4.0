package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectInspectDetailDevice;
import com.aurine.cloudx.estate.vo.ProjectInspectDetailDeviceVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 巡检任务明细设备列表(ProjectInspectDetailDevice)表数据库访问层
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:31
 */
@Mapper
public interface ProjectInspectDetailDeviceMapper extends BaseMapper<ProjectInspectDetailDevice> {

    /**
     * <p>
     * 根据任务明细id获取到任务明细设备id
     * </p>
     *
     * @param detailId 任务明细设备id
     * @return 巡检任务明细设备vo对象列表
     */
    List<ProjectInspectDetailDeviceVo> listDetailDeviceByDetailId(@Param("detailId") String detailId);

}