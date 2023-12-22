package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectAttrVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * (ProjectAttrMapper)
 *
 * @author xull
 * @since 2020/7/6 10:33
 */
@Mapper
public interface ProjectAttrMapper  {


    IPage<ProjectAttrVo> page(Page page, @Param("query") ProjectAttrVo query);
}
