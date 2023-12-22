package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectDeviceParamHisPageVo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceParamHis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 记录项目设备参数配置的历史记录(ProjectDeviceParamHis)表数据库访问层
 *
 * @author makejava
 * @since 2020-12-23 09:31:51
 */
@Mapper
public interface ProjectDeviceParamHisMapper extends BaseMapper<ProjectDeviceParamHis> {

    Page<ProjectDeviceParamHisPageVo> page(Page page, @Param("query") ProjectDeviceParamHisPageVo projectDeviceInfoPageFormVo);
}