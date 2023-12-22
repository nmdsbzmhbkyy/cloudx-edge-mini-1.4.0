
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.constant.ProjectNoticeDeviceConstant;
import com.aurine.cloudx.estate.constant.ReadStatusConstant;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.constant.enums.HouseHoldTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectNoticeMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 信息发布
 *
 * @author guhl@aurine.cn
 * @date 2020-05-20 11:52:46
 */
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

    private static final String INDOOR = "1";
    private static final String LADDER_WAY = "2";
    private static final String STAFF_TYPE = "1";

    @Override
    public Page<ProjectNoticeVo> pageNotice(Page<ProjectNoticeVo> page, ProjectNoticeFormVo projectNoticeFormVo) {
        return baseMapper.pageNotice(page, projectNoticeFormVo);
    }


    @Override
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
            deviceIds = projectDeviceInfoService.findByType(projectDeviceInfoFormVo).stream()
                    .filter(e -> houseIds.contains(e.getHouseId()))
                    .map(e -> {
                        return e.getDeviceId();
                    }).collect(Collectors.toList());
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        projectNoticeAddVo.setNoticeId(uuid);
        //批量获取设备信息 xull@aurine.cn 2020年5月21日 15点43分
        if (CollUtil.isNotEmpty(deviceIds)) {
            projectDeviceInfos = projectDeviceInfoService.listByIds(deviceIds);
        }
        ProjectNotice projectNotice = new ProjectNotice();
        BeanUtils.copyProperties(projectNoticeAddVo, projectNotice);
        projectNotice.setTargetType(StringUtil.join(",", projectNoticeAddVo.getCheckTargetType()));
        //如果是住户信息发布
        if (CollUtil.isNotEmpty(projectNoticeAddVo.getCheckTargetType())) {
            for (String checkTargetType : projectNoticeAddVo.getCheckTargetType()) {
                switch (Integer.valueOf(checkTargetType.trim())) {
                    case 1:
                        projectNotice = sendDevice(projectDeviceInfos, projectNotice);
                        break;
                    case 2:
                        sendApp(projectNoticeAddVo.getHouseId(), projectNotice);
                        break;
                    case 3:
                        // TODO: app消息发送接口
                        break;
                    case 4:
                        // TODO: 短信消息发送接口
                        break;
                }
            }
        } else {
            projectNotice = sendDevice(projectDeviceInfos, projectNotice);
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
        baseMapper.insert(projectNotice);
    }

    /**
     * 发送消息到平台端
     *
     * @param projectDeviceInfos
     * @param projectNotice
     */
    private ProjectNotice sendDevice(List<ProjectDeviceInfo> projectDeviceInfos, ProjectNotice projectNotice) {
        if (CollUtil.isNotEmpty(projectDeviceInfos)) {
            //发送设备消息列表  xull@aurine.cn  2020年5月21日 15点43分
            List<ProjectNoticeDevice> projectNoticeDevices = new ArrayList<>();
            //设备对象名称列表 xull@aurine.cn 2020年5月21日 15点43分
            List<String> deviceNames = new ArrayList<>();
            projectDeviceInfos.forEach(e -> {
                ProjectNoticeDevice projectNoticeDevice = new ProjectNoticeDevice();
                projectNoticeDevice.setDlStatus(ProjectNoticeDeviceConstant.LOADING);
                projectNoticeDevice.setNoticeId(projectNotice.getNoticeId());
                projectNoticeDevice.setDeviceId(e.getDeviceId());
                projectNoticeDevices.add(projectNoticeDevice);
                deviceNames.add(e.getDeviceName());

                /**
                 * 对接发送接口
                 */

                try {
                    //TODO:
                } catch (Exception ex) {
                    projectNoticeDevice.setDlStatus(ProjectNoticeDeviceConstant.FAIL);
                }
            });
            //将设备对象名称转换成 "111室内机,112室内机" 形式 xull@aurine.cn 2020年5月21日 15点49分
            String deviceNameString = StringUtil.join(",", deviceNames);
            if (deviceNameString.length()>50){
                projectNotice.setTarget(deviceNameString.substring(0,50)+"...");
            }else {
                projectNotice.setTarget(deviceNameString);
            }
            projectNoticeDeviceService.saveBatch(projectNoticeDevices);


        }
        return projectNotice;
    }

    /**
     * 发送消息到app端
     *
     * @param houseIds
     * @param projectNotice
     */
    private void sendApp(List<String> houseIds, ProjectNotice projectNotice) {
        //获取有效的人员
        List<ProjectHousePersonRel> persons = projectHousePersonRelService
                .list(Wrappers.lambdaQuery(ProjectHousePersonRel.class)
                        .and(qw -> qw.le(ProjectHousePersonRel::getRentStopTime, LocalDate.now())
                                .or(e -> e.eq(ProjectHousePersonRel::getHouseholdType, HouseHoldTypeEnum.OWNER.code)))
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
                        ProjectNoticeDeviceConstant.FAIL,
                        ProjectNoticeDeviceConstant.CLEANED);
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
        projectNoticeDeviceLists.forEach(e -> {
            //重新发送后会回调更新状态值操作,但未更新前先设置状态值为加载
            e.setDlStatus(ProjectNoticeDeviceConstant.LOADING);
        });
        projectNoticeDeviceService.updateBatchById(projectNoticeDeviceLists);
        //TODO:该方法需要调用重新发布信息的接口 顾煌龙
        // 模拟接口调用后回调为下载成功  xull@aurine.cn 2020/5/25 9:34
        projectNoticeDeviceLists.forEach(e -> {
            //重新发送后会回调更新状态值操作,但未更新前先设置状态值为加载
            e.setDlStatus(ProjectNoticeDeviceConstant.SUCCESS);
        });
        projectNoticeDeviceService.updateBatchById(projectNoticeDeviceLists);
        return Boolean.TRUE;
    }

    @Override
    public boolean removeNotice(List<String> deviceIds) {
        List<ProjectNoticeDevice> projectNoticeDevices = projectNoticeDeviceList(deviceIds);
        projectNoticeDevices.forEach(e -> {
            //TODO:这里需要调用公告清除的方法
            e.setDlStatus(ProjectNoticeDeviceConstant.CLEANED);
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
    public void sendBatch(ProjectNoticeAddVo projectNoticeAddVo) {
        //分批保存信息
        Page page = new Page();
        page.setSize(100);
        page.setCurrent(0L);
        List<String> allHouseIds = projectNoticeAddVo.getHouseId();
        do {
            page.setCurrent(page.getCurrent() + 1);
            IPage<String> houseIds=  projectFrameInfoService.getCheckedIndoorId(page,allHouseIds);
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

    }

    @Override
    public Page<ProjectPersonNoticeVo> pageByPerson(Page page, String personId, String type) {
        return baseMapper.pageByNoticeType(page, personId, type);
    }
}