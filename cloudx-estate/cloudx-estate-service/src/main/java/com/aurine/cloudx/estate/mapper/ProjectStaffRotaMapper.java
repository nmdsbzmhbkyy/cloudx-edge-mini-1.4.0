package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.vo.ProjectStaffRotaPageVo;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectStaffRota;

import java.util.List;

/**
 * 项目员工值班表信息(ProjectStaffRota)表数据库访问层
 *
 * @author guhl@aurine.cn
 * @since 2020-08-03 11:48:49
 */
@Mapper
public interface ProjectStaffRotaMapper extends BaseMapper<ProjectStaffRota> {

}