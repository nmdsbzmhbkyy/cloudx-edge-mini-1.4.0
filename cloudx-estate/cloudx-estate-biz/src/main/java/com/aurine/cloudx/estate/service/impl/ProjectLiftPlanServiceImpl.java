package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.LiftMacroIdEnum;
import com.aurine.cloudx.estate.constant.enums.PassMacroIdEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightActiveTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectLiftPlanMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectDeviceLiftVo;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.aurine.cloudx.estate.vo.ProjectLiftPlanVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>电梯通行方案实现</p>
 *
 * @ClassName: ProjectLiftPlanServiceImpl
 * @author: 陈喆 <chenz'@aurine.cn>
 * @date: 2020/5/22 8:34
 * @Copyright:
 */
@Service
public class ProjectLiftPlanServiceImpl extends ServiceImpl<ProjectLiftPlanMapper, ProjectLiftPlan> implements ProjectLiftPlanService {

    @Autowired
    private ProjectLiftPlanPolicyRelService projectLiftPlanPolicyRelService;

    @Autowired
    ProjectPersonLiftRelService projectPersonLiftRelService;

    @Autowired
    ProjectPersonLiftPlanRelService projectPersonLiftPlanRelService;

    @Autowired
    ProjectDeviceInfoService projectDeviceInfoService;


    /**
     * 验证通行方案是否重名
     *
     * @param name
     * @return
     */
    private boolean checkPlanName(String name) {
        return this.count(new QueryWrapper<ProjectLiftPlan>().lambda().eq(ProjectLiftPlan::getPlanName, name)) >= 1;
    }

    /**
     * 验证通行方案是否重名
     *
     * @param name
     * @return
     */
    private boolean checkPlanName(String name, String uid) {
        return this.count(new QueryWrapper<ProjectLiftPlan>().lambda().eq(ProjectLiftPlan::getPlanName, name).notLike(StringUtil.isNotEmpty(uid), ProjectLiftPlan::getPlanId, uid)) >= 1;
    }

