package com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.service.impl.ProjectRightDeviceServiceImpl;
import com.aurine.cloudx.estate.thirdparty.business.platform.BusinessBaseService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("wr20ProjectRightDeviceServiceImplV1")
@Slf4j
public class Wr20ProjectRightDeviceServiceImplV1 extends ProjectRightDeviceServiceImpl implements ProjectRightDeviceService, BusinessBaseService {

    @Resource
    private ProjectHousePersonRelService webProjectHousePersonRelService;
    @Resource
    private WR20RemoteService wr20RemoteService;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectCardService projectCardService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    /**
     * 根据设备，调用删除或添加凭证接口
     *
     * @param rightDeviceList
     * @return
     * @author: 王伟
     */
    @Override
    public boolean remoteInterfaceByDevices(List<ProjectRightDevice> rightDeviceList, boolean isAdd) {
        if (isAdd && CollUtil.isNotEmpty(rightDeviceList)) {
            log.info("[WR20] 执行重新下发:{}", rightDeviceList);


            for (ProjectRightDevice rightDevice : rightDeviceList) {

                //获取下发设备信息
                ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(rightDevice.getDeviceId());


                JSONObject param = new JSONObject();
                if (StringUtils.equals(rightDevice.getCertMedia(), CertmediaTypeEnum.Face.code)) {
                    if (StringUtils.isNotEmpty(rightDevice.getCertMediaCode())) {
                        ProjectFaceResources face = projectFaceResourcesService.getById(rightDevice.getCertMediaId());
                        if (face != null) {
                            param.put("accessId", face.getFaceCode());
                            param.put("type", "0");
                            param.put("deviceNo", deviceInfo.getThirdpartNo());
                        } else {
                            log.error("[WR20] 下发凭证失败，未找到ID为{} 的人脸", rightDevice.getCertMediaId());
                        }
                    } else {
                        log.error("[WR20] 凭证CODE为空，不执行下发操作：{}", rightDevice);
                    }
                } else if (StringUtils.equals(rightDevice.getCertMedia(), CertmediaTypeEnum.Card.code)) {
                    ProjectCard card = projectCardService.getById(rightDevice.getCertMediaId());
                    if (card != null) {
                        param.put("accessId", card.getCardNo());
                        param.put("type", "1");
                        param.put("deviceNo", deviceInfo.getThirdpartNo());
                    } else {
                        log.error("[WR20] 下发凭证失败，未找到ID为{} 的卡片", rightDevice.getCertMediaId());
                    }
                }

                wr20RemoteService.redown(ProjectContextHolder.getProjectId(), param, rightDevice.getCertMediaId());

            }

        }
        //获取到要下发的住户列表

        //根据下发或删除操作，配置接口变更授权方法

        //WR20不进行直接下发业务


        return true;
    }


    @Override
    protected Class<ProjectRightDevice> currentModelClass() {
        return ProjectRightDevice.class;
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
