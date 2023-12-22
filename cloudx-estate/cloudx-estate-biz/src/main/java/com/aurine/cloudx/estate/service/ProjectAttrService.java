package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.vo.ProjectAttrVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * (ProjectAttrService) 属性拓展配置
 *
 * @author xull
 * @since 2020/7/6 10:13
 */
public interface ProjectAttrService {
    IPage<ProjectAttrVo> page(Page page,  ProjectAttrVo query);

    boolean add(ProjectAttrVo projectAttrVo);

    boolean update(ProjectAttrVo projectAttrVo);

    boolean remove(String style, String attrId);

}
