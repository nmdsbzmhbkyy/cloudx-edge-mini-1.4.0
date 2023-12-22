
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PassMacroIdEnum;
import com.aurine.cloudx.estate.entity.ProjectPassPlan;
import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.mapper.ProjectPassPlanMapper;
import com.aurine.cloudx.estate.service.ProjectPassPlanPolicyRelService;
import com.aurine.cloudx.estate.service.ProjectPassPlanService;
import com.aurine.cloudx.estate.service.ProjectPersonDeviceService;
import com.aurine.cloudx.estate.service.ProjectPersonPlanRelService;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.aurine.cloudx.estate.vo.ProjectPassPlanVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>通行方案实现</p>
 *
 * @ClassName: ProjectPassPlanServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 8:34
 * @Copyright:
 */
@Service
public class ProjectPassPlanServiceImpl extends ServiceImpl<ProjectPassPlanMapper, ProjectPassPlan> implements ProjectPassPlanService {


    @Autowired
    private ProjectPassPlanPolicyRelService projectPassPlanPolicyRelService;

    @Autowired
    private ProjectPersonPlanRelService projectPersonPlanRelService;

    @Autowired
    private ProjectPersonDeviceService projectPersonDeviceService;



    /**
     * 获取通行方案VO
     *
     * @param id
     * @return
     */
    @Override
    public ProjectPassPlanVo getVo(String id) {
        ProjectPassPlanVo vo = new ProjectPassPlanVo();

        ProjectPassPlan passPlan = this.getById(id);
        BeanUtils.copyProperties(passPlan, vo);

        // 获取方案所选设备
        List<ProjectPassDeviceVo> deviceVoList = projectPassPlanPolicyRelService.listDeviceByPlan(passPlan, "", "");
        vo.setDeviceList(deviceVoList);

        //获取逻辑数组
        String[] macroArray = projectPassPlanPolicyRelService.listMacroIdArrayByPlanId(id);
        vo.setMacroIdArray(macroArray);

        //转换设备对象（逻辑选中，不可取消/ 物理选中，可取消）
        String[] phyDeviceIdArray = projectPassPlanPolicyRelService.listPhysicalDeviceIdArrayByPlanId(id);
        vo.setDeviceIdArray(phyDeviceIdArray);

        return vo;
    }

    /**
     * 根据方案,获取该方案下所有可以使用的设备
     *
     * @param planId
     * @param buildingId
     * @param unitId
     * @return
     */
    @Override
    public List<ProjectPassDeviceVo> listDeviceByPlanId(String planId, String buildingId, String unitId) {
        ProjectPassPlan plan = this.getById(planId);
        if (plan == null) {
            return new ArrayList<>();
        }
        return projectPassPlanPolicyRelService.listDeviceByPlan(plan, buildingId, unitId);
    }

    /**
     * 根据方案类型（方案对象） 获取方案列表，默认方案在前，其他的按照时间排序
     *
     * @param planObject
     * @return
     */
    @Override
    public List<ProjectPassPlan> listByType(String planObject) {
        return this.list(new QueryWrapper<ProjectPassPlan>().lambda().eq(ProjectPassPlan::getPlanObject, planObject));
    }

    @Override
    public ProjectPassPlan getdefaultPass() {
         List<ProjectPassPlan> projectPassPlan = this.list(new QueryWrapper<ProjectPassPlan>().lambda().eq(ProjectPassPlan::getIsDefault, "1"));
         if (CollectionUtil.isNotEmpty(projectPassPlan)) {
            return projectPassPlan.get(0);
         }
        return null;
    }

    /**
     * 删除方案
     *
     * @param planId
     * @return
     */
    @Override
    public boolean delete(String planId) {

        //验证能否删除（默认的不允许，已经被使用的不允许）

        ProjectPassPlan plan = this.getById(planId);
        if (plan.getIsDefault().equals("1")) {
            throw new RuntimeException("系统方案禁止删除");
        }

        boolean used = projectPersonPlanRelService.checkUsed(planId);
        if (used) {
            throw new RuntimeException("当前方案正在被使用，无法删除");
        }

        //删除关联关系
        projectPassPlanPolicyRelService.removeBathLogic(planId);
        projectPassPlanPolicyRelService.removeBathPhysical(planId);

        //删除方案
        return this.removeById(planId);
    }

    /**
     * 获取使用了指定逻辑策略宏的通行方案
     *
     * @param macroEnum
     * @return
     */
    @Override
    public List<ProjectPassPlan> listByMacro(PassMacroIdEnum macroEnum) {
        List<ProjectPassPlan> planList = new ArrayList<>();
        String[] planIdArray = this.projectPassPlanPolicyRelService.listPlanIdByMacro(macroEnum);

        if (planIdArray != null && planIdArray.length >= 1) {
            planList = this.list(new QueryWrapper<ProjectPassPlan>().lambda().in(ProjectPassPlan::getPlanId, planIdArray));
        }
        return planList;
    }



}
