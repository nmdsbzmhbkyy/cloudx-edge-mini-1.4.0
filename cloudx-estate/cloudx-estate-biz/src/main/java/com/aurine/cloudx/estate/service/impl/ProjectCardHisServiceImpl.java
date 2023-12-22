package com.aurine.cloudx.estate.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.constant.enums.CardOperationTypeEnum;
import com.aurine.cloudx.estate.constant.enums.CardStateEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.entity.ProjectCardHis;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.mapper.ProjectCardHisMapper;
import com.aurine.cloudx.estate.service.ProjectCardHisService;
import com.aurine.cloudx.estate.service.ProjectCardService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.aurine.cloudx.estate.vo.ProjectCardHisVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 卡操作记录
 *
 * @author zy
 * @date 2022-10-18 08:40:49
 */
@Slf4j
@Service
public class ProjectCardHisServiceImpl extends ServiceImpl<ProjectCardHisMapper, ProjectCardHis> implements ProjectCardHisService {

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Resource
    private ProjectCardService projectCardService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Page<ProjectCardHisVo> pageVo(Page page, ProjectCardHisVo projectCardVo) {
        return baseMapper.pageVo(page, projectCardVo);
    }

    private ProjectCardHis getLast(ProjectRightDevice projectRightDevice) {
        String key = "CARD_HIS" + ":" +projectRightDevice.getPersonId()  + "_" + projectRightDevice.getCertMediaInfo();
        String obj = null;
        try {
            obj = redisTemplate.opsForValue().get(key);
        }catch (Exception e) {
            log.warn("读取卡操作记录缓存异常", e);
        }
        if (StringUtils.isNotEmpty(obj)) {
            return JSONObject.parseObject(obj, ProjectCardHis.class);
        }
        else {
            ProjectCardHis projectCardHis = getOne(Wrappers.lambdaQuery(ProjectCardHis.class)
                    .eq(ProjectCardHis::getCardNo, projectRightDevice.getCertMediaInfo())
                    .eq(ProjectCardHis::getPersonId, projectRightDevice.getPersonId())
                    .eq(ProjectCardHis::getState, CardStateEnum.ONGOING.code)
                    .orderByDesc(ProjectCardHis::getCreateTime).last("limit 1"));
            if (projectCardHis!=null) {
                try {
                    redisTemplate.opsForValue().set(key, JSONObject.toJSONString(projectCardHis), 600, TimeUnit.SECONDS);
                }catch (Exception e) {
                    log.warn("设置卡操作记录缓存异常", e);
                }
            }
            return projectCardHis;
        }
    }

    private void removeLastCache(ProjectRightDevice projectRightDevice) {
        try{
            String key = "CARD_HIS" + ":" +projectRightDevice.getPersonId()  + "_" + projectRightDevice.getCertMediaInfo();
            redisTemplate.delete(key);
        }
        catch (Exception e) {
            log.warn("读取卡操作记录缓存异常", e);
        }
    }

