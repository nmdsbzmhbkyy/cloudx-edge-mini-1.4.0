package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.open.common.entity.vo.PersonDeviceRelVo;
import com.aurine.cloudx.open.origin.constant.enums.PassMacroIdEnum;
import com.aurine.cloudx.open.origin.constant.enums.PassRightActiveTypeEnum;
import com.aurine.cloudx.open.origin.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.open.origin.dto.ProjectPersonDeviceDTO;
import com.aurine.cloudx.open.origin.dto.ProjectUserHouseDTO;
import com.aurine.cloudx.open.origin.entity.*;
import com.aurine.cloudx.open.origin.mapper.ProjectPersonDeviceMapper;
import com.aurine.cloudx.open.origin.service.*;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.open.origin.vo.ProjectPassDeviceVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>人员设备权限 基础 接口</p>
 *
 * @ClassName: ProjectPersonDeviceService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 8:40
 * @Copyright:
 */
@Service
@Primary
@Slf4j
public class ProjectPersonDeviceServiceImpl extends ServiceImpl<ProjectPersonDeviceMapper, ProjectPersonDevice> implements ProjectPersonDeviceService {


    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;
    @Resource
    private ProjectPassPlanService projectPassPlanService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceServiceProxyImpl;
    @Resource
    private ProjectPassPlanPolicyRelService projectPassPlanPolicyRelService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePersonDevice(ProjectPersonDeviceDTO personDeviceDTO) {

        /**
         * 基于需求原型 1.6 2020-07-03
         * 用户不选择过期时间时，自动设置过期时间未2199-01-01
         * @author: 王伟 2020-07-06
         */
        if (personDeviceDTO.getExpTime() == null) {
            personDeviceDTO.setExpTime(DateUtil.toLocalDateTime(DateUtil.parse("2199-01-01", "yyyy-MM-dd")));
        }

        ProjectPersonDevice projectPersonDevice;
        List<ProjectPersonDevice> projectPersonDeviceList = new ArrayList<>();
        List<ProjectPersonDevice> originalProjectPersonDeviceList = this.listByPersonId(personDeviceDTO.getPersonId()); //最初的设备列表
        List<ProjectPersonDevice> delProjectPersonDeviceList = new ArrayList<>();//要删除的设备列表
        List<ProjectPassDeviceVo> deviceByPlanList = new ArrayList<>();


        boolean isExist = false;
        //删除旧数据
        this.deleteByPersonId(personDeviceDTO.getPersonId());

        String planId = personDeviceDTO.getPlanId();
        String[] deviceIdArray = personDeviceDTO.getDeviceIdArray();


        //如果是未配置过的用户，默认给激活状态
        if (StringUtil.isEmpty(personDeviceDTO.getIsActive())) {
            personDeviceDTO.setIsActive(PassRightActiveTypeEnum.ACTIVED.code);
        }


        deviceByPlanList = this.listDeviceByPersonIdAndPlanId(personDeviceDTO.getPersonId(), planId);
        /**
         * 只保存用户手动选择的数据
         */
        //转换用户选择的设备列表
        if (deviceIdArray != null && deviceIdArray.length > 0) {

            //设备数组去重复，应对前端可能出现的问题
            List<String> deviceIdList = new ArrayList<>();
            for (int i = 0; i < deviceIdArray.length; i++) {
                if (!deviceIdList.contains(deviceIdArray[i])) {
                    deviceIdList.add(deviceIdArray[i]);
                }
            }


            for (String id : deviceIdList) {

                //如果设备已经存在于方案列表中，不在写入系统
                isExist = deviceByPlanList.stream().anyMatch(e -> e.getDeviceId().equals(id));
                if (isExist) {
                    continue;
                }

                projectPersonDevice = new ProjectPersonDevice();
                BeanUtils.copyProperties(personDeviceDTO, projectPersonDevice);
                projectPersonDevice.setDeviceId(id);
                projectPersonDevice.setStatus("1");
                projectPersonDevice.setPlanId(""); //用户选择的设备，且不在方案之中
                projectPersonDevice.setEffTime(personDeviceDTO.getEffTime());
                projectPersonDevice.setExpTime(personDeviceDTO.getExpTime());
                projectPersonDeviceList.add(projectPersonDevice);
            }
        }

        if (projectPersonDeviceList.size() > 0) {
            this.saveBatch(projectPersonDeviceList);
        }
        //添加新数据



        /*
        //获取方案設设备列表
        List<ProjectPassDeviceVo> deviceVoList = this.listDeviceByPersonIdAndPlanId(personDeviceDTO.getPersonId(), personDeviceDTO.getPlanId());

        if (CollUtil.isNotEmpty(deviceVoList)) {
            for (ProjectPassDeviceVo deviceVo : deviceVoList) {
                projectPersonDevice = new ProjectPersonDevice();
                BeanUtils.copyProperties(personDeviceDTO, projectPersonDevice);
                projectPersonDevice.setDeviceId(deviceVo.getDeviceId());
                projectPersonDevice.setStatus("1");
                projectPersonDevice.setEffTime(personDeviceDTO.getEffTime());
                projectPersonDevice.setExpTime(personDeviceDTO.getExpTime());

                projectPersonDeviceList.add(projectPersonDevice);
            }

        }

        //转换用户选择的设备列表
        if (deviceIdArray != null && deviceIdArray.length > 0) {

            //设备数组去重复，应对前端可能出现的问题
            List<String> deviceIdList = new ArrayList<>();
            for (int i = 0; i < deviceIdArray.length; i++) {
                if (!deviceIdList.contains(deviceIdArray[i])) {
                    deviceIdList.add(deviceIdArray[i]);
                }
            }

            for (String id : deviceIdList) {
                isExist = deviceVoList.stream().anyMatch(e -> e.getDeviceId().equals(id));
                if (isExist) continue;

                projectPersonDevice = new ProjectPersonDevice();
                BeanUtils.copyProperties(personDeviceDTO, projectPersonDevice);
                projectPersonDevice.setDeviceId(id);
                projectPersonDevice.setStatus("1");
                projectPersonDevice.setPlanId(""); //用户选择的设备，且不在方案之中
                projectPersonDevice.setEffTime(personDeviceDTO.getEffTime());
                projectPersonDevice.setExpTime(personDeviceDTO.getExpTime());
                projectPersonDeviceList.add(projectPersonDevice);
            }
        }

        //添加新数据
        this.saveBatch(projectPersonDeviceList);
*/

//        /**
//         * 获取需要删除的设备
//         * @author: 王伟
//         * @since 2020-08-20
//         */
//
//        if (CollUtil.isNotEmpty(originalProjectPersonDeviceList)) {
//            originalProjectPersonDeviceList.removeAll(projectPersonDeviceList);
//            delProjectPersonDeviceList = originalProjectPersonDeviceList;
//        }

        //下发变更
        List<String> deviceIdList = new ArrayList<>();

        //方案选择

        if (CollUtil.isNotEmpty(deviceByPlanList)) {
            deviceIdList.addAll(deviceByPlanList.stream().map(ProjectPassDeviceVo::getDeviceId).collect(Collectors.toList()));
        }
        //用户手动选择
        if (CollUtil.isNotEmpty(projectPersonDeviceList)) {
            deviceIdList.addAll(projectPersonDeviceList.stream().map(ProjectPersonDevice::getDeviceId).collect(Collectors.toList()));
        }

        projectRightDeviceService.saveRelationship(deviceIdList, personDeviceDTO.getPersonId());
//        projectRightDeviceService.changeByPersonDevice(projectPersonDeviceList, delProjectPersonDeviceList, personDeviceDTO.getPersonId());
        return true;

        //TODO: 过期情况的处理逻辑需要根据中台确定 （如何触发、怎么处理等） 王伟 ->
    }

