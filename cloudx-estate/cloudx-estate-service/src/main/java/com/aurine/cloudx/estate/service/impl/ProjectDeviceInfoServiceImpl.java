
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vcs.model.v20200515.GetDeviceLiveUrlRequest;
import com.aliyuncs.vcs.model.v20200515.GetDeviceLiveUrlResponse;
import com.aliyuncs.vcs.model.v20200515.GetDeviceVideoUrlRequest;
import com.aliyuncs.vcs.model.v20200515.GetDeviceVideoUrlResponse;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.common.uniview.model.GetLiveUrlVo;
import com.aurine.cloudx.common.uniview.model.GetRecordUrlVo;
import com.aurine.cloudx.common.uniview.model.GetVideoResponse;
import com.aurine.cloudx.common.uniview.vcs.UniviewVcsClient;
import com.aurine.cloudx.estate.constant.enums.DeviceStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.mapper.ProjectDeviceInfoMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceAttrService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设备信息表
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:38:59
 */
@Service
@RefreshScope
public class ProjectDeviceInfoServiceImpl extends ServiceImpl<ProjectDeviceInfoMapper, ProjectDeviceInfo>
    implements ProjectDeviceInfoService {

    @Resource
    private ProjectDeviceAttrService projectDeviceAttrService;

    private final static String CAMERA_TYPE = "6";

    @Value("${aliyun.vsc.accessKeyId:}")
    private String accessKeyId;

    @Value("${aliyun.vsc.accessSecret:}")
    private String accessSecret;

    @Value("${uniview.vsc.accessKeyId:25645212521}")
    private Long appId;

    @Value("${uniview.vsc.accessSecret:65a4f27ee572d23e3e977e19a341708f}")
    private String secretKey;

    @Override
    public Page pageVo(Page page, ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo) {
        switch (projectDeviceInfoPageFormVo.getDeviceTypeId()) {
            case DeviceTypeConstants.GATE_DEVICE:
            case DeviceTypeConstants.LADDER_WAY_DEVICE:
            case DeviceTypeConstants.CENTER_DEVICE:
            case DeviceTypeConstants.INDOOR_DEVICE:
            case DeviceTypeConstants.ENCODE_DEVICE:
                return baseMapper.pageVo(page, projectDeviceInfoPageFormVo, ProjectContextHolder.getProjectId());
            case DeviceTypeConstants.MONITOR_DEVICE:
                return baseMapper.pageCameraVo(page, projectDeviceInfoPageFormVo);
            default:
                return page;
        }
    }

    @Override
    public List<ProjectDeviceInfo> findByType(ProjectDeviceInfoFormVo projectDeviceInfoFormVo) {
        LambdaQueryWrapper<ProjectDeviceInfo> queryWrapper = Wrappers.lambdaQuery();

        if (projectDeviceInfoFormVo.getTypes().size() > 0) {
            queryWrapper.in(ProjectDeviceInfo::getDeviceType, projectDeviceInfoFormVo.getTypes());
        }
        if (projectDeviceInfoFormVo.getBuildingId() != null && !"".equals(projectDeviceInfoFormVo.getBuildingId())) {
            queryWrapper.eq(ProjectDeviceInfo::getBuildingId, projectDeviceInfoFormVo.getBuildingId());
        }
        if (projectDeviceInfoFormVo.getUnitId() != null && !"".equals(projectDeviceInfoFormVo.getUnitId())) {
            queryWrapper.eq(ProjectDeviceInfo::getUnitId, projectDeviceInfoFormVo.getUnitId());
        }
        if (projectDeviceInfoFormVo.getDeviceName() != null && !"".equals(projectDeviceInfoFormVo.getDeviceName())) {
            queryWrapper.like(ProjectDeviceInfo::getDeviceName, projectDeviceInfoFormVo.getDeviceName());
        }
        queryWrapper.ne(ProjectDeviceInfo::getStatus, "4");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public ProjectDeviceInfoVo getProjectDeviceInfoById(String id) {
        ProjectDeviceInfoVo projectDeviceInfoVo = baseMapper.getProjectDeviceInfoById(id);
        // 获取设备增值属性
        List<ProjectDeviceAttrListVo> projectDeviceAttrs =
            projectDeviceAttrService.getDeviceAttrListVo(ProjectContextHolder.getProjectId(),
                projectDeviceInfoVo.getDeviceType(), projectDeviceInfoVo.getDeviceId());
        if (projectDeviceAttrs != null && projectDeviceAttrs.size() > 0) {
            projectDeviceInfoVo.setDeviceAttrs(projectDeviceAttrs);
        }
        return projectDeviceInfoVo;
    }

    @Override
    public List<ProjectDeviceInfo> getListByDeviceEntityId(String deviceEntityId) {
        List<ProjectDeviceInfo> projectDeviceInfos = list(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
            .eq(ProjectDeviceInfo::getDeviceType, DeviceTypeConstants.GATE_DEVICE)
            .and(e -> e.eq(ProjectDeviceInfo::getDeviceEntityId, deviceEntityId).or()
                .isNull(ProjectDeviceInfo::getDeviceEntityId).or().eq(ProjectDeviceInfo::getDeviceEntityId, "")));
        return projectDeviceInfos;
    }

    @Override
    public List<ProjectPointDeviceVo> listDeviceByPointId(String pointId) {
        return baseMapper.listDeviceByPointId(pointId);
    }

    /**
     * 根据设备的第三方编号更新设备状态
     *
     * @param deviceCode
     * @param status
     * @return
     * @author: 王伟
     * @since 2020-08-07
     */
    @Override
    public boolean updateStatusByDeviceCode(String deviceCode, String status) {

        List<ProjectDeviceInfo> deviceList =
            this.list(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getThirdpartyCode, deviceCode));
        ProjectDeviceInfo projectDeviceInfo = null;

        if (CollectionUtil.isNotEmpty(deviceList)) {
            projectDeviceInfo = deviceList.get(0);
            projectDeviceInfo.setStatus(status);

            return this.updateById(projectDeviceInfo);
        }
        return false;
    }

    @Override
    public boolean updateStatusByDeviceSn(String deviceSn, String status) {

        List<ProjectDeviceInfo> deviceList =
            this.list(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getSn, deviceSn));
        ProjectDeviceInfo projectDeviceInfo = null;

        if (CollectionUtil.isNotEmpty(deviceList)) {
            projectDeviceInfo = deviceList.get(0);
            projectDeviceInfo.setStatus(status);

            return this.updateById(projectDeviceInfo);
        }
        return false;
    }

    /**
     * 通过第三方ID,获取设备
     *
     * @param deviceCode
     * @return
     * @author: 王伟
     * @since 2020-08-07
     */
    @Override
    public ProjectDeviceInfo getByDeviceCode(String deviceCode) {
        List<ProjectDeviceInfo> deviceList =
            this.list(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getThirdpartyCode, deviceCode));

        if (CollectionUtil.isNotEmpty(deviceList)) {
            return deviceList.get(0);
        }
        return null;
    }

    @Override
    public String getLiveUrl(String deviceId) {
        List<ProjectDeviceAttrListVo> attrs =
            projectDeviceAttrService.getDeviceAttrListVo(ProjectContextHolder.getProjectId(), CAMERA_TYPE, deviceId);
        String corpId = "";
        String gbId = "";
        String regionId = "";

        for (ProjectDeviceAttrListVo attr : attrs) {
            switch (attr.getAttrCode()) {
                case "corpId":
                    corpId = attr.getAttrValue();
                    break;
                case "gbId":
                    gbId = attr.getAttrValue();
                    break;
                case "regionId":
                    regionId = attr.getAttrValue();
                    break;
                default:
                    break;
            }
        }

        // 获取地址 - 阿里云
//        return getDeviceLiveUrl(regionId, corpId, gbId);
        // 获取地址 - 宇视
        String liveUrl;
        try {
            liveUrl = getUniviewLiveUrl(corpId, Integer.parseInt(gbId));
        } catch (NumberFormatException e1) {
            e1.printStackTrace();
            throw new NumberFormatException("设备配置参数异常，或未配置");
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new RuntimeException(e2.getMessage(), e2.getCause());
        }

        return liveUrl.replace("http://", "https://");
    }

    @Override
    public String getVideoUrl(String deviceId, Long startTime, Long endTime) {
        List<ProjectDeviceAttrListVo> attrs =
            projectDeviceAttrService.getDeviceAttrListVo(ProjectContextHolder.getProjectId(), CAMERA_TYPE, deviceId);

        String corpId = "";
        String gbId = "";
        String regionId = "";

        for (ProjectDeviceAttrListVo attr : attrs) {
            switch (attr.getAttrCode()) {
                case "corpId":
                    corpId = attr.getAttrValue();
                    break;
                case "gbId":
                    gbId = attr.getAttrValue();
                    break;
                case "regionId":
                    regionId = attr.getAttrValue();
                    break;
                default:
                    break;
            }
        }

        // 获取地址 - 阿里云
//        return getDeviceVideoUrl(regionId, corpId, gbId, startTime, endTime);
        // 获取地址 - 宇视
        String liveUrl;
        try {
            liveUrl = getUniviewRecordUrl(corpId, Integer.parseInt(gbId), startTime / 1000, endTime / 1000);
        } catch (NumberFormatException e1) {
            e1.printStackTrace();
            throw new NumberFormatException("设备配置参数异常，或未配置");
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new RuntimeException(e2.getMessage(), e2.getCause());
        }

        return liveUrl.replace("http://", "https://");
    }

    /**
     * 获取直播地址，后期做成工具包
     *
     * @param regionId
     * @param corpId
     * @param gbId
     * @return
     */
    private String getDeviceLiveUrl(String regionId, String corpId, String gbId) {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        GetDeviceLiveUrlRequest request = new GetDeviceLiveUrlRequest();
        request.setRegionId(regionId);
        request.setCorpId(corpId);
        request.setGbId(gbId);
        request.setOutProtocol("httpshls");

        try {
            GetDeviceLiveUrlResponse response = client.getAcsResponse(request);

            return response.getUrl();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return "";
    }

    private String getDeviceVideoUrl(String regionId, String corpId, String gbId, Long startTime, Long endTime) {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        GetDeviceVideoUrlRequest request = new GetDeviceVideoUrlRequest();
        request.setRegionId(regionId);
        request.setCorpId(corpId);
        request.setGbId(gbId);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setOutProtocol("httpshls");

        try {
            GetDeviceVideoUrlResponse response = client.getAcsResponse(request);

            return response.getUrl();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取宇视直播视频
     * @param deviceSerial
     * @param channelNo
     * @return
     */
    private String getUniviewLiveUrl(String deviceSerial, Integer channelNo) throws Exception {
        UniviewVcsClient client = null;
        try {
            client = UniviewVcsClient.getInstance(appId, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        GetLiveUrlVo getLiveUrlVo = new GetLiveUrlVo();
        getLiveUrlVo.setDeviceSerial(deviceSerial);
        getLiveUrlVo.setChannelNo(channelNo);
        getLiveUrlVo.setStreamIndex(0);
        getLiveUrlVo.setType(GetVideoResponse.STREAM_TYPE_HLS);

        return client.getLiveUrl(getLiveUrlVo);
    }

    /**
     * 获取宇视回放视频
     * @param deviceSerial
     * @param channelNo
     * @return
     */
    private String getUniviewRecordUrl(String deviceSerial, Integer channelNo, Long startTime, Long endTime) throws Exception {
        UniviewVcsClient client = null;
        try {
            client = UniviewVcsClient.getInstance(appId, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        GetRecordUrlVo getRecordUrlVo = new GetRecordUrlVo();
        getRecordUrlVo.setDeviceSerial(deviceSerial);
        getRecordUrlVo.setChannelNo(channelNo);
        getRecordUrlVo.setStreamIndex(0);
        getRecordUrlVo.setType(GetVideoResponse.STREAM_TYPE_HLS);
        getRecordUrlVo.setStartTime(startTime);
        getRecordUrlVo.setEndTime(endTime);
        getRecordUrlVo.setRecordTypes(0);

        return client.getRecordUrl(getRecordUrlVo);
    }

    /**
     * 获取离线设备数量
     *
     * @return
     * @author: 王伟
     * @since: 2020-09-03
     */
    @Override
    public int countOfflineDevice() {
        return this.count(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getStatus,
            DeviceStatusEnum.OFFLINE.code));
    }

    @Override
    public List<ProjectDeviceInfo> listDeviceByType(Integer projectId, String type) {
        return baseMapper.listByDeviceType(projectId, type);
    }

    @Override
    public Boolean uniqueDeviceNameByProject(String deviceName, String deviceId) {
        if (StringUtil.isEmpty(deviceName)) {
            return null;
        }
        if (StringUtil.isNotEmpty(deviceId)) {
            return null == getOne(
                new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getDeviceName, deviceName)
                    .eq(ProjectDeviceInfo::getProjectId, ProjectContextHolder.getProjectId())
                    .ne(ProjectDeviceInfo::getDeviceId, deviceId));
        }
        return null == getOne(
            new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getDeviceName, deviceName)
                .eq(ProjectDeviceInfo::getProjectId, ProjectContextHolder.getProjectId()));
    }

    @Override
    public int countByProductId(String productId) {
        return this
            .count(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getProductId, productId));
    }

    @Override
    public Page<ProjectDeviceInfoPageVo> pageDeviceParam(Page page,
        ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo) {
        LambdaQueryWrapper<ProjectDeviceInfo> wrapper = Wrappers.<ProjectDeviceInfo>lambdaQuery()
            .eq(ProjectDeviceInfo::getProductId, projectDeviceInfoPageFormVo.getProductId());
        if (StringUtil.isNotEmpty(projectDeviceInfoPageFormVo.getSn())) {
            wrapper.like(ProjectDeviceInfo::getSn, projectDeviceInfoPageFormVo.getSn());
        }
        if (StringUtil.isNotEmpty(projectDeviceInfoPageFormVo.getDeviceName())) {
            wrapper.and(qw -> qw.like(ProjectDeviceInfo::getDeviceName, projectDeviceInfoPageFormVo.getDeviceName())
                .or().like(ProjectDeviceInfo::getDeviceAlias, projectDeviceInfoPageFormVo.getDeviceName()));
        }
        return this.page(page, wrapper);
    }

    @Override
    public List<ProjectDeviceInfo> findRichByType(ProjectDeviceInfoFormVo projectDeviceInfoFormVo) {
        return baseMapper.findRichByType(projectDeviceInfoFormVo);
    }

    @Override
    public Integer countBySn(String sn,String deviceId) {
        return baseMapper.countBySn(sn,deviceId);
    }

}
