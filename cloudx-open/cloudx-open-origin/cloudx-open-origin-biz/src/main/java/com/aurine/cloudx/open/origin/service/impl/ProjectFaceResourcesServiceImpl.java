package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.common.core.util.RedisUtils;
import com.aurine.cloudx.open.common.core.util.ShortUUIDUtil;
import com.aurine.cloudx.open.common.entity.vo.FaceInfoVo;
import com.aurine.cloudx.open.origin.constant.enums.*;
import com.aurine.cloudx.open.origin.dto.ProjectHouseDTO;
import com.aurine.cloudx.open.origin.entity.*;
import com.aurine.cloudx.open.origin.mapper.ProjectFaceResourcesMapper;
import com.aurine.cloudx.open.origin.service.*;
import com.aurine.cloudx.open.origin.util.NoticeUtil;
import com.aurine.cloudx.open.origin.vo.ProjectFaceResourceAppPageVo;
import com.aurine.cloudx.open.origin.vo.ProjectFaceResourcesAppVo;
import com.aurine.cloudx.open.origin.vo.ProjectPassDeviceVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目人脸库，用于项目辖区内的人脸识别设备下载
 *
 * @author 王良俊
 * @date 2020-05-22 11:21:06
 */
@Service
@Primary
@Slf4j
public class ProjectFaceResourcesServiceImpl extends ServiceImpl<ProjectFaceResourcesMapper, ProjectFaceResources> implements ProjectFaceResourcesService {
    @Lazy
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Lazy
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;

    @Resource
    private NoticeUtil noticeUtil;
    //    @Resource
//    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private ProjectVisitorHisService projectVisitorHisService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    //    @Resource
//    private AbstractProjectFaceResourcesService adapterWebProjectFaceResourcesServiceImpl;
    @Resource
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;

    private final String LOCK_KEY_PER = "FACE_RESOURCES_LOCK_";

//
//    @Resource
//    DockModuleConfigUtil dockModuleConfigUtil;

    /**
     * 保存人臉
     * 如果项目使用中台3.0，则直接生成第三方ID数据
     *
     * @param po
     * @return
     * @auther:王伟
     * @since;2021-04-23 10:50
     */
    @Override
    public boolean saveFace(ProjectFaceResources po) {
//        SysThirdPartyInterfaceConfig thirdPartyConfig = sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(ProjectContextHolder.getProjectId());
//
//        if (thirdPartyConfig != null && StringUtils.equals(thirdPartyConfig.getName(), PlatformEnum.AURINE_EDGE_MIDDLE.value)) {
//
//            //获取默认的第三方前缀
//            AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(DeviceTypeEnum.GATE_DEVICE.getCode(), ProjectContextHolder.getProjectId(), 1, AurineEdgeConfigDTO.class);
//            String pre = config.getFaceUuidPre();
//
//            //获取住户的房屋框架号
//            String houseCode = "";
//            if (StringUtils.equals(po.getPersonType(), PersonTypeEnum.PROPRIETOR.code)) {
//                List<ProjectHouseDTO> houseList = projectHousePersonRelService.listHouseByPersonId(po.getPersonId());
//                if (CollUtil.isNotEmpty(houseList)) {
//                    for (ProjectHouseDTO houseDTO : houseList) {
//                        if (StringUtils.isNotEmpty(houseDTO.getHouseCode())) {
//                            houseCode = houseDTO.getHouseCode();
//                            break;
//                        }
//                    }
//                }
//            }
//
//            //拼接第三方ID
//            if (StringUtil.isEmpty(po.getFaceCode())) {
////                String uuid = String.valueOf(po.getSeq());
//                String uuid = ShortUUIDUtil.shortUUID();
//                po.setFaceCode(String.format(pre, houseCode + "0", uuid));
//            }
//        }

        return save(po);
    }