    /**
     * 批量更新并下发人员设备权限
     * 通常用于设备、方案变更。
     *
     * @return
     */
    @Override
    public Boolean updateBatchPersonDevice(List<ProjectPersonPlanRel> personPlanRelList, int projectId) {
//        ProjectContextHolder.setProjectId(projectId);
        if (CollUtil.isEmpty(personPlanRelList)) {
            return true;
        }

        String planId = "";
//        List<ProjectPersonDevice> originalProjectPersonDeviceList = null;
//        List<ProjectPersonDevice> delProjectPersonDeviceList = null;
        List<ProjectPassDeviceVo> newDeviceByPlanList = null;//根据方案获取的新的可通行设备列表
//        List<ProjectPersonDevice> projectPersonDeviceList = new ArrayList<>();//批量添加list
//        List<ProjectPersonDevice> perProjectPersonDeviceList = new ArrayList<>();//批量添加list
//
//        List<String> userDeviceIdList = null;//用户手动选择的设备
//        ProjectPersonDevice projectPersonDevice = null;
//
////        ProjectPersonPlanRel personPlanRel = null;
//        boolean isExist = false;
//        Map<String, List<String>> devicePersonMap = new HashMap<>();
//
//
//        //添加新数据
        for (ProjectPersonPlanRel personPlanRel : personPlanRelList) {
//            perProjectPersonDeviceList = new ArrayList<>();
//            planId = personPlanRel.getPlanId();
            projectRightDeviceServiceProxyImpl.saveRelationshipProxy(personPlanRel.getPlanId(), personPlanRel.getPersonId(), projectId);
//
//            if (personPlanRel.getExpTime() == null) {
//                personPlanRel.setExpTime(DateUtil.toLocalDateTime(DateUtil.parse("2199-01-01", "yyyy-MM-dd")));
//            }
//
//
//            //获取用户选择的设备
//            originalProjectPersonDeviceList = this.listByPersonId(personPlanRel.getPersonId());//person的原有可通行设备列表
//            if (CollUtil.isNotEmpty(originalProjectPersonDeviceList)) {
//                // 只保留用户手动选择的设备
//                userDeviceIdList = originalProjectPersonDeviceList.stream().filter(device -> device.getPlanId() == null || device.getPlanId().isEmpty()).map(ProjectPersonDevice::getDeviceId).collect(Collectors.toList());
//            }
//
////            //获取方案設设备列表
//            newDeviceByPlanList = this.listDeviceByPersonIdAndPlanId(personPlanRel.getPersonId(), planId);
//            /**
//             * 变更方案不需要改变物理存储，只要执行变更下发即可
//             */
//            projectRightDeviceService.saveRelationshipProxy(newDeviceByPlanList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList()), personPlanRel.getPersonId());
////
//            //转换用户选择设备列表
//            if (userDeviceIdList != null && userDeviceIdList.size() > 0) {
//
//                for (String id : userDeviceIdList) {
//                    //如果设备已经存在于方案列表中，不在写入系统
//                    isExist = newDeviceByPlanList.stream().anyMatch(e -> e.getDeviceId().equals(id));
//                    if (isExist) continue;
//
//                    projectPersonDevice = new ProjectPersonDevice();
//
//                    BeanUtils.copyProperties(projectPersonDevice, projectPersonDevice);
//                    projectPersonDevice.setPersonId(personPlanRel.getPersonId());
//                    projectPersonDevice.setIsActive(personPlanRel.getIsActive());
//                    projectPersonDevice.setPersonType(personPlanRel.getPersonType());
//                    projectPersonDevice.setDeviceId(id);
//                    projectPersonDevice.setStatus("1");
//                    projectPersonDevice.setPlanId(""); //用户选择的设备，且不在方案之中
//                    projectPersonDevice.setEffTime(personPlanRel.getEffTime());
//                    projectPersonDevice.setExpTime(personPlanRel.getExpTime());
//                    projectPersonDeviceList.add(projectPersonDevice);
//                    perProjectPersonDeviceList.add(projectPersonDevice);
//                }
//            }
//
//            //封装每个person最终的设备权限
//            if (CollUtil.isNotEmpty(perProjectPersonDeviceList)) {
//                devicePersonMap.put(personPlanRel.getPersonId(), perProjectPersonDeviceList.stream().map(ProjectPersonDevice::getDeviceId).collect(Collectors.toList()));
//            }
//
//
        }
//
////        //清空全部
//        List<String> personIdList = personPlanRelList.stream().map(e -> e.getPersonId()).collect(Collectors.toList());
//        this.remove(new QueryWrapper<ProjectPersonDevice>().lambda().in(ProjectPersonDevice::getPersonId, personIdList));
//
//        //批量添加全部
//        this.saveBatch(projectPersonDeviceList);


//        //批量下发变更
//        List<String> deviceIdList = null;
//        for (String personId : personIdList) {
//            deviceIdList = devicePersonMap.get(personId);
//            if (CollUtil.isNotEmpty(deviceIdList)) {
//                projectRightDeviceService.saveRelationshipProxy(deviceIdList, personId);
//            }
//        }

        return true;
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
        PassMacroIdEnum macro = null;
        ProjectPersonDevice projectPersonDeviceNew = null;
        ProjectPersonDevice projectPersonDeviceOld = null;
        List<ProjectPersonDevice> projectPersonDeviceOldList = null;

        List<ProjectPersonDevice> projectPersonDeviceNewList = new ArrayList<>();
        List<ProjectPassPlan> planList = null;
        List<ProjectPassPlan> planListLadder = null;
        List<ProjectPassPlan> planListBuild = null;
        String buildingId = "";
        String unitId = "";
        List<String> personIdList = new ArrayList<>();
        List<String> planIdList = new ArrayList<>();

        //根据设备类型，区分逻辑策略宏，获取相关的方案列表
        //区口机
        if (device.getDeviceType().equals(DeviceTypeConstants.GATE_DEVICE)) {
            macro = PassMacroIdEnum.GATE;
            //获取所有使用相关方案
            planList = this.projectPassPlanService.listByMacro(macro);

            /**
             * 添加新的逻辑策略，当新增区口机，获取该区口机有使用到的方案
             * @author: 王伟 2020-07-03
             * */
            macro = PassMacroIdEnum.FRAME_GATE;
            List<ProjectPassPlan> projectPassPlans = this.projectPassPlanService.listByMacro(macro);
            planList.addAll(projectPassPlans);

            if (CollUtil.isNotEmpty(planList)) {
                personIdList = this.baseMapper.findPerson(planList.stream().map(plan -> plan.getPlanId()).collect(Collectors.toList()), null, null, null);
                List<String> personList = this.baseMapper.findPerson(personIdList, null, null, device.getDeviceEntityId());
                if (CollUtil.isNotEmpty(personList)) {
                    CollUtil.addAll(personIdList, personList);
                }
            }
            //梯口机
        } else if (device.getDeviceType().equals(DeviceTypeConstants.LADDER_WAY_DEVICE)) {
            //一个人只可能使用一个方案，不存在重复情况
            macro = PassMacroIdEnum.UNIT_LADDER;
            planListLadder = this.projectPassPlanService.listByMacro(macro);
            planList = new ArrayList<>();
            if (CollUtil.isNotEmpty(planListLadder)) {
                planList.addAll(planListLadder);
            }

            if (CollUtil.isNotEmpty(planListLadder)) {
                personIdList = this.baseMapper.findPerson(planListLadder.stream().map(plan -> plan.getPlanId()).collect(Collectors.toList()), device.getBuildingId(), device.getUnitId(), null);
            }

            macro = PassMacroIdEnum.BUILDING_LADDER;
            planListBuild = this.projectPassPlanService.listByMacro(macro);
            if (CollUtil.isNotEmpty(planListBuild)) {
                planList.addAll(planListLadder);
            }


            if (CollUtil.isNotEmpty(planListBuild)) {
                CollUtil.addAll(personIdList, this.baseMapper.findPerson(planListBuild.stream().map(plan -> plan.getPlanId()).collect(Collectors.toList()), device.getBuildingId(), null, null));
            }

        }

        if (CollUtil.isEmpty(planList)) {
            return true;
        }

        //获取方案使用的人员
//        List<String> planIdList = planList.stream().map(plan -> plan.getPlanId()).collect(Collectors.toList());
//        List<String> personIdList = this.baseMapper.findPerson(planIdList, , );

        if (CollUtil.isEmpty(personIdList)) {
            return true;
        }


        //对使用方案的人员，变更数据
        /**
         * 设备权限的存储已调整，直接动态获取
         */
//        for (String personId : personIdList) {
//
//            //获取人员带方案信息的数据（部分人员设备权限数据来自于自选，不带有方案ID）
//            projectPersonDeviceOldList = this.listByPersonId(personId);
//            projectPersonDeviceOldList = projectPersonDeviceOldList.stream().filter(personDevice -> personDevice.getPlanId() != null && !personDevice.getPlanId().equals("")).collect(Collectors.toList());
//            projectPersonDeviceOld = projectPersonDeviceOldList.get(0);
//
//            //增加信息
//            projectPersonDeviceNew = new ProjectPersonDevice();
//            BeanUtils.copyProperties(projectPersonDeviceOld, projectPersonDeviceNew);
//            projectPersonDeviceNew.setDeviceId(device.getDeviceId());
//
//            projectPersonDeviceNewList.add(projectPersonDeviceNew);
//        }
//
//        //更新人员设备权限信息
//        if (CollUtil.isNotEmpty(projectPersonDeviceNewList)) {
//            this.saveBatch(projectPersonDeviceNewList);
//        }


        //更新下发信息
        this.projectRightDeviceService.authNewDeviceById(device.getDeviceId(), personIdList);

        return true;
    }

