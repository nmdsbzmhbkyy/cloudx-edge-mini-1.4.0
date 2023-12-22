package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.common.entity.vo.RightDeviceRelVo;
import com.aurine.cloudx.open.origin.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.open.origin.constant.enums.PassMacroIdEnum;
import com.aurine.cloudx.open.origin.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.open.origin.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.open.origin.entity.*;
import com.aurine.cloudx.open.origin.mapper.ProjectPersonDeviceMapper;
import com.aurine.cloudx.open.origin.mapper.ProjectRightDeviceMapper;
import com.aurine.cloudx.open.origin.service.*;
import com.aurine.cloudx.open.origin.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限设备关系表，记录权限（认证介质）的下发状态
 *
 * @author 王良俊
 * @date 2020-05-21 09:52:28
 */
@Service
@Primary
@Slf4j
public class ProjectRightDeviceServiceImpl extends ServiceImpl<ProjectRightDeviceMapper, ProjectRightDevice> implements ProjectRightDeviceService {

    @Resource
    private ProjectPassPlanPolicyRelService projectPassPlanPolicyRelService;
    @Resource
    private ProjectFingerprintsService projectFingerprintsServiceImpl;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesServiceImpl;
    @Resource
    private ProjectPersonDeviceMapper projectPersonDeviceMapper;

    @Resource
    private ProjectPasswdService projectPasswdServiceImpl;

    @Resource
    @Lazy
    private ProjectCardService projectCardServiceImpl;
//    @Resource
//    @Lazy
//    private AbstractProjectRightDeviceService abstractWebProjectRightDeviceService;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectStaffService projectStaffService;
//    @Resource
//    DockModuleConfigUtil dockModuleConfigUtil;

    /**
     * 统一冻结凭证到设备的方法
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     * @since 2020-08-18
     */
    @Override
    public boolean freezeCerts(List<ProjectRightDevice> rightDeviceList) {
        if (CollUtil.isNotEmpty(rightDeviceList)) {
            //将凭证设置为删除中状态，并调用删除接口，通过接口回调删除凭证
            this.updateCerts(rightDeviceList, PassRightCertDownloadStatusEnum.FREEZING);
//            for (ProjectRightDevice rightDevice : rightDeviceList) {
//                rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.FREEZING.code);
//            }
//
//            this.updateBatchById(rightDeviceList);
//            remoteInterfaceAfterTransactional(rightDeviceList, false);
//            abstractWebProjectRightDeviceService.remoteInterfaceByDevices(rightDeviceList, false);
        }

        return true;
    }


    /**
     * 存储凭证
     *
     * @param rightDeviceList
     */
    void saveCerts(List<ProjectRightDevice> rightDeviceList) {
        String uid;
        //插入凭证信息，状态为正在下载，等待第三方接口回调后改变状态
        for (ProjectRightDevice rightDevice : rightDeviceList) {
            if (StrUtil.isBlank(rightDevice.getUid())) {
                uid = UUID.randomUUID().toString().replaceAll("-", "");
                rightDevice.setUid(uid);
                rightDevice.setOperator(1);
            }
            rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.DOWNLOADING.code);
        }
        List<ProjectRightDevice> saveList = rightDeviceList.stream().filter(rightDevice -> rightDevice.getSeq() == null).collect(Collectors.toList());
        saveList = filterRightDeviceList(saveList);
        List<ProjectRightDevice> updateList = rightDeviceList.stream().filter(rightDevice -> rightDevice.getSeq() != null).collect(Collectors.toList());
        // 这里mybatis-plus的saveOrUpdateBatch太慢了所以自己拆分
        if (CollUtil.isNotEmpty(saveList)) {
            this.saveBatch(new HashSet<>(saveList));
        }
        if (CollUtil.isNotEmpty(updateList)) {
            this.updateBatchById(updateList);
        }
    }

    /**
     * 更新凭证状态
     *
     * @param rightDeviceList
     */
    void updateCerts(List<ProjectRightDevice> rightDeviceList, PassRightCertDownloadStatusEnum status) {
        for (ProjectRightDevice rightDevice : rightDeviceList) {
            rightDevice.setDlStatus(status.code);
        }
        if (CollUtil.isNotEmpty(rightDeviceList)) {
            this.updateBatchById(rightDeviceList);
        }
    }