    /**
     * 保存
     *
     * @param projectLiftPlanVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ProjectLiftPlanVo projectLiftPlanVo) {
        //初始化
        String planId = UUID.randomUUID().toString().replace("-", "");

        //方案名称验证(非系统方案)
        if (!"1".equalsIgnoreCase(projectLiftPlanVo.getIsDefault()) && this.checkPlanName(projectLiftPlanVo.getPlanName(), planId)) {
            throw new RuntimeException("与已有方案名称重复");
        }


        //保存逻辑策略
        projectLiftPlanPolicyRelService.saveBatchLogic(projectLiftPlanVo.getMacroIdArray(), planId);


//        //保存物理策略
//        projectLiftPlanPolicyRelService.saveBatchPhysical(ProjectLiftPlanVo.getFloorArray(), planId);

        //保存通行方案
        ProjectLiftPlan ProjectLiftPlan = new ProjectLiftPlan();
        BeanUtils.copyProperties(projectLiftPlanVo, ProjectLiftPlan);

        if (StringUtils.isEmpty(ProjectLiftPlan.getIsDefault())) {
            ProjectLiftPlan.setIsDefault("0"); // 所有自己创建的方案都不为默认方案
        }

        ProjectLiftPlan.setPlanId(planId);

        return this.save(ProjectLiftPlan);
    }


    /**
     * 修改
     *
     * @param ProjectLiftPlanVo
     * @return
     */
    @Override
//    @Transactional(rollbackFor = Exception.class)
    public boolean update(ProjectLiftPlanVo ProjectLiftPlanVo) {
        //初始化
        String planId = ProjectLiftPlanVo.getPlanId();

        //方案名称验证
        if (!"1".equalsIgnoreCase(ProjectLiftPlanVo.getIsDefault()) && this.checkPlanName(ProjectLiftPlanVo.getPlanName(), planId)) {
            throw new RuntimeException("与已有方案名称重复");
        }
        //是否变更用户电梯关系
        boolean isChange = false;
        String[] oldMacorIdArray = projectLiftPlanPolicyRelService.listMacroIdArrayByPlanId(planId);
        if(oldMacorIdArray[0].equals(ProjectLiftPlanVo.getMacroIdArray()[0])){
            isChange = true;
        }

        //保存逻辑策略
        projectLiftPlanPolicyRelService.saveBatchLogic(ProjectLiftPlanVo.getMacroIdArray(), planId);

        //保存物理策略
        //projectLiftPlanPolicyRelService.saveBatchPhysical(ProjectLiftPlanVo.getFloorArray(), planId);

        //保存通行方案
        ProjectLiftPlan projectLiftPlan = new ProjectLiftPlan();
        BeanUtils.copyProperties(ProjectLiftPlanVo, projectLiftPlan);
        projectLiftPlan.setPlanId(planId);
        this.updateById(projectLiftPlan);


        //更新通行方案后变更设备，并下发

        //使用当前方案的personList
        List<ProjectPersonLiftPlanRel> planRelList = this.projectPersonLiftPlanRelService.listByPlanId(planId);
        //方案变更，自动关联用户电梯关系
        List<ProjectPersonLiftRel> projectPersonLiftRelList = new ArrayList<>();
        ProjectPersonLiftRel projectPersonLiftRel;
        if(isChange){
            for (ProjectPersonLiftPlanRel rel:
            planRelList) {
                //删除旧数据
                this.projectPersonLiftRelService.deleteByPersonId(rel.getPersonId());
                List<ProjectDeviceLiftVo> lifts = projectDeviceInfoService.getLiftsWithFloor(rel.getLiftPlanId(),rel.getPersonType(),rel.getPersonId());
                if(lifts != null){
                    for (ProjectDeviceLiftVo lift:
                            lifts) {
                        if(lift.getChecked() == null || lift.getChecked().length == 0){
                            continue;
                        }
                        //deviceIdList.add(lift.getDeviceId());
                        projectPersonLiftRel = new ProjectPersonLiftRel();
                        projectPersonLiftRel.setBuildingId(lift.getBuildingId());
                        projectPersonLiftRel.setIsActive(PassRightActiveTypeEnum.ACTIVED.code);
                        projectPersonLiftRel.setDeviceId(lift.getDeviceId());
                        projectPersonLiftRel.setStatus("1");
                        projectPersonLiftRel.setEffTime(projectPersonLiftRel.getEffTime());
                        projectPersonLiftRel.setExpTime(projectPersonLiftRel.getExpTime());
                        projectPersonLiftRel.setFloors(JSON.toJSONString(lift.getChecked()));
                        projectPersonLiftRelList.add(projectPersonLiftRel);

                    }
                }
                if(projectPersonLiftRelList.size() > 0){
                    this.projectPersonLiftRelService.saveBatch(projectPersonLiftRelList);
                }
            }
        }
        /**
         * 调用批量更新方法，提升性能
         */
        projectPersonLiftRelService.updateBatchPersonDevice(planRelList, ProjectContextHolder.getProjectId());
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
            ProjectLiftPlanVo projectLiftPlanVo = new ProjectLiftPlanVo();

            projectLiftPlanVo.setPlanName("系统方案");
            projectLiftPlanVo.setPlanObject("1");
            projectLiftPlanVo.setIsDefault("1");
            String[] macroArray = new String[]{LiftMacroIdEnum.FLOOR_BELONG_HOUSE.name()};
            projectLiftPlanVo.setMacroIdArray(macroArray);
            projectLiftPlanVo.setProjectId(projectId);
            projectLiftPlanVo.setTenantId(tenantId);

            this.save(projectLiftPlanVo);

            //生成员工方案
            projectLiftPlanVo = new ProjectLiftPlanVo();

            projectLiftPlanVo.setPlanName("系统方案");
            projectLiftPlanVo.setPlanObject("2");
            projectLiftPlanVo.setIsDefault("1");

            macroArray = new String[]{LiftMacroIdEnum.ALL_FLOOR_ANYWHERE.name()};
            projectLiftPlanVo.setMacroIdArray(macroArray);

            projectLiftPlanVo.setProjectId(projectId);
            projectLiftPlanVo.setTenantId(tenantId);

            this.save(projectLiftPlanVo);
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
        List<ProjectLiftPlan> defaultPlanList = this.list(new QueryWrapper<ProjectLiftPlan>().lambda().eq(ProjectLiftPlan::getIsDefault, "1"));
        if (CollUtil.isNotEmpty(defaultPlanList)) {
            for (ProjectLiftPlan plan : defaultPlanList) {
                projectLiftPlanPolicyRelService.remove(new QueryWrapper<ProjectLiftPlanPolicyRel>().lambda().eq(ProjectLiftPlanPolicyRel::getPlanId, plan.getPlanId()));
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
    public ProjectLiftPlan getDefaultPlan(PersonTypeEnum personTypeEnum) {
        List<ProjectLiftPlan> defaultPlanList = this.list(new QueryWrapper<ProjectLiftPlan>()
                .lambda().eq(ProjectLiftPlan::getIsDefault, "1")
                .eq(ProjectLiftPlan::getPlanObject, personTypeEnum.code));

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
    public ProjectLiftPlanVo getVo(String id) {
        ProjectLiftPlanVo vo = new ProjectLiftPlanVo();

        ProjectLiftPlan passPlan = this.getById(id);
        BeanUtils.copyProperties(passPlan, vo);


        //获取逻辑数组
        String[] macroArray = projectLiftPlanPolicyRelService.listMacroIdArrayByPlanId(id);
        vo.setMacroIdArray(macroArray);

        //转换设备对象（逻辑选中，不可取消/ 物理选中，可取消）
        String[] phyDeviceIdArray = projectLiftPlanPolicyRelService.listPhysicalFloorArrayByPlanId(id);
        vo.setFloorArray(phyDeviceIdArray);

        return vo;
    }

//    /**
//     * 根据方案,获取该方案下所有可以使用的设备
//     *
//     * @param planId
//     * @param buildingId
//     * @param unitId
//     * @return
//     */
//    @Override
//    public List<ProjectPassDeviceVo> listDeviceByPlanId(String planId, String buildingId, String unitId) {
//        ProjectLiftPlan plan = this.getById(planId);
//        if (plan == null) {
//            return new ArrayList<>();
//        }
//        return projectLiftPlanPolicyRelService.listDeviceByPlan(plan, buildingId, unitId);
//    }

    /**
     * 根据方案类型（方案对象） 获取方案列表，默认方案在前，其他的按照时间排序
     *
     * @param planObject
     * @return
     */
    @Override
    public List<ProjectLiftPlan> listByType(String planObject) {
        return this.list(new QueryWrapper<ProjectLiftPlan>().lambda().eq(ProjectLiftPlan::getPlanObject, planObject));
    }

    @Override
    public ProjectLiftPlan getdefaultPass() {
        List<ProjectLiftPlan> projectLiftPlan = this.list(new QueryWrapper<ProjectLiftPlan>().lambda().eq(ProjectLiftPlan::getIsDefault, "1"));
        if (CollectionUtil.isNotEmpty(projectLiftPlan)) {
            return projectLiftPlan.get(0);
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

        ProjectLiftPlan plan = this.getById(planId);
        if ("1".equals(plan.getIsDefault())) {
            throw new RuntimeException("系统方案禁止删除");
        }

        boolean used = projectPersonLiftPlanRelService.checkUsed(planId);
        if (used) {
            throw new RuntimeException("当前方案正在被使用，无法删除");
        }

        //删除关联关系
        projectLiftPlanPolicyRelService.removeBathLogic(planId);
        projectLiftPlanPolicyRelService.removeBathPhysical(planId);

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
    public List<ProjectLiftPlan> listByMacro(PassMacroIdEnum macroEnum) {
        List<ProjectLiftPlan> planList = new ArrayList<>();
        String[] planIdArray = this.projectLiftPlanPolicyRelService.listPlanIdByMacro(macroEnum);

        if (planIdArray != null && planIdArray.length >= 1) {
            planList = this.list(new QueryWrapper<ProjectLiftPlan>().lambda().in(ProjectLiftPlan::getPlanId, planIdArray));
        }
        return planList;
    }


}