    /**
     * 关闭通行权限
     *
     * @param personType 人员类型
     * @param personId   人员ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disablePassRight(String personType, String personId) {
        List<ProjectPersonDevice> list = this.listByPersonId(personId);

        for (ProjectPersonDevice personDevice : list) {
            personDevice.setIsActive(PassRightActiveTypeEnum.DISABLE.code);
        }

        //变更方案关联可通行状态
        projectPersonPlanRelService.disablePerson(personId);
        if (CollUtil.isNotEmpty(list)) {

            this.updateBatchById(list);
        }
        //下发权限变更
        return projectRightDeviceService.deactive(personId);
    }

    /**
     * 开启通行权限
     *
     * @param personType
     * @param personId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enablePassRight(String personType, String personId) {

        //变更用户选择设备
        List<ProjectPersonDevice> list = this.listByPersonId(personId);

        for (ProjectPersonDevice personDevice : list) {
            personDevice.setIsActive(PassRightActiveTypeEnum.ACTIVED.code);
        }
        if (CollUtil.isNotEmpty(list)) {

            this.updateBatchById(list);
        }

        //变更方案关联可通行状态
        projectPersonPlanRelService.enablePerson(personId);

        //下发权限变更
        return projectRightDeviceService.active(personId);

    }


    /**
     * 设备变更后，所有用户重新下发数据(低性能，完整逻辑)
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean refresh() {
        List<String> personIdList = this.baseMapper.findPerson(null, null, null, null);
        List<String> deviceIdList;

        if (CollUtil.isNotEmpty(personIdList)) {
            for (String personId : personIdList) {
                //获取人员数据
                ProjectPersonDeviceDTO dto = this.getDTOByPersonId(personId);

                //转换设备存储
                if (CollUtil.isNotEmpty(dto.getDeviceList())) {
                    deviceIdList = dto.getDeviceList().stream().map(device -> device.getDeviceId()).collect(Collectors.toList());
                    dto.setDeviceIdArray(deviceIdList.toArray(new String[deviceIdList.size()]));
                }
                this.savePersonDevice(dto);
            }
        }
        return true;
    }

    /**
     * 根据personId,重载该用户的权限，用于用户房屋归属变化等情况
     *
     * @param personId
     * @return
     */
    @Override
    public boolean refreshByPersonId(String personId, PersonTypeEnum personTypeEnum) {
        // 如果该人员是住户且已经没有房屋，就不需要再重载该用户的权限了
        if (personTypeEnum == PersonTypeEnum.PROPRIETOR) {
            int houseCount = projectHousePersonRelService.countHouseByPersonId(personId);
            if (houseCount <= 0) {
                this.deleteByPersonId(personId);
                return true;
            }
        }

        ProjectPersonDeviceDTO dto;
        List<ProjectPassDeviceVo> deviveVoList;
        List<String> deviceIdList;

        dto = this.getDTOByPersonId(personId);
        deviveVoList = dto.getDeviceList();//获取的原有设备
        dto.setPersonType(personTypeEnum.code);

        if (CollUtil.isNotEmpty(deviveVoList)) {

            // 只保留用户手动选择的设备
            deviceIdList = deviveVoList.stream().filter(device -> !device.getDisable()).map(ProjectPassDeviceVo::getDeviceId).collect(Collectors.toList());
            dto.setDeviceIdArray(deviceIdList.toArray(new String[deviceIdList.size()]));//选中的设备
        }
        return this.savePersonDevice(dto);//自动根据方案重载设备权限
    }