//    /**
//     * 在事务提交之后发送数据到接口，用于解决事务提交比接口回调慢的问题
//     * 不在事务控制中的，切勿调用此方法
//     */
//    private void remoteInterfaceAfterTransactional(List<ProjectRightDevice> rightDeviceList, boolean isAdd) {
//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
//            @Override
//            public void afterCommit() {
//                abstractWebProjectRightDeviceService.remoteInterfaceByDevices(rightDeviceList, isAdd);
//            }
//        });
//    }

    /**
     * 根据设备，调用删除或添加凭证接口
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     */
    @Override
    public boolean remoteInterfaceByDevices(List<ProjectRightDevice> rightDeviceList, boolean isAdd) {
//        if (isAdd) {
//            rightDeviceList = filterRightDeviceList(rightDeviceList);
//        }
//
//        if (CollUtil.isNotEmpty(rightDeviceList) && isAdd) {
//            rightDeviceList.forEach(rightDevice -> {
//                rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.DOWNLOADING.code);
//            });
//        }
//        log.info("云平台准备调用凭证 {} 接口，共{}条数据", isAdd ? "下发" : "删除", rightDeviceList != null ? rightDeviceList.size() : 0);
//        log.info(rightDeviceList.toString());
//        //將列表按照设备ID的不同，进行拆分
//        Set<String> deviceIdSet = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toSet());
//
//        //业务数据按照设备id分组对接
//        List<List<ProjectRightDevice>> deviceCertListList = new ArrayList<>();
//        for (String deviceId : deviceIdSet) {
//            deviceCertListList.add(rightDeviceList.stream().filter(e -> e.getDeviceId().equals(deviceId)).collect(Collectors.toList()));
//        }
//
//        List<ProjectRightDevice> deviceRightList;
//
//
//        /***********按设备ID分组,将通行凭证按设备id分类调用***********/
//        for (List<ProjectRightDevice> deviceCertList : deviceCertListList) {
//            deviceRightList = rightDeviceList.stream().filter(e -> e.getDeviceId().equals(deviceCertList.get(0).getDeviceId())).collect(Collectors.toList());
//
//            if (CollUtil.isNotEmpty(deviceCertList)) {
//                if (isAdd) {
//                    DeviceFactoryProducer.getFactory(deviceCertList.get(0).getDeviceId())
//                            .getPassWayDeviceService().addCert(deviceRightList);
//                } else {
//                    DeviceFactoryProducer.getFactory(deviceCertList.get(0).getDeviceId())
//                            .getPassWayDeviceService().delCert(deviceRightList);
//                }
//            }
//        }

        return true;
    }


    /**
     * 统一删除凭证到设备的方法
     *
     * @param rightDeviceList
     * @param changeStatus    是否变更当前系统的数据状态
     * @return
     * @author: 王伟
     * @since 2020-09-14
     */
    private boolean delCerts(List<ProjectRightDevice> rightDeviceList, boolean changeStatus, boolean tansactional) {
        if (CollUtil.isNotEmpty(rightDeviceList)) {
            // 这里获取到下载状态为下载失败的权限数据
            List<ProjectRightDevice> delRightDeviceList = rightDeviceList.stream().filter(projectRightDevice -> {
                return PassRightCertDownloadStatusEnum.FAIL.code.equals(projectRightDevice.getDlStatus());
            }).collect(Collectors.toList());
            // 如果 原本是下载失败则直接删除这条记录即可
            if (CollUtil.isNotEmpty(delRightDeviceList)) {
                this.remove(new QueryWrapper<ProjectRightDevice>().lambda()
                        .in(ProjectRightDevice::getUid, delRightDeviceList.stream().map(ProjectRightDevice::getUid).collect(Collectors.toList())));
            }
            rightDeviceList.removeAll(delRightDeviceList);
            if (changeStatus) {
                //将凭证设置为删除中状态，并调用删除接口，通过接口回调删除凭证

                this.updateCerts(rightDeviceList, PassRightCertDownloadStatusEnum.DELETING);
//                for (ProjectRightDevice rightDevice : rightDeviceList) {
//                    rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.DELETING.code);
//                }
//                this.updateBatchById(rightDeviceList);
            }

//            if (tansactional) {
//                remoteInterfaceAfterTransactional(rightDeviceList, false);
//            } else {
//                abstractWebProjectRightDeviceService.remoteInterfaceByDevices(rightDeviceList, false);
//            }
        }

        return true;
    }

    private boolean addCerts(List<ProjectRightDevice> rightDeviceList, boolean changeStatus, boolean transactional) {

        if (CollUtil.isNotEmpty(rightDeviceList)) {
            if (changeStatus) {
//                System.out.println("时间统计：开始保存" + LocalDateTime.now());
                this.saveCerts(rightDeviceList);
//                System.out.println("时间统计：保存完成" + LocalDateTime.now());
            }

//            if (transactional) {
//                remoteInterfaceAfterTransactional(rightDeviceList, true);
//            } else {
//                abstractWebProjectRightDeviceService.remoteInterfaceByDevices(rightDeviceList, true);
//            }
        }
        return true;
    }

