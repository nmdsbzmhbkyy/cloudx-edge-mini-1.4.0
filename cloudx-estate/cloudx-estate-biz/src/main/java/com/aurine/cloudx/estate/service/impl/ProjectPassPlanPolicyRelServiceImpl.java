
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.estate.constant.enums.PassMacroIdEnum;
import com.aurine.cloudx.estate.constant.enums.PassPlanPolicyTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectPassPlanPolicyRelMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoFormVo;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 策略方案关联服务
 *
 * @author pigx code generator
 * @date 2020-05-20 15:54:21
 */
@Service
@Slf4j
public class ProjectPassPlanPolicyRelServiceImpl extends ServiceImpl<ProjectPassPlanPolicyRelMapper, ProjectPassPlanPolicyRel> implements ProjectPassPlanPolicyRelService {

    @Autowired
    private ProjectLogicPassPolicyService projectLogicPassPolicyService;
    @Autowired
    private ProjectPhysicalPassPolicyService projectPhysicalPassPolicyService;
    @Autowired
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Autowired
    private ProjectDeviceInfoService projectDeviceInfoService;

    /**
     * 根据方案ID,获取该方案下所有可以使用的设备
     *
     * @param projectPassPlan
     * @return
     */
    @Override
    public List<ProjectPassDeviceVo> listDeviceByPlan(ProjectPassPlan projectPassPlan, String buildingId, String unitId) {
        //申明
        List<ProjectPassDeviceVo> deviceVoList = new ArrayList<>();

        ProjectDeviceInfoFormVo projectDeviceInfoFormVo;
        List<ProjectDeviceInfo> deviceInfoList;

        //初始化
//        if (StringUtils.isEmpty(buildingId)) {//传参为空，则检查方案是否有给值
//            buildingId = projectPassPlan.getBuildingId();
//
//        }
//        if (StringUtils.isEmpty(unitId)) {//传参为空，则检查方案是否有给值
//            unitId = projectPassPlan.getUnitId();
//        }


        //获取逻辑策略
        String[] macroArray = this.listMacroIdArrayByPlanId(projectPassPlan.getPlanId());
        if (macroArray != null && macroArray.length > 0) {

            for (int i = 0; i < macroArray.length; i++) {
                //全部区口
                if (PassMacroIdEnum.GATE.toString().equals(macroArray[i])) {

                    deviceInfoList = projectDeviceInfoProxyService.listAllGateDevice();
                    this.deviceInfoToVo(deviceVoList, deviceInfoList, true);

                    //所有公共区口以及用户所在区域的区口机
                } else if (PassMacroIdEnum.FRAME_GATE.toString().equals(macroArray[i])) {
                    /**
                     * 增加新的逻辑策略，所有公共区口以及用户所在区域的区口机
                     * @author: 王伟 2020-07-03
                     * */

                    deviceInfoList = projectDeviceInfoProxyService.listPublicGateAndGateByFrameDevice(buildingId);
                    this.deviceInfoToVo(deviceVoList, deviceInfoList, true);

                    //所在楼栋梯口
                } else if (PassMacroIdEnum.BUILDING_LADDER.toString().equals(macroArray[i])) {

                    //buildingId非空状态下获取对应设备数据
                    if (StringUtils.isNotEmpty(buildingId)) {
                        deviceInfoList = projectDeviceInfoProxyService.listLadderDevice(buildingId, "");
                        this.deviceInfoToVo(deviceVoList, deviceInfoList, true);
                    }


                    //所在楼栋单元梯口
                } else if (PassMacroIdEnum.UNIT_LADDER.toString().equals(macroArray[i])) {
                    //unitId非空状态下获取对应设备数据
                    if (StringUtils.isNotEmpty(unitId)) {
                        deviceInfoList = projectDeviceInfoProxyService.listLadderDevice("", unitId);
                        this.deviceInfoToVo(deviceVoList, deviceInfoList, true);
                    }
                } else {
                    throw new RuntimeException("无法匹配宏类型：" + macroArray[i]);
                }
            }

        }

        //获取物理策略设备数组
        String[] deviceIdArray = this.listPhysicalDeviceIdArrayByPlanId(projectPassPlan.getPlanId());
        if (deviceIdArray != null && deviceIdArray.length > 0) {
            deviceInfoList = projectDeviceInfoService.listByIds(Arrays.asList(deviceIdArray));
            this.deviceInfoToVo(deviceVoList, deviceInfoList, false);
        }

        return deviceVoList;
    }