    /**
     * 批量添加人员-设备权限信息
     *
     * @param personId
     * @param deviceIdArray
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePersonDevice(String personId, PersonTypeEnum personType, String[] deviceIdArray, LocalDateTime effTime, LocalDateTime expTime) {

        ProjectPersonDevice projectPersonDevice;
        List<ProjectPersonDevice> projectPersonDeviceList = new ArrayList<>();

        //删除旧数据
        this.deleteByPersonId(personId);

        //转换用户选择的设备列表
        if (deviceIdArray != null && deviceIdArray.length > 0) {

            //设备数组去重复，应对前端可能出现的问题
            List<String> deviceIdList = new ArrayList<>();
            for (int i = 0; i < deviceIdArray.length; i++) {
                if (!deviceIdList.contains(deviceIdArray[i])) {
                    deviceIdList.add(deviceIdArray[i]);
                }
            }

            //检查设备是否通过通行方案获取
            //获取方案設设备列表
//            List<ProjectPassDeviceVo> deviceFromPlanList = null;
//            String planId = projectPersonPlanRelService.getPlanIdByPersonId(personId);
//            if (StringUtils.isNotEmpty(planId)) {
//                deviceFromPlanList = this.listDeviceByPersonIdAndPlanId(personId, planId);
//            }
//
//            if (CollUtil.isEmpty(deviceFromPlanList)) {
//                deviceFromPlanList = new ArrayList<>();
//            }

            for (String id : deviceIdList) {

                projectPersonDevice = new ProjectPersonDevice();
                projectPersonDevice.setIsActive(PassRightActiveTypeEnum.ACTIVED.code);
                projectPersonDevice.setPersonId(personId);
                projectPersonDevice.setPersonType(personType.code);
                projectPersonDevice.setDeviceId(id);
                projectPersonDevice.setStatus("1");
//                projectPersonDevice.setPlanId(deviceFromPlanList.stream().anyMatch(e -> e.getDeviceId().equals(id)) ? planId : "");//设备组中存在来自方案的设备，则设置方案id
                projectPersonDevice.setPlanId("");
                projectPersonDevice.setEffTime(effTime);
                projectPersonDevice.setExpTime(expTime);
                projectPersonDeviceList.add(projectPersonDevice);
            }
        }

        //添加新数据
        return this.saveBatch(projectPersonDeviceList);
    }


    /**
     * 删除设备后，更新项目下全部用户权限，不做下发处理
     *
     * @param deviceId
     */
    @Override
    public boolean refreshDeleteDevice(String deviceId) {
        return this.remove(new QueryWrapper<ProjectPersonDevice>().lambda().eq(ProjectPersonDevice::getDeviceId, deviceId));
    }

