
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.estate.constant.enums.PassMacroIdEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightActiveTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectPersonDeviceDTO;
import com.aurine.cloudx.estate.entity.ProjectPassPlan;
import com.aurine.cloudx.estate.entity.ProjectPersonDevice;
import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.mapper.ProjectPersonDeviceMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>人员设备权限实现类</p>
 *
 * @ClassName: ProjectPersonDeviceServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 8:40
 * @Copyright:
 */
@Service
public class ProjectPersonDeviceServiceImpl extends ServiceImpl<ProjectPersonDeviceMapper, ProjectPersonDevice> implements ProjectPersonDeviceService {


    @Autowired
    private ProjectPersonPlanRelService projectPersonPlanRelService;
    @Autowired
    private ProjectPassPlanService projectPassPlanService;

    @Lazy
    @Autowired
    private ProjectHousePersonRelService projectHousePersonRelService;

    @Autowired
    @Lazy
    private ProjectRightDeviceService projectRightDeviceService;

    @Resource
    private ProjectPassPlanPolicyRelService projectPassPlanPolicyRelService;


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

        if(personPlanRel != null){
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
        if (CollectionUtil.isNotEmpty(list)) {
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
     * 根据人员ID 和 方案ID 获得该用户可以使用的设备VO列表(小程序业务)
     *
     * @param personId
     * @param planId
     * @return
     */
    @Override
    public List<ProjectPassDeviceVo> wechatGetlistDevice(String personId, String planId) {
        List<String> macroList = projectPassPlanPolicyRelService.listMacroIdListByPlanId(planId);

        return baseMapper.listByPerson(
                personId,
                planId,
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
//        if (CollectionUtil.isEmpty(houstList)) {
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
                List<String> defaultPlanIdList = planList.stream().filter(f -> f.getIsDefault().equals("1")).map(e -> e.getPlanId()).collect(Collectors.toList());

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

}
