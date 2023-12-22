
package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.mapper.ProjectHouseBatchAddTemplateMapper;
import com.aurine.cloudx.open.origin.entity.ProjectHouseBatchAddTemplate;
import com.aurine.cloudx.open.origin.service.ProjectHouseBatchAddTemplateService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 单元模板
 *
 * @author 王伟
 * @date 2020-06-04 15:36:50
 */
@Service
public class ProjectHouseBatchAddTemplateServiceImpl extends ServiceImpl<ProjectHouseBatchAddTemplateMapper, ProjectHouseBatchAddTemplate> implements ProjectHouseBatchAddTemplateService {

    /**
     * 根据unitId批量删除楼栋模板
     *
     * @param unitTemplateId
     * @return
     */
    @Override
    public boolean deleteByUnitId(String unitTemplateId) {
        return this.remove(new QueryWrapper<ProjectHouseBatchAddTemplate>().lambda().eq(ProjectHouseBatchAddTemplate::getUnitTemplateId, unitTemplateId));
    }

    @Override
    public boolean checkIsUsing(String houseDesignId) {
        List<ProjectHouseBatchAddTemplate> houseBatchAddTemplateList = this.list(new QueryWrapper<ProjectHouseBatchAddTemplate>().lambda().eq(ProjectHouseBatchAddTemplate::getHouseDesignId, houseDesignId));
        return houseBatchAddTemplateList.size() > 0;
    }

}
