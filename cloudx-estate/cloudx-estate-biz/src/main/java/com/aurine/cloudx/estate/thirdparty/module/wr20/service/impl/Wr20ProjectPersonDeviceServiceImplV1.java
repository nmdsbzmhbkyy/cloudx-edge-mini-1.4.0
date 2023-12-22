package com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PassRightActiveTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectPersonDeviceDTO;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.service.impl.ProjectPersonDeviceServiceImpl;
import com.aurine.cloudx.estate.thirdparty.business.platform.BusinessBaseService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * WR20 权限服务
 */
@Service("wr20ProjectPersonDeviceServiceImplV1")
@Slf4j
public class Wr20ProjectPersonDeviceServiceImplV1 extends ProjectPersonDeviceServiceImpl implements ProjectPersonDeviceService, BusinessBaseService {
    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Resource
    private ProjectHousePersonRelService webProjectHousePersonRelService;
    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private WR20RemoteService wr20RemoteService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;


    /**
     * WR20变更权限业务
     *
     * @param personDeviceDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePersonDevice(ProjectPersonDeviceDTO personDeviceDTO) {

        log.info("[WR20] 不通过4.0变更通行权限");
        /**
         * 基于需求原型 1.6 2020-07-03
         * 用户不选择过期时间时，自动设置过期时间未2199-01-01
         * @author: 王伟 2020-07-06
         */
        if (personDeviceDTO.getExpTime() == null) {
            personDeviceDTO.setExpTime(DateUtil.toLocalDateTime(DateUtil.parse("2199-01-01", "yyyy-MM-dd")));
        }
//
//        ProjectPersonDevice projectPersonDevice;
//        List<ProjectPersonDevice> projectPersonDeviceList = new ArrayList<>();
//        List<ProjectPersonDevice> originalProjectPersonDeviceList = this.listByPersonId(personDeviceDTO.getPersonId()); //最初的设备列表
//        List<ProjectPersonDevice> delProjectPersonDeviceList = new ArrayList<>();//要删除的设备列表
//        List<ProjectPassDeviceVo> deviceByPlanList = new ArrayList<>();
//
//
//        boolean isExist = false;
//        //删除旧数据
//        this.deleteByPersonId(personDeviceDTO.getPersonId());
//
//        String planId = personDeviceDTO.getPlanId();
//        String[] deviceIdArray = personDeviceDTO.getDeviceIdArray();
//
//
//        //如果是未配置过的用户，默认给激活状态
//        if (StringUtil.isEmpty(personDeviceDTO.getIsActive())) {
//            personDeviceDTO.setIsActive(PassRightActiveTypeEnum.ACTIVED.code);
//        }
//
////WR20不进行授权控制
////        /**
////         * 只保存用户手动选择的数据
////         */
////        //转换用户选择的设备列表
////        if (deviceIdArray != null && deviceIdArray.length > 0) {
////
////            //设备数组去重复，应对前端可能出现的问题
////            List<String> deviceIdList = new ArrayList<>();
////            for (int i = 0; i < deviceIdArray.length; i++) {
////                if (!deviceIdList.contains(deviceIdArray[i])) {
////                    deviceIdList.add(deviceIdArray[i]);
////                }
////            }
////
////            deviceByPlanList = this.listDeviceByPersonIdAndPlanId(personDeviceDTO.getPersonId(), planId);
////
////            for (String id : deviceIdList) {
////
////                //如果设备已经存在于方案列表中，不在写入系统
////                isExist = deviceByPlanList.stream().anyMatch(e -> e.getDeviceId().equals(id));
////                if (isExist) {
////                    continue;
////                }
////
////                projectPersonDevice = new ProjectPersonDevice();
////                BeanUtils.copyProperties(personDeviceDTO, projectPersonDevice);
////                projectPersonDevice.setDeviceId(id);
////                projectPersonDevice.setStatus("1");
////                projectPersonDevice.setPlanId(""); //用户选择的设备，且不在方案之中
////                projectPersonDevice.setEffTime(personDeviceDTO.getEffTime());
////                projectPersonDevice.setExpTime(personDeviceDTO.getExpTime());
////                projectPersonDeviceList.add(projectPersonDevice);
////            }
////        }
////
////        if (projectPersonDeviceList.size() > 0) {
////            this.saveBatch(projectPersonDeviceList);
////        }
//
////        WR20 不控制凭证下发
////        //下发变更
////        List<String> deviceIdList = new ArrayList<>();
////
////        //方案选择
////        if (CollectionUtil.isNotEmpty(deviceByPlanList)) {
////            deviceIdList.addAll(deviceByPlanList.stream().map(ProjectPassDeviceVo::getDeviceId).collect(Collectors.toList()));
////        }
////        //用户手动选择
////        if (CollectionUtil.isNotEmpty(projectPersonDeviceList)) {
////            deviceIdList.addAll(projectPersonDeviceList.stream().map(ProjectPersonDevice::getDeviceId).collect(Collectors.toList()));
////        }
////
////        //下发凭证（WR20对接中仅记录要下发的数据，对应改为删除中和下载中）
////        projectRightDeviceService.saveRelationship(deviceIdList, personDeviceDTO.getPersonId());
//
//
//        /**
//         * WR20新增部分
//         * //           {"ID":["3","4"],"DeviceNo":["000000003","010100000"],"IsEnable":"1"}
//         */
//
//
//        List<String> oriDeviceIdList = originalProjectPersonDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());
//        List<String> newDeviceIdList = projectPersonDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());
//        List<String> newDeviceNoList = new ArrayList<>();
//        List<String> oriDeviceNoList = new ArrayList<>();
//
//        if (CollUtil.isNotEmpty(oriDeviceIdList)) {
//            List<ProjectDeviceInfo> originalDeviceList = projectDeviceInfoService.list(new QueryWrapper<ProjectDeviceInfo>().lambda().in(ProjectDeviceInfo::getDeviceId, oriDeviceIdList));
//            if (CollUtil.isNotEmpty(originalDeviceList)) {
//                oriDeviceNoList = originalDeviceList.stream().map(e -> e.getThirdpartyCode()).collect(Collectors.toList());
//            }
//        }
//        if (CollUtil.isNotEmpty(newDeviceIdList)) {
//            List<ProjectDeviceInfo> newDeviceList = projectDeviceInfoService.list(new QueryWrapper<ProjectDeviceInfo>().lambda().in(ProjectDeviceInfo::getDeviceId, newDeviceIdList));
//            if (CollUtil.isNotEmpty(newDeviceList)) {
//                newDeviceNoList = newDeviceList.stream().map(e -> e.getThirdpartyCode()).collect(Collectors.toList());
//            }
//        }
//
//        //获取需要新增的设备列表 =  新列表 - 原列表
//        JSONArray personCodeParams = new JSONArray();
//        JSONArray deviceNoParams = new JSONArray();
//        JSONObject param = new JSONObject();
//
//        for (String deviceNo : CollUtil.subtract(newDeviceNoList, oriDeviceNoList)) {
//            //调用新增
//            deviceNoParams.add(deviceNo);
//        }
//
//        if (personDeviceDTO.getPersonType().equals(PersonTypeEnum.PROPRIETOR.code)) {
//            //住户内容
//            List<ProjectHousePersonRel> housePersonRelList = webProjectHousePersonRelService.listHousePersonByPersonId(personDeviceDTO.getPersonId());
//            //处理人员对象信息
//            for (ProjectHousePersonRel housePersonRel : housePersonRelList) {
//                if (StringUtils.isNotEmpty(housePersonRel.getRelaCode())) {
//                    personCodeParams.add(housePersonRel.getRelaCode());
//                }
//            }
//        } else {
//            ProjectStaff staff = projectStaffService.getById(personDeviceDTO.getPersonId());
//            if (staff == null) {
//                throw new RuntimeException("当前员工不存在");
//            } else if (StringUtils.isEmpty(staff.getStaffCode())) {
//                log.error("[WR20] 员工第三方ID为空：{}", staff);
//                throw new RuntimeException("当前员工数据异常，请联系管理员");
//            }
//
//            personCodeParams.add(staff.getStaffCode());
//        }


//        if (CollUtil.isNotEmpty(deviceNoParams)) {
//            param.put("ID", personCodeParams);
//            param.put("deviceNo", deviceNoParams);
//            param.put("isEnable", "1");
//            log.info("[WR20] 添加权限：{}", param);
//            wr20RemoteService.permission(ProjectContextHolder.getProjectId(), param, PersonTypeEnum.getEnum(personDeviceDTO.getPersonType()));
//        }


//        //获取需要删除的设备列表 新列表 - 原列表
//        deviceNoParams = new JSONArray();
//        param = new JSONObject();
//
//        for (String deviceId : CollUtil.subtract(oriDeviceNoList, newDeviceNoList)) {
//            //调用删除
//            deviceNoParams.add(deviceId);
//        }

//        if (CollUtil.isNotEmpty(deviceNoParams)) {
//            param.put("ID", personCodeParams);
//            param.put("deviceNo", deviceNoParams);
//            param.put("isEnable", "0");
//            log.info("[WR20] 删除权限：{}", param);
//            wr20RemoteService.permission(ProjectContextHolder.getProjectId(), param, PersonTypeEnum.getEnum(personDeviceDTO.getPersonType()));
//        }

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