    /**
     * 根据personId、人脸路径 添加人脸
     * <p>
     * 如果当前用户不存在人脸，下发
     * 如果当前用户已经存在人脸，下发 + 下发成功替换
     * 如果当前用户存在未完成提交的人脸，则删除旧的人脸，并且，启动下发 + 下发成功替换
     *
     * @param faceResourcesWeChatVo
     * @return
     * @author: 王伟
     * @since 2020-09-09
     */
    @Override
    public R addFaceFromApp(ProjectFaceResourcesAppVo faceResourcesWeChatVo) {


        List<ProjectFaceResources> faceResourcesList = this.listByPersonId(faceResourcesWeChatVo.getPersonId());


        if (CollUtil.isEmpty(faceResourcesList)) {
            //没有面部，直接添加
            addFaceResouce(faceResourcesWeChatVo);

        } else {
            List<ProjectFaceResources> weChatFaceResourcesList = faceResourcesList.stream().filter(e -> e.getOrigin().equals(DataOriginEnum.WECHAT.code)).collect(Collectors.toList());


            if (CollUtil.isEmpty(weChatFaceResourcesList)) {
                //当前用户不存在微信面部信息，直接添加
                addFaceResouce(faceResourcesWeChatVo);

            } else {

                List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda()
                        .eq(ProjectRightDevice::getCertMediaId, weChatFaceResourcesList.get(0).getFaceId()));


                if (CollUtil.isEmpty(rightDeviceList)) {
                    //该用户无任何设备通行权限,直接添加面部照片
                    addFaceResouce(faceResourcesWeChatVo);

                } else {
                    String faceId = weChatFaceResourcesList.get(0).getFaceId();
                    //用户存在来自微信的面部信息，获取当前人脸下发情况
                    int countSuccess = 0, countTotal = rightDeviceList.size();

                    for (ProjectRightDevice rightDevice : rightDeviceList) {
                        if (rightDevice.getDlStatus().equals(PassRightCertDownloadStatusEnum.SUCCESS.code)) {
                            countSuccess++;
                        }
                    }

                    if (countTotal == countSuccess) {
                        //当前用户存在来自微信的面部信息，且状态为全部成功，执行替换
                        addFaceResouce(faceResourcesWeChatVo);

                    } else if (countTotal >= countSuccess) {
                        //当前用户存在来自微信的面部信息，且状态并非全部成功，执行删除，并直接添加
                        projectRightDeviceService.removeCertmedia(faceId);//删除凭证
//                        adapterWebProjectFaceResourcesServiceImpl.removeFace(faceId);
//                        this.removeById(faceId);
                        addFaceResouce(faceResourcesWeChatVo);

                    }
                }


            }


            //如果人脸来自于其他平台，则查询人脸下发的情况


        }


