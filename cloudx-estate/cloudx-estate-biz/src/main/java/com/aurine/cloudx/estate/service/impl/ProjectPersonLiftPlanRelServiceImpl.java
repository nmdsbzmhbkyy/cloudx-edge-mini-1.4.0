
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.entity.ProjectPersonLiftPlanRel;
import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.mapper.ProjectPersonLiftPlanRelMapper;
import com.aurine.cloudx.estate.service.ProjectPersonLiftPlanRelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>人员d电梯方案关系管理实现</p>
 *
 * @ClassName: ProjectPersonLiftPlanRelServiceImpl
 * @author: hjj
 * @date: 2022-03-15
 * @Copyright:
 */
@Service
@Slf4j
public class ProjectPersonLiftPlanRelServiceImpl extends ServiceImpl<ProjectPersonLiftPlanRelMapper, ProjectPersonLiftPlanRel> implements ProjectPersonLiftPlanRelService {


    @Override
    public boolean saveOrUpdateByPersonId(ProjectPersonPlanRel projectPersonPlanRel) {
        return false;
    }

    @Override
    public boolean checkChange(String personId, String planId) {
        return false;
    }

    @Override
    public String getPlanIdByPersonId(String personId) {
        return this.baseMapper.getPlanIdByPersonId(personId);
    }

    @Override
    public List<ProjectPersonPlanRel> listPersonPlanRelExpDate() {
        return null;
    }

    @Override
    public ProjectPersonLiftPlanRel getPoByPersonId(String personId) {
        return null;
    }

    @Override
    public boolean deleteByPersonId(String personId) {
        return this.remove(new QueryWrapper<ProjectPersonLiftPlanRel>().lambda().eq(ProjectPersonLiftPlanRel::getPersonId, personId));
    }

    @Override
    public List<ProjectPersonLiftPlanRel> listByPlanId(String planId) {
        return this.list(new QueryWrapper<ProjectPersonLiftPlanRel>().lambda().eq(ProjectPersonLiftPlanRel::getLiftPlanId, planId));
    }

    @Override
    public boolean checkUsed(String planId) {
        int count = this.count(new QueryWrapper<ProjectPersonLiftPlanRel>().lambda().eq(ProjectPersonLiftPlanRel::getLiftPlanId, planId));
        return count != 0;
    }

    @Override
    public boolean enablePerson(String personId) {
        return changePersonActive(personId, "1");
    }

    @Override
    public boolean disablePerson(String personId) {
        return changePersonActive(personId, "0");
    }

    @Override
    public void initTodayTask() {

    }

    @Override
    public void handleAllTimeOutRight(String timeStr) {

    }

    /**
     * 激活或禁用人员权限关系
     *
     * @param personId
     * @param active
     * @return
     */
    private boolean changePersonActive(String personId, String active) {
        List<ProjectPersonLiftPlanRel> personPlanRelList = this.list(new QueryWrapper<ProjectPersonLiftPlanRel>().lambda().eq(ProjectPersonLiftPlanRel::getPersonId, personId));

        if (CollUtil.isNotEmpty(personPlanRelList)) {
            personPlanRelList.get(0).setIsActive(active);
            return this.updateById(personPlanRelList.get(0));
        } else {
            return true;
        }
    }
}
