package com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.service.impl.ProjectVisitorServiceImpl;
import com.aurine.cloudx.estate.thirdparty.business.platform.BusinessBaseService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20VisitorObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.enums.WR20CredentialTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.enums.WR20GenderEnum;
import com.aurine.cloudx.estate.util.NoticeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (WebProjectPersonDeviceServiceImpl)
 * WR20 目前暂无接口，无需对接
 *
 * @author 王伟
 * @version 1.0.0
 * @date 2020/12/8 17:48
 */
@Slf4j
@Service("wr20ProjectVisitorServiceImplV1")
public class Wr20ProjectVisitorServiceImplV1 extends ProjectVisitorServiceImpl implements ProjectVisitorService, BusinessBaseService {

    @Resource
    private ProjectVisitorHisService projectVisitorHisService;
    @Resource
    private ProjectCardService projectCardService;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private NoticeUtil noticeUtil;
    @Resource
    private WR20RemoteService wr20RemoteService;
    @Resource
    private ProjectVisitorService projectVisitorService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private ProjectHouseInfoService projectHouseInfoService;
    @Value("${server.base-uri}")
    private String baseUriPath;


    /**
     * 通过授权
     *
     * @param visitId
     * @param auditStatus
     * @return
     */
    @Override
    public Boolean passAudit(String visitId, AuditStatusEnum auditStatus) {
        List<ProjectVisitorHis> visitorHisList = projectVisitorHisService.list(new QueryWrapper<ProjectVisitorHis>().lambda().eq(ProjectVisitorHis::getVisitId, visitId));
        if (CollUtil.isNotEmpty(visitorHisList)) {
            ProjectVisitorHis projectVisitorHis = visitorHisList.get(0);
            ProjectVisitor visitor = projectVisitorService.getById(projectVisitorHis.getVisitorId());
            //获取卡片、人脸
            List<ProjectCard> cardList = projectCardService.listByPersonId(projectVisitorHis.getVisitId());
            List<ProjectFaceResources> faceList = projectFaceResourcesService.listByPersonId(projectVisitorHis.getVisitId());

            WR20VisitorObj visitorObj = new WR20VisitorObj();
            visitorObj.setViName(projectVisitorHis.getVisitorName());
            visitorObj.setViSex(Integer.valueOf(WR20GenderEnum.getByCloudCode(visitor.getGender()).wr20Code));
//            visitorObj.setViCredentialType(WR20CredentialTypeEnum.getByCloudCode(visitor.getCredentialType()).wr20Code);
            visitorObj.setViCredentialType(WR20CredentialTypeEnum.JMSFZ.wr20Code);

            //证件号空值处理
            if (StringUtils.isNotEmpty(visitor.getCredentialNo())) {
                visitorObj.setViCredentialID(visitor.getCredentialNo());
            } else {
                visitorObj.setViCredentialID("111111111");
            }

            //来访人数空值处理
            if (projectVisitorHis.getPersonCount() != null && projectVisitorHis.getPersonCount() >= 1) {
                visitorObj.setViPersonNumber(projectVisitorHis.getPersonCount());
            } else {
                visitorObj.setViPersonNumber(1);
            }

            //获取被访人code
            if (StringUtils.isNotEmpty(projectVisitorHis.getVisitPersonId()) && StringUtils.isNotEmpty(projectVisitorHis.getVisitHouseId())) {
//                    //通过被访房屋、被访人id，获取被访人code
//                    List<ProjectHousePersonRel> housePersonRelList = projectHousePersonRelService.list(new QueryWrapper<ProjectHousePersonRel>().lambda()
//                            .eq(ProjectHousePersonRel::getHouseId, projectVisitorHis.getVisitHouseId())
//                            .eq(ProjectHousePersonRel::getPersonId, projectVisitorHis.getVisitPersonId()));
//
//                    if (CollUtil.isNotEmpty(housePersonRelList)) {
//                        ProjectHousePersonRel projectHousePersonRel = housePersonRelList.get(0);
//                        if (StringUtils.isNotEmpty(projectHousePersonRel.getRelaCode())) {
//                            visitorObj.setViDestID(Long.valueOf(projectHousePersonRel.getRelaCode()));
//                        } else {
//                            log.error("[WR20] 访客添加 被访人CODE不存在，被访人信息被忽略：{}", projectVisitorHis);
//                        }
//
//                    }

                ProjectHouseInfo houseInfo = projectHouseInfoService.getById(projectVisitorHis.getVisitHouseId());
                if (houseInfo == null) {
                    log.error("被访房屋 {} 不存在", projectVisitorHis.getVisitHouseId());
                    throw new RuntimeException("被访房屋不存在，请联系管理员");
                }

                if (StringUtils.isEmpty(houseInfo.getHouseCode())) {
                    log.error("被访房屋编码不存在 {} ", projectVisitorHis.getVisitHouseId());
                    throw new RuntimeException("被访房屋编码不存在，请联系管理员");
                }

                try {
                    visitorObj.setViDestFrameNo(houseInfo.getHouseCode());
                } catch (Exception e) {
                    log.error("被访房屋 {} 编码错误 {},  ", projectVisitorHis.getVisitHouseId(), houseInfo.getHouseCode());
                    throw new RuntimeException("被访房屋编码错误，请联系管理员");
                }


            } else {
                throw new RuntimeException("此小区已限制必填被访人");
            }

            if (CollUtil.isNotEmpty(cardList)) {
                visitorObj.setViCardNo(cardList.get(0).getCardNo());
            }
            if (CollUtil.isNotEmpty(faceList)) {
                if (faceList.get(0).getPicUrl().indexOf("http") == -1) {
                    visitorObj.setViImage(baseUriPath + faceList.get(0).getPicUrl());
                } else {
                    visitorObj.setViImage(faceList.get(0).getPicUrl());
                }
            }

            visitorObj.setViCarID(projectVisitorHis.getPlateNumber());
            visitorObj.setViVisitorTime(projectVisitorHis.getPassBeginTime());
            visitorObj.setViLeaveTime(projectVisitorHis.getPassEndTime());
            visitorObj.setViReason(projectVisitorHis.getReason());


            wr20RemoteService.addVisitor(ProjectContextHolder.getProjectId(), visitorObj, projectVisitorHis.getVisitId());
        }

        //调用原有业务接口
        return super.passAudit(visitId, auditStatus);
    }

    /**
     * 迁离
     *
     * @param visitId
     * @return
     */
    @Override
    public boolean signOff(String visitId) {
        ProjectVisitorHis visitorHis = projectVisitorHisService.getById(visitId);
        if (visitorHis == null || StringUtils.isEmpty(visitorHis.getVisitCode())) {
            log.error("[WR20] 访客数据错误，未找到访客或访客第三方code为空：{},{}", visitId, visitorHis);
            throw new RuntimeException("访客数据异常，请联系管理员");
        }

        JSONObject param = new JSONObject();
        param.put("userId", visitorHis.getVisitCode());
        wr20RemoteService.delVisitor(ProjectContextHolder.getProjectId(), param, visitId);

        return super.signOff(visitId);

    }


    @Override
    protected Class<ProjectVisitor> currentModelClass() {
        return ProjectVisitor.class;
    }

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

    /**
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.BUSINESS_WR20.code;
    }
}
