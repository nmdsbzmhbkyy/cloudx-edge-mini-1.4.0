
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.constant.ProjectNoticeDeviceConstant;
import com.aurine.cloudx.estate.constant.ReadStatusConstant;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.constant.enums.HouseHoldTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectNoticeMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 信息发布
 *
 * @author guhl@aurine.cn
 * @date 2020-05-20 11:52:46
 */
@Slf4j
@Service
public class ProjectNoticeServiceImpl extends ServiceImpl<ProjectNoticeMapper, ProjectNotice> implements ProjectNoticeService {
    @Resource
    private ProjectNoticeDeviceService projectNoticeDeviceService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectStaffNoticeObjectService projectStaffNoticeObjectService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;

    @Resource
    private ProjectNoticeObjectService projectNoticeObjectService;

    private static final String INDOOR = "1";
    private static final String LADDER_WAY = "2";
    private static final String STAFF_TYPE = "1";

    @Override
    public Page<ProjectNoticeVo> pageNotice(Page<ProjectNoticeVo> page, ProjectNoticeFormVo projectNoticeFormVo) {
        return baseMapper.pageNotice(page, projectNoticeFormVo);
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveByDeviceIds(ProjectNoticeAddVo projectNoticeAddVo) {
        List<String> deviceIds = new ArrayList<>();
        List<ProjectDeviceInfo> projectDeviceInfos = new ArrayList<>();
        if (CollUtil.isNotEmpty(projectNoticeAddVo.getDeviceId())) {
            deviceIds = projectNoticeAddVo.getDeviceId();
        } else {
            List<String> houseIds = projectNoticeAddVo.getHouseId();
            ProjectDeviceInfoFormVo projectDeviceInfoFormVo = new ProjectDeviceInfoFormVo();
            projectDeviceInfoFormVo.setTypes(ListUtil.toList("1"));
            //判断消息内容是否是纯文本或富文本，富文本则获取到支持富文本能力的室内机
            if (StringUtils.equals(projectNoticeAddVo.getContentType(), "1")) {
                deviceIds = projectDeviceInfoService.findByType(projectDeviceInfoFormVo).stream()
                        .filter(e -> houseIds.contains(e.getHouseId()))
                        .map(e -> {
                            return e.getDeviceId();
                        }).collect(Collectors.toList());
            } else {
                deviceIds = projectDeviceInfoService.findRichByType(projectDeviceInfoFormVo).stream()
                        .filter(e -> houseIds.contains(e.getHouseId()))
                        .map(e -> {
                            return e.getDeviceId();
                        }).collect(Collectors.toList());
            }

        }

        //批量获取设备信息 xull@aurine.cn 2020年5月21日 15点43分
        if (CollUtil.isNotEmpty(deviceIds)) {
            projectDeviceInfos = projectDeviceInfoService.listByIds(deviceIds);
        }
        //判断房屋id非空进行保存
        if (projectNoticeAddVo.getHouseId() != null && projectNoticeAddVo.getHouseId().size() > 0) {
            saveNoticeObject(projectNoticeAddVo.getHouseId(), projectNoticeAddVo.getNoticeId());
        }
        //如果是住户信息发布
        if (CollUtil.isNotEmpty(projectNoticeAddVo.getCheckTargetType())) {
            for (String checkTargetType : projectNoticeAddVo.getCheckTargetType()) {
                switch (Integer.parseInt(checkTargetType.trim())) {
                    case 1:
                        //保存发送对象
                        //发送设备
                        sendDevice(projectDeviceInfos, projectNoticeAddVo);
                        break;
                    case 2:
                        // 小程序，app 整合为移动端
                        sendApp(projectNoticeAddVo.getHouseId(), projectNoticeAddVo);
                        break;
                    case 3:
                        // app消息发送接口
                        sendApp(projectNoticeAddVo.getHouseId(), projectNoticeAddVo);
                        break;
                    case 4:
                        // TODO: 短信消息发送接口
                        break;
                    default:
                        break;
                }
            }
        } else {
            sendDevice(projectDeviceInfos, projectNoticeAddVo);
        }

        //根据发送类型分别发送到手机或设备端
/*        if (TargetTypeConstant.ALL.equals(projectNoticeAddVo.getTargetType()) && INDOOR.equals(projectNoticeAddVo.getNoticeType())) {
            sendApp(projectNoticeAddVo.getHouseId(), projectNotice);
            projectNotice = sendDevice(projectDeviceInfos, projectNotice);
        } else if (TargetTypeConstant.APP.equals(projectNoticeAddVo.getTargetType()) && INDOOR.equals(projectNoticeAddVo.getNoticeType())) {
            sendApp(projectNoticeAddVo.getHouseId(), projectNotice);
        } else {
            projectNotice = sendDevice(projectDeviceInfos, projectNotice);

        }*/

    }

    private void saveNoticeObject(List<String> houseIds, String noticeId) {
        List<ProjectNoticeObject> projectNoticeObjects = new ArrayList<>();
        for (String houseId : houseIds) {
            ProjectNoticeObject projectNoticeObject = new ProjectNoticeObject();
            projectNoticeObject.setObjectId(houseId);
            projectNoticeObject.setNoticeId(noticeId);
            projectNoticeObjects.add(projectNoticeObject);
        }
        projectNoticeObjectService.saveBatch(projectNoticeObjects);
    }

    /**
     * 发送消息到平台端
     *
     * @param projectDeviceInfos
     * @param projectNotice
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    public void sendDevice(List<ProjectDeviceInfo> projectDeviceInfos, ProjectNotice projectNotice) {
        List<ProjectNoticeDevice> failNoticeList = new ArrayList<>();
        if (CollUtil.isNotEmpty(projectDeviceInfos)) {
            //发送设备消息列表  xull@aurine.cn  2020年5月21日 15点43分
//            List<ProjectNoticeDevice> projectNoticeDevices = new ArrayList<>();
            //设备对象名称列表 xull@aurine.cn 2020年5月21日 15点43分
//            List<String> deviceNames = new ArrayList<>();
            projectDeviceInfos.forEach(e -> {
                ProjectNoticeDevice projectNoticeDevice = new ProjectNoticeDevice();
                projectNoticeDevice.setDlStatus(ProjectNoticeDeviceConstant.LOADING);
                projectNoticeDevice.setNoticeId(projectNotice.getNoticeId());
                projectNoticeDevice.setDeviceId(e.getDeviceId());
                ProjectNoticeDevice temp = projectDeviceInfoService.getDeviceNameAndBuildingNameAndUnitName(e.getDeviceId());
                projectNoticeDevice.setDeviceName(temp.getDeviceName());
                projectNoticeDevice.setBuildingName(temp.getBuildingName());
                projectNoticeDevice.setUnitName(temp.getUnitName());
//                projectNoticeDevices.add(projectNoticeDevice);
//                deviceNames.add(e.getDeviceName());
                projectNoticeDeviceService.save(projectNoticeDevice);

                projectNotice.setSeq(projectNoticeDevice.getSeq());

                /**
                 * 对接发送接口
                 */
                try {
                    if (StringUtils.equals(e.getDeviceType(), DeviceTypeEnum.LADDER_WAY_DEVICE.getCode()) ||
                            StringUtils.equals(e.getDeviceType(), DeviceTypeEnum.GATE_DEVICE.getCode()) ||
                            StringUtils.equals(e.getDeviceType(), DeviceTypeEnum.INDOOR_DEVICE.getCode()))
                    {
                        boolean success = DeviceFactoryProducer.getFactory(e.getDeviceId()).getPassWayDeviceService().sendTextMessage(e.getDeviceId(), projectNotice);
                        projectNoticeDevice.setDlStatus(success ? ProjectNoticeDeviceConstant.SUCCESS : ProjectNoticeDeviceConstant.FAIL);
                        if (!success) {
                            failNoticeList.add(projectNoticeDevice);
                        }
                    } else {
                        log.error("无法给设备{} 发送信息", e.getDeviceName());
                        projectNoticeDevice.setDlStatus(ProjectNoticeDeviceConstant.FAIL);
                        failNoticeList.add(projectNoticeDevice);
                    }
                } catch (Exception ex) {
                    projectNoticeDevice.setDlStatus(ProjectNoticeDeviceConstant.FAIL);
                    failNoticeList.add(projectNoticeDevice);
                }
            });
            //将设备对象名称转换成 "111室内机,112室内机" 形式 xull@aurine.cn 2020年5月21日 15点49分
            //设备如果太多不现实
