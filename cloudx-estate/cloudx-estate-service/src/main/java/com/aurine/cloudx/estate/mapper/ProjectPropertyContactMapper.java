package com.aurine.cloudx.estate.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectPropertyContact;

/**
 * 项目物业联系方式表(ProjectPropertyContact)表数据库访问层
 *
 * @author xull
 * @version 1.0.0
 * @date 2020-10-27 15:38:49
 */
@Mapper
public interface ProjectPropertyContactMapper extends BaseMapper<ProjectPropertyContact> {

}