//    /**
//     * 统一新增凭证到设备的方法
//     * 注意：这里的凭证都是设备上不存在的
//     *
//     * @param rightDeviceList
//     * @author: 王良俊
//     * @since 2020-08-18
//     */
//    public boolean addCertsNotSave(List<ProjectRightDevice> rightDeviceList) {
//        return abstractWebProjectRightDeviceService.remoteInterfaceByDevices(rightDeviceList, true);
//    }

    /**
     * 统一删除凭证到设备的方法
     *
     * @param rightDeviceList
     * @param changeStatus    是否变更当前系统的数据状态
     * @return
     * @author: 王伟
     * @since 2020-09-14
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delCerts(List<ProjectRightDevice> rightDeviceList, boolean changeStatus) {
        return this.delCerts(rightDeviceList, changeStatus, true);
    }


    /**
     * 统一新增凭证到设备的方法
     * 注意：这里的凭证都是设备上不存在的
     *
     * @param rightDeviceList
     * @param changeStatus
     * @return
     * @author: 王伟
     * @since 2020-08-18
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCerts(List<ProjectRightDevice> rightDeviceList, boolean changeStatus) {
        return this.addCerts(rightDeviceList, changeStatus, true);
    }


    /**
     * @param deviceIdList
     * @param personId
     * @param transactional 是否开启事务
     * @return
     */
    protected boolean saveRelationship(List<String> deviceIdList, String personId, boolean transactional) {

//        WR20跳过凭证下发流程
//        if (dockModuleConfigUtil.isWr20()) {
//            return true;
//        }

        List<ProjectRightDevice> rightDeviceList = this.listCertInfoByPersonId(personId);

        if (CollUtil.isNotEmpty(rightDeviceList)) {

            // 这里生成要添加的
            List<ProjectRightDevice> projectRightDevices = new ArrayList<>(getAddList(rightDeviceList, deviceIdList));
            log.info("saveRelationShip:要添加的数据" + projectRightDevices.size());
            // 汇总要删除的数据
            List<ProjectRightDevice> removeList = new ArrayList<>(getRemoveRightDeviceList(rightDeviceList.stream()
                    .map(ProjectRightDevice::getCertMediaId).collect(Collectors.toList()), deviceIdList));
            log.info("saveRelationShip:要删除的数据" + removeList.size());

            this.delCerts(removeList, true, transactional);
            if (CollUtil.isNotEmpty(projectRightDevices)) {
                this.addCerts(projectRightDevices, true, transactional);
            } else {
                return true;
            }
        }

        // 用户没有任何介质返回true
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRelationship(List<String> deviceIdList, String personId) {
        return this.saveRelationship(deviceIdList, personId, true);
    }


    /**
     * <p>
     * 在配置了不同新的设备列表后更新介质和设备的关系
     * </p>
     *
     * @param personId 人员id
     * @return 处理结果
     * @author: 王良俊
     */
    @Override
    public void saveRelationshipProxy(String planId, String personId, Integer projectId) {

        List<String> macroList = projectPassPlanPolicyRelService.listMacroIdListByPlanId(planId);
        List<ProjectPassDeviceVo> newDeviceByPlanList = projectPersonDeviceMapper.listByPerson(
                personId, null,
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.GATE.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.FRAME_GATE.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.BUILDING_LADDER.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.UNIT_LADDER.name())),
                false
        );

        this.saveRelationship(newDeviceByPlanList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList()), personId, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resendBatch(List<String> rightDeviceIdList) {
        List<ProjectRightDevice> rightDeviceList;
        List<ProjectRightDevice> redownloadList = new ArrayList<>();
        List<ProjectRightDevice> removeList = new ArrayList<>();
        if (CollUtil.isNotEmpty(rightDeviceIdList)) {
            List<String> statusList = new ArrayList<>();
            statusList.add(PassRightCertDownloadStatusEnum.FREEZE.code);
            statusList.add(PassRightCertDownloadStatusEnum.DELETING.code);
            rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                    .in(ProjectRightDevice::getUid, rightDeviceIdList));
            redownloadList = rightDeviceList.stream().filter(rightDevice -> !statusList.contains(rightDevice.getDlStatus())).collect(Collectors.toList());
            removeList = rightDeviceList.stream().filter(rightDevice -> PassRightCertDownloadStatusEnum.DELETING.code.equals(rightDevice.getDlStatus())).collect(Collectors.toList());
        }
        redownloadList.forEach(rightDevice -> {
            rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.DOWNLOADING.code);
        });
        if (CollUtil.isNotEmpty(redownloadList)) {
            // 这里更新这个记录为下载中
            this.updateBatchById(redownloadList);
//            this.addCertsNotSave(redownloadList);
        }
        if (CollUtil.isNotEmpty(removeList)) {
            // 下载中的重新下载
            this.delCerts(removeList);
        }
        return true;
    }


    /**
     * <p>
     * 返回存储人脸、指纹、卡片的id列表
     * </p>
     *
     * @param personId 人员id
     * @return 返回介质id列表（所有介质的）
     * @author: 王良俊
     */
    public List<String> listCertmediaIdByPersonId(String personId) {
        List<ProjectRightDevice> certVoList = this.listCertInfoByPersonId(personId);
        if (CollUtil.isNotEmpty(certVoList)) {
            return certVoList.stream().map(ProjectRightDevice::getCertMediaId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 根据人员设备列表，变更凭证设备列表，适用于通行权限配置业务
     *
     * @param newPersonDeviceList
     * @param removePersonDeviceList
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    @Override
    public boolean changeByPersonDevice(List<ProjectPersonDevice> newPersonDeviceList, List<ProjectPersonDevice> removePersonDeviceList, String personId) {
        //当前住户、员工可以使用的所有通行凭证id
        List<String> certmediaIdByPersonId = listCertmediaIdByPersonId(personId);
        List<ProjectRightDevice> tmpRightDeviceList = null;
        List<ProjectRightDevice> removeList = new ArrayList<>();
        List<ProjectRightDevice> newList = new ArrayList<>();

        if (CollUtil.isNotEmpty(certmediaIdByPersonId)) {
            //根据设备ID，获取要删除设备的凭证列表列表
            if (CollUtil.isNotEmpty(removePersonDeviceList)) {
                //对应设备
                for (ProjectPersonDevice removePersonDevice : removePersonDeviceList) {
                    tmpRightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                            .eq(ProjectRightDevice::getDeviceId, removePersonDevice.getDeviceId())
                            .in(ProjectRightDevice::getCertMediaId, certmediaIdByPersonId));
                }

                if (CollUtil.isNotEmpty(tmpRightDeviceList)) {
                    removeList.addAll(tmpRightDeviceList);
                }
            }
            this.delCerts(removeList);

            //对于其他设备，根据实际的凭证变动执行下发业务（新增凭证、删除凭证引起的数据变动等）
            if (CollUtil.isNotEmpty(newPersonDeviceList)) {
                //对应设备
                for (ProjectPersonDevice newPersonDevice : removePersonDeviceList) {
                    tmpRightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                            .eq(ProjectRightDevice::getDeviceId, newPersonDevice.getDeviceId())
                            .in(ProjectRightDevice::getCertMediaId, certmediaIdByPersonId));
                }

                if (CollUtil.isNotEmpty(tmpRightDeviceList)) {
                    newList.addAll(tmpRightDeviceList);
                }
            }
            changeRightDevice(newList);
        }
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean authPersonCertmdiaDevice(List<String> deviceIdList, String personId) {
        if (StrUtil.isBlank(personId)) {
            return false;
        }
        this.removeCertDeviceAuthorize(personId);
        List<ProjectRightDevice> rightDeviceList = this.listCertInfoByPersonId(personId);

        List<ProjectRightDevice> projectRightDeviceList = new ArrayList<>();
        projectRightDeviceList.addAll(getAddList(rightDeviceList, deviceIdList));
        if (CollUtil.isNotEmpty(projectRightDeviceList)) {
            return this.addCerts(projectRightDeviceList);
        } else {
            return true;
        }
    }

    @Override
    public boolean authNewDeviceById(String deviceId, List<String> personIdList) {
        if (StrUtil.isBlank(deviceId) || CollUtil.isEmpty(personIdList)) {
            return false;
        }
        List<ProjectRightDevice> rightDeviceList = this.listCertInfoByPersonId(personIdList);
        List<String> deviceIdList = new ArrayList<>();
        deviceIdList.add(deviceId);
        getAddList(rightDeviceList, deviceIdList);
        if (CollUtil.isNotEmpty(rightDeviceList)) {
            for (ProjectRightDevice rightDevice : rightDeviceList) {
                if (StringUtils.isEmpty(rightDevice.getDeviceId())) {
                    rightDevice.setDeviceId(deviceId);
                }
            }
            return this.addCerts(rightDeviceList);
        } else {
            return true;
        }
    }


    /**
     * <p>
     * 生成对应介质的关系列表
     * </p>
     *
     * @param deviceIdList    新勾选的设备id列表
     * @param rightDeviceList 设备权限列表
     * @author: 王良俊
     */
    @Transactional(rollbackFor = Exception.class)
    public List<ProjectRightDevice> getAddList(List<ProjectRightDevice> rightDeviceList, List<String> deviceIdList) {
        if (CollUtil.isEmpty(rightDeviceList) || CollUtil.isEmpty(deviceIdList)) {
            return new ArrayList<>();
        }
        List<String> certmediaIdList = rightDeviceList.stream().map(ProjectRightDevice::getCertMediaId).collect(Collectors.toList());

        // 这里是现有需要进行重新下发的
        List<ProjectRightDevice> haveExistRightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                .in(ProjectRightDevice::getDeviceId, deviceIdList)
                .in(ProjectRightDevice::getCertMediaId, certmediaIdList));

        // 这里排除掉已经下发成功的避免重复下发
        List<ProjectRightDevice> saveList = haveExistRightDeviceList.stream().filter(rightDevice -> {
            return !(PassRightCertDownloadStatusEnum.SUCCESS.code.equals(rightDevice.getDlStatus()) ||
                    PassRightCertDownloadStatusEnum.FREEZE.code.equals(rightDevice.getDlStatus()));
        }).collect(Collectors.toList());
        List<String> existDeviceCert = haveExistRightDeviceList.stream().map(rightDevice -> {
            return rightDevice.getDeviceId() + rightDevice.getCertMediaId();
        }).collect(Collectors.toList());

        rightDeviceList.forEach(rightDevice -> {
            deviceIdList.forEach(deviceId -> {
                if (!existDeviceCert.contains(deviceId + rightDevice.getCertMediaId())) {
                    ProjectRightDevice projectRightDevice = new ProjectRightDevice();
                    BeanUtil.copyProperties(rightDevice, projectRightDevice);
                    projectRightDevice.setDeviceId(deviceId);
                    saveList.add(projectRightDevice);
                }
            });
        });
        return saveList;
    }

    // 获取要移除权限的列表
    public List<ProjectRightDevice> getRemoveRightDeviceList(List<String> certmediaIdList, List<String> deviceIdList) {
        // 要被删除权限的列表
        return this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                .in(ProjectRightDevice::getCertMediaId, certmediaIdList)
                .notIn(CollUtil.isNotEmpty(deviceIdList), ProjectRightDevice::getDeviceId, deviceIdList));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeCertDeviceAuthorize(String personId) {
        boolean resule;
        if (StrUtil.isNotEmpty(personId)) {
            List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                    .eq(ProjectRightDevice::getPersonId, personId));
            resule = this.delCerts(rightDeviceList, true, false);
        } else {
            resule = true;
        }

        /*List<ProjectCard> cardList = projectCardServiceImpl.list(new QueryWrapper<ProjectCard>().lambda().eq(ProjectCard::getPersonId, personId));
        if (CollUtil.isNotEmpty(cardList)) {
            cardList.forEach(projectCard -> {
                projectCard.setStatus(PassRightTokenStateEnum.UNUSE.code);
                projectCard.setPersonType("");
                projectCard.setPersonId("");
            });
            projectCardServiceImpl.updateBatchById(cardList);
        }*/

        projectCardServiceImpl.removeByPersonId(personId);
        projectFaceResourcesServiceImpl.removeByPersonId(personId);
        projectPasswdServiceImpl.removeByPersonId(personId);

        return resule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeCertDeviceAuthorize(List<String> personIdList) {
        if (CollUtil.isNotEmpty(personIdList)) {
            List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                    .in(ProjectRightDevice::getPersonId, personIdList));
            return this.delCerts(rightDeviceList);
        }
        return true;
    }


    @Override
    public boolean active(String personId) {
        List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>()
                .lambda().eq(ProjectRightDevice::getPersonId, personId));
        if (CollUtil.isNotEmpty(rightDeviceList)) {
            // 这里更新这个记录为下载中
            return this.addCerts(rightDeviceList);
        }
        return true;
    }

    @Override
    public boolean deactive(String personId) {
        List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>()
                .lambda().eq(ProjectRightDevice::getPersonId, personId));
        if (CollUtil.isNotEmpty(rightDeviceList)) {
            return this.freezeCerts(rightDeviceList);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeCertmedia(String certmediaId) {
        List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                .eq(ProjectRightDevice::getCertMediaId, certmediaId));
        return this.delCerts(rightDeviceList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeCertmedias(List<String> certmediaIdList) {
        if (CollUtil.isNotEmpty(certmediaIdList)) {
            List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                    .in(ProjectRightDevice::getCertMediaId, certmediaIdList));
            return this.delCerts(rightDeviceList);
        }
        return true;
    }


    /**
     * 统一删除凭证到设备的方法
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     * @since 2020-08-18
     */
    @Override
    public boolean delCerts(List<ProjectRightDevice> rightDeviceList) {
        return this.delCerts(rightDeviceList, true);
    }


    /**
     * 清空设备凭证
     * 不引起设备数据状态变动，仅清空设备
     * 注意，该接口会导致设备和系统数据不同
     * 通过重新下发业务可以恢复
     *
     * @param deviceId          要清空的设备
     * @param certmediaTypeEnum 要清空的凭证类型枚举
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean clearCerts(String deviceId, CertmediaTypeEnum certmediaTypeEnum) {
        List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                .eq(ProjectRightDevice::getDeviceId, deviceId)
                .eq(ProjectRightDevice::getCertMedia, certmediaTypeEnum.code));

        return this.delCerts(rightDeviceList, false);
    }


    /**
     * 获取差异列表
     *
     * @param rightDeviceList
     * @param isAddList
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    private List<ProjectRightDevice> getDiffrentList(List<ProjectRightDevice> rightDeviceList, boolean isAddList) {
        if (CollUtil.isEmpty(rightDeviceList)) {
            return null;
        }
        List<ProjectRightDevice> originalRightDeviceList = null;

        List<ProjectRightDevice> addList = new ArrayList<>();
        List<ProjectRightDevice> delList = new ArrayList<>();


        List<ProjectRightDevice> originalRightDeviceCopyList = new ArrayList<>();//旧数据列表副本
        List<ProjectRightDevice> newRightDeviceCopyList = new ArrayList<>();//新数据列表副本


        //將列表按照设备ID的不同，进行拆分
        Set<String> deviceIdSet = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toSet());

        List<List<ProjectRightDevice>> deviceCertListList = new ArrayList<>();
        for (String deviceId : deviceIdSet) {
            deviceCertListList.add(rightDeviceList.stream().filter(e -> e.getDeviceId().equals(deviceId)).collect(Collectors.toList()));
        }

        /***********按设备ID分组,并获与传入数据不同的数据***********/
        for (List<ProjectRightDevice> newRightDeviceList : deviceCertListList) {
            if (CollUtil.isEmpty(newRightDeviceList)) {
                continue;
            }

            originalRightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getDeviceId, newRightDeviceList.get(0).getDeviceId()));


            //不存在原始数据时，所有数据均为要添加的数据
            if (CollUtil.isEmpty(originalRightDeviceList)) {
                addList.addAll(originalRightDeviceList);
            } else {
                originalRightDeviceCopyList = new ArrayList<ProjectRightDevice>(originalRightDeviceList);
                newRightDeviceCopyList = new ArrayList<ProjectRightDevice>(newRightDeviceList);


                //要删除的数据 = 原始数据列表副本 - 新数据列表中共有的部分
                originalRightDeviceCopyList.removeAll(newRightDeviceList);

                //要新增的数据 = 新数据列表副本 - 原始数据列表中共有的部分
                newRightDeviceCopyList.removeAll(originalRightDeviceList);

                addList.addAll(originalRightDeviceCopyList);
                delList.addAll(newRightDeviceCopyList);
            }
        }

        if (isAddList) {
            return addList;
        } else {
            return delList;
        }

    }


    /**
     * 获取需要添加的关系列表
     *
     * @param rightDeviceList
     * @return
     */
    @Override
    public List<ProjectRightDevice> getAddList(List<ProjectRightDevice> rightDeviceList) {
        return getDiffrentList(rightDeviceList, true);
    }

    /**
     * 获取需要删除的关系列表
     *
     * @param rightDeviceList
     * @return
     */
    @Override
    public List<ProjectRightDevice> getDelList(List<ProjectRightDevice> rightDeviceList) {
        return getDiffrentList(rightDeviceList, false);
    }


    /**
     * 根据设备ID和下载状态，重新对凭证进行下发操作
     *
     * @param deviceId
     * @param downloadStatusEnum
     * @return
     * @author: 王伟
     * @since 2020-09-10
     */
    @Override
    public boolean resendByDeviceId(String deviceId, PassRightCertDownloadStatusEnum downloadStatusEnum) {
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(deviceId);
        //获取凭证
        List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                .eq(ProjectRightDevice::getDeviceId, deviceId)
                .eq(ProjectRightDevice::getDlStatus, downloadStatusEnum.code));

        if (CollUtil.isNotEmpty(rightDeviceList)) {
            //设置状态为下载中
            rightDeviceList.forEach(rightDevice -> {
                rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.DOWNLOADING.code);
            });
            this.updateBatchById(rightDeviceList);

            log.info("设备 {} 已上线，重新下发，状态为{}的凭证", deviceInfo.getDeviceName(), downloadStatusEnum.name());

            //下发
            return this.addCerts(rightDeviceList, false, false);
        }
        return true;
    }


    /**
     * 根据设备删除凭证，仅限于删除设备接口调用
     *
     * @param deviceId
     * @return
     * @since： 2020-09-03
     * @author: 王伟
     */
    @Override
    public boolean removeByDevice(String deviceId) {
        return this.remove(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getDeviceId, deviceId));
    }


    /**
     * 统一新增凭证到设备的方法
     * 注意：这里的凭证都是设备上不存在的
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     * @since 2020-08-18
     */
    @Override
    public boolean addCerts(List<ProjectRightDevice> rightDeviceList) {
        return this.addCerts(rightDeviceList, true);
    }


    /**
     * 改变设备权限关联配置，根据原有数据自动分析差异并进行业务处理
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    @Override
    public boolean changeRightDevice(List<ProjectRightDevice> rightDeviceList) {
        this.addCerts(this.getAddList(rightDeviceList));
        this.delCerts(this.getAddList(rightDeviceList));
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    public boolean redownloadCertByType(String deviceId, String certType) {
        if (StrUtil.isNotBlank(deviceId) && StrUtil.isNotBlank(certType)) {
            List<String> statusLIst = new ArrayList<>();
            statusLIst.add(PassRightCertDownloadStatusEnum.FREEZING.code);
            statusLIst.add(PassRightCertDownloadStatusEnum.FREEZE.code);
            statusLIst.add(PassRightCertDownloadStatusEnum.DELETING.code);
            List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                    .eq(ProjectRightDevice::getDeviceId, deviceId).eq(ProjectRightDevice::getCertMedia, certType)
                    .notIn(ProjectRightDevice::getDlStatus, statusLIst));
            return this.addCerts(rightDeviceList);
        }
        return false;
    }

    /**
     * 保存或修改卡下发状态
     *
     * @param cardNo
     * @param deviceIdList
     * @param downloadStatusEnum
     */
    @Override
    public void saveOrUpdateCard(String cardNo, List<String> deviceIdList, PassRightCertDownloadStatusEnum downloadStatusEnum) {
        ProjectCard card = projectCardServiceImpl.getByCardNo(cardNo, ProjectContextHolder.getProjectId());
        ProjectRightDevice rightDevice;
        if (card != null) {
            //根据不同的业务逻辑，转换数据

            if (CollUtil.isEmpty(deviceIdList)) {
                //如果要设备列表未传入，获取该卡片所有已记录的设备列表信息
                if (CollUtil.isEmpty(deviceIdList)) {
                    List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertMediaId, card.getCardId()));
                    if (CollUtil.isNotEmpty(rightDeviceList)) {
                        deviceIdList = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());
                    }
                }
            }