//            String deviceNameString = StringUtil.join(",", deviceNames);
//            projectNotice.setTarget(deviceNameString);
//            projectNoticeDeviceService.saveBatch(projectNoticeDevices);

        }
        if (CollUtil.isNotEmpty(failNoticeList)) {
            projectNoticeDeviceService.updateBatchById(failNoticeList);
        }
    }

    /**
     * 发送消息到app端
     *
     * @param houseIds
     * @param projectNotice
     */
    private void sendApp(List<String> houseIds, ProjectNotice projectNotice) {
        // app 小程序同时发布消息，只创建一次
        List<ProjectStaffNoticeObject> staffNoticeObject = projectStaffNoticeObjectService.list(new QueryWrapper<ProjectStaffNoticeObject>().lambda()
                .eq(ProjectStaffNoticeObject::getNoticeId, projectNotice.getNoticeId()));
        if (ObjectUtil.isNotEmpty(staffNoticeObject) && staffNoticeObject.size() > 0) {
            return;
        }
        //获取有效的人员
        List<ProjectHousePersonRel> persons = projectHousePersonRelService
                .list(Wrappers.lambdaQuery(ProjectHousePersonRel.class)
                        .and(qw -> qw.ge(ProjectHousePersonRel::getRentStopTime, LocalDate.now())
                                .and(e -> e.eq(ProjectHousePersonRel::getHouseholdType, HouseHoldTypeEnum.TENANT.code))
                                .or(e -> e.ne(ProjectHousePersonRel::getHouseholdType, HouseHoldTypeEnum.TENANT.code)))
                        .in(ProjectHousePersonRel::getHouseId, houseIds)
                        .eq(ProjectHousePersonRel::getStatus, "1")
                        .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.pass.code));
        if (!(persons != null && persons.size() > 0)) {
            return;
        }
        List<String> userIds = persons.stream().map(ProjectHousePersonRel::getPersonId).collect(Collectors.toList());
        List<ProjectPersonInfo> projectPersonInfos = projectPersonInfoService.listByIds(userIds);
        //消息列表
        List<ProjectStaffNoticeObject> projectStaffNoticeObjects = new ArrayList<>();
        //员工姓名列表
        //List<String> userNames = new ArrayList<>();
        projectPersonInfos.forEach(e -> {
            ProjectStaffNoticeObject projectStaffNoticeObject = new ProjectStaffNoticeObject();
            projectStaffNoticeObject.setNoticeId(projectNotice.getNoticeId());
            projectStaffNoticeObject.setUserId(e.getPersonId());
            projectStaffNoticeObject.setStatus(ReadStatusConstant.UnRead);
            projectStaffNoticeObject.setUserType(STAFF_TYPE);
            projectStaffNoticeObjects.add(projectStaffNoticeObject);
            //userNames.add(e.getPersonName());
        });
        projectStaffNoticeObjectService.saveBatch(projectStaffNoticeObjects);
    }


    /**
     * 失败的Project集合,即不为成功的状态
     *
     * @param projectNoticeId 设备消息id
     * @return 返回失败集合
     */
    @Override
    public List<ProjectNoticeDevice> projectNoticeList(String projectNoticeId) {
        LambdaQueryWrapper<ProjectNoticeDevice> queryWrapper = Wrappers.<ProjectNoticeDevice>lambdaQuery()
                .eq(ProjectNoticeDevice::getNoticeId, projectNoticeId)
                .in(ProjectNoticeDevice::getDlStatus,
                        ProjectNoticeDeviceConstant.FAIL);
        return projectNoticeDeviceService.list(queryWrapper);
    }

    @Override
    public List<ProjectNoticeDevice> projectNoticeDeviceList(List<String> deviceIds) {
        return baseMapper.projectNoticeDeviceList(deviceIds);
    }

    @Override
    public boolean removeNoticeById(String id) {
        //TODO:预留功能
        return false;
    }

    @Override
    public boolean resendAll(String id) {
        List<ProjectNoticeDevice> projectNoticeDeviceLists = projectNoticeList(id);

        Set<Integer> noticeDeviceIdSet = projectNoticeDeviceLists.stream().map(ProjectNoticeDevice::getSeq).collect(Collectors.toSet());
        projectNoticeDeviceService.removeByIds(noticeDeviceIdSet);

        ProjectNotice notice = this.getById(id);
        Set<String> deviceIdSet = projectNoticeDeviceLists.stream().map(ProjectNoticeDevice::getDeviceId).collect(Collectors.toSet());
        if (CollUtil.isNotEmpty(deviceIdSet)) {
            List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new LambdaQueryWrapper<ProjectDeviceInfo>()
                    .in(ProjectDeviceInfo::getDeviceId, deviceIdSet));
            this.sendDevice(deviceInfoList, notice);
        }
        return Boolean.TRUE;
    }

    @Override
    public boolean removeNotice(List<String> deviceIds) {
        List<ProjectNoticeDevice> projectNoticeDevices = projectNoticeDeviceList(deviceIds);
        projectNoticeDevices.forEach(e -> {
            //TODO:这里需要调用公告清除的方法
            e.setDlStatus(ProjectNoticeDeviceConstant.CLEANED);
        });
        deviceIds.forEach(deviceId -> {
            DeviceFactoryProducer.getFactory(deviceId).getPassWayDeviceService().cleanTextMessage(deviceId);
        });

        return projectNoticeDeviceService.updateBatchById(projectNoticeDevices);
    }

    @Override
    public boolean resend(String noticeId, String deviceId) {
        //获取消息对象 xull@aurine.cn 2020/5/22 13:49
        ProjectNotice projectNotice = baseMapper.selectById(noticeId);
        //根据noticeId和deviceId获取消息发送对象信息 xull@aurine.cn 2020/5/22 13:46
        ProjectNoticeDevice projectNoticeDevice = projectNoticeDeviceService.getOne(Wrappers.lambdaQuery(ProjectNoticeDevice.class)
                .eq(ProjectNoticeDevice::getNoticeId, noticeId)
                .eq(ProjectNoticeDevice::getDeviceId, deviceId));

        projectNoticeDevice.setDlStatus(ProjectNoticeDeviceConstant.LOADING);
        projectNoticeDeviceService.updateById(projectNoticeDevice);
        //TODO:该方法需要调用重新发布一条信息的接口
        // 模拟接口调用后回调为下载成功  xull@aurine.cn 2020/5/25 9:34
        projectNoticeDevice.setDlStatus(ProjectNoticeDeviceConstant.SUCCESS);
        projectNoticeDeviceService.updateById(projectNoticeDevice);
        return Boolean.TRUE;
    }

    @Override
    public Page<ProjectPersonNoticeVo> pageByPerson(Page page, String personId) {
        return baseMapper.pageByPerson(page, personId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String  sendBatch(ProjectNoticeAddVo projectNoticeAddVo) {
        //分批保存信息
        Page page = new Page();
        page.setSize(100);
        page.setCurrent(0L);
        List<String> allHouseIds = projectNoticeAddVo.getHouseId();
        ProjectNotice projectNotice = new ProjectNotice();


        //        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String uuid = String.valueOf(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);//使用Long类型uuid，以满足中台2.0需求
//        String uuid = String.valueOf(347);//使用Long类型uuid，以满足中台2.0需求
        projectNoticeAddVo.setNoticeId(uuid);
        projectNoticeAddVo.setTargetType(StringUtil.join(",", projectNoticeAddVo.getCheckTargetType()));
        BeanUtils.copyProperties(projectNoticeAddVo, projectNotice);
        baseMapper.insert(projectNotice);
        do {
            page.setCurrent(page.getCurrent() + 1);
            IPage<String> houseIds = projectFrameInfoService.getCheckedIndoorId(page, allHouseIds);
            List<String> ids = houseIds.getRecords();
            //判断数据是否为空 为空则直接退出循环
            if (ids == null || ids.size() == 0) {
                break;
            }
            //发布时设置发布时间为当前时间
            projectNoticeAddVo.setPubTime(LocalDateTime.now());
            projectNoticeAddVo.setHouseId(ids);
            saveByDeviceIds(projectNoticeAddVo);
        } while (page.getTotal() / page.getSize() <= page.getCurrent());

        return projectNotice.getNoticeId();
    }

    @Override
    public void sendVo(ProjectNoticeAddVo projectNoticeAddVo) {
        ProjectNotice projectNotice = new ProjectNotice();
        //        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String uuid = String.valueOf(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);//使用Long类型uuid，以满足中台2.0需求
//        String uuid = String.valueOf(347);//使用Long类型uuid，以满足中台2.0需求
        projectNoticeAddVo.setNoticeId(uuid);
        projectNoticeAddVo.setTargetType(StringUtil.join(",", projectNoticeAddVo.getCheckTargetType()));
        BeanUtils.copyProperties(projectNoticeAddVo, projectNotice);
        baseMapper.insert(projectNotice);
        saveByDeviceIds(projectNoticeAddVo);

    }


}