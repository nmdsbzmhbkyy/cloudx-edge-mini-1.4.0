package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.constant.enums.CardOperationTypeEnum;
import com.aurine.cloudx.open.origin.constant.enums.CardStateEnum;
import com.aurine.cloudx.open.origin.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.open.origin.entity.ProjectCard;
import com.aurine.cloudx.open.origin.entity.ProjectCardHis;
import com.aurine.cloudx.open.origin.entity.ProjectRightDevice;
import com.aurine.cloudx.open.origin.mapper.ProjectCardHisMapper;
import com.aurine.cloudx.open.origin.service.ProjectCardHisService;
import com.aurine.cloudx.open.origin.service.ProjectCardService;
import com.aurine.cloudx.open.origin.service.ProjectRightDeviceService;
import com.aurine.cloudx.open.origin.vo.ProjectCardHisVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 卡操作记录
 *
 * @author zy
 * @date 2022-10-18 08:40:49
 */
@Service
public class ProjectCardHisServiceImpl extends ServiceImpl<ProjectCardHisMapper, ProjectCardHis> implements ProjectCardHisService {

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Resource
    private ProjectCardService projectCardService;

    @Override
    public Page<ProjectCardHisVo> pageVo(Page page, ProjectCardHisVo projectCardVo) {
        return baseMapper.pageVo(page, projectCardVo);
    }

    @Override
    public void updateState(ProjectRightDevice projectRightDevice, boolean bool) {
        ProjectCardHis projectCardHis = getOne(Wrappers.lambdaQuery(ProjectCardHis.class)
                .like(ProjectCardHis::getCardNo, projectRightDevice.getCertMediaInfo())
                .eq(ProjectCardHis::getPersonName, projectRightDevice.getPersonName())
                .eq(ProjectCardHis::getPhone, projectRightDevice.getMobileNo())
                .eq(ProjectCardHis::getState, CardStateEnum.ONGOING.code)
                .orderByDesc(ProjectCardHis::getCreateTime).last("limit 1"));

        if(projectCardHis == null){
            return;
        }
        boolean flag;
        Long deviceCount = Long.valueOf(projectCardHis.getDeviceCount());
        List<ProjectRightDevice> projectRightDeviceList = projectRightDeviceService.list(Wrappers.lambdaQuery(ProjectRightDevice.class).eq(ProjectRightDevice::getCertMediaInfo, projectRightDevice.getCertMediaInfo()));
        //下发 解挂
        if (projectCardHis.getOperationType().equals(CardOperationTypeEnum.SEND.code) || projectCardHis.getOperationType().equals(CardOperationTypeEnum.OBTAIN_CARD.code)) {
            //下载中的数量
            long downloadingCount = projectRightDeviceList.stream().filter(e -> e.getDlStatus().equals(PassRightCertDownloadStatusEnum.DOWNLOADING.code)).count();
            if (downloadingCount > 0) {
                return;
            }
            //成功的数量
            long successCount = projectRightDeviceList.stream().filter(e -> e.getDlStatus().equals(PassRightCertDownloadStatusEnum.SUCCESS.code)).count();
            //失败的数量
            long failCount = projectRightDeviceList.stream().filter(e -> e.getDlStatus().equals(PassRightCertDownloadStatusEnum.FAIL.code)).count();
            if (deviceCount.equals(successCount) && failCount == 0) {
                projectCardHis.setState(CardStateEnum.SUCCESS.code);
            } else {
                projectCardHis.setState(CardStateEnum.FAIL.code);
            }
            //挂失 注销
        } else if (projectCardHis.getOperationType().equals(CardOperationTypeEnum.LOSE_CARD.code) || projectCardHis.getOperationType().equals(CardOperationTypeEnum.REMOVE_CARD.code)) {
            //删除的数量
            long deleteCount = projectRightDeviceList.stream().filter(e -> e.getDlStatus().equals(PassRightCertDownloadStatusEnum.DELETING.code)).count();
            if(deleteCount == 0){
                projectCardHis.setState(CardStateEnum.SUCCESS.code);
            }else{
                projectCardHis.setState(CardStateEnum.FAIL.code);
            }
            //换卡
        }else if(projectCardHis.getOperationType().equals(CardOperationTypeEnum.CHANGE_CARD.code)){
            String[] cardNo = projectCardHis.getCardNo().split("变更为");
            if(projectRightDevice.getCertMediaInfo().equals(cardNo[0])){
                return;
            }
            //更换之后的卡
            String newCardNo = cardNo[1];
            ProjectCard projectCard = projectCardService.getOne(Wrappers.lambdaQuery(ProjectCard.class).eq(ProjectCard::getCardNo, newCardNo).eq(ProjectCard::getStatus,"1"));
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
        updateById(projectCardHis);
    }
}
