
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.constant.enums.DeviceMediaAdDlStatusEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectMediaAdMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.util.delay.TaskUtil;
import com.aurine.cloudx.estate.util.delay.entity.MediaAdDelayTask;
import com.aurine.cloudx.estate.vo.MediaAdInfoVo;
import com.aurine.cloudx.estate.vo.ProjectMediaAdFormVo;
import com.aurine.cloudx.estate.vo.ProjectMediaAdVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 媒体广告表
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:37:46
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProjectMediaAdServiceImpl extends ServiceImpl<ProjectMediaAdMapper, ProjectMediaAd> implements ProjectMediaAdService {


    private final ProjectDeviceInfoService projectDeviceInfoService;
    private final ProjectMediaAdDevCfgService projectMediaAdDevCfgService;
    private final ProjectMediaRepoService projectMediaRepoService;
    private final ProjectMediaAdPlaylistService projectMediaAdPlaylistService;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    public String saveMedia(ProjectMediaAdFormVo projectMediaAdVo) {

        List<String> deviceIds = projectMediaAdVo.getDeviceIds();
        List<String> repoIds = projectMediaAdVo.getRepoIds();

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        projectMediaAdVo.setAdId(uuid);

        //批量获取设备信息
        List<ProjectDeviceInfo> projectDeviceInfos = projectDeviceInfoService.listByIds(deviceIds);
        //发送设备消息列表
        List<ProjectMediaAdDevCfg> projectMediaAdDevCfgs = new ArrayList<>();
        projectDeviceInfos.forEach(e -> {
            ProjectMediaAdDevCfg projectMediaAdDevCfg = new ProjectMediaAdDevCfg();
            projectMediaAdDevCfg.setDlStatus(DeviceMediaAdDlStatusEnum.DOWNLOADING.systemCode);
            projectMediaAdDevCfg.setAdId(projectMediaAdVo.getAdId());
            projectMediaAdDevCfg.setDeviceId(e.getDeviceId());
            ProjectNoticeDevice temp = projectDeviceInfoService.getDeviceNameAndBuildingNameAndUnitName(e.getDeviceId());
            projectMediaAdDevCfg.setDeviceName(temp.getDeviceName());
            projectMediaAdDevCfg.setBuildingName(temp.getBuildingName());
            projectMediaAdDevCfg.setUnitName(temp.getUnitName());
            projectMediaAdDevCfgs.add(projectMediaAdDevCfg);
        });

        //设置发送设备列表
        projectMediaAdDevCfgService.saveBatch(projectMediaAdDevCfgs);

        //批量获取媒体库信息
        List<ProjectMediaRepo> projectMediaRepos = projectMediaRepoService.listByIds(repoIds);
        //发送媒体信息列表
        List<ProjectMediaAdPlaylist> projectMediaAdPlaylists = new ArrayList<>();
        projectMediaRepos.forEach(e -> {
            ProjectMediaAdPlaylist projectMediaAdPlaylist = new ProjectMediaAdPlaylist();
            projectMediaAdPlaylist.setAdId(projectMediaAdVo.getAdId());
            projectMediaAdPlaylist.setRepoId(e.getRepoId());
            projectMediaAdPlaylists.add(projectMediaAdPlaylist);
        });
        //设置发送媒体广告列表
        projectMediaAdPlaylistService.saveBatch(projectMediaAdPlaylists);

        ProjectMediaAd projectMediaAd = new ProjectMediaAd();
        BeanUtils.copyProperties(projectMediaAdVo, projectMediaAd);
        baseMapper.insert(projectMediaAd);

        this.sendMediaAd(projectMediaAd.getAdId());

        return projectMediaAd.getAdId();
    }

    @Override
    public boolean cleanMedia(List<ProjectMediaAdDevCfg> projectMediaAdDevCfgs) {
        Set<String> adIdSet = projectMediaAdDevCfgs.stream().map(ProjectMediaAdDevCfg::getAdId).collect(Collectors.toSet());
        if (CollUtil.isEmpty(adIdSet)) {
            return true;
        }
        List<ProjectMediaAd> mediaAdList = this.list(new LambdaQueryWrapper<ProjectMediaAd>().in(ProjectMediaAd::getAdId, adIdSet).select(ProjectMediaAd::getSeq, ProjectMediaAd::getAdId));
        Map<String, Long> seqMap = mediaAdList.stream().collect(Collectors.toMap(ProjectMediaAd::getAdId, ProjectMediaAd::getSeq, (aLong, aLong2) -> aLong2));

        projectMediaAdDevCfgs.forEach(e -> {
            if (seqMap.containsKey(e.getAdId())) {
                try {
                    cleanMediaAd(seqMap.get(e.getAdId()), e.getDeviceId());
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        return true;
    }

    @Override
    public boolean resend(String adId, String deviceId) {
        String[] dlStatusArr = {DeviceMediaAdDlStatusEnum.FAIL.systemCode, DeviceMediaAdDlStatusEnum.DOWNLOADING.systemCode};
        ProjectMediaAd mediaAd = this.getById(adId);
        int count = projectMediaAdDevCfgService.count(new LambdaQueryWrapper<ProjectMediaAdDevCfg>().eq(ProjectMediaAdDevCfg::getAdId, mediaAd.getAdId()).in(ProjectMediaAdDevCfg::getDlStatus, dlStatusArr));
        if (count == 0) {
            return true;
        }
        List<ProjectMediaRepo> projectMediaRepoList = projectMediaRepoService.listMediaRepoByAdSeq(mediaAd.getSeq());
        projectMediaAdDevCfgService.update(new LambdaUpdateWrapper<ProjectMediaAdDevCfg>()
                .eq(ProjectMediaAdDevCfg::getAdId, adId)
                .eq(ProjectMediaAdDevCfg::getDeviceId, deviceId)
                .in(ProjectMediaAdDevCfg::getDlStatus, dlStatusArr)
                .set(ProjectMediaAdDevCfg::getDlStatus, DeviceMediaAdDlStatusEnum.DOWNLOADING.systemCode));
        MediaAdInfoVo mediaAdInfoVo = new MediaAdInfoVo(mediaAd.getSeq(), mediaAd.getFrequency(), projectMediaRepoList);
        boolean b = DeviceFactoryProducer.getFactory(deviceId).getDeviceService().sendMediaAd(deviceId, mediaAdInfoVo);
        if (!b) {
            projectMediaAdDevCfgService.update(new LambdaUpdateWrapper<ProjectMediaAdDevCfg>()
                    .eq(ProjectMediaAdDevCfg::getAdId, adId)
                    .eq(ProjectMediaAdDevCfg::getDeviceId, deviceId)
                    .set(ProjectMediaAdDevCfg::getDlStatus, DeviceMediaAdDlStatusEnum.FAIL.systemCode));
        }else {
            projectMediaAdDevCfgService.update(new LambdaUpdateWrapper<ProjectMediaAdDevCfg>()
                    .eq(ProjectMediaAdDevCfg::getAdId, adId)
                    .eq(ProjectMediaAdDevCfg::getDeviceId, deviceId)
                    .set(ProjectMediaAdDevCfg::getDlStatus, DeviceMediaAdDlStatusEnum.DOWNLOADING.systemCode));
        }
        return b;
    }

    @Override
    public Page<ProjectMediaAdVo> pageMediaAd(Page page, ProjectMediaAdFormVo projectMediaAdFormVo) {
        return baseMapper.pageMediaAd(page, projectMediaAdFormVo);
    }

    @Override
    public boolean resendAll(String id) {
        return this.sendMediaAd(id) == 0;
    }


    /**
     * 失败和已清除的Project集合,即不为成功的状态
     *
     * @param adId
     * @return
     */
    @Override
    public List<ProjectMediaAdDevCfg> projectMediaAdDevCfg(String adId) {
        LambdaQueryWrapper<ProjectMediaAdDevCfg> queryWrapper = Wrappers.lambdaQuery(ProjectMediaAdDevCfg.class)
                .eq(ProjectMediaAdDevCfg::getAdId, adId)
                .in(ProjectMediaAdDevCfg::getDlStatus,
                        DeviceMediaAdDlStatusEnum.FAIL,
                        DeviceMediaAdDlStatusEnum.CLEARED);
        return projectMediaAdDevCfgService.list(queryWrapper);
    }

    /**
     * 获取媒体名称
     *
     * @param adName
     * @return
     */
    @Override
    public List<ProjectMediaAd> getMediaAdByAdName(String adName) {
        return baseMapper.getMediaAdByAdName(adName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    public Integer sendMediaAd(String adId) {
        AtomicInteger faildNum = new AtomicInteger();
        ProjectMediaAd projectMediaAd = this.getOne(new LambdaQueryWrapper<ProjectMediaAd>().eq(ProjectMediaAd::getAdId, adId));
        String[] dlStatusArr = {DeviceMediaAdDlStatusEnum.DOWNLOADING.systemCode, DeviceMediaAdDlStatusEnum.FAIL.systemCode};
        List<ProjectMediaAdDevCfg> adDeviceList = projectMediaAdDevCfgService.list(new LambdaQueryWrapper<ProjectMediaAdDevCfg>()
                .eq(ProjectMediaAdDevCfg::getAdId, projectMediaAd.getAdId())
                .in(ProjectMediaAdDevCfg::getDlStatus, dlStatusArr)
                .select(ProjectMediaAdDevCfg::getDeviceId));
        if (CollUtil.isNotEmpty(adDeviceList)) {
            List<String> deviceIdList = adDeviceList.stream().map(ProjectMediaAdDevCfg::getDeviceId).collect(Collectors.toList());
            List<ProjectMediaRepo> projectMediaRepoList = projectMediaRepoService.listMediaRepoByAdSeq(projectMediaAd.getSeq());
            projectMediaAdDevCfgService.update(new LambdaUpdateWrapper<ProjectMediaAdDevCfg>()
                    .eq(ProjectMediaAdDevCfg::getAdId, projectMediaAd.getAdId())
                    .in(ProjectMediaAdDevCfg::getDeviceId, deviceIdList)
                    .set(ProjectMediaAdDevCfg::getDlStatus, DeviceMediaAdDlStatusEnum.DOWNLOADING.systemCode));
            if (CollUtil.isNotEmpty(projectMediaRepoList)) {
                MediaAdInfoVo mediaAdInfoVo = new MediaAdInfoVo(projectMediaAd.getSeq(), projectMediaAd.getFrequency(), projectMediaRepoList);
                adDeviceList.forEach(item -> {
                    try {
                        boolean b = DeviceFactoryProducer.getFactory(item.getDeviceId()).getDeviceService().sendMediaAd(item.getDeviceId(), mediaAdInfoVo);
                        if (!b) {
                            faildNum.incrementAndGet();
                        }else {
                            projectMediaAdDevCfgService.update(new LambdaUpdateWrapper<ProjectMediaAdDevCfg>()
                                    .eq(ProjectMediaAdDevCfg::getAdId, adId)
                                    .eq(ProjectMediaAdDevCfg::getDeviceId, item.getDeviceId())
                                    .set(ProjectMediaAdDevCfg::getDlStatus, DeviceMediaAdDlStatusEnum.SUCCESS.systemCode));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        faildNum.incrementAndGet();
                    }
                });
            }
            return faildNum.get();
        }
        return 0;
    }

    @Override
    public boolean sendMediaAd(String adId, String deviceId) {
        ProjectMediaAd mediaAd = this.getById(adId);
        List<ProjectMediaRepo> projectMediaRepoList = projectMediaRepoService.listMediaRepoByAdSeq(mediaAd.getSeq());
        projectMediaAdDevCfgService.update(new LambdaUpdateWrapper<ProjectMediaAdDevCfg>()
                .eq(ProjectMediaAdDevCfg::getAdId, adId)
                .eq(ProjectMediaAdDevCfg::getDeviceId, deviceId)
                .set(ProjectMediaAdDevCfg::getDlStatus, DeviceMediaAdDlStatusEnum.DOWNLOADING.systemCode));
        MediaAdInfoVo mediaAdInfoVo = new MediaAdInfoVo(mediaAd.getSeq(), mediaAd.getFrequency(), projectMediaRepoList);
        boolean b = DeviceFactoryProducer.getFactory(deviceId).getDeviceService().sendMediaAd(deviceId, mediaAdInfoVo);
        if (!b) {
            projectMediaAdDevCfgService.update(new LambdaUpdateWrapper<ProjectMediaAdDevCfg>()
                    .eq(ProjectMediaAdDevCfg::getAdId, adId)
                    .eq(ProjectMediaAdDevCfg::getDeviceId, deviceId)
                    .set(ProjectMediaAdDevCfg::getDlStatus, DeviceMediaAdDlStatusEnum.FAIL.systemCode));
        }
        return b;
    }

    @Override
    public boolean cleanMediaAd(Long adSeq, String deviceId) {
        return DeviceFactoryProducer.getFactory(deviceId).getDeviceService().cleanMediaAd(adSeq, deviceId);
    }
}