    /**
     * 批量删除设备后，更新项目下全部用户权限，并重新下发
     *
     * @param deviceId
     */
    @Override
    public boolean refreshDeleteDeviceAll(List<String> deviceId) {
        return this.remove(new QueryWrapper<ProjectPersonDevice>().lambda().in(ProjectPersonDevice::getDeviceId, deviceId));
    }


    /**
     * 删除指定人员的全部权限信息
     *
     * @param personId
     * @return
     */
    @Override
    public boolean deleteByPersonId(String personId) {
        return this.remove(new QueryWrapper<ProjectPersonDevice>().lambda().eq(ProjectPersonDevice::getPersonId, personId));
    }

    /**
     * 根据personId 查DTO
     *
     * @param personId
     * @return
     */
    @Override
    public ProjectPersonDeviceDTO getDTOByPersonId(String personId) {
        ProjectPersonDeviceDTO dto = new ProjectPersonDeviceDTO();

        ProjectPersonDevice projectPersonDevice = this.getByPersonId(personId);

        dto.setDeviceList(this.listDeviceByPersonId(personId));
        dto.setPersonId(personId);
        if (projectPersonDevice != null) {
            dto.setPersonType(projectPersonDevice.getPersonType());
        }

        //获取用户选择的通行模板
        ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(personId);

        if (personPlanRel != null) {
            dto.setPlanId(personPlanRel.getPlanId());

            dto.setEffTime(personPlanRel.getEffTime());
            dto.setExpTime(personPlanRel.getExpTime());
            dto.setIsActive(personPlanRel.getIsActive());
        }

        return dto;
    }

