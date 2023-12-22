package com.aurine.cloudx.estate.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.OpenApiProjectPersonDeviceDto;
import com.aurine.cloudx.estate.entity.ProjectPersonDevice;
import com.aurine.cloudx.estate.mapper.ProjectPersonDeviceMapper;
import com.aurine.cloudx.estate.service.OpenApiProjectPassPlanService;
import com.aurine.cloudx.estate.service.OpenApiProjectPersonDeviceService;
import com.aurine.cloudx.estate.service.ProjectPersonDeviceService;
import com.aurine.cloudx.estate.service.ProjectPersonPlanRelService;
import com.aurine.cloudx.estate.service.ProjectProprietorDeviceProxyService;
import com.aurine.cloudx.estate.service.ProjectStaffDeviceProxyService;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceVo;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: wrm
 * @Date: 2022/05/27 17:31
 * @Package: com.aurine.openv2.service.impl
 * @Version: 1.0
 * @Remarks:
 **/
@Service
public class OpenApiProjectPersonDeviceServiceImpl extends ServiceImpl<ProjectPersonDeviceMapper, ProjectPersonDevice> implements OpenApiProjectPersonDeviceService {

    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;

    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;

    @Resource
    private ProjectStaffDeviceProxyService projectStaffDeviceProxyService;

    @Resource
    private ProjectProprietorDeviceProxyService projectProprietorDeviceProxyService;

    @Resource
    private OpenApiProjectPersonDeviceService openApiProjectPersonDeviceService;

    @Resource
    private OpenApiProjectPassPlanService openApiProjectPassPlanService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<List<OpenApiProjectPersonDeviceDto>> staffSaveList(OpenApiProjectPersonDeviceDto openApiProjectPersonDeviceDto) {
        openApiProjectPersonDeviceDto.setPersonType(PersonTypeEnum.STAFF.code);

        // 返回参数
        List<OpenApiProjectPersonDeviceDto> personDeviceDtoList = new ArrayList<>();

        //循环人员列表，新增人员通行权限
        for (String personId : openApiProjectPersonDeviceDto.getPersonIdList()) {
            if (StringUtil.isEmpty(openApiProjectPersonDeviceDto.getPlanId())) {
                String planIdByPersonId = projectPersonPlanRelService.getPlanIdByPersonId(personId);

                if (StringUtil.isEmpty(planIdByPersonId)) {
                    // 没有设置通行权限默认 设置默认通行权限
                    String defaultPlanId = openApiProjectPassPlanService.getDefaultPlanId(PersonTypeEnum.STAFF.code);
                    openApiProjectPersonDeviceDto.setPlanId(defaultPlanId);
                } else {
                    openApiProjectPersonDeviceDto.setPlanId(planIdByPersonId);
                }

            } else {
                openApiProjectPersonDeviceDto.setPlanId(openApiProjectPersonDeviceDto.getPlanId());
            }
            openApiProjectPersonDeviceDto.setPersonId(personId);

            List<String> deviceIdArray = new ArrayList<>(Arrays.asList(openApiProjectPersonDeviceDto.getDeviceIdArray()));
            // 查数据库获取用户的设备列表 project_person_device
            LambdaQueryWrapper<ProjectPersonDevice> lambda = new QueryWrapper<ProjectPersonDevice>().lambda();
            lambda.eq(ProjectPersonDevice::getPersonId, personId);
            List<ProjectPersonDevice> list = this.list(lambda);
            // 和deviceIdArray 并集
            List<String> collect = deviceIdArray;
            if (CollectionUtil.isNotEmpty(list)) {
                deviceIdArray.addAll(list.stream().map(ProjectPersonDevice::getDeviceId).collect(Collectors.toList()));
                collect = deviceIdArray.stream().distinct().collect(Collectors.toList());
            }
            // set 回 openApiProjectPersonDeviceDto.setDeviceIdArray()
            openApiProjectPersonDeviceDto.setDeviceIdArray((collect.toArray(new String[0])));

            ProjectStaffDeviceVo projectStaffDeviceVo = BeanUtil.copyProperties(openApiProjectPersonDeviceDto, ProjectStaffDeviceVo.class);

            if (projectStaffDeviceVo.getEffTime() == null) {
                projectStaffDeviceVo.setEffTime(LocalDateTime.now());
            }

            boolean save = projectStaffDeviceProxyService.save(projectStaffDeviceVo);
            if (!save) {
                throw new OpenApiServiceException(String.format("新增员工通行权限失败, personId[%s]", personId));
            }

            personDeviceDtoList.add(openApiProjectPersonDeviceDto);
        }
        return R.ok(personDeviceDtoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<List<String>> staffRemoveList(OpenApiProjectPersonDeviceDto openApiProjectPersonDeviceDto) {

        //循环人员列表，新增人员通行权限
        for (String personId : openApiProjectPersonDeviceDto.getPersonIdList()) {
            String planId = projectPersonPlanRelService.getPlanIdByPersonId(personId);

            openApiProjectPersonDeviceDto.setPlanId(planId);
            openApiProjectPersonDeviceDto.setPersonId(personId);

            String[] deviceIdArray = new String[0];

            // 可删除设备只能是用户手动选择新增的设备，默认根据通行方案获取权限的设备不允许被删除
            if (ArrayUtil.isNotEmpty(openApiProjectPersonDeviceDto.getDeviceIdArray())) {
                // 校验传入设备Id，默认通行方案下的设备不允许删除返回报错
                R<Boolean> checkDevicePlanRo = checkDeviceIdArray(personId, planId, openApiProjectPersonDeviceDto.getDeviceIdArray());

                if (checkDevicePlanRo.getCode() != CommonConstants.SUCCESS) {
                    throw new OpenApiServiceException(checkDevicePlanRo.getMsg());
                }

                // 取设备id和数据库用户拥有设备的差集
                List<ProjectPersonDevice> list = this.list(new QueryWrapper<ProjectPersonDevice>().lambda()
                        .eq(ProjectPersonDevice::getPersonId, personId)
                        .eq(ProjectPersonDevice::getPersonType, PersonTypeEnum.STAFF.code)
                        .notIn(ProjectPersonDevice::getDeviceId, Arrays.asList(openApiProjectPersonDeviceDto.getDeviceIdArray())));

                if (!CollectionUtils.isEmpty(list)) {
                    List<String> collect = list.stream().map(ProjectPersonDevice::getDeviceId).collect(Collectors.toList());
                    deviceIdArray = collect.toArray(new String[0]);
                }

            }

            openApiProjectPersonDeviceDto.setDeviceIdArray(deviceIdArray);

            ProjectStaffDeviceVo projectStaffDeviceVo = BeanUtil.copyProperties(openApiProjectPersonDeviceDto, ProjectStaffDeviceVo.class);

            boolean save = projectStaffDeviceProxyService.save(projectStaffDeviceVo);

            if (!save) {
                log.error(String.format("estate 删除员工通行权限失败, personId[%s]", personId));
                throw new OpenApiServiceException(String.format("删除员工通行权限失败, personId[%s]", personId));
            }

        }

        return R.ok(openApiProjectPersonDeviceDto.getPersonIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<List<OpenApiProjectPersonDeviceDto>> proprietorSaveList(OpenApiProjectPersonDeviceDto openApiProjectPersonDeviceDto) {
        openApiProjectPersonDeviceDto.setPersonType(PersonTypeEnum.PROPRIETOR.code);

        // 返回对象
        List<OpenApiProjectPersonDeviceDto> personDeviceDtoList = new ArrayList<>();

        //循环人员列表，新增人员通行权限
        for (String personId : openApiProjectPersonDeviceDto.getPersonIdList()) {
            if (StringUtil.isEmpty(openApiProjectPersonDeviceDto.getPlanId())) {
                String planIdByPersonId = projectPersonPlanRelService.getPlanIdByPersonId(personId);
                if (StringUtil.isEmpty(planIdByPersonId)) {
                    // 没有设置通行权限默认 设置默认通行权限
                    String defaultPlanId = openApiProjectPassPlanService.getDefaultPlanId(PersonTypeEnum.PROPRIETOR.code);

                    openApiProjectPersonDeviceDto.setPlanId(defaultPlanId);
                } else {
                    openApiProjectPersonDeviceDto.setPlanId(planIdByPersonId);
                }
            } else {
                openApiProjectPersonDeviceDto.setPlanId(openApiProjectPersonDeviceDto.getPlanId());
            }

            openApiProjectPersonDeviceDto.setPersonId(personId);

            List<String> deviceIdArray = new ArrayList<>(Arrays.asList(openApiProjectPersonDeviceDto.getDeviceIdArray()));
            // 查数据库获取用户的设备列表 project_person_device
            LambdaQueryWrapper<ProjectPersonDevice> lambda = new QueryWrapper<ProjectPersonDevice>().lambda();
            lambda.eq(ProjectPersonDevice::getPersonId, personId);
            List<ProjectPersonDevice> list = this.list(lambda);
            // 和deviceIdArray 并集
            List<String> collect = deviceIdArray;
            if (CollectionUtil.isNotEmpty(list)) {
                deviceIdArray.addAll(list.stream().map(ProjectPersonDevice::getDeviceId).collect(Collectors.toList()));
                collect = deviceIdArray.stream().distinct().collect(Collectors.toList());
            }
            // set 回 openApiProjectPersonDeviceDto.setDeviceIdArray()
            openApiProjectPersonDeviceDto.setDeviceIdArray((collect.toArray(new String[0])));

            ProjectProprietorDeviceVo proprietorDeviceVo = BeanUtil.copyProperties(openApiProjectPersonDeviceDto, ProjectProprietorDeviceVo.class);

            if (proprietorDeviceVo.getEffTime() == null) {
                proprietorDeviceVo.setEffTime(LocalDateTime.now());
            }

            boolean saveProprietorDeviceResult = projectProprietorDeviceProxyService.save(proprietorDeviceVo);

            if (!saveProprietorDeviceResult) {
                log.error(String.format("estate 保存住户通行权限失败, personId[%s]", personId));
                throw new OpenApiServiceException(String.format("estate 保存住户通行权限失败, personId[%s]", personId));
            }
            personDeviceDtoList.add(openApiProjectPersonDeviceDto);
        }

        return R.ok(personDeviceDtoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<List<String>> proprietorRemoveList(OpenApiProjectPersonDeviceDto openApiProjectPersonDeviceDto) {
        openApiProjectPersonDeviceDto.setPersonType(PersonTypeEnum.PROPRIETOR.code);

        //循环人员列表，新增人员通行权限
        for (String personId : openApiProjectPersonDeviceDto.getPersonIdList()) {

            String planId = projectPersonPlanRelService.getPlanIdByPersonId(personId);

            openApiProjectPersonDeviceDto.setPlanId(planId);
            openApiProjectPersonDeviceDto.setPersonId(personId);

            String[] deviceIdArray = new String[0];

            if (openApiProjectPersonDeviceDto.getDeviceIdArray() != null) {
                // 校验传入设备Id，默认通行方案下的设备不允许删除返回报错
                R<Boolean> checkDevicePlanRo = checkDeviceIdArray(personId, planId, openApiProjectPersonDeviceDto.getDeviceIdArray());

                if (checkDevicePlanRo.getCode() != CommonConstants.SUCCESS) {
                    throw new OpenApiServiceException(checkDevicePlanRo.getMsg());
                }

                // 根据传入的设备id，取设备id和数据库用户拥有设备的差集
                List<ProjectPersonDevice> list = this.list(new QueryWrapper<ProjectPersonDevice>().lambda()
                        .eq(ProjectPersonDevice::getPersonId, personId)
                        .eq(ProjectPersonDevice::getPersonType, PersonTypeEnum.PROPRIETOR.code)
                        .notIn(ProjectPersonDevice::getDeviceId, Arrays.asList(openApiProjectPersonDeviceDto.getDeviceIdArray())));

                if (!CollectionUtils.isEmpty(list)) {
                    List<String> collect = list.stream().map(ProjectPersonDevice::getDeviceId).collect(Collectors.toList());
                    deviceIdArray = collect.toArray(new String[0]);
                }

            }
            openApiProjectPersonDeviceDto.setDeviceIdArray(deviceIdArray);

            ProjectProprietorDeviceVo proprietorDeviceVo = BeanUtil.copyProperties(openApiProjectPersonDeviceDto, ProjectProprietorDeviceVo.class);

            boolean saveProprietorDeviceResult = projectProprietorDeviceProxyService.save(proprietorDeviceVo);

            if (!saveProprietorDeviceResult) {
                log.error(String.format("estate 保存住户通行权限失败, personId[%s]", personId));
                throw new OpenApiServiceException(String.format("删除住户通行权限失败, personId[%s]", personId));
            }
        }

        return R.ok(openApiProjectPersonDeviceDto.getPersonIdList());
    }

    @Override
    public List<String> getDeviceIdByPersonId(String personId, String personType) {
        List<ProjectPersonDevice> list = this.list(new QueryWrapper<ProjectPersonDevice>().lambda()
                .eq(ProjectPersonDevice::getPersonId, personId)
                .eq(ProjectPersonDevice::getPersonType, PersonTypeEnum.STAFF.code)
        );
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().map(ProjectPersonDevice::getDeviceId).collect(Collectors.toList());
    }

    @Override
    public Boolean addPersonPassRightDevice(String personId, String personType, String planId) {
        // 根据人员id获取人员设备
        List<String> deviceIdList = openApiProjectPersonDeviceService.getDeviceIdByPersonId(personId, personType);

        String[] deviceIdArray = new String[]{};

        if (CollectionUtil.isNotEmpty(deviceIdList)) {
            deviceIdArray = deviceIdList.toArray(new String[0]);
        }

        OpenApiProjectPersonDeviceDto personDeviceDto = new OpenApiProjectPersonDeviceDto();

        personDeviceDto.setDeviceIdArray(deviceIdArray);
        personDeviceDto.setPlanId(planId);
        List<String> personIdList = new ArrayList<>();
        personIdList.add(personId);
        personDeviceDto.setPersonIdList(personIdList);

        R listRo;
        if (personType.equals(PersonTypeEnum.STAFF.code)) {
            listRo = openApiProjectPersonDeviceService.staffSaveList(personDeviceDto);
        } else if (personType.equals(PersonTypeEnum.PROPRIETOR.code)) {
            listRo = openApiProjectPersonDeviceService.proprietorSaveList(personDeviceDto);
        } else {
            return false;
        }

        return listRo.getCode() == CommonConstants.SUCCESS;
    }

    /**
     * 校验传入设备Id，默认通行方案下的设备不允许删除返回报错
     *
     * @param personId
     * @param planId
     * @param deviceIdArray
     * @return
     */
    private R<Boolean> checkDeviceIdArray(String personId, String planId, String[] deviceIdArray) {
        List<ProjectPassDeviceVo> deviceByPlanList = projectPersonDeviceService.listDeviceByPersonIdAndPlanIdOnlyPlan(personId, planId);

        if (CollectionUtil.isNotEmpty(deviceByPlanList)) {
            // 存在默认通行设备校验传入设备id是否包含默认通行设备，是则返回异常信息
            List<String> deviceIdList = deviceByPlanList.stream()
                    .map(ProjectPassDeviceVo::getDeviceId)
                    .filter(planDeviceId -> Arrays.asList(deviceIdArray).contains(planDeviceId))
                    .collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(deviceIdList)) {
                throw new OpenApiServiceException(String.format("无法删除默认通行设备设备，设备Id[%s]", Arrays.toString(deviceIdList.toArray())));
            }
        }

        return R.ok();
    }

}
