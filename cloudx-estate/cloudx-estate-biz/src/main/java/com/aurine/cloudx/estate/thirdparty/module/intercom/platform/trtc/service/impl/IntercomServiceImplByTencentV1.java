package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;

import com.aurine.cloudx.estate.dto.ProjectUserHouseDTO;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.IntercomService;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.entity.constant.TencentErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.entity.dto.TencentRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.remote.TencentIntercomRemoteService;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.remote.factory.TencentRemoteServiceFactory;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.ProjectHouseInfoVo;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;

/**
 * 冠林中台 V1 版本 对接业务实现
 *
 * @ClassName: PassWayDeviceServiceImplByAurineV1
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-12 14:00
 * @Copyright:
 */
@Service
@Slf4j
@RefreshScope
public class IntercomServiceImplByTencentV1 implements IntercomService {
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;

    @Resource
    private ProjectHouseInfoService projectHouseInfoService;

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;

    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;

    @Resource
    ProjectDeviceInfoService projectDeviceInfoService;

    private Integer enabled = 1; //0禁用 1可用
    @Resource

    private RemoteUserService remoteUserServic;


    private String rtcCode = "TRTC";
    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    public VersionEnum getVer() {
        return VersionEnum.V1;
    }

    /**
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.INTERCOM_TENCENT.code;
    }


    /**
     * 添加项目
     * 如果项目下存在住户设备，根据规则添加住户和设备的云对讲关系
     *
     * @param projectId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addProject(Integer projectId) {
        // 基本参数校验
        ProjectInfo projectInfo = projectInfoService.getProjectInfoVoById(projectId);
        if (StringUtils.isBlank(projectInfo.getProjectName())) {
            log.error("[腾讯云对讲] 项目{}不存在", projectId);
            throw new RuntimeException("项目启用失败");
        }
        // 添加项目
        TencentRespondDTO tencentRespondDTO = TencentRemoteServiceFactory.getInstance(getVer()).saveProject(enabled,
                projectInfo.getProjectId().toString(), projectInfo.getProjectName(), rtcCode);
        // 项目已存在 启用项目
        if (!checkResult(tencentRespondDTO)) {
            // 远程调用 启用项目
            TencentRespondDTO updateProject = TencentRemoteServiceFactory.getInstance(getVer()).updateProject(1, projectId.toString(), null, rtcCode);
            if (!checkResult(updateProject)) {
                log.error("[腾讯云对讲] 项目{}启用失败 ", projectId);
                throw new RuntimeException("项目启用失败");
            }
        }
        // 查询到项目下的(与云对讲有关的区口机,梯口机)设备
        List<ProjectDeviceInfoProxyVo> deviceInfoList = projectDeviceInfoProxyService.listAllDevice(projectId);

        // 根据设备添加房屋 添加住户 关联住户与房屋 住户与设备的关系
        if (CollUtil.isNotEmpty(deviceInfoList)) {
            // 添加房屋 添加住户 处理关系数据
            Map<String, Map<String, Set<String>>> telephoneMap = copePersonDevicesRoomsRel(deviceInfoList, projectId, true);
            // 远程调用 批量导入项目下人员与设备，人员与房屋的关系
            if (telephoneMap != null) {
                TencentRespondDTO saveUsersAndDevicesRoomsRel = TencentRemoteServiceFactory.getInstance(getVer()).saveUsersAndDevicesRoomsRel(telephoneMap, projectId, enabled, rtcCode);
                if (!checkResult(saveUsersAndDevicesRoomsRel)) {
                    log.error("[腾讯云对讲] 项目{}批量关联房屋-住户，设备-住户失败  {}", projectInfo.getProjectId(), saveUsersAndDevicesRoomsRel);
                    throw new RuntimeException("项目启用失败");
                }
            }
        }
        log.info("[腾讯云对讲] 项目{}启用成功 {}  ", projectInfo.getProjectId(), projectInfo.getProjectName());
        return true;
    }

    /**
     * 删除项目
     * 如果项目下存在设备、住户，这按照关系、房屋、住户、设备、项目顺序依次清空
     *
     * @param projectId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delProject(Integer projectId) {
        // 基本参数校验
        ProjectInfo projectInfo = projectInfoService.getProjectInfoVoById(projectId);
        log.info("项目信息");
        if (StringUtils.isEmpty(projectInfo.getProjectName())) {
            log.error("[腾讯云对讲] 项目{}不存在  ", projectId);
            throw new RuntimeException("项目不存在，请联系管理员");
        }
        // 查询到项目下的(与云对讲有关的区口机,梯口机)设备
        List<ProjectDeviceInfoProxyVo> deviceInfoList = projectDeviceInfoProxyService.listAllDevice(projectId);
        for (ProjectDeviceInfoProxyVo deviceInfo : deviceInfoList) {
            List<ProjectUserHouseDTO> devicePersonInfoList = projectPersonDeviceService.getDevicePersonInfoList(deviceInfo);
            Set<String> registerIdSet = new HashSet<>();
            for (ProjectUserHouseDTO projectUserHouseDTO:devicePersonInfoList) {
                R<SysUser> user = remoteUserServic.user(projectUserHouseDTO.getTelephone());
                log.info("用户信息{}",user.getData());
                if(ObjectUtil.isNotNull(user.getData())){
                    registerIdSet.add(user.getData().getUserId().toString());
                }
            }
            List<String> registerIdList = new ArrayList<>(registerIdSet);
            // 批量解绑设备下的人员
            TencentRespondDTO removeUserListFromDevice = TencentRemoteServiceFactory.getInstance(getVer()).removeUserListFromDevice(getDeviceIdByDeviceSn(deviceInfo.getSn()), projectId.toString(), registerIdList, registerIdList);
            if (!checkResult(removeUserListFromDevice)) {
                log.error("[腾讯云对讲] 项目{}解绑失败  ", projectId);
                throw new RuntimeException("项目解绑失败，请联系管理员");
            }
        }
        // 远程调用 关闭项目
        TencentRespondDTO updateProject = TencentRemoteServiceFactory.getInstance(getVer()).updateProject(0, projectId.toString(), null, rtcCode);
        // 远程调用 关闭项目成功
        if (!checkResult(updateProject)) {
            log.error("[腾讯云对讲] 项目{}关闭失败 {} ", projectId);
            throw new RuntimeException("项目关闭失败，请联系管理员");
        }
        return true;
    }

    /**
     * 添加设备
     * 已存在住户，需要管理住户、设备关系
     *
     * @param deviceId
     * @param projectId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDevice(String deviceId, Integer projectId) {
        // 获取设备对象，并校验设备
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);
        if (deviceInfo == null) {
            log.error("[腾讯云对讲] 根据设备查询可用住户出错 设备{}不存在", deviceId);
            throw new RuntimeException("设备不存在，请联系管理员");
        }
        // 设备启用
        String productName = projectDeviceInfoService.getProductIdByDeviceSn(deviceInfo.getSn());
        if (StringUtils.isEmpty(productName)) {
            log.error("[腾讯云对讲] 项目{} 设备{} {}中sn{}未查询到对应productId", projectId, deviceInfo.getDeviceId(), deviceInfo.getDeviceName(),deviceInfo.getSn());
            throw new RuntimeException("设备"+deviceInfo.getSn()+"无对应产品，请联系管理员");
        }
        TencentRespondDTO updateDevice = TencentRemoteServiceFactory.getInstance(getVer()).updateDevice(deviceInfo.getSn(), deviceInfo.getProjectId().toString(), deviceInfo.getDeviceName(), null,
                deviceInfo.getSn(), 1, deviceInfo.getProjectId().toString());
        if (checkResult(updateDevice)) {
            log.error("[腾讯云对讲] 项目{}启用设备{}失败", projectId,deviceId);
            throw new RuntimeException("启用设备失败，请联系管理员");
        }
        // 查询设备下的住户
        List<ProjectUserHouseDTO> devicePersonInfoList = projectPersonDeviceService.getDevicePersonInfoList(deviceInfo);
        if (ObjectUtil.isEmpty(devicePersonInfoList)) {
            log.info("[腾讯云对讲] 项目添加设备　项目{} 下不存在订阅云对讲的住户和房屋，添加设备完成", projectId);
            return true;
        }
        // 设备关联用户
        Set<String> registerIdSet = new HashSet<>();
        for (ProjectUserHouseDTO projectUserHouseDTO:devicePersonInfoList) {
            R<SysUser> user = remoteUserServic.user(projectUserHouseDTO.getTelephone());
            registerIdSet.add(user.getData().getUserId().toString());
        }
        List<String> registerIdList = new ArrayList<>(registerIdSet);
        // 批量绑定设备下的住户
        TencentRespondDTO saveUserListToDevice = TencentRemoteServiceFactory.getInstance(getVer()).saveUserListToDevice(deviceInfo.getSn(), projectId.toString(), registerIdList, registerIdList);
        if (!checkResult(saveUserListToDevice)) {
            log.error("[腾讯云对讲] 项目{}设备{}绑定住户失败", projectId,deviceId);
            throw new RuntimeException("设备绑定住户失败，请联系管理员");
        }
        return true;
    }

    /**
     * 删除设备
     * 已存在住户，需要管理住户、设备关系
     *
     * @param deviceId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delDevice(String deviceId) {
        // 设备基本校验
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);
        if (deviceInfo == null) {
            log.error("[腾讯云对讲] 设备{}不存在", deviceId);
            throw new RuntimeException("设备不存在，请联系管理员");
        }
        // 设备禁用
        String productName = projectDeviceInfoService.getProductIdByDeviceSn(deviceInfo.getSn());
        if (StringUtils.isEmpty(productName)) {
            log.error("[腾讯云对讲] 项目{} 设备{} {}中sn{}未查询到对应productId", deviceInfo.getProductId(), deviceInfo.getDeviceId(), deviceInfo.getDeviceName(),deviceInfo.getSn());
            throw new RuntimeException("设备"+deviceInfo.getSn()+"无对应产品，请联系管理员");
        }
        TencentRespondDTO updateDevice = TencentRemoteServiceFactory.getInstance(getVer()).updateDevice(deviceInfo.getSn(), deviceInfo.getProjectId().toString(), deviceInfo.getDeviceName(), null,
                deviceInfo.getSn(), 0, deviceInfo.getProjectId().toString());
        if (!checkResult(updateDevice)) {
            log.error("[腾讯云对讲] 设备{}禁用失败", deviceInfo.getSn());
            throw new RuntimeException("设备禁用失败，请联系管理员");
        }
        // 获取设备下的住户信息
        List<ProjectUserHouseDTO> devicePersonInfoList = projectPersonDeviceService.getDevicePersonInfoList(deviceInfo);
        if (ObjectUtil.isEmpty(devicePersonInfoList)) {
            log.info("[腾讯云对讲] 设备{}  {} 下不存在订阅云对讲的住户和房屋，删除设备完成", deviceInfo.getDeviceId(),deviceInfo.getDeviceName());
            return true;
        }
        // 住户去重
        Set<String> registerIdSet = new HashSet<>();
        for (ProjectUserHouseDTO projectUserHouseDTO:devicePersonInfoList) {
            R<SysUser> user = remoteUserServic.user(projectUserHouseDTO.getTelephone());
            registerIdSet.add(user.getData().getUserId().toString());
        }
        List<String> registerIdList = new ArrayList<>(registerIdSet);
        // 批量解绑设备下的人员
        TencentRespondDTO removeUserListFromDeviceRespond = TencentRemoteServiceFactory.getInstance(getVer()).removeUserListFromDevice(deviceInfo.getSn(), deviceInfo.getProjectId().toString(), registerIdList, registerIdList);
        if (!checkResult(removeUserListFromDeviceRespond)){
            log.error("[腾讯云对讲] 项目{} 设备{}下解除关联住户{}，解除关联失败", deviceInfo.getProjectId(), deviceInfo.getDeviceId(), registerIdList.toArray());
            throw new RuntimeException("设备解绑失败，请联系管理员");
        }
        return true;
    }

    /**
     * 添加住户
     * 增加住户所有可用设备 房屋关系
     * 1。保存住户 2。天假住户与该房屋下有关的设备
     *
     * @param projectHousePersonRel 房屋住户关系
     * @param projectId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPerson(ProjectHousePersonRel projectHousePersonRel, Integer projectId) {
        // 获取住户和设备房屋的关系
        List<ProjectUserHouseDTO> personDeviceInfoList = projectPersonDeviceService.getPersonDeviceInfoList(projectHousePersonRel);
        if (personDeviceInfoList.size() < 1) {
            log.error("[腾讯云对讲] 项目{} 用户{}不存在",projectId , projectHousePersonRel.getPersonId());
            throw new RuntimeException("用户不存在，请联系管理员");
        }
        // 保存住户信息
        ProjectUserHouseDTO projectUserHouseDTO = personDeviceInfoList.get(0);
        R<SysUser> user = remoteUserServic.user(projectUserHouseDTO.getTelephone());
        if (user.getData().getUserId() == null) {
            //处理方式一：抛出异常
//            throw new RuntimeException("账户" + projectUserHouseDTO.getTelephone() + "云对讲未注册，不能添加该住户信息");
            //优化处理方式：远程调用注册该手机的住户到云对讲
            TencentRemoteServiceFactory.getInstance(getVer()).saveAppUserRegister(projectUserHouseDTO.getTelephone(), rtcCode);
            user = remoteUserServic.user(projectUserHouseDTO.getTelephone());    //这里不清楚是否能从上行返回值获取到userid后传入
        }
        TencentRespondDTO saveUser = TencentRemoteServiceFactory.getInstance(getVer()).saveUser(rtcCode, projectId.toString(), enabled, projectId.toString(), user.getData().getUserId(),projectUserHouseDTO.getTelephone(),
                projectUserHouseDTO.getPeopleTypeCode(), null, null, projectUserHouseDTO.getPicUrl(), projectUserHouseDTO.getPersonName(), null);
        if (!checkResult(saveUser)) {
            log.error("[腾讯云对讲] 项目{} 用户{}保存失败",projectId , projectHousePersonRel.getPersonId());
            throw new RuntimeException("用户保存失败，请联系管理员");
        }
        // 批量保存用户与设备的关系
        HashSet<String> deviceIdsSet = new HashSet<>();
        for (ProjectUserHouseDTO projectPersonDeviceInfo : personDeviceInfoList) {
            deviceIdsSet.add(getDeviceIdByDeviceSn(projectPersonDeviceInfo.getSn()));
        }
        ArrayList<String> deviceIds = new ArrayList<>(deviceIdsSet);
        ArrayList<String> roomIds = new ArrayList<>();
        roomIds.add(projectUserHouseDTO.getHouseCode());
        TencentRespondDTO saveUserAndDevicesRoomsRel = TencentRemoteServiceFactory.getInstance(getVer()).saveUserAndDevicesRoomsRel(deviceIds, roomIds, rtcCode, projectId.toString(), enabled, projectId.toString(),
                user.getData().getUserId().toString(), projectUserHouseDTO.getPeopleTypeCode(), null, null, projectUserHouseDTO.getPicUrl(), projectUserHouseDTO.getPersonName(), projectUserHouseDTO.getPersonName());
        if (!checkResult(saveUserAndDevicesRoomsRel)) {
            log.error("[腾讯云对讲] 项目{} 用户{}与设备{}绑定失败",projectId ,deviceIds.toArray());
            throw new RuntimeException("用户与设备绑定失败，请联系管理员");
        }
        return true;
    }

    /**
     * 删除住户
     * 末套房，需要注销人员
     * 多套房，删除人员设备配置，删除人员房屋配置等
     *
     * @param personHouseRelId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delPerson(String personHouseRelId) {
        // 获取住户和房屋的关联信息
        ProjectHousePersonRel projectHousePersonRel = projectHousePersonRelService.getById(personHouseRelId);
        // 查询该住户是多套房住户还是首套房住户
        List<ProjectHousePersonRel> projectHousePersonRels = projectHousePersonRelService.listHousePersonByPersonId(projectHousePersonRel.getPersonId());
        // 当前房屋关联设备
        List<ProjectUserHouseDTO> personDeviceInfoList = projectPersonDeviceService.getPersonDeviceInfoList(projectHousePersonRel);
        List<String> registerId = new ArrayList<>();
        R<SysUser> user = remoteUserServic.user(personDeviceInfoList.get(0).getTelephone());
        registerId.add(user.getData().getUserId().toString());
        // 多套房住户迁出
        if (projectHousePersonRels.size() > 1) {
            // 筛选要删除的设备 0 删除 1不删除
            Map<String, Integer> removeMap = new HashMap<>();
            for (ProjectUserHouseDTO projectUserHouseDTO:personDeviceInfoList) {
                removeMap.put(projectUserHouseDTO.getSn(), 0);
            }
            for (ProjectHousePersonRel housePersonRel:projectHousePersonRels) {
                if (!housePersonRel.equals(projectHousePersonRel)) {
                    List<ProjectUserHouseDTO> otherPersonDeviceInfoList = projectPersonDeviceService.getPersonDeviceInfoList(projectHousePersonRel);
                    for (ProjectUserHouseDTO projectUserHouseDTO:otherPersonDeviceInfoList) {
                        if (removeMap.containsKey(projectUserHouseDTO.getSn())) {
                            removeMap.put(projectUserHouseDTO.getSn(),1);
                        }
                    }
                }
            }
            // 解除设备与用户之间的关联
            for (Map.Entry<String, Integer> entry : removeMap.entrySet()) {
                if (entry.getValue().equals("0")) {
                    // 解除用户与设备之间的关系
                    TencentRespondDTO removeUserListFromDevice = TencentRemoteServiceFactory.getInstance(getVer()).removeUserListFromDevice(getDeviceIdByDeviceSn(entry.getKey()), personDeviceInfoList.get(0).getProjectId(), registerId, registerId);
                    if (!checkResult(removeUserListFromDevice)) {
                        log.error("[腾讯云对讲] 项目{} 用户{}与设备{}解绑失败", personDeviceInfoList.get(0).getProjectId(), registerId.toArray(), entry.getKey());
                        throw new RuntimeException("用户与设备解绑失败，请联系管理员");
                    }
                }
            }
        } else {
            // 单套房住户迁出
            // 解除设备与用户之间的关联
            for (ProjectUserHouseDTO projectUserHouseDTO:personDeviceInfoList) {
                TencentRespondDTO removeUserListFromDevice = TencentRemoteServiceFactory.getInstance(getVer()).removeUserListFromDevice(getDeviceIdByDeviceSn(projectUserHouseDTO.getSn()), personDeviceInfoList.get(0).getProjectId(), registerId, registerId);
                if (!checkResult(removeUserListFromDevice)) {
                    log.error("[腾讯云对讲] 项目{} 用户{}与设备{}解绑失败", personDeviceInfoList.get(0).getProjectId(), registerId.get(0), projectUserHouseDTO.getSn());
                    throw new RuntimeException("用户与设备解绑失败，请联系管理员");
                }
            }
        }
        // 远程调用 解除房屋与住户之间的关联
        TencentRespondDTO removeUserListToRoom = TencentRemoteServiceFactory.getInstance(getVer()).removeUserListToRoom(personDeviceInfoList.get(0).getProjectId(), registerId, personDeviceInfoList.get(0).getHouseCode(), registerId);
        if (!checkResult(removeUserListToRoom)) {
            log.error("[腾讯云对讲] 项目{} 用户{}与房屋{}解绑失败", personDeviceInfoList.get(0).getProjectId(), registerId.get(0), personDeviceInfoList.get(0).getHouseCode());
            throw new RuntimeException("用户与房屋解绑失败，请联系管理员");
        }
        return true;
    }

    /**
     * 删除某个房屋下的所有住户的云对讲(关闭云对讲)
     *
     * @param houseId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delByHouse(String houseId) {
        // 基本参数校验
        ProjectHouseInfoVo houseInfoVo = projectHouseInfoService.getVoById(houseId);
        if (houseInfoVo == null) {
            log.error("[腾讯云对讲] 房屋{}不存在，关闭云对讲失败", houseId);
            throw new RuntimeException("关闭服务失败，请联系管理员");
        }
        // 获取房屋下住户和房屋的关联信息列表
        List<ProjectHousePersonRel> projectHousePersonRels = projectHousePersonRelService.getHousePersonRelListByHouseId(houseId);
        for (ProjectHousePersonRel projectHousePersonRel:projectHousePersonRels) {
            // 删除住户的设备关联（单套房 多套房）
            this.delPerson(projectHousePersonRel.getRelaId());
        }
        // 远程调用 房屋设置为禁用
        TencentRespondDTO updateRoom = TencentRemoteServiceFactory.getInstance(getVer()).updateRoom(0, houseInfoVo.getProjectId().toString(), houseInfoVo.getHouseCode(), houseInfoVo.getHouseName());
        if (!checkResult(updateRoom)) {
            log.error("[腾讯云对讲] 房屋{} {}禁用失败，未找到该房屋", houseId, houseInfoVo.getHouseName());
            throw new RuntimeException("关闭服务失败,请联系管理员");
        }
        return true;
    }

    /**
     * 为某个房间下所有住户添加云对讲（打开云对讲）
     * 已存在的业主不再同步
     *
     *
     * @param houseId
     * @param projectId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addByHouse(String houseId, Integer projectId) {
        // 基本参数校验
        ProjectHouseInfoVo houseInfoVo = projectHouseInfoService.getVoById(houseId);
        if (houseInfoVo == null) {
            log.error("[腾讯云对讲] 房屋{}不存在，关闭云对讲失败", houseId);
            throw new RuntimeException("启用服务失败,请联系管理员");
        }
        // 远程调用 添加房屋
        TencentRespondDTO tencentRespondDTO = TencentRemoteServiceFactory.getInstance(getVer()).saveRoom(enabled, projectId.toString(), houseInfoVo.getHouseCode(), houseInfoVo.getHouseName());
        if (!checkResult(tencentRespondDTO)) {
            // 添加失败 启用房屋
            tencentRespondDTO=TencentRemoteServiceFactory.getInstance(getVer()).updateRoom(enabled, projectId.toString(), houseInfoVo.getHouseCode(), houseInfoVo.getHouseName());
        }
        if (!checkResult(tencentRespondDTO)) {
            // 添加房屋和启用房屋均失败
            log.error("[腾讯云对讲] 项目{}下的房屋{}打开云对讲失败", projectId,houseId);
            throw new RuntimeException("启用服务失败,请联系管理员");
        }

        Map<String, Map<String, Set<String>>> telephoneMap = new HashMap<>();
        // 房屋下的设备和用户
        Set<String> deviceIds = new HashSet<>();
        Set<String> registerIds = new HashSet<>();
        Set<String> roomIds = new HashSet<>();
        roomIds.add(houseInfoVo.getHouseCode());
        Set<String> userType = new HashSet<>();
        userType.add("0");
        // 获取房屋下的用户
        List<ProjectHousePersonRel> projectHousePersonRels = projectHousePersonRelService.getHousePersonRelListByHouseId(houseId);
        for (ProjectHousePersonRel projectHousePersonRel:projectHousePersonRels) {
            // 根据用户id和房屋id获取到设备
            List<ProjectUserHouseDTO> personDeviceInfoList = projectPersonDeviceService.getPersonDeviceInfoList(projectHousePersonRel);
            if (CollUtil.isNotEmpty(personDeviceInfoList)) {
                for (ProjectUserHouseDTO projectPersonDeviceInfo : personDeviceInfoList) {
                    // 房屋下关联的设备
                    deviceIds.add(getDeviceIdByDeviceSn(projectPersonDeviceInfo.getSn()));
                    R<SysUser> user = remoteUserServic.user(projectPersonDeviceInfo.getTelephone());
                    registerIds.add(user.getData().getUserId().toString());
                }
            }
        }
        Map registerInfo = new HashMap();
        registerInfo.put("deviceIds", deviceIds);
        registerInfo.put("roomIds", roomIds);
        registerInfo.put("userType", userType);
        for (String registerId:registerIds) {
            telephoneMap.put(registerId, registerInfo);
        }

        TencentRespondDTO saveUsersAndDevicesRoomsRel = TencentRemoteServiceFactory.getInstance(getVer()).saveUsersAndDevicesRoomsRel(telephoneMap, projectId, enabled, rtcCode);
        if (!checkResult(saveUsersAndDevicesRoomsRel)) {
            log.error("[腾讯云对讲] 项目{}下的房屋设备人员绑定失败", projectId);
            throw new RuntimeException("启用服务失败,请联系管理员");
        }
        return true;
    }

    /**
     * deviceInfoList的用户 设备 房屋之间的关系处理，对数据去重,增加房屋 设备 人员
     *
     * @param deviceInfoList
     * @param projectId
     * @param addUserHouse
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private Map<String, Map<String, Set<String>>> copePersonDevicesRoomsRel(List<ProjectDeviceInfoProxyVo> deviceInfoList, Integer projectId, boolean addUserHouse) {
        Map<String, Map<String, Set<String>>> telephoneMap = new HashMap<>();
        for (ProjectDeviceInfoProxyVo deviceInfo : deviceInfoList) {
            List<ProjectUserHouseDTO> devicePersonInfoList = projectPersonDeviceService.getDevicePersonInfoList(deviceInfo);
            if (ObjectUtil.isEmpty(devicePersonInfoList)) {
                log.info("[腾讯云对讲] 项目{} 设备{} {}下不存在云对讲服务的住户和房屋", projectId, deviceInfo.getDeviceId(), deviceInfo.getDeviceName());
                continue;
            }
            R<SysUser> user = new R<>();
            for (ProjectUserHouseDTO projectUserHouseDTO : devicePersonInfoList) {
                if (addUserHouse) {
                    user = remoteUserServic.user(projectUserHouseDTO.getTelephone());
                    // 添加住户
                    if (user.getData().getUserId() == null) {
                        //处理方式一：抛出异常
//                        throw new RuntimeException("账户" + projectUserHouseDTO.getTelephone() + "云对讲未注册，不能添加该住户信息");
                        //优化处理方式：远程调用注册该手机的住户到云对讲
                        TencentRemoteServiceFactory.getInstance(getVer()).saveAppUserRegister(projectUserHouseDTO.getTelephone(), rtcCode);
                        user = remoteUserServic.user(projectUserHouseDTO.getTelephone());
                    }
                    if (projectUserHouseDTO.getPeopleTypeCode() != null) {
                        TencentRemoteServiceFactory.getInstance(getVer()).saveUser(rtcCode, projectId.toString(), enabled, projectId.toString(), user.getData().getUserId(),projectUserHouseDTO.getTelephone(),
                                projectUserHouseDTO.getPeopleTypeCode(), null, null, projectUserHouseDTO.getPicUrl(), projectUserHouseDTO.getPersonName(), projectUserHouseDTO.getPersonName());
                    }// 添加房屋
                    TencentRemoteServiceFactory.getInstance(getVer()).saveRoom(enabled, projectId.toString(), projectUserHouseDTO.getHouseCode(), projectUserHouseDTO.getHouseName());
                }
                // 存key值 写入注册id
                telephoneMap.put(user.getData().getUserId().toString(), telephoneMap.get(user.getData().getUserId().toString()));
                // 获取原始数据
                Map<String, Set<String>> telephoneMapMap = telephoneMap.get(user.getData().getUserId().toString());
                // 对 telephoneMapMap 为空的数据分配内存
                if (telephoneMapMap == null) {
                    telephoneMapMap = new HashMap<>();
                }
                // 从原始数据获取key为deviceIds的数据
                Set<String> deviceIds= new HashSet<>();
                Set<String> roomIds = new HashSet<>();
                Set<String> userType = new HashSet<>();
                if (telephoneMapMap.get("deviceIds") != null) {
                    deviceIds = telephoneMapMap.get("deviceIds");
                }
                if (telephoneMapMap.get("roomIds") != null) {
                    roomIds = telephoneMapMap.get("roomIds");
                }
                if (telephoneMapMap.get("userType") != null) {
                    userType = telephoneMapMap.get("userType");
                }
                // 对原始deviceIds数据新增设备
                deviceIds.add(getDeviceIdByDeviceSn(deviceInfo.getSn()));
                if (projectUserHouseDTO.getHouseCode().contains("null")) {
                    throw new RuntimeException("项目下房屋"+projectUserHouseDTO.getHouseId()+"code存在错误，请联系管理员");
                }
                roomIds.add(projectUserHouseDTO.getHouseCode());
                userType.add(String.valueOf(projectUserHouseDTO.getPeopleTypeCode()));
                // 将更新后的deviceIds数据加入到原始数据
                telephoneMapMap.put("deviceIds",  deviceIds);
                telephoneMapMap.put("roomIds", roomIds);
                telephoneMapMap.put("userType", userType);
                // 将更新后的原始数据添加到总数据源
                telephoneMap.put(user.getData().getUserId().toString(), telephoneMapMap);
            }
        }
        return telephoneMap;
    }

    /**
     * 通过设备sn拼接对应的中台id
     * deviceid = productName + / + sn
     *
     * @param sn
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private String getDeviceIdByDeviceSn(String sn) {  // 这里不应该多次调用，数据库查询设备可通过连接关联将产品名关联返回即可

        String productName = projectDeviceInfoService.getProductIdByDeviceSn(sn);
        if (StringUtils.isEmpty(productName)) {
            log.error("[腾讯云对讲] 设备 {}  未查询到对应productId", sn);
            throw new RuntimeException("设备" + sn + "无对应产品，请联系管理员");
        }
        return productName + "/" + sn;
    }

    /**
     * 校验结果方法 msg操作成功的返回true
     * 可扩产异常处理
     *
     * @param respondDTO
     */
    private boolean checkResult(TencentRespondDTO respondDTO) {
        if (TencentErrorEnum.SUCCESS_CODE.code.equals(respondDTO.getCode())) {
            return true;
        } else {
            return false;
        }
    }
}