//            if (downloadStatusEnum == PassRightCertDownloadStatusEnum.DOWNLOADING) {//下载中
//            } else if (downloadStatusEnum == PassRightCertDownloadStatusEnum.SUCCESS) {//下载成功
//            } else if (downloadStatusEnum == PassRightCertDownloadStatusEnum.DELETING) {//删除中
//                //如果要删除的设备列表未传入，获取该卡片所有已记录的设备列表信息
//                if (CollUtil.isEmpty(deviceIdList)) {
//                    List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertmediaId, card.getCardId()));
//                    if (CollUtil.isNotEmpty(rightDeviceList)) {
//                        deviceIdList = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());
//                    }
//                }
//            }

            //批量修改记录(记录可能出现部分存在的可能)
            if (CollUtil.isNotEmpty(deviceIdList)) {
                for (String deviceId : deviceIdList) {
                    saveOrUpdateCert(card.getCardId(), card.getCardNo(), CertmediaTypeEnum.Card.code, card.getPersonId(), card.getPersonType(), deviceId, downloadStatusEnum);
                }
            }

        }
    }

    /**
     * 移除凭证在所有设备下的记录，用于凭证全部删除成功时调用。
     *
     * @param certId
     */
    @Override
    public void removeCert(String certId) {
        this.remove(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertMediaId, certId));
    }

    /**
     * 移除凭证在所有设备下的记录，用于凭证全部删除成功时调用。
     *
     * @param certCode
     */
    @Override
    public void removeCertByCode(String certCode) {
        this.remove(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertMediaCode, certCode));
    }

    /**
     * 保存或修改人脸下发状态
     *
     * @param faceId
     * @param deviceIdList
     * @param downloadStatusEnum
     */
    @Override
    public void saveOrUpdateFace(String faceId, List<String> deviceIdList, PassRightCertDownloadStatusEnum downloadStatusEnum) {
        ProjectFaceResources face = projectFaceResourcesServiceImpl.getById(faceId);
        ProjectRightDevice rightDevice;
        if (face != null) {
            if (CollUtil.isEmpty(deviceIdList)) {
                //如果要设备列表未传入，获取该卡片所有已记录的设备列表信息
                if (CollUtil.isEmpty(deviceIdList)) {
                    List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertMediaId, face.getFaceId()));
                    if (CollUtil.isNotEmpty(rightDeviceList)) {
                        deviceIdList = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());
                    }
                }
            }