    /**
     * 通过方案ID，获取该方案的逻辑策略的宏类型数组
     *
     * @param planId
     * @return
     */
    @Override
    public String[] listMacroIdArrayByPlanId(String planId) {
        //获取逻辑策略列表
        List<ProjectLogicPassPolicy> logicList = this.listLogic(planId);
        if (CollectionUtil.isEmpty(logicList)) {
            return new String[0];
        } else {
            List<String> macroIdList = logicList.stream().map(e -> e.getMacroId()).collect(Collectors.toList());
            return macroIdList.toArray(new String[macroIdList.size()]);
        }
    }

    /**
     * 通过方案id获取方案宏列表
     *
     * @param planId
     * @return
     */
    @Override
    public List<String> listMacroIdListByPlanId(String planId) {
        return this.baseMapper.listMacroByPlan(planId);
    }

    /**
     * 通过personId获取该用户使用方案的宏列表
     *
     * @param personId
     * @return
     */
    @Override
    public List<String> listMacroIdListByPersonId(String personId) {
        return this.baseMapper.listMacroByPerson(personId);
    }

    /**
     * 通过方案ID，获取该方案的物理策略的设备ID数组
     *
     * @param planId
     * @return
     */
    @Override
    public String[] listPhysicalDeviceIdArrayByPlanId(String planId) {
        //获取物理策略
        List<ProjectPhysicalPassPolicy> physicaList = this.listPhysical(planId);
        if (CollectionUtil.isEmpty(physicaList)) {
            return new String[0];
        } else {
            List<String> deviceIdList = physicaList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());
            return deviceIdList.toArray(new String[deviceIdList.size()]);
        }
    }

    /**
     * 获取方案下选择的物理设备
     *
     * @param planId
     * @return
     */
    @Override
    public List<ProjectPassDeviceVo> listPhysicalDeviceListByPlanId(String planId) {
        //申明
        List<ProjectPassDeviceVo> deviceVoList = new ArrayList<>();

        List<ProjectDeviceInfo> deviceInfoList;


        //获取物理策略设备数组
        String[] deviceIdArray = this.listPhysicalDeviceIdArrayByPlanId(planId);
        if (deviceIdArray != null && deviceIdArray.length > 0) {
            deviceInfoList = projectDeviceInfoService.listByIds(Arrays.asList(deviceIdArray));
            this.deviceInfoToVo(deviceVoList, deviceInfoList, false);
        }

        return deviceVoList;
    }

    /**
     * 通过逻辑宏名称，获取使用该宏策略的方案ID
     *
     * @param macroIdEnum
     * @return
     */
    @Override
    public String[] listPlanIdByMacro(PassMacroIdEnum macroIdEnum) {
        List<ProjectLogicPassPolicy> logicList = this.projectLogicPassPolicyService.list(new QueryWrapper<ProjectLogicPassPolicy>().lambda().eq(ProjectLogicPassPolicy::getMacroId, macroIdEnum.name()));

        if (CollectionUtil.isEmpty(logicList)) {
            return new String[0];
        } else {
            List<String> policyIdList = logicList.stream().map(e -> e.getPolicyId()).collect(Collectors.toList());
            List<ProjectPassPlanPolicyRel> passPlanPolicyRelList = list(new QueryWrapper<ProjectPassPlanPolicyRel>().lambda().in(ProjectPassPlanPolicyRel::getPolicyId, policyIdList));

            if (CollectionUtil.isEmpty(passPlanPolicyRelList)) {
                return new String[0];
            } else {
                List<String> planIdList = passPlanPolicyRelList.stream().map(e -> e.getPlanId()).collect(Collectors.toList());
                return planIdList.toArray(new String[planIdList.size()]);
            }
        }
    }

    /**
     * 逻辑方案策略与关联表批量存储
     *
     * @param macroIdArray
     * @param planId
     * @return
     */
    @Override
    public boolean saveBatchLogic(String[] macroIdArray, String planId) {
        String policyId;
        List<ProjectLogicPassPolicy> projectLogicPassPolicyList = new ArrayList<>();
        List<ProjectPassPlanPolicyRel> projectPassPlanPolicyRelsList = new ArrayList<>();

        ProjectPassPlanPolicyRel policyRel;//逻辑策略与方案关联
        ProjectLogicPassPolicy projectLogicPassPolicy;//逻辑策略实体

        for (int i = 0; i < macroIdArray.length; i++) {
            policyId = UUID.randomUUID().toString().replace("-", "");

            //逻辑策略关联
            policyRel = new ProjectPassPlanPolicyRel();
            policyRel.setPlanId(planId);
            policyRel.setPolicyType(PassPlanPolicyTypeEnum.LOGIC.code);
            policyRel.setPolicyId(policyId);
            projectPassPlanPolicyRelsList.add(policyRel);

            //逻辑策略实体
            projectLogicPassPolicy = new ProjectLogicPassPolicy();
            projectLogicPassPolicy.setPolicyId(policyId);
            projectLogicPassPolicy.setMacroId(macroIdArray[i]);
            projectLogicPassPolicyList.add(projectLogicPassPolicy);
        }

        //删除旧数据
        this.removeBathLogic(planId);
        log.info("逻辑方案策略与关联表批量存储 projectPassPlanPolicyRelsList: {}", projectPassPlanPolicyRelsList);
        log.info("逻辑方案策略与关联表批量存储 projectLogicPassPolicyService: {}", projectLogicPassPolicyService);
        //批量保存实体和关联
        projectLogicPassPolicyService.saveBatch(projectLogicPassPolicyList);
        return this.saveBatch(projectPassPlanPolicyRelsList);
    }

    /**
     * 物理策略与关联表批量存储
     *
     * @param deviceIdArray
     * @param planId
     * @return
     */
    @Override
    public boolean saveBatchPhysical(String[] deviceIdArray, String planId) {
        String policyId;
        List<ProjectPhysicalPassPolicy> projectPhysicalPassPolicyList = new ArrayList<>();
        List<ProjectPassPlanPolicyRel> projectPassPlanPolicyRelsList = new ArrayList<>();

        ProjectPassPlanPolicyRel policyRel;//逻辑策略与方案关联
        ProjectPhysicalPassPolicy projectPhysicalPassPolicy;//物理策略实体

        if (deviceIdArray != null && deviceIdArray.length >= 1) {
            for (int i = 0; i < deviceIdArray.length; i++) {
                policyId = UUID.randomUUID().toString().replace("-", "");

                //逻辑策略关联
                policyRel = new ProjectPassPlanPolicyRel();
                policyRel.setPlanId(planId);
                policyRel.setPolicyType(PassPlanPolicyTypeEnum.PHYSICS.code);
                policyRel.setPolicyId(policyId);
                projectPassPlanPolicyRelsList.add(policyRel);

                //逻辑策略实体
                projectPhysicalPassPolicy = new ProjectPhysicalPassPolicy();
                projectPhysicalPassPolicy.setPolicyId(policyId);
                projectPhysicalPassPolicy.setDeviceId(deviceIdArray[i]);
                projectPhysicalPassPolicyList.add(projectPhysicalPassPolicy);
            }
        }
        //删除旧数据
        this.removeBathPhysical(planId);

        //批量保存实体和关联
        projectPhysicalPassPolicyService.saveBatch(projectPhysicalPassPolicyList);
        return this.saveBatch(projectPassPlanPolicyRelsList);
    }

    /**
     * 删除逻辑策略与关联
     *
     * @param planId
     * @return
     */
    @Override
    public boolean removeBathLogic(String planId) {

        List<ProjectLogicPassPolicy> projectLogicPassPolicyList = this.listLogic(planId);

        //删除策略实体
        if (CollectionUtil.isNotEmpty(projectLogicPassPolicyList)) {
            List<String> uidList = projectLogicPassPolicyList.stream().map(e -> e.getPolicyId()).collect(Collectors.toList());
            projectLogicPassPolicyService.removeByIds(uidList);
        }

        //删除策略关系
        return this.remove(new QueryWrapper<ProjectPassPlanPolicyRel>()
                .lambda()
                .eq(ProjectPassPlanPolicyRel::getPlanId, planId)
                .eq(ProjectPassPlanPolicyRel::getPolicyType, PassPlanPolicyTypeEnum.LOGIC.code));
    }

    /**
     * 删除物理策略与关联
     *
     * @param planId
     * @return
     */
    @Override
    public boolean removeBathPhysical(String planId) {
        List<ProjectPhysicalPassPolicy> projectPhysicalPassPolicyList = this.listPhysical(planId);

        //删除策略实体
        if (CollectionUtil.isNotEmpty(projectPhysicalPassPolicyList)) {
            List<String> uidList = projectPhysicalPassPolicyList.stream().map(e -> e.getPolicyId()).collect(Collectors.toList());
            projectPhysicalPassPolicyService.removeByIds(uidList);
        }

        //删除策略关系
        return this.remove(new QueryWrapper<ProjectPassPlanPolicyRel>()
                .lambda()
                .eq(ProjectPassPlanPolicyRel::getPlanId, planId)
                .eq(ProjectPassPlanPolicyRel::getPolicyType, PassPlanPolicyTypeEnum.PHYSICS.code)
        );


    }

    /**
     * 根据planId获取关系列表
     *
     * @param planId
     * @return
     */
    private List<ProjectPassPlanPolicyRel> list(String planId) {
        List<ProjectPassPlanPolicyRel> relList = this.list(new QueryWrapper<ProjectPassPlanPolicyRel>().lambda()
                .eq(ProjectPassPlanPolicyRel::getPlanId, planId));
        return relList;
    }

    /**
     * 根据planId 获取逻辑策略列表
     *
     * @param planId
     * @return
     */
    private List<ProjectLogicPassPolicy> listLogic(String planId) {
        List<ProjectPassPlanPolicyRel> relList = this.list(new QueryWrapper<ProjectPassPlanPolicyRel>().lambda()
                .eq(ProjectPassPlanPolicyRel::getPlanId, planId)
                .eq(ProjectPassPlanPolicyRel::getPolicyType, PassPlanPolicyTypeEnum.LOGIC.code));

        if (CollectionUtil.isEmpty(relList)) {
            return null;
        } else {
            List<String> idList = relList.stream().map(e -> e.getPolicyId()).collect(Collectors.toList());
            return projectLogicPassPolicyService.listByIds(idList);
        }
    }

    /**
     * 根据planId 获取物理策略列表
     *
     * @param planId
     * @return
     */
    private List<ProjectPhysicalPassPolicy> listPhysical(String planId) {
        List<ProjectPassPlanPolicyRel> relList = this.list(new QueryWrapper<ProjectPassPlanPolicyRel>().lambda()
                .eq(ProjectPassPlanPolicyRel::getPlanId, planId)
                .eq(ProjectPassPlanPolicyRel::getPolicyType, PassPlanPolicyTypeEnum.PHYSICS.code));

        if (CollectionUtil.isEmpty(relList)) {
            return null;
        } else {
            List<String> idList = relList.stream().map(e -> e.getPolicyId()).collect(Collectors.toList());
            return projectPhysicalPassPolicyService.listByIds(idList);
        }

    }

    /**
     * 设备PO转换成为方案的设备VO,并写入deviceVoList
     *
     * @param deviceVoList
     * @param deviceInfoList
     */
    private void deviceInfoToVo(List<ProjectPassDeviceVo> deviceVoList, List<ProjectDeviceInfo> deviceInfoList, boolean disabled) {

        boolean isExist = false;
        ProjectPassDeviceVo deviceVo;

        for (ProjectDeviceInfo deviceInfo : deviceInfoList) {
            //是否已存在，存在不写入
            isExist = deviceVoList.stream().anyMatch(e -> e.getDeviceId().equals(deviceInfo.getDeviceId()));

            if (isExist){
                continue;
            }

            deviceVo = new ProjectPassDeviceVo();
            BeanUtils.copyProperties(deviceInfo, deviceVo);
            deviceVo.setDisabled(disabled);
            deviceVoList.add(deviceVo);

            isExist = false;
        }
    }
}