    @Override
    public void updateState(ProjectRightDevice projectRightDevice, boolean bool) {
        /*ProjectCardHis projectCardHis = getOne(Wrappers.lambdaQuery(ProjectCardHis.class)
                .eq(ProjectCardHis::getCardNo, projectRightDevice.getCertMediaInfo())
                .eq(ProjectCardHis::getPersonName, projectRightDevice.getPersonName())
                .eq(ProjectCardHis::getPhone, projectRightDevice.getMobileNo())
                .eq(ProjectCardHis::getState, CardStateEnum.ONGOING.code)
                .orderByDesc(ProjectCardHis::getCreateTime).last("limit 1"));*/
        ProjectCardHis projectCardHis = getLast(projectRightDevice);
        if (projectCardHis == null) {
            return;
        }
        Long deviceCount = Long.valueOf(projectCardHis.getDeviceCount());
        //下发 解挂
        if (projectCardHis.getOperationType().equals(CardOperationTypeEnum.SEND.code) || projectCardHis.getOperationType().equals(CardOperationTypeEnum.OBTAIN_CARD.code)) {
            List<ProjectRightDevice> projectRightDeviceList = projectRightDeviceService.list(Wrappers.lambdaQuery(ProjectRightDevice.class)
                    .select(ProjectRightDevice::getDeviceId,ProjectRightDevice::getCertMediaId,ProjectRightDevice::getDlStatus)
                    .eq(ProjectRightDevice::getCertMediaInfo, projectRightDevice.getCertMediaInfo()));
            //下载中的数量
            long downloadingCount = projectRightDeviceList.stream().filter(e -> e.getDlStatus().equals(PassRightCertDownloadStatusEnum.DOWNLOADING.code)).count();
            if (downloadingCount > 0) {
                return;
            }
            if (projectCardHis.getDeviceCount().equals("1")) {
                List<String> deviceIdList = projectRightDeviceList.stream()
                        .filter(e -> e.getDeviceId().equals(projectRightDevice.getDeviceId()))
                        .filter(e -> e.getDlStatus().equals(PassRightCertDownloadStatusEnum.SUCCESS.code))
                        .map(ProjectRightDevice::getDeviceId)
                        .collect(Collectors.toList());
                if (deviceIdList.contains(projectRightDevice.getDeviceId())) {
                    projectCardHis.setState(CardStateEnum.SUCCESS.code);
                } else {
                    projectCardHis.setState(CardStateEnum.FAIL.code);
                }

            } else {
                //成功的数量
                long successCount = projectRightDeviceList.stream().filter(e -> e.getDlStatus().equals(PassRightCertDownloadStatusEnum.SUCCESS.code)).count();
                //失败的数量
                long failCount = projectRightDeviceList.stream().filter(e -> e.getDlStatus().equals(PassRightCertDownloadStatusEnum.FAIL.code)).count();
                if ((deviceCount.equals(successCount) || successCount > deviceCount) && failCount == 0) {
                    projectCardHis.setState(CardStateEnum.SUCCESS.code);
                } else {
                    projectCardHis.setState(CardStateEnum.FAIL.code);
                }
            }
            //挂失 注销
        } else if (projectCardHis.getOperationType().equals(CardOperationTypeEnum.LOSE_CARD.code) || projectCardHis.getOperationType().equals(CardOperationTypeEnum.REMOVE_CARD.code)) {
            List<ProjectRightDevice> projectRightDeviceList = projectRightDeviceService.list(Wrappers.lambdaQuery(ProjectRightDevice.class)
                    .select(ProjectRightDevice::getDeviceId,ProjectRightDevice::getCertMediaId,ProjectRightDevice::getDlStatus)
                    .eq(ProjectRightDevice::getCertMediaInfo, projectRightDevice.getCertMediaInfo()));
            //删除中的数量
            long deleteCount = projectRightDeviceList.stream().filter(e -> e.getDlStatus().equals(PassRightCertDownloadStatusEnum.DELETING.code)).count();
            if (deleteCount > 0) {
                return;
            }
            projectCardHis.setState(CardStateEnum.SUCCESS.code);
            //换卡
        } else if (projectCardHis.getOperationType().equals(CardOperationTypeEnum.CHANGE_CARD.code)) {
            //旧卡直接return结束
            if (projectRightDevice.getCertMediaInfo().equals(projectCardHis.getOldCardNo())) {
                return;
            }
            //更换之后的卡
            String newCardNo = projectCardHis.getCardNo();
            ProjectCard projectCard = projectCardService.getOne(Wrappers.lambdaQuery(ProjectCard.class).eq(ProjectCard::getCardNo, newCardNo).eq(ProjectCard::getStatus, "1"));
            List<ProjectRightDevice> newProjectRightDeviceList = projectRightDeviceService.list(Wrappers.lambdaQuery(ProjectRightDevice.class).eq(ProjectRightDevice::getCertMediaId, projectCard.getCardId()));
            //下载中的数量
            long downloadingCount = newProjectRightDeviceList.stream().filter(e -> e.getDlStatus().equals(PassRightCertDownloadStatusEnum.DOWNLOADING.code)).count();
            if (downloadingCount > 0) {
                return;
            }
            //成功的数量
            long successCount = newProjectRightDeviceList.stream().filter(e -> e.getDlStatus().equals(PassRightCertDownloadStatusEnum.SUCCESS.code)).count();
            //失败的数量
            long failCount = newProjectRightDeviceList.stream().filter(e -> e.getDlStatus().equals(PassRightCertDownloadStatusEnum.FAIL.code)).count();
            if (deviceCount.equals(successCount) && failCount == 0) {
                projectCardHis.setState(CardStateEnum.SUCCESS.code);
            } else {
                projectCardHis.setState(CardStateEnum.FAIL.code);
            }
        }
        log.info("更新卡操作记录：{}", projectCardHis);
        removeLastCache(projectRightDevice);
        updateById(projectCardHis);
    }

    @Override
    public void handleCardHis() {
        baseMapper.handleCardHis();
    }
}
