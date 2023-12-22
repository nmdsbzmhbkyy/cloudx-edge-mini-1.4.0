package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectCardHis;
import com.aurine.cloudx.open.origin.vo.ProjectCardHisVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 卡操作记录
 *
 * @author zy
 *
 * @date 2022-10-18 08:40:49
 */
@Mapper
public interface ProjectCardHisMapper extends BaseMapper<ProjectCardHis> {


    Page<ProjectCardHisVo> pageVo(Page page, @Param("query") ProjectCardHisVo projectCardVo);
}