    /**
     * 通过personId 获取到第一个权限对象
     *
     * @param personId
     * @return
     */
    @Override
    public ProjectPersonDevice getByPersonId(String personId) {
        List<ProjectPersonDevice> list = this.listByPersonId(personId);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据人员ID 获取该用户可以使用的设备权限
     *
     * @param personId
     * @return
     */
    @Override
    public List<ProjectPersonDevice> listByPersonId(String personId) {
        return this.list(new QueryWrapper<ProjectPersonDevice>().lambda().eq(ProjectPersonDevice::getPersonId, personId));
    }

    /**
     * 根据人员ID 分页查询获取该用户可以使用的设备权限
     *
     * @param personId
     * @return
     */
    @Override
    public Page<ProjectPersonDevice> pageByPersonId(Page page, String personId) {
        return this.page(page, Wrappers.lambdaQuery(ProjectPersonDevice.class).eq(ProjectPersonDevice::getPersonId, personId));
    }

    /**
     * 根据人员ID 获得该用户可以使用的设备VO列表
     *
     * @param personId
     * @return
     */
    @Override
    public List<ProjectPassDeviceVo> listDeviceByPersonId(String personId) {
        //获取到该人员使用的逻辑策略
        List<String> macroList = projectPassPlanPolicyRelService.listMacroIdListByPersonId(personId);
//        Arrays.stream(macroArray).anyMatch(e -> e.equals(PassMacroIdEnum.GATE.name()));
//        Arrays.stream(macroArray).anyMatch(e -> e.equals(PassMacroIdEnum.FRAME_GATE.name()));
//        Arrays.stream(macroArray).anyMatch(e -> e.equals(PassMacroIdEnum.BUILDING_LADDER.name()));
//        Arrays.stream(macroArray).anyMatch(e -> e.equals(PassMacroIdEnum.UNIT_LADDER.name()));

        return baseMapper.listByPerson(
                personId, null,
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.GATE.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.FRAME_GATE.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.BUILDING_LADDER.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.UNIT_LADDER.name())),
                false
        );
    }

    /**
     * 根据人员ID 获得该用户可以使用的设备VO列表
     *
     * @param personId
     * @return
     */
    @Override
    public Page<ProjectPassDeviceVo> pageDeviceByPersonId(Page page, String personId) {
        //获取到该人员使用的逻辑策略
        List<String> macroList = projectPassPlanPolicyRelService.listMacroIdListByPersonId(personId);
        return baseMapper.pageByPerson(
                page,
                personId,
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.GATE.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.FRAME_GATE.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.BUILDING_LADDER.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.UNIT_LADDER.name())),
                false
        );
    }

    /**
     * 根据人员ID 和 方案ID 获得该用户可以使用的设备VO列表(根据规则)
     *
     * @param personId
     * @param planId
     * @return
     */
    @Override
    public List<ProjectPassDeviceVo> listDeviceByPersonIdAndPlanId(String personId, String planId) {
        List<String> macroList = projectPassPlanPolicyRelService.listMacroIdListByPlanId(planId);

        return baseMapper.listByPerson(
                personId,
                planId,
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.GATE.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.FRAME_GATE.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.BUILDING_LADDER.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.UNIT_LADDER.name())),
                true
        );
