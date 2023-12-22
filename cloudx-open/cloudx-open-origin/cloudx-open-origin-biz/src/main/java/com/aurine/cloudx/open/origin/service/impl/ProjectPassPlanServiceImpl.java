package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.common.entity.vo.PassPlanVo;
import com.aurine.cloudx.open.origin.constant.enums.PassMacroIdEnum;
import com.aurine.cloudx.open.origin.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.open.origin.entity.ProjectPassPlan;
import com.aurine.cloudx.open.origin.entity.ProjectPassPlanPolicyRel;
import com.aurine.cloudx.open.origin.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.open.origin.mapper.ProjectPassPlanMapper;
import com.aurine.cloudx.open.origin.service.ProjectPassPlanPolicyRelService;
import com.aurine.cloudx.open.origin.service.ProjectPassPlanService;
import com.aurine.cloudx.open.origin.service.ProjectPersonDeviceService;
import com.aurine.cloudx.open.origin.service.ProjectPersonPlanRelService;
import com.aurine.cloudx.open.origin.vo.ProjectPassDeviceVo;
import com.aurine.cloudx.open.origin.vo.ProjectPassPlanVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 验证通行方案是否重名
     *
     * @param name
     * @return
     */
    private boolean checkPlanName(String name) {
        return this.count(new QueryWrapper<ProjectPassPlan>().lambda().eq(ProjectPassPlan::getPlanName, name)) >= 1;
    }

    /**
     * 验证通行方案是否重名
     *
     * @param name
     * @return
     */
    private boolean checkPlanName(String name, String uid) {
        return this.count(new QueryWrapper<ProjectPassPlan>().lambda().eq(ProjectPassPlan::getPlanName, name).notLike(StringUtil.isNotEmpty(uid), ProjectPassPlan::getPlanId, uid)) >= 1;
    }

    /**
     * 保存
     *
     * @param projectPassPlanVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ProjectPassPlanVo projectPassPlanVo) {
        //初始化
        String planId = UUID.randomUUID().toString().replace("-", "");

        //方案名称验证(非系统方案)
        if (!"1".equalsIgnoreCase(projectPassPlanVo.getIsDefault()) && this.checkPlanName(projectPassPlanVo.getPlanName(), planId)) {
            throw new RuntimeException("与已有方案名称重复");
        }


        //保存逻辑策略
        projectPassPlanPolicyRelService.saveBatchLogic(projectPassPlanVo.getMacroIdArray(), planId);

        //获取逻辑设备列表，删除物理策略中的并集部分（确保设备归属变更后不需要去改动物理设备逻辑）

        //保存物理策略
        projectPassPlanPolicyRelService.saveBatchPhysical(projectPassPlanVo.getDeviceIdArray(), planId);

        //保存通行方案
        ProjectPassPlan projectPassPlan = new ProjectPassPlan();
        BeanUtils.copyProperties(projectPassPlanVo, projectPassPlan);

        if (StringUtils.isEmpty(projectPassPlan.getIsDefault())) {
            projectPassPlan.setIsDefault("0"); // 所有自己创建的方案都不为默认方案
        }

        projectPassPlan.setPlanId(planId);

        return this.save(projectPassPlan);
    }


    /**
     * 修改
     *
     * @param projectPassPlanVo
     * @return
     */
    @Override
