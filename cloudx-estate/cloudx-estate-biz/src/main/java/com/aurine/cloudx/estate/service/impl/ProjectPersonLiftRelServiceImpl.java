package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.constant.enums.PassMacroIdEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightActiveTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectPersonLiftDTO;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectPersonLiftRelMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectDeviceLiftVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceRelVo;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: hjj
 * @Date: 2022/3/16 09:13
 * @Description: 人员电梯权限 基础 接口
 */
@Service
@Primary
@Slf4j
public class ProjectPersonLiftRelServiceImpl extends ServiceImpl<ProjectPersonLiftRelMapper, ProjectPersonLiftRel> implements ProjectPersonLiftRelService {

    @Resource
    private ProjectDeviceRelService projectDeviceRelService;
    @Resource
    private ProjectPersonLiftPlanRelService projectPersonLiftPlanRelService;
    @Resource
    @Lazy
    ProjectRightDeviceService projectRightDeviceServiceProxyImpl;
    @Resource
    ProjectPassPlanPolicyRelService projectPassPlanPolicyRelService;
    @Resource
    ProjectPassPlanService projectPassPlanService;
    @Resource
    ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    ProjectDeviceInfoService projectDeviceInfoService;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePersonDevice(ProjectPersonLiftDTO projectPersonLiftDTO) {

        /**
         * 基于需求原型 1.6 2020-07-03
         * 用户不选择过期时间时，自动设置过期时间未2199-01-01
         * @author: 王伟 2020-07-06
         */
        if (projectPersonLiftDTO.getExpTime() == null) {
            projectPersonLiftDTO.setExpTime(DateUtil.toLocalDateTime(DateUtil.parse("2199-01-01", "yyyy-MM-dd")));
        }

        List<ProjectPersonLiftRel> projectPersonLiftRelList = new ArrayList<>();
        ProjectPersonLiftRel projectPersonLiftRel;
        //下发变更
        //List<String> deviceIdList = new ArrayList<>();

        //删除旧数据
        this.deleteByPersonId(projectPersonLiftDTO.getPersonId());

        //如果是未配置过的用户，默认给激活状态
        if (StringUtil.isEmpty(projectPersonLiftDTO.getIsActive())) {
            projectPersonLiftDTO.setIsActive(PassRightActiveTypeEnum.ACTIVED.code);
        }

        List<ProjectDeviceLiftVo> lifts = projectPersonLiftDTO.getLifts();
        if(lifts != null){
            for (ProjectDeviceLiftVo lift:
            lifts) {
                if(lift.getChecked() == null || lift.getChecked().length == 0){
                    continue;
                }
                //deviceIdList.add(lift.getDeviceId());
                projectPersonLiftRel = new ProjectPersonLiftRel();
                BeanUtils.copyProperties(projectPersonLiftDTO, projectPersonLiftRel);
                projectPersonLiftRel.setDeviceId(lift.getDeviceId());
                projectPersonLiftRel.setStatus("1");
                projectPersonLiftRel.setEffTime(projectPersonLiftRel.getEffTime());
                projectPersonLiftRel.setExpTime(projectPersonLiftRel.getExpTime());
                projectPersonLiftRel.setFloors(JSON.toJSONString(lift.getChecked()));
                projectPersonLiftRelList.add(projectPersonLiftRel);

            }
        }
        if(projectPersonLiftRelList.size() > 0){
            this.saveBatch(projectPersonLiftRelList);
        }

        //projectRightDeviceService.saveRelationship(deviceIdList, projectPersonLiftDTO.getPersonId());

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePersonDevice(String personId, PersonTypeEnum personType, List<String> liftList, LocalDateTime effTime, LocalDateTime expTime) {

        List<ProjectPersonLiftRel> relList = new ArrayList<>();
        //删除旧数据
        this.deleteByPersonId(personId);

        if(CollUtil.isNotEmpty(liftList)){
            for (String id:
            liftList) {
                ProjectPersonLiftRel projectPersonLiftRel = new ProjectPersonLiftRel();
                projectPersonLiftRel.setIsActive(PassRightActiveTypeEnum.ACTIVED.code);
                projectPersonLiftRel.setPersonId(personId);
                projectPersonLiftRel.setPersonType(personType.code);
                projectPersonLiftRel.setDeviceId(id);
                projectPersonLiftRel.setStatus("1");
                projectPersonLiftRel.setEffTime(effTime);
                projectPersonLiftRel.setExpTime(expTime);
                relList.add(projectPersonLiftRel);
            }
        }

        return this.saveBatch(relList);
    }

    @Override
    public Boolean updateBatchPersonDevice(List<ProjectPersonLiftPlanRel> projectPersonLiftPlanRels, int projectId) {
        if (CollUtil.isEmpty(projectPersonLiftPlanRels)) {
            return true;
        }
        //添加新数据
        for (ProjectPersonLiftPlanRel projectPersonLiftPlanRel : projectPersonLiftPlanRels) {
            projectRightDeviceServiceProxyImpl.saveRelationshipProxy(null, projectPersonLiftPlanRel.getPersonId(), projectId);
        }
        return true;
    }

    @Override
    public boolean disablePassRight(String personType, String personId) {
        List<ProjectPersonLiftRel> relList = this.listByPersonId(personId);
        for (ProjectPersonLiftRel rel:
             relList) {
            rel.setIsActive(PassRightActiveTypeEnum.DISABLE.code);
        }
        projectPersonLiftPlanRelService.disablePerson(personId);
        if (CollUtil.isNotEmpty(relList)) {
            this.updateBatchById(relList);
        }
        return true;
    }

    /**
     * 根据人员ID 和 方案ID 获得该用户可以使用的电梯设备VO列表(根据规则)
     *
     * @param personId
     * @param planId
     * @return
     */
    @Override
    public List<ProjectPassDeviceVo> listLiftByPersonIdAndPlanId(String personId, String planId) {
        List<String> macroList = projectPassPlanPolicyRelService.listMacroIdListByPlanId(planId);
        if(macroList.contains(PassMacroIdEnum.UNIT_LIFT.name())){
            return baseMapper.liftListByPerson(
                    personId,
                    planId,
                    macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.UNIT_LIFT.name())),
                    true
            );
        }else {
            return new ArrayList<>();
        }
    }

    /**
     * 根据人员ID 和 方案ID 获得该用户可以使用的电梯设备VO列表(包含自选)
     *
     * @param personId
     * @param planId
     * @return
     */
    @Override
    public List<ProjectPassDeviceVo> listLiftByPersonId(String personId, String planId) {
        List<String> macroList = projectPassPlanPolicyRelService.listMacroIdListByPlanId(planId);
        if(!macroList.contains(PassMacroIdEnum.UNIT_LIFT.name())){
            macroList.add(PassMacroIdEnum.UNIT_LIFT.name());
        }
        return baseMapper.liftListByPerson(
                personId,
                planId,
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.UNIT_LIFT.name())),
                false
        );
    }

    @Override
    public boolean enablePassRight(String personType, String personId) {

        List<ProjectPersonLiftRel> relList = this.listByPersonId(personId);
        for (ProjectPersonLiftRel rel:
                relList) {
            rel.setIsActive(PassRightActiveTypeEnum.ACTIVED.code);
        }
        projectPersonLiftPlanRelService.enablePerson(personId);
        if (CollUtil.isNotEmpty(relList)) {
            this.updateBatchById(relList);
        }
        return true;
    }

    @Override
    public boolean deleteByPersonId(String personId) {
        return this.remove(new QueryWrapper<ProjectPersonLiftRel>().lambda().eq(ProjectPersonLiftRel::getPersonId, personId));
    }

    @Override
    public List<ProjectPersonLiftRel> listByPersonId(String personId) {
        return this.list(new QueryWrapper<ProjectPersonLiftRel>().lambda().eq(ProjectPersonLiftRel::getPersonId, personId));
    }
    @Override
    public List<String> childDeviceIdByLift(String personId){
        List<String> ids = new ArrayList<>();
        List<ProjectPersonLiftRel> liftRels = listByPersonId(personId);
        if(liftRels != null){
            for (ProjectPersonLiftRel liftRel:
            liftRels) {
                List<ProjectDeviceRelVo> deviceRelVoList = projectDeviceRelService.ListByDeviceIdAndDeviceType(liftRel.getDeviceId(), DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE);
                ids.addAll(deviceRelVoList.stream().map(ProjectDeviceRelVo::getDeviceId).collect(Collectors.toList()));
            }
        }
        return ids;
    }

    @Override
    public boolean refreshDeleteDevice(String deviceId) {
        return this.remove(new QueryWrapper<ProjectPersonLiftRel>().lambda().eq(ProjectPersonLiftRel::getDeviceId, deviceId));
    }

    @Override
    public boolean refreshDeleteDeviceAll(List<String> ids) {
        return this.remove(new QueryWrapper<ProjectPersonLiftRel>().lambda().in(ProjectPersonLiftRel::getDeviceId, ids));
    }

    /**
     * 添加设备后，更新项目下用户权限，并重新下发
     * 根据设备类型，分别获取固定的逻辑策略，并通过逻辑策略获取到使用该策略的计划，并通过计划、楼栋和单元找到符合该计划的人员，进行定量更新
     *
     * @param device
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean refreshAddDevice(ProjectDeviceInfo device) {
        log.info("刷新设备 {} 的通行权限信息", device.getDeviceName());
        List<ProjectPassPlan> planList = null;
        List<String> personIdList = new ArrayList<>();
        List<String> planIdList = new ArrayList<>();
        planList = this.projectPassPlanService.list();

        if (CollUtil.isNotEmpty(planList)) {
            personIdList = this.projectPersonDeviceService.findPerson(planList.stream().map(plan -> plan.getPlanId()).collect(Collectors.toList()), device.getBuildingId(), device.getUnitId(), null);
        }

        if (CollUtil.isEmpty(planList)) {
            return true;
        }

        if (CollUtil.isEmpty(personIdList)) {
            return true;
        }
        //乘梯识别终端
        List<String> liftChildDevice;
        List<String> liftIdList = new ArrayList<>();
        liftIdList.add(device.getDeviceId());
        liftChildDevice = projectDeviceInfoService.childDeviceIdByLift(liftIdList);
        //更新下发信息
        if(CollUtil.isNotEmpty(liftChildDevice)){
            for (String deviceId:
            liftChildDevice) {
                this.projectRightDeviceServiceProxyImpl.authNewDeviceById(deviceId, personIdList);
            }
        }


        return true;
    }
}
