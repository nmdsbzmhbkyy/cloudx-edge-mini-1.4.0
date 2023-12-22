package com.aurine.cloudx.open.origin.mapper;


import com.aurine.cloudx.open.origin.vo.ProjectPerimeterAlarmAreaVo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.entity.ProjectPerimeterAlarmArea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 防区管理
 *
 * @author 邹宇
 * @date 2021-6-15 14:45:18
 */
@Mapper
public interface ProjectPerimeterAlarmAreaMapper extends BaseMapper<ProjectPerimeterAlarmArea> {


    Page<ProjectPerimeterAlarmAreaVo> findPage(Page page, @Param("query") ProjectPerimeterAlarmAreaVo projectPerimeterAlarmAreaVo);

    ProjectDeviceInfo reacquireDefenseArea(String deviceId);

    Integer comparison(String sn);
}
