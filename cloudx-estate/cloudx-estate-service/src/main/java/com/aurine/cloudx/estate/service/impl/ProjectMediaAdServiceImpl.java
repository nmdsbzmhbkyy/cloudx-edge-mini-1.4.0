
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.constant.ProjectNoticeDeviceConstant;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectMediaAdMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectMediaAdFormVo;
import com.aurine.cloudx.estate.vo.ProjectMediaAdVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 媒体广告表
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:37:46
 */
@Service
@AllArgsConstructor
public class ProjectMediaAdServiceImpl extends ServiceImpl<ProjectMediaAdMapper, ProjectMediaAd> implements ProjectMediaAdService {

    private final ProjectDeviceInfoService projectDeviceInfoService;
    private final ProjectMediaAdDevCfgService projectMediaAdDevCfgService;
    private final ProjectMediaRepoService projectMediaRepoService;
    private final ProjectMediaAdPlaylistService projectMediaAdPlaylistService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveMedia(ProjectMediaAdFormVo projectMediaAdVo) {

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
            projectMediaAdDevCfg.setDlStatus(ProjectNoticeDeviceConstant.LOADING);
            projectMediaAdDevCfg.setAdId(projectMediaAdVo.getAdId());
            projectMediaAdDevCfg.setDeviceId(e.getDeviceId());
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
        //TODO: 调用第三方发送媒体广告方法
        return true;
    }

    @Override
    public boolean cleanMedia(List<ProjectMediaAdDevCfg> projectMediaAdDevCfgs) {
        projectMediaAdDevCfgs.forEach(e -> {
            //TODO:这里需要调用广告告清除的方法 暂时直接设置状态为清除
            e.setDlStatus(ProjectNoticeDeviceConstant.CLEANED);
        });
        return projectMediaAdDevCfgService.updateBatchById(projectMediaAdDevCfgs);
    }

    @Override
    public boolean resend(String adId, String deviceId) {
        //获取消息对象
        ProjectMediaAd projectMediaAd = baseMapper.selectById(adId);
        //根据adId和deviceId获取消息发送对象信息
        ProjectMediaAdDevCfg projectNoticeDevice = projectMediaAdDevCfgService.getOne(Wrappers.lambdaQuery(ProjectMediaAdDevCfg.class)
                .eq(ProjectMediaAdDevCfg::getAdId, adId)
                .eq(ProjectMediaAdDevCfg::getDeviceId, deviceId));
        //根据adId获取需要发送的媒体信息
        List<ProjectMediaAdPlaylist> projectMediaAdPlaylists = projectMediaAdPlaylistService.list(Wrappers.lambdaQuery(ProjectMediaAdPlaylist.class).eq(ProjectMediaAdPlaylist::getAdId, adId));
        projectNoticeDevice.setDlStatus(ProjectNoticeDeviceConstant.LOADING);
        projectMediaAdDevCfgService.updateById(projectNoticeDevice);
        //TODO:该方法需要调用重新发布广告的接口
        // 模拟接口调用后回调为下载成功  xull@aurine.cn 2020/5/25 9:34
        projectNoticeDevice.setDlStatus(ProjectNoticeDeviceConstant.SUCCESS);
        projectMediaAdDevCfgService.updateById(projectNoticeDevice);
        return true;
    }

    @Override
    public Page<ProjectMediaAdVo> pageMediaAd(Page page, ProjectMediaAdFormVo projectMediaAdFormVo) {
        return baseMapper.pageMediaAd(page, projectMediaAdFormVo);
    }

    @Override
    public boolean resendAll(String id) {

        List<ProjectMediaAdDevCfg> projectMediaAdDevCfgs = projectMediaAdDevCfg(id);
        projectMediaAdDevCfgs.forEach(e -> {
            //重新发送后会回调更新状态值操作,但未更新前先设置状态值为加载
            e.setDlStatus(ProjectNoticeDeviceConstant.LOADING);
        });
        projectMediaAdDevCfgService.updateBatchById(projectMediaAdDevCfgs);
        //TODO:该方法需要调用重新发布广告的接口
        // 模拟接口调用后回调为下载成功  xull@aurine.cn 2020/5/25 9:34
        projectMediaAdDevCfgs.forEach(e -> {
            e.setDlStatus(ProjectNoticeDeviceConstant.SUCCESS);
        });
        projectMediaAdDevCfgService.updateBatchById(projectMediaAdDevCfgs);
        return true;
    }


    /**
     * 失败和已清除的Project集合,即不为成功的状态
     *
     * @param adId
     *
     * @return
     */
    @Override
    public List<ProjectMediaAdDevCfg> projectMediaAdDevCfg(String adId) {
        LambdaQueryWrapper<ProjectMediaAdDevCfg> queryWrapper = Wrappers.lambdaQuery(ProjectMediaAdDevCfg.class)
                .eq(ProjectMediaAdDevCfg::getAdId, adId)
                .in(ProjectMediaAdDevCfg::getDlStatus,
                        ProjectNoticeDeviceConstant.FAIL,
                        ProjectNoticeDeviceConstant.CLEANED);
        return projectMediaAdDevCfgService.list(queryWrapper);
    }
}
