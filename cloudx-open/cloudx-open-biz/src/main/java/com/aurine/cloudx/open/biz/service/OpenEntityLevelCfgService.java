package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.EntityLevelCfgVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-组团配置管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenEntityLevelCfgService {

    /**
     * 根据ID获取组团配置
     *
     * @param id
     * @return
     */
    R<EntityLevelCfgVo> getById(String id);

    /**
     * 根据ID获取组团配置
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<EntityLevelCfgVo>> page(Page page, EntityLevelCfgVo vo);

    /**
     * 新增组团配置
     *
     * @param vo
     * @return
     */
    R<EntityLevelCfgVo> save(EntityLevelCfgVo vo);

    /**
     * 修改组团配置
     *
     * @param vo
     * @return
     */
    R<EntityLevelCfgVo> update(EntityLevelCfgVo vo);

    /**
     * 删除组团配置
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

}
