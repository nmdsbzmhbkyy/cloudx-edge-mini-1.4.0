

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectHouseBatchAddTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 单元模板
 *
 * @author 王伟
 * @date 2020-06-04 15:36:50
 */
public interface ProjectHouseBatchAddTemplateService extends IService<ProjectHouseBatchAddTemplate> {

    /**
     * 根据unitId批量删除楼栋模板
     * @param unitTemplateId
     * @return
     */
    boolean deleteByUnitId(String unitTemplateId);

    /**
     * <p>
     * 根据户型id判断这个户型是否有模板在使用
     * </p>
     * @param houseDesignId 户型id
     * @return 是否在使用中
     * @author: 王良俊
     */
    boolean checkIsUsing(String houseDesignId);

}