//            //根据不同的业务逻辑，转换数据
//            if (downloadStatusEnum == PassRightCertDownloadStatusEnum.DOWNLOADING) {//下载中
//            } else if (downloadStatusEnum == PassRightCertDownloadStatusEnum.SUCCESS) {//下载成功
//            } else if (downloadStatusEnum == PassRightCertDownloadStatusEnum.DELETING) {//删除中
//                //如果要删除的设备列表未传入，获取该卡片所有已记录的设备列表信息
//                if (CollUtil.isEmpty(deviceIdList)) {
//                    List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertmediaId, face.getFaceId()));
//                    if (CollUtil.isNotEmpty(rightDeviceList)) {
//                        deviceIdList = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());
//                    }
//                }
//            }

            //批量修改记录(记录可能出现部分存在的可能)
            if (CollUtil.isNotEmpty(deviceIdList)) {
                for (String deviceId : deviceIdList) {
//                    log.info("添加凭证状态：{}",deviceIdList);
                    saveOrUpdateCert(face.getFaceId(), face.getPicUrl(), CertmediaTypeEnum.Face.code, face.getPersonId(), face.getPersonType(), deviceId, downloadStatusEnum);
                }
            }

        }
    }

    @Override
    public List<String> getHouseCode(String certId) {
        return this.baseMapper.getHouseCodeByCertId(certId);
    }

    /**
     * 保存或更新凭证信息
     * 如果凭证记录不存在，则创建凭证
     */
    private void saveOrUpdateCert(String certId, String info, String certType, String personId, String personType, String deviceId, PassRightCertDownloadStatusEnum downloadStatusEnum) {
        ProjectRightDevice rightDevice = this.getByCertIdAndDeviceId(certId, deviceId);
        //创建记录
        // 这里貌似不用判断设备是否有对应介质能力。因为如果没有的话也不应该查的到对应的记录
        if (rightDevice == null && projectDeviceInfoService.checkCapabilityByCertType(deviceId, CertmediaTypeEnum.getEnumByCode(certType))) {
            rightDevice = new ProjectRightDevice();
            rightDevice.setDlStatus(downloadStatusEnum.code);
            rightDevice.setDeviceId(deviceId);
            rightDevice.setCertMediaId(certId);
            rightDevice.setCertMedia(certType);
            rightDevice.setPersonId(personId);
            rightDevice.setPersonType(personType);

            String personName = "", phone = "";

            if (StringUtils.equals(PersonTypeEnum.PROPRIETOR.code, personType)) {
                ProjectPersonInfo personInfo = projectPersonInfoService.getById(personId);
                if (personInfo != null) {
                    personName = personInfo.getPersonName();
                    phone = personInfo.getTelephone();
                }
            } else if (StringUtils.equals(PersonTypeEnum.STAFF.code, personType)) {
                ProjectStaff staff = projectStaffService.getById(personId);
                if (staff != null) {
                    personName = staff.getStaffName();
                    phone = staff.getMobile();
                }
            } else {
                //访客暂不考虑
            }

            //如果是人脸，获取人脸第三方ID
            if (StringUtils.equals(certType, CertmediaTypeEnum.Face.code)) {
                ProjectFaceResources face = projectFaceResourcesServiceImpl.getById(certId);
                if (face != null) {
                    rightDevice.setCertMediaCode(face.getFaceCode());
                }
            }

            rightDevice.setPersonName(personName);
            rightDevice.setMobileNo(phone);
            rightDevice.setCertMediaInfo(info);

            this.save(rightDevice);
            //更新记录
        } else {
            rightDevice.setDlStatus(downloadStatusEnum.code);
            this.updateById(rightDevice);
        }

    }

    /**
     * 根据凭证ID和设备ID，获取凭证信息，不存在返回空
     *
     * @return
     */
    private ProjectRightDevice getByCertIdAndDeviceId(String certId, String deviceId) {
        List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getDeviceId, deviceId).eq(ProjectRightDevice::getCertMediaId, certId));
        if (CollUtil.isNotEmpty(rightDeviceList)) {
            return rightDeviceList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean updateStateByIds(List<String> certIdList, String deviceId, PassRightCertDownloadStatusEnum statusEnum) {
        baseMapper.updateStateByIds(certIdList, deviceId, statusEnum.code);
        return true;
    }

    @Override
    public Page<ProjectRightDevice> pageByDeviceId(Page page, ProjectRightDeviceOptsAccessSearchConditionVo query, String deviceId) {
        return this.page(page, new QueryWrapper<ProjectRightDevice>().lambda()
                .eq(ProjectRightDevice::getDeviceId, deviceId)
                .eq(StrUtil.isNotEmpty(query.getDlStatus()), ProjectRightDevice::getDlStatus, query.getDlStatus())
                .like(StrUtil.isNotEmpty(query.getPersonName()), ProjectRightDevice::getPersonName, query.getPersonName())
                .eq(StrUtil.isNotEmpty(query.getCertMedia()), ProjectRightDevice::getCertMedia, query.getCertMedia())
                .orderByDesc(ProjectRightDevice::getCreateTime));
    }

    @Override
    public Page<ProjectPersonRightStatusVo> pagePersonRightStatus(Page page, PersonRightStatusSearchCondition searchCondition) {
        return baseMapper.pagePersonRightStatus(page, searchCondition, ProjectContextHolder.getProjectId());
    }


    @Override
    public Integer countDeviceId(String dlStatus, String deviceId) {
        return baseMapper.countByDeviceId(dlStatus, deviceId, ProjectContextHolder.getProjectId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer countFailNumbs(List<String> deviceIdList) {
        if (CollUtil.isNotEmpty(deviceIdList)) {
            return baseMapper.countFailByDeviceId(deviceIdList, ProjectContextHolder.getProjectId());
        } else {
            return 0;
        }
    }

    /**
     * <p>
     * 重新下发失败凭证
     * </p>
     *
     * @param deviceIdList
     * @return 处理结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resendFailCert(List<String> deviceIdList) {
        List<ProjectRightDevice> rightDeviceList = new ArrayList<>();
        List<ProjectRightDevice> noMemoryDeviceList = new ArrayList<>();
        if (CollUtil.isNotEmpty(deviceIdList)) {
//        获取下发凭证
            rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                    .in(ProjectRightDevice::getDeviceId, deviceIdList)
                    .eq(ProjectRightDevice::getDlStatus, PassRightCertDownloadStatusEnum.FAIL.code));

            noMemoryDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                    .in(ProjectRightDevice::getDeviceId, deviceIdList)
                    .eq(ProjectRightDevice::getDlStatus, PassRightCertDownloadStatusEnum.OUT_OF_MEMORY.code));
            rightDeviceList.addAll(noMemoryDeviceList);
//        调用下发接口
            if (CollUtil.isNotEmpty(rightDeviceList)) {
                return this.addCerts(rightDeviceList);
            }

        }
        return true;

    }

    @Override
    public Page<ProjectRightStatusVo> pageRightStatus(Page page, RightStatusSearchCondition query) {
        return baseMapper.pageRightStatus(page, query, ProjectContextHolder.getProjectId());
    }

    @Override
    public boolean resendFailedByPersonId(String personId) {
        List<String> failedCodeList = new ArrayList<>();
        failedCodeList.add(PassRightCertDownloadStatusEnum.FAIL.code);
        failedCodeList.add(PassRightCertDownloadStatusEnum.OUT_OF_MEMORY.code);
        List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda()
                .eq(ProjectRightDevice::getPersonId, personId).in(ProjectRightDevice::getDlStatus, failedCodeList));
        return this.addCerts(rightDeviceList, true, false);
    }

    @Override
    public Integer countFailedPersonNUm() {
        return baseMapper.getExceptionPersonIdList(ProjectContextHolder.getProjectId()).size();
    }

    @Override
    public boolean resendAllFailed() {
        List<String> failedCodeList = new ArrayList<>();
        failedCodeList.add(PassRightCertDownloadStatusEnum.FAIL.code);
        failedCodeList.add(PassRightCertDownloadStatusEnum.OUT_OF_MEMORY.code);
        List<ProjectRightDevice> rightDeviceList = this.list(new QueryWrapper<ProjectRightDevice>().lambda().in(ProjectRightDevice::getDlStatus, failedCodeList));
        return this.addCerts(rightDeviceList, true, false);
    }


    /**
     * <p>
     * 返回存储人脸、指纹、卡片的id列表
     * </p>
     *
     * @param personId 人员id
     * @return 返回介质id列表（所有介质的）
     * @author: 王良俊
     */
    @Override
    public List<ProjectRightDevice> listCertInfoByPersonId(String personId) {
        return baseMapper.listCertByPersonId(personId);
    }

    @Override
    public List<ProjectRightDevice> listCertInfoByPersonId(List<String> personIdList) {
        return baseMapper.listCertByPersonIdList(personIdList);
    }

    @Override
    protected Class<ProjectRightDevice> currentModelClass() {
        return ProjectRightDevice.class;
    }

    @Override
    public List<ProjectRightDevice> filterRightDeviceList(List<ProjectRightDevice> rightDeviceList) {
        if (CollUtil.isNotEmpty(rightDeviceList)) {
            return rightDeviceList.stream().filter(rightDevice -> projectDeviceInfoService.checkCapabilityByCertType(rightDevice.getDeviceId(), CertmediaTypeEnum.getEnumByCode(rightDevice.getCertMedia())))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();

    }

    @Override
    public Page<RightDeviceRelVo> page(Page page, RightDeviceRelVo vo) {
        ProjectRightDevice po = new ProjectRightDevice();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }
}