        /**
         * WR20新增业务
         */
        JSONObject param = null;
        //获取如果是住户类型，获得该人员的所有房屋关系
        if (PersonTypeEnum.PROPRIETOR.code.equals(personType)) {

            List<ProjectHousePersonRel> housePersonRelList = webProjectHousePersonRelService.listHousePersonByPersonId(personId);

            //停用或启用该人员的全部房屋关联
            for (ProjectHousePersonRel housePersonRel : housePersonRelList) {
                if (StringUtils.isNotEmpty(housePersonRel.getRelaCode())) {
                    //逐个调用WR20，停用接口
                    param = new JSONObject();
                    param.put("ID", housePersonRel.getRelaCode());
                    param.put("isEnable", "0");
                    param.put("personType", "0");
                    wr20RemoteService.passCtrl(ProjectContextHolder.getProjectId(), param, PersonTypeEnum.getEnum(personType), false);
                }
            }

        } else {
//            log.error("[WR20] 无停用员工通行权限接口");
//            throw new RuntimeException("暂时无法禁用员工通行权限");
            ProjectStaff staff = projectStaffService.getById(personId);
            param = new JSONObject();
            param.put("ID", staff.getStaffCode());
            param.put("deviceNo", new JSONArray());
            param.put("isEnable", "0");
            param.put("personType", "1");
            wr20RemoteService.passCtrl(ProjectContextHolder.getProjectId(), param, PersonTypeEnum.getEnum(personType), false);
        }

        //下发权限变更
//        return projectRightDeviceService.deactive(personId);
        return true;
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

        /**
         * WR20新增业务
         */
        JSONObject param = null;
        //获取如果是住户类型，获得该人员的所有房屋关系
        if (PersonTypeEnum.PROPRIETOR.code.equals(personType)) {

            List<ProjectHousePersonRel> housePersonRelList = webProjectHousePersonRelService.listHousePersonByPersonId(personId);

            //停用或启用该人员的全部房屋关联
            for (ProjectHousePersonRel housePersonRel : housePersonRelList) {
                if (StringUtils.isNotEmpty(housePersonRel.getRelaCode())) {
                    //逐个调用WR20，停用接口
                    param = new JSONObject();
                    param.put("ID", housePersonRel.getRelaCode());
                    param.put("isEnable", "1");
                    param.put("personType", "0");
                    wr20RemoteService.passCtrl(ProjectContextHolder.getProjectId(), param, PersonTypeEnum.getEnum(personType), true);
                }
            }

        } else {
            //获取员工的第三方id
//            log.error("[WR20] 无启用员工通行权限接口");
//            throw new RuntimeException("暂时无法启用员工通行权限");
            ProjectStaff staff = projectStaffService.getById(personId);
            param = new JSONObject();
            param.put("ID", staff.getStaffCode());
            param.put("deviceNo", new JSONArray());
            param.put("isEnable", "1");
            param.put("personType", "1");
            wr20RemoteService.passCtrl(ProjectContextHolder.getProjectId(), param, PersonTypeEnum.getEnum(personType), true);
        }


        //下发权限变更
//        return projectRightDeviceService.active(personId);
        return true;

    }


    @Override
    protected Class<ProjectPersonDevice> currentModelClass() {
        return ProjectPersonDevice.class;
    }

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    /**
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.BUSINESS_WR20.code;
    }
}