        return R.ok();
    }

    /**
     * 如果全部下载成功，且之前还有一张来自微信、APP的人脸，则删除之前旧的人脸数据
     * 用于接口回调使用
     */
    @Override
    public boolean removeOldAppFace(String faceId) {
        //上redis锁，避免同一时间多台设备人脸同时回调，造成并发问题
        boolean isLock = false;
        while (!isLock) {
            isLock = RedisUtils.lock(LOCK_KEY_PER + faceId, 1, 2);
        }

        //检查当前的凭证是否来自APP或者微信
        ProjectFaceResources face = this.getOne(new LambdaQueryWrapper<ProjectFaceResources>().eq(ProjectFaceResources::getFaceId, faceId).last("limit 1"));
        if (face == null) {
            log.error("面部ID:" + faceId + " 在系统中不存在");
            return true;
        }

        if (face.getOrigin().equals(DataOriginEnum.WECHAT.code)) {

            //查询是否存在旧凭证,并按创建日期排序
            List<ProjectFaceResources> faceList = this.list(new QueryWrapper<ProjectFaceResources>().lambda()
                    .eq(ProjectFaceResources::getPersonId, face.getPersonId())
                    .eq(ProjectFaceResources::getOrigin, DataOriginEnum.WECHAT.code).orderByAsc(ProjectFaceResources::getCreateTime));

            //检查是否全部都下载成功（）
            List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertMediaId, faceList.get(faceList.size() - 1).getFaceId()));
            int downloadState = checkIsSuccess(rightDeviceList);

            //如果存在多个来自非WEB端的凭证，则删除旧凭证
            if (faceList.size() >= 2) {
                if (downloadState == 1) {
                    projectRightDeviceService.removeCertmedia(faceList.get(0).getFaceId());
                    this.removeById(faceList.get(0).getFaceId());
                }
            }


            //人脸授权用户消息发送
            try {
//                PassRightDownloadStateEnum stateEnum= getDownloadStatus(faceId);
                if (downloadState == 1) {
                    // 人脸授权成功消息发送
                    log.info("[人脸下载状态校验] 当前条目成功 其他全部成功");
                    noticeUtil.send(faceId, StringUtils.equals(face.getPersonType(), PersonTypeEnum.STAFF.code), "人脸授权通知", "授权结果：授权成功<br>备注：您的人脸照片已授权成功", face.getPersonId());
                } else if (downloadState == 2) {
                    log.info("[人脸下载状态校验] 当前条目成功 其他部分失败");
                    noticeUtil.send(faceId, StringUtils.equals(face.getPersonType(), PersonTypeEnum.STAFF.code), "人脸授权通知", "授权结果：授权失败<br>备注：人脸照片授权异常，请联系物业处理", face.getPersonId());
                } else if (downloadState == 0) {
                    log.info("[人脸下载状态校验] 还在下载，不予处理");
//                    noticeUtil.send(faceId, StringUtils.equals(face.getPersonType(), PersonTypeEnum.STAFF.code), "人脸授权通知", "授权结果：授权失败<br>备注：人脸照片授权异常，请联系物业处理", face.getPersonId());
                } else {
                    log.info("[人脸下载状态校验] 当前条目成功 其他部分失败");
                    noticeUtil.send(faceId, StringUtils.equals(face.getPersonType(), PersonTypeEnum.STAFF.code), "人脸授权通知", "授权结果：授权失败<br>备注：人脸照片授权异常，请联系物业处理", face.getPersonId());
                }

            } catch (Exception e) {
                log.error("业主人脸授权提示消息发送失败", e);
                e.printStackTrace();
            }
        }

        RedisUtils.deleteLock(LOCK_KEY_PER + faceId);
        return true;
    }

    @Override
    public void sendNotice(String faceId, boolean success) {
        //上redis锁，避免同一时间多台设备人脸同时回调，造成并发问题
        boolean isLock = false;
        while (!isLock) {
            isLock = RedisUtils.lock(LOCK_KEY_PER + faceId, 1, 2);
        }

        ProjectFaceResources face = this.getById(faceId);

        //检查是否全部都下载成功（）
        List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertMediaId, face.getFaceId()));

        int downloadState = checkIsSuccess(rightDeviceList);

        //人脸授权用户消息发送
        try {
//                PassRightDownloadStateEnum stateEnum= getDownloadStatus(faceId);
            if (success) {
                if (downloadState == 1) {
                    log.info("[人脸下载状态校验] 当前条目成功 其他全部成功");
                    noticeUtil.send(faceId, StringUtils.equals(face.getPersonType(), PersonTypeEnum.STAFF.code), "人脸授权通知", "授权结果：授权成功<br>备注：您的人脸照片已授权成功", face.getPersonId());
                } else if (downloadState == 2) {
                    log.info("[人脸下载状态校验] 当前条目成功 其他部分失败");
                    noticeUtil.send(faceId, StringUtils.equals(face.getPersonType(), PersonTypeEnum.STAFF.code), "人脸授权通知", "授权结果：授权失败<br>备注：人脸照片授权异常，请联系物业处理", face.getPersonId());
                } else if (downloadState == -1) {
                    log.info("[人脸下载状态校验] 当前条目成功 其他全部失败");
                    noticeUtil.send(faceId, StringUtils.equals(face.getPersonType(), PersonTypeEnum.STAFF.code), "人脸授权通知", "授权结果：授权失败<br>备注：人脸照片授权异常，请联系物业处理", face.getPersonId());
                }
            } else {
                //当前失败的情况
                if (downloadState == 1) {
                    log.info("[人脸下载状态校验] 当前条目失败 其他全部成功");
                    noticeUtil.send(faceId, StringUtils.equals(face.getPersonType(), PersonTypeEnum.STAFF.code), "人脸授权通知", "授权结果：授权失败<br>备注：人脸照片授权异常，请联系物业处理", face.getPersonId());
                } else if (downloadState == 2) {
                    log.info("[人脸下载状态校验] 当前条目失败 其他部分失败");
                    noticeUtil.send(faceId, StringUtils.equals(face.getPersonType(), PersonTypeEnum.STAFF.code), "人脸授权通知", "授权结果：授权失败<br>备注：人脸照片授权异常，请联系物业处理", face.getPersonId());
                } else if (downloadState == -1) {
                    log.info("[人脸下载状态校验] 当前条目失败 其他全部失败");
                    this.sendFailNotice(faceId);
                }
            }


        } catch (Exception e) {
            log.error("业主人脸授权提示消息发送失败", e);
            e.printStackTrace();
        }

        RedisUtils.deleteLock(LOCK_KEY_PER + faceId);
    }

    @Override
    public void sendFailNotice(String faceId) {

        ProjectFaceResources face = this.getById(faceId);
        if (face == null) {
            log.error("面部ID:" + faceId + " 在系统中不存在");
            return;
        }

        if (face.getOrigin().equals(DataOriginEnum.WECHAT.code)) {
            //人脸授权用户消息发送
            try {
                // 人脸授权成功消息发送
                noticeUtil.send(faceId, StringUtils.equals(face.getPersonType(), PersonTypeEnum.STAFF.code), "人脸授权通知", "授权结果：授权失败<br>备注：授权失败人脸照片不符合要求，请重新上传", face.getPersonId());

            } catch (Exception e) {
                log.error("业主人脸授权提示消息发送失败", e);
                e.printStackTrace();
            }
        }

    }


    /**
     * 删除面部
     *
     * @param faceId
     * @return
     */
    @Override
    public boolean removeFace(String faceId) {
        projectRightDeviceService.removeCertmedia(faceId);
        return this.removeById(faceId);
    }

    /**
     * 根据第三方ID，保存人脸，如果第三方ID已存在，则不保存
     *
     * @param faceResources
     * @return
     */
    @Override
    public String saveByThirdCode(ProjectFaceResources faceResources) {
        List<ProjectFaceResources> faceList = this.list(new QueryWrapper<ProjectFaceResources>().lambda().eq(ProjectFaceResources::getFaceCode, faceResources.getFaceCode()));
        if (CollUtil.isNotEmpty(faceList)) {
            return faceList.get(0).getFaceId();
        } else {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            faceResources.setFaceId(uuid);
            this.saveFace(faceResources);
            return uuid;
        }
    }

    @Override
    public Page<FaceInfoVo> page(Page page, FaceInfoVo vo) {
        ProjectFaceResources po = new ProjectFaceResources();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }

    /**
     * 校验人脸是否都下载成功，并返回成功结果，-1，全部失败，0，还在下载，1，全部成功，2 全部下载完成，部分失败
     *
     * @param rightDeviceList
     * @return
     */
    private int checkIsSuccess(List<ProjectRightDevice> rightDeviceList) {

        if (CollUtil.isNotEmpty(rightDeviceList)) {
            int countSuccess = 0, countTotal = rightDeviceList.size(), countFails = 0;
            for (ProjectRightDevice rightDevice : rightDeviceList) {
                if (rightDevice.getDlStatus().equals(PassRightCertDownloadStatusEnum.SUCCESS.code)) {
                    countSuccess++;
                } else if (rightDevice.getDlStatus().equals(PassRightCertDownloadStatusEnum.DOWNLOADING.code)) {

                } else {
                    countFails++;
                }

            }
            log.info("[人脸下载状态校验] 当前共有{}条人脸记录,成功{},失败{}", countTotal, countSuccess, countFails);

            //由于该接口在更新数据之前被调用，因此实际的成功数量应该为 用户可通行设备数量-1
            if (countSuccess >= countTotal - 1) {
                return 1;
            } else if (countSuccess == 0 && countFails >= countTotal - 1) {
                //全部下载完成，且部分失败
                return -1;
            } else if (countFails > 0 && (countFails + countSuccess) >= countTotal - 1) {
                //全部下载完成，且部分失败
                return 2;
            } else {
                return 0;
            }

        }
//        return false;
        return 0;

    }

    /**
     * 保存面部，并保存权限
     *
     * @return
     */
    private boolean addFaceResouce(ProjectFaceResourcesAppVo faceResourcesWeChatVo) {
        //保存凭证到对象
        ProjectFaceResources face = new ProjectFaceResources();
        BeanUtils.copyProperties(faceResourcesWeChatVo, face);
        face.setOrigin(DataOriginEnum.WECHAT.code);
        this.saveFace(face);

        //保存住户、员工的权限
        if (faceResourcesWeChatVo.getPersonType().equals(PersonTypeEnum.PROPRIETOR.code) || faceResourcesWeChatVo.getPersonType().equals(PersonTypeEnum.STAFF.code)) {

            List<ProjectPassDeviceVo> projectPassDeviceVoList = projectPersonDeviceService.listDeviceByPersonId(faceResourcesWeChatVo.getPersonId());
            List<String> deviceIdArrayList = projectPassDeviceVoList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());
            projectPersonDeviceService.savePersonDevice(faceResourcesWeChatVo.getPersonId(), PersonTypeEnum.getEnum(faceResourcesWeChatVo.getPersonType()), deviceIdArrayList.toArray(new String[deviceIdArrayList.size()]), LocalDateTime.now(), LocalDateTime.parse("2199-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            //下发变更
            //如果是WR20,不进行下发
//            if (dockModuleConfigUtil.isWr20()) {
//                log.info("WR20项目，不进行凭证下发操作");
//            } else {
            projectRightDeviceService.saveRelationship(deviceIdArrayList, faceResourcesWeChatVo.getPersonId());
//            }

        } else if (faceResourcesWeChatVo.getPersonType().equals(PersonTypeEnum.VISITOR.code)) {//保存访客权限

            //访客权限在通过审核后在进行配置
        } else {
            return false;
        }

        return true;
    }


    @Override
    public List<ProjectFaceResources> listByPersonId(String personId) {
        return this.list(new QueryWrapper<ProjectFaceResources>().lambda()
                .eq(ProjectFaceResources::getPersonId, personId)
                .orderByDesc(ProjectFaceResources::getCreateTime));
    }

    @Override
    public List<ProjectFaceResources> listByPersonId(List<String> personIdList) {
        if (CollUtil.isNotEmpty(personIdList)) {
            return this.list(new QueryWrapper<ProjectFaceResources>().lambda().in(ProjectFaceResources::getPersonId, personIdList));
        }
        return new ArrayList<>();
    }

    @Override
    public boolean updateFacesBatch(String personId, List<String> personIdList) {
        ProjectFaceResources projectFaceResources = new ProjectFaceResources();
        projectFaceResources.setPersonId(personId);
        return this.update(projectFaceResources, new QueryWrapper<ProjectFaceResources>().lambda()
                .in(ProjectFaceResources::getFaceId, personIdList));
    }

    @Override
    public boolean removeByPersonId(String personId) {
        return this.remove(new QueryWrapper<ProjectFaceResources>().lambda().eq(ProjectFaceResources::getPersonId, personId));
    }

    @Override
    public void operateFace(List<ProjectFaceResources> faceList, String visitorId) {

        String pre = null;
//        SysThirdPartyInterfaceConfig thirdPartyConfig = sysDeviceTypeThirdPartyConfigService.getConfigByProjectId(ProjectContextHolder.getProjectId());
//        if (thirdPartyConfig != null && StringUtils.equals(thirdPartyConfig.getName(), PlatformEnum.AURINE_EDGE_MIDDLE.value)) {
//            //获取默认的第三方前缀
//            AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(DeviceTypeEnum.GATE_DEVICE.getCode(), ProjectContextHolder.getProjectId(), 1, AurineEdgeConfigDTO.class);
//            pre = config.getVisitorFaceUuidPre();
//        }
        List<ProjectFaceResources> beSaveFaceList = new ArrayList<>();
        if (CollUtil.isNotEmpty(faceList)) {
            String finalPre = pre;
            StringBuilder visitorHouseCode = new StringBuilder();
            ProjectVisitorHis visitorHis = projectVisitorHisService.getOne(new LambdaQueryWrapper<ProjectVisitorHis>()
                    .eq(ProjectVisitorHis::getVisitId, visitorId).last("limit 1"));
            String visitHouseId = visitorHis.getVisitHouseId();
            ProjectFrameInfo house = projectFrameInfoService.getOne(new LambdaQueryWrapper<ProjectFrameInfo>().eq(ProjectFrameInfo::getEntityId, visitHouseId).last("limit 1"));
            if (house != null) {
                visitorHouseCode.append(house.getEntityCode());
            } else {
                List<ProjectEntityLevelCfg> levelCfgList = projectEntityLevelCfgService.list(new LambdaQueryWrapper<ProjectEntityLevelCfg>().eq(ProjectEntityLevelCfg::getProjectId, ProjectContextHolder.getProjectId()).eq(ProjectEntityLevelCfg::getIsDisable, "0"));
                int houseCodeLen = 0;
                for (ProjectEntityLevelCfg projectEntityLevelCfg : levelCfgList) {
                    houseCodeLen += projectEntityLevelCfg.getCodeRule();
                }
                for (int i = 0; i < houseCodeLen; i++) {
                    visitorHouseCode.append("0");
                }
            }
            faceList.forEach(po -> {

                //获取住户的房屋框架号
                StringBuilder houseCode = new StringBuilder("0");
                Map<String, String> visitorHouseCodeMap = new HashMap<>();
                if (StringUtils.equals(po.getPersonType(), PersonTypeEnum.PROPRIETOR.code)) {
                    List<ProjectHouseDTO> houseList = projectHousePersonRelService.listHouseByPersonId(po.getPersonId());
                    if (CollUtil.isNotEmpty(houseList)) {
                        for (ProjectHouseDTO houseDTO : houseList) {
                            if (StringUtils.isNotEmpty(houseDTO.getHouseCode())) {
                                houseCode = new StringBuilder(houseDTO.getHouseCode());
                                break;
                            }
                        }
                    }
                } else if (StringUtils.equals(po.getPersonType(), PersonTypeEnum.VISITOR.code)) {
                    houseCode = visitorHouseCode;
                }

                //拼接第三方ID
                if (StringUtil.isEmpty(po.getFaceCode())) {
                    po.setPersonId(visitorId);
                    po.setStatus(PassRightTokenStateEnum.USED.code);
                    if (StringUtil.isNotEmpty(finalPre)) {
                        String uuid = ShortUUIDUtil.shortUUID();
                        po.setFaceCode(String.format(finalPre, houseCode + "0", uuid));
                    }
                    beSaveFaceList.add(po);
                }
            });
            this.saveBatch(beSaveFaceList);
        }
    }

    /**
     * 根据第三方id和项目编号，获取对象
     *
     * @param code
     * @param projectId
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    @Override
    public ProjectFaceResources getByCode(String code, int projectId) {
        return this.baseMapper.getByCode(projectId, code);
    }

    /**
     * 根据FaceId和项目编号，获取对象
     *
     * @param faceId
     * @param projectId
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    @Override
    public ProjectFaceResources getByFaceId(String faceId, int projectId) {
        return this.baseMapper.getByFaceId(projectId, faceId);
    }

    /**
     * 根据用户id，获取用户的第一张面部识别照片
     * <p>
     * 当用户不存在人脸，返回empty对象
     * 当用户存在多张人脸，且不存在微信上传的人脸时，返回最新上传的头像
     * 当用户存在多张人脸，存在微信上传的人脸时，返回微信上传的人脸
     * 当用户正在下发人脸, 根据来源获取人脸的下载状态，并返回正在下载人脸的url地址
     *
     * @param personId
     * @return
     * @author: 王伟
     * @since 2020-09-09
     */
    @Override
    public ProjectFaceResourcesAppVo getByPersonIdForApp(String personId, String personType) {
        ProjectFaceResourcesAppVo appVo = new ProjectFaceResourcesAppVo();
        appVo.setHaveFace("1");

        //根据personid，获取人脸
        List<ProjectFaceResources> faceResourcesList = this.listByPersonId(personId);

        //当用户不存在人脸，返回empty对象
        if (CollUtil.isEmpty(faceResourcesList)) {
            appVo.setHaveFace("0");
            return appVo;
        }

        //是否包含来自于小程序的人脸, 并获取最新的一张人脸
        List<ProjectFaceResources> weChatFaceResourcesList = faceResourcesList.stream().filter(e -> e.getOrigin().equals(DataOriginEnum.WECHAT.code)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(weChatFaceResourcesList)) {
            BeanUtils.copyProperties(weChatFaceResourcesList.get(0), appVo);

        } else {
            BeanUtils.copyProperties(faceResourcesList.get(0), appVo);
        }


        PassRightDownloadStateEnum dlStatus = this.getDownloadStatus(appVo.getFaceId());

        //如果照片来自于web平台，且状态并非成功，则显示为无人脸
        if (appVo.getOrigin().equals(DataOriginEnum.WEB.code) && dlStatus != PassRightDownloadStateEnum.SUCCESS) {
            appVo.setHaveFace("0");
            return appVo;
        }

        appVo.setDlStatus(dlStatus.code);

        return appVo;
    }

    /**
     * 获取人脸的下载状态
     *
     * @param faceId
     * @return
     */
    protected PassRightDownloadStateEnum getDownloadStatus(String faceId) {

        //查询人脸下发的情况
        List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertMediaId, faceId));

        //该用户无任何设备通行权限,直接返回成功
        if (CollUtil.isEmpty(rightDeviceList)) {
            return PassRightDownloadStateEnum.SUCCESS;
        }

        //获取当前人脸下发情况
        int countSuccess = 0, countFail = 0, countDownloading = 0, countTotal = rightDeviceList.size();
        ProjectRightDevice downloadingRightDevice = null;

        for (ProjectRightDevice rightDevice : rightDeviceList) {
            if (rightDevice.getDlStatus().equals(PassRightCertDownloadStatusEnum.SUCCESS.code)) {
                countSuccess++;
            } else if (rightDevice.getDlStatus().equals(PassRightCertDownloadStatusEnum.FAIL.code)) {
                countFail++;
            } else if (rightDevice.getDlStatus().equals(PassRightCertDownloadStatusEnum.DOWNLOADING.code)) {
                downloadingRightDevice = rightDevice;
                countDownloading++;
            }
        }

        if (countTotal == countSuccess) {
            //下载成功，不处理
            return PassRightDownloadStateEnum.SUCCESS;
        } else if (countTotal == countFail) {
            //全部失败
            return PassRightDownloadStateEnum.ALLFAIL;
        } else if (countFail >= 1 && countTotal > countFail) {
            //部分失败
            return PassRightDownloadStateEnum.PARTFAIL;
        } else if (downloadingRightDevice != null) {

            long diffentMillis = ChronoUnit.MILLIS.between(downloadingRightDevice.getUpdateTime().toInstant(ZoneOffset.of("+8")), LocalDateTime.now().toInstant(ZoneOffset.of("+8")));
            //如果处于下载中状态超过12小时，转为超时状态
//            if ((diffentMillis >= (1000 * 60 * 60 * 12))) {
            if ((diffentMillis >= (1000 * 60 * 5))) {
                return PassRightDownloadStateEnum.OUTTIME;
            } else {
                return PassRightDownloadStateEnum.DOWNLOADING;
            }
        } else {
            return PassRightDownloadStateEnum.ALLFAIL;
        }
    }


    @Override
    public Page<ProjectFaceResourceAppPageVo> pagePersonFace(Page page, String dlStatus) {

        return baseMapper.pagePersonFace(page, ProjectContextHolder.getProjectId(), dlStatus);
    }

}