//    @Transactional(rollbackFor = Exception.class)
    public boolean update(ProjectPassPlanVo projectPassPlanVo) {
        //初始化
        String planId = projectPassPlanVo.getPlanId();

        //方案名称验证
        if (!"1".equalsIgnoreCase(projectPassPlanVo.getIsDefault()) && this.checkPlanName(projectPassPlanVo.getPlanName(), planId)) {
            throw new RuntimeException("与已有方案名称重复");
        }

        //保存逻辑策略
        projectPassPlanPolicyRelService.saveBatchLogic(projectPassPlanVo.getMacroIdArray(), planId);

        //保存物理策略
        projectPassPlanPolicyRelService.saveBatchPhysical(projectPassPlanVo.getDeviceIdArray(), planId);

        //保存通行方案
        ProjectPassPlan projectPassPlan = new ProjectPassPlan();
        BeanUtils.copyProperties(projectPassPlanVo, projectPassPlan);
        projectPassPlan.setPlanId(planId);
        this.updateById(projectPassPlan);


        //更新通行方案后变更设备，并下发

        //使用当前方案的personList
        List<ProjectPersonPlanRel> planRelList = projectPersonPlanRelService.listByPlanId(planId);


        /**
         * 调用批量更新方法，提升性能
         */
        projectPersonDeviceService.updateBatchPersonDevice(planRelList, ProjectContextHolder.getProjectId());
        return true;

//        //重新调用保存方法，重写设备权限
//        ProjectPersonDeviceDTO dto;
//        List<ProjectPassDeviceVo> deviveVoList;
//
//        List<String> deviceIdList;
//        for (ProjectPersonPlanRel rel : planRelList) {
//            dto = projectPersonDeviceService.getDTOByPersonId(rel.getPersonId());
//            deviveVoList = dto.getDeviceList();//获取的原有设备
//
//            if (CollectionUtil.isNotEmpty(deviveVoList)) {
//
//                // 只保留用户手动选择的设备
//                deviceIdList = deviveVoList.stream().filter(device -> !device.getDisable()).map(ProjectPassDeviceVo::getDeviceId).collect(Collectors.toList());
//                dto.setDeviceIdArray(deviceIdList.toArray(new String[deviceIdList.size()]));//选中的设备
//            }
//            projectPersonDeviceService.savePersonDevice(dto);//自动根据方案重载设备权限
//        }
//
//        return true;
    }


    /**
     * 根据默认配置生成方案
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createByDefault(int projectId, int tenantId) {
        //检查当前项目是否已存在方案
        if (count() == 0) {
            //生成住户方案
            ProjectPassPlanVo projectPassPlanVo = new ProjectPassPlanVo();

            projectPassPlanVo.setPlanName("系统方案");
            projectPassPlanVo.setPlanObject("1");
            projectPassPlanVo.setIsDefault("1");
            String[] macroArray = new String[]{PassMacroIdEnum.GATE.name(), PassMacroIdEnum.UNIT_LADDER.name()};
            projectPassPlanVo.setMacroIdArray(macroArray);
            projectPassPlanVo.setProjectId(projectId);
            projectPassPlanVo.setTenantId(tenantId);

            this.save(projectPassPlanVo);

            //生成员工方案
            projectPassPlanVo = new ProjectPassPlanVo();

            projectPassPlanVo.setPlanName("系统方案");
            projectPassPlanVo.setPlanObject("2");
            projectPassPlanVo.setIsDefault("1");

            macroArray = new String[]{PassMacroIdEnum.GATE.name()};
            projectPassPlanVo.setMacroIdArray(macroArray);

            projectPassPlanVo.setProjectId(projectId);
            projectPassPlanVo.setTenantId(tenantId);

            this.save(projectPassPlanVo);
        }

        return true;
    }

    /**
     * 清空默认方案
     *
     * @return
     */
    @Override
    public boolean clearDefaultPlan() {
        //获取到当前项目的默认方案
        List<ProjectPassPlan> defaultPlanList = this.list(new QueryWrapper<ProjectPassPlan>().lambda().eq(ProjectPassPlan::getIsDefault, "1"));
        if (CollUtil.isNotEmpty(defaultPlanList)) {
            for (ProjectPassPlan plan : defaultPlanList) {
                projectPassPlanPolicyRelService.remove(new QueryWrapper<ProjectPassPlanPolicyRel>().lambda().eq(ProjectPassPlanPolicyRel::getPlanId, plan.getPlanId()));
            }
        }

        return true;
    }

    /**
     * 获取当前项目的默认方案
     *
     * @param personTypeEnum
     * @return
     */
    @Override
    public ProjectPassPlan getDefaultPlan(PersonTypeEnum personTypeEnum) {
        List<ProjectPassPlan> defaultPlanList = this.list(new QueryWrapper<ProjectPassPlan>()
                .lambda().eq(ProjectPassPlan::getIsDefault, "1")
                .eq(ProjectPassPlan::getPlanObject, personTypeEnum.code));

        if (CollUtil.isNotEmpty(defaultPlanList)) {
            return defaultPlanList.get(0);
        }
        return null;
    }


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
        List<ProjectPassDeviceVo> deviceVoList = projectPassPlanPolicyRelService.listPhysicalDeviceListByPlanId(passPlan.getPlanId());
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
        if ("1".equals(plan.getIsDefault())) {
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

    @Override
    public Page<PassPlanVo> page(Page page, PassPlanVo vo) {
        ProjectPassPlan po = new ProjectPassPlan();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }

}
