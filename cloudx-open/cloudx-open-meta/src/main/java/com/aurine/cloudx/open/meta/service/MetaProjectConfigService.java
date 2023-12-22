package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectConfig;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * 项目参数设置
 *
 * @author : zy
 * @date : 2022-11-24 09:50:33
 */

public interface MetaProjectConfigService {


    /**
     * 新增
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectConfig> save(ProjectConfig po);

    /**
     * 修改
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectConfig> update(ProjectConfig po);

    /**
     * 删除
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