//        //根据人员ID, 获取到人员所在的楼栋，如果人员存在多个楼栋或单元，则获取并集
//
//        List<ProjectPassDeviceVo> deviceVoList = new ArrayList<>();
//        List<ProjectPassDeviceVo> deviceVoPreList;
//        boolean isExist = false;
//
//        //获取到人员的所有房地产
//        List<ProjectHouseDTO> houstList = projectHousePersonRelService.listHouseByPersonId(personId);
//
//        //没有房地产，直接查询方案
//        if (CollUtil.isEmpty(houstList)) {
//            deviceVoList = projectPassPlanService.listDeviceByPlanId(planId, "", "");
//            //强制配置方案相关的设备为不可更改
//            for (ProjectPassDeviceVo deviceVo : deviceVoList) {
//                deviceVo.setDisabled(true);
//            }
//
//            return deviceVoList;
//        } else {
//            //存在房地产，根据规则获取全部方案的并集
//            for (ProjectHouseDTO houseDto : houstList) {
//                deviceVoPreList = projectPassPlanService.listDeviceByPlanId(planId, houseDto.getBuildingId(), houseDto.getUnitId());
//
//                //去重复
//                for (ProjectPassDeviceVo deviceVo : deviceVoPreList) {
//                    isExist = deviceVoList.stream().anyMatch(e -> e.getDeviceId().equals(deviceVo.getDeviceId()));
//                    //如果已存在，不添加
//                    if (isExist) continue;
//                    //强制配置方案相关的设备为不可更改
//                    deviceVo.setDisabled(true);
//                    deviceVoList.add(deviceVo);
//                }
//            }
//
//            return deviceVoList;
//        }
    }


    /**
     * 根据系统默认配置，生成用户可通行设备
     * 通常用于第三方接口调用的方法，避免新创建的人员没有配置设备权限
     *
     * @param personType
     * @param personId
     * @return
     * @since 2020-10-30 8:47
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String initDefaultPassRightPlan(PersonTypeEnum personType, String personId) {
        List<ProjectPassDeviceVo> personDeviceList;

        switch (personType) {
            case PROPRIETOR:
            case STAFF:
                //校验当前人员是否已存储了通行方案（针对住户、员工）
                String planId = projectPersonPlanRelService.getPlanIdByPersonId(personId);

                //当前用户存在方案，说明用户已配置,不再根据默认方案生成权限
                if (StringUtils.isNotEmpty(planId)) {
                    return planId;
                }

                //根据人员类型，获取对应的默认方案，存储配置和通行权限
                List<ProjectPassPlan> planList = projectPassPlanService.listByType(personType.code);
                List<String> defaultPlanIdList = planList.stream().filter(f -> "1".equals(f.getIsDefault())).map(e -> e.getPlanId()).collect(Collectors.toList());

                if (CollUtil.isEmpty(defaultPlanIdList)) {
                    throw new RuntimeException("项目未配置默认方案，请联系管理员");
                }

                planId = defaultPlanIdList.get(0);

                //根据planid存储权限,但不执行下发
                ProjectPersonPlanRel personPlanRel = new ProjectPersonPlanRel();
                personPlanRel.setPersonType(personType.code);
                personPlanRel.setEffTime(LocalDateTime.now());
                personPlanRel.setExpTime(LocalDateTime.parse("2199-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                personPlanRel.setPersonId(personId);
                personPlanRel.setIsActive("1");
                personPlanRel.setPlanId(planId);

                projectPersonPlanRelService.save(personPlanRel);
                return planId;
            case VISITOR:
                //检查当前人员是否已配通行设备（针对访客）
                return "";


        }

        return "";
    }

    @Override
    public List<ProjectUserHouseDTO> getDevicePersonInfoList(ProjectDeviceInfoProxyVo deviceInfo) {
        List<ProjectUserHouseDTO> resultlist = new ArrayList<>();

        DeviceTypeEnum deviceTypeEnum = DeviceTypeEnum.getByCode(deviceInfo.getDeviceType());

        if (deviceTypeEnum == DeviceTypeEnum.GATE_DEVICE) {

            if (StringUtils.isEmpty(deviceInfo.getDeviceEntityId())) {
                //公共区口机，项目下所有住户都可以被呼叫
                resultlist = this.baseMapper.listOriginByFrame(deviceInfo.getProjectId(), "", "", "", false);

            } else {
                //组团区口机，组团下楼栋住户都可以被呼叫
                String groupId = deviceInfo.getDeviceEntityId();
                resultlist = this.baseMapper.listOriginByFrame(deviceInfo.getProjectId(), groupId, "", "", false);

            }

        } else if (deviceTypeEnum == DeviceTypeEnum.LADDER_WAY_DEVICE) {

            //梯口机，梯口机单元内的住户都可以被呼叫
            //梯口机呼叫时，使用房间号作为拨号号码
            resultlist = this.baseMapper.listOriginByFrame(deviceInfo.getProjectId(), "", "", deviceInfo.getUnitId(), true);


        } else {

        }


        return resultlist;
    }

    @Override
    public List<ProjectUserHouseDTO> getPersonDeviceInfoList(ProjectHousePersonRel projectHousePersonRel) {
        return this.baseMapper.listOriginByHousePerson(projectHousePersonRel.getHouseId(), projectHousePersonRel.getPersonId());
    }

    @Override
    protected Class<ProjectPersonDevice> currentModelClass() {
        return ProjectPersonDevice.class;
    }

    @Override
    public Page<PersonDeviceRelVo> page(Page page, PersonDeviceRelVo vo) {
        ProjectPersonDevice po = new ProjectPersonDevice();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }


}
