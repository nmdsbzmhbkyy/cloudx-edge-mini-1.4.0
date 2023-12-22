package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.util.NoticeUtil;
import com.aurine.cloudx.estate.constant.enums.*;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.mapper.ProjectFaceResourcesMapper;
import com.aurine.cloudx.estate.service.ProjectFaceResourcesService;
import com.aurine.cloudx.estate.service.ProjectPersonDeviceService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.aurine.cloudx.estate.vo.ProjectFaceResourceAppPageVo;
import com.aurine.cloudx.estate.vo.ProjectFaceResourcesAppVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 项目人脸库，用于项目辖区内的人脸识别设备下载
 * </p>
 *
 * @author 王良俊
 * @date 2020-05-22 11:21:06
 */
@Service
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
        // 人脸操作
//        List<ProjectFaceResources> oldFaceList = this.list(new QueryWrapper<ProjectFaceResources>()
//                .lambda().eq(ProjectFaceResources::getPersonId, visitorId));
//        List<String> beRemoveFaceList = oldFaceList.stream().map(ProjectFaceResources::getFaceId).collect(Collectors.toList());
//        List<String> unRemoveFaceList = new ArrayList<>();
        List<ProjectFaceResources> beSaveFaceList = new ArrayList<>();
        if (CollUtil.isNotEmpty(faceList)) {
            faceList.forEach(faceResources -> {
//                if (StrUtil.isNotBlank(faceResources.getFaceId())) {
//                    unRemoveFaceList.add(faceResources.getFaceId());
//                } else {
                faceResources.setPersonId(visitorId);
                faceResources.setStatus(PassRightTokenStateEnum.USED.code);
                beSaveFaceList.add(faceResources);
//                }
            });
        }

//        beRemoveFaceList.removeAll(unRemoveFaceList);
//        projectRightDeviceService.removeCertmedias(beRemoveFaceList);
//
//        if (CollUtil.isNotEmpty(beRemoveFaceList)) {
//            this.remove(new QueryWrapper<ProjectFaceResources>().lambda()
//                    .in(ProjectFaceResources::getFaceId, beRemoveFaceList));
//        }
        this.saveBatch(beSaveFaceList);
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
            log.info("获取人脸已下载状态为 成功：countTotal:{},countSuccess：{}", countTotal, countSuccess);
            //下载成功，不处理
            return PassRightDownloadStateEnum.SUCCESS;
        } else if (countTotal == countFail) {
            log.info("获取人脸已下载状态为 失败：countTotal:{},countFail：{}", countTotal, countFail);
            //全部失败
            return PassRightDownloadStateEnum.ALLFAIL;
        } else if (countFail >= 1 && countTotal > countFail) {
            //部分失败
            log.info("获取人脸已下载状态为 部分失败：countTotal:{},countFail：{}", countTotal, countFail);
            return PassRightDownloadStateEnum.PARTFAIL;
        } else if (downloadingRightDevice != null) {

            long diffentMillis = ChronoUnit.MILLIS.between(downloadingRightDevice.getUpdateTime().toInstant(ZoneOffset.of("+8")), LocalDateTime.now().toInstant(ZoneOffset.of("+8")));
            //如果处于下载中状态超过12小时，转为超时状态
//            if ((diffentMillis >= (1000 * 60 * 60 * 12))) {
            log.info("获取人脸已下载事件为：{},是否超时：{}", diffentMillis, (diffentMillis >= (1000 * 60 * 5)));
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

        return baseMapper.getCurrentUserCreatePage(page, ProjectContextHolder.getProjectId(), dlStatus, SecurityUtils.getUser().getId());
    }


}
