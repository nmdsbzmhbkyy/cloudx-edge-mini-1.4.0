package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.aurine.cloudx.common.core.constant.enums.AttrsConfig;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.constant.enums.DoorControllerEnum;
import com.aurine.cloudx.common.core.util.MacUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.common.uniview.model.GetLiveUrlVo;
import com.aurine.cloudx.common.uniview.model.GetRecordUrlVo;
import com.aurine.cloudx.common.uniview.model.GetVideoResponse;
import com.aurine.cloudx.common.uniview.vcs.UniviewVcsClient;
import com.aurine.cloudx.estate.constant.DeviceCollectConstant;
import com.aurine.cloudx.estate.constant.DeviceExcelConstant;
import com.aurine.cloudx.estate.constant.DeviceInfoConstant;
import com.aurine.cloudx.estate.constant.ExcelMinIOConstant;
import com.aurine.cloudx.estate.constant.enums.*;
import com.aurine.cloudx.estate.constant.enums.device.DeviceManageOperateTypeEnum;
import com.aurine.cloudx.estate.dto.DeviceAttrDto;
import com.aurine.cloudx.estate.dto.OpenApiProjectDeviceInfoManageDto;
import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.excel.ProjectDeviceCodeVerifyListener;
import com.aurine.cloudx.estate.excel.ProjectDeviceInfoInitListener;
import com.aurine.cloudx.estate.excel.ProjectDeviceInfoListener;
import com.aurine.cloudx.estate.excel.entity.access.IndoorDeviceExportExcel;
import com.aurine.cloudx.estate.feign.RemotePigxUserService;
import com.aurine.cloudx.estate.mapper.ProjectDeviceInfoMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.param.DeviceParamFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.param.DeviceParamServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceParamConfigService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceParamDataService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.PerimeterAlarmDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeOnlineStatusEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeChannelInfoDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoExtParamDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.factory.AurineEdgeRemoteDeviceOperateServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.entity.StreetLightDeviceStatus;
import com.aurine.cloudx.estate.thirdparty.module.intercom.factory.IntercomFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.business.service.DdDeviceMapService;
import com.aurine.cloudx.estate.util.ExcelUtil;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.ObjectPaseUtils;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import com.aurine.cloudx.estate.vo.*;
import com.aurine.parking.entity.vo.*;
import com.aurine.parking.feign.RemoteExitLaneService;
import com.aurine.parking.feign.RemoteParkingDeviceParamConfService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.Lists;
import com.pig4cloud.pigx.admin.api.entity.SysDictItem;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.minio.service.MinioTemplate;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 设备信息表
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:38:59
 */
@Service
@Slf4j
@RefreshScope
public class ProjectDeviceInfoServiceImpl extends ServiceImpl<ProjectDeviceInfoMapper, ProjectDeviceInfo> implements ProjectDeviceInfoService {

    private final static String CAMERA_TYPE = "6";
    private static final String HOME_ROOT = "0";
    private static ObjectMapper objectMapper = ObjectMapperUtil.instance();
    private static Map<String, List<ProjectDeviceParamInfo>> deviceParamInfoMap = new HashMap<>();
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;
    @Resource
    private ProjectDeviceLocationService projectDeviceLocationService;
    @Resource
    private ProjectServiceService projectServiceService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private ProjectDeviceInfoProxyServiceImpl projectDeviceInfoProxyService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ProjectDeviceCollectService projectDeviceCollectService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private ProjectDeviceAttrService projectDeviceAttrService;
    @Resource
    private ProjectDeviceParamInfoService projectDeviceParamInfoService;
    @Resource
    private SysServiceParamConfService sysServiceParamConfService;
    @Resource
    private SysProductServiceService sysProductServiceService;
    @Resource
    private ProjectDeviceParamHisService projectDeviceParamHisService;
    @Resource
    private ProjectDeviceReplaceInfoService projectDeviceReplaceInfoService;
    @Resource
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;
    @Resource
    private ProjectInfoService projectInfoService;
    @Resource
    private DdDeviceMapService ddDeviceMapService;
    @Autowired
    @Resource
    private ProjectDeviceLoadLogService projectDeviceLoadLogService;
    @Resource
    private ProjectPerimeterAlarmAreaService projectPerimeterAlarmAreaService;
    @Resource
    private ProjectDeviceLoadLogDetailService projectDeviceLoadLogDetailService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;
    @Resource
    private ProjectDeviceEventCallbackService projectDeviceEventCallbackService;
    @Resource
    private ProjectInspectPointDeviceRelService projectInspectPointDeviceRelService;
    @Autowired
    MinioTemplate minioTemplate;
    @Resource
    private ProjectDeviceMonitorRelService projectDeviceMonitorRelService;
    @Resource
    private ProjectDeviceModifyLogService projectDeviceModifyLogService;
    @Resource
    private ProjectDeviceSubsystemService projectDeviceSubsystemService;
    @Resource
    private ProjectDeviceAbnormalService projectDeviceAbnormalService;
    @Resource
    private ProjectMediaAdDevCfgService projectMediaAdDevCfgService;
    @Resource
    private ProjectDeviceRelService projectDeviceRelService;
    @Resource
    private ProjectDeviceGatherAlarmRuleService projectDeviceGatherAlarmRuleService;
    @Resource
    private ProjectHouseInfoService projectHouseInfoService;
    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private ProjectPassPlanPolicyRelService projectPassPlanPolicyRelService;
    @Resource
    private ProjectPersonLiftRelService projectPersonLiftRelService;
    @Resource
    private RemotePigxUserService remotePigxUserService;
    @Resource
    private RemoteParkingDeviceParamConfService remoteParkingDeviceParamConfService;
    @Resource
    private ProjectPlateNumberDeviceService projectPlateNumberDeviceService;
    @Resource
    private ProjectPerimeterAlarmRecordService projectPerimeterRecordAreaService;
    @Resource
    private RemoteExitLaneService remoteExitLaneService;
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;

    @Value("${aliyun.vsc.accessKeyId:}")
    private String accessKeyId;
    @Value("${aliyun.vsc.accessSecret:}")
    private String accessSecret;
    @Value("${uniview.vsc.accessKeyId:25645212521}")
    private Long appId;
    @Value("${uniview.vsc.accessSecret:65a4f27ee572d23e3e977e19a341708f}")
    private String secretKey;
    @Value("${rtsp.server:''}")
    private String rtspServer;
    @Resource(name = "proxyRestTemplate")
    private RestTemplate restTemplate;



    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public boolean save(ProjectDeviceInfo entity) {
        try {
            if(StringUtils.isNotBlank(entity.getMac())){
                String mac = entity.getMac();
                mac = MacUtil.formatMac(mac);
                mac = MacUtil.formatMac(mac,"-");
                entity.setMac(mac.toUpperCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean flag = super.save(entity);
        if (flag) {
            // 设备管理变更通知
            OpenApiProjectDeviceInfoManageDto dto = getDeviceInfoManageDtoByDeviceId(entity.getDeviceId(), DeviceManageOperateTypeEnum.ADD.code);
            sendDeviceManageChangeNotice(dto);
        }
        return flag;
    }

    @Override
    public boolean saveBatch(Collection<ProjectDeviceInfo> entityList) {
        entityList.forEach(entity -> {
            try {
                if(StringUtils.isNotBlank(entity.getMac())){
                    String mac = entity.getMac();
                    mac = MacUtil.formatMac(mac);
                    mac = MacUtil.formatMac(mac,"-");
                    entity.setMac(mac.toUpperCase());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        boolean flag = super.saveBatch(entityList);
        if (flag) {
            for (ProjectDeviceInfo deviceInfo : entityList) {
                // 设备管理变更通知
                OpenApiProjectDeviceInfoManageDto dto = getDeviceInfoManageDtoByDeviceId(deviceInfo.getDeviceId(), DeviceManageOperateTypeEnum.ADD.code);
                sendDeviceManageChangeNotice(dto);
            }
        }
        return flag;
    }

    @Override
    public boolean updateById(ProjectDeviceInfo entity) {
        try {
            if(StringUtils.isNotBlank(entity.getMac())){
                String mac = entity.getMac();
                mac = MacUtil.formatMac(mac);
                mac = MacUtil.formatMac(mac,"-");
                entity.setMac(mac.toUpperCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean flag = super.updateById(entity);
        if (flag) {
            // 设备管理变更通知
            OpenApiProjectDeviceInfoManageDto dto = getDeviceInfoManageDtoByDeviceId(entity.getDeviceId(), DeviceManageOperateTypeEnum.UPDATE.code);
            sendDeviceManageChangeNotice(dto);
        }
        return flag;
    }
    @Override
    public boolean removeById(Serializable id) {
        // 先获取设备信息,否则删除设备后就查询不到了
        OpenApiProjectDeviceInfoManageDto dto = getDeviceInfoManageDtoByDeviceId(String.valueOf(id), DeviceManageOperateTypeEnum.DELETE.code);
        boolean flag = super.removeById(id);
        if (flag) {
            // 设备管理变更通知
            sendDeviceManageChangeNotice(dto);
        }
        return flag;
    }
    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        List<OpenApiProjectDeviceInfoManageDto> dtoList = new ArrayList<>();
        for (Serializable deviceId : idList) {
            // 设备管理变更通知
            OpenApiProjectDeviceInfoManageDto dto = getDeviceInfoManageDtoByDeviceId(String.valueOf(deviceId), DeviceManageOperateTypeEnum.DELETE.code);
            dtoList.add(dto);
        }
        boolean flag = super.removeByIds(idList);
        if (flag && CollUtil.isNotEmpty(dtoList)) {
            for (OpenApiProjectDeviceInfoManageDto deviceInfoManageDto : dtoList) {
                // 设备管理变更通知
                sendDeviceManageChangeNotice(deviceInfoManageDto);
            }
        }
        return flag;
    }

    @Override
    public Page pageVo(Page page, ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo) {
        switch (projectDeviceInfoPageFormVo.getDeviceTypeId()) {
            case DeviceTypeConstants.GATE_DEVICE:
            case DeviceTypeConstants.LADDER_WAY_DEVICE:
            case DeviceTypeConstants.CENTER_DEVICE:
            case DeviceTypeConstants.INDOOR_DEVICE:
            case DeviceTypeConstants.ENCODE_DEVICE:
            case DeviceTypeConstants.AI_BOX_DEVICE:
            /*  20230704 V1.3.0.0（人行版）去除车场相关信息
            case DeviceTypeConstants.VEHICLE_BARRIER_DEVICE: */
                return baseMapper.pageVo(page, projectDeviceInfoPageFormVo, ProjectContextHolder.getProjectId());
            case DeviceTypeConstants.MONITOR_DEVICE:
                return baseMapper.pageCameraVo(page, projectDeviceInfoPageFormVo);
            case DeviceTypeConstants.ALARM_HOST:
                return baseMapper.pageAlarmVo(page, projectDeviceInfoPageFormVo);
            case DeviceTypeConstants.SMART_WATER_METER:
            case DeviceTypeConstants.LEVEL_GAUGE:
            case DeviceTypeConstants.SMOKE:
            case DeviceTypeConstants.SMART_MANHOLE_COVER:
            case DeviceTypeConstants.SMART_STREET_LIGHT:
            case DeviceTypeConstants.DEVICE_DRIVER:
            case DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE:
            case DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE:
            case DeviceTypeConstants.ELEVATOR_STATE_DETECTOR_DEVICE:
                return baseMapper.pageDeviceVo(page, projectDeviceInfoPageFormVo);
            default:
                return page;
        }
    }

    @Override
    public List<ProjectDeviceInfoAbnormalVo> listAbnormalDevice(String deviceId) {
        return baseMapper.listAbnormalDevice(deviceId, ProjectContextHolder.getProjectId());
    }

    @Override
    public List<ProjectDeviceInfo> findByType(ProjectDeviceInfoFormVo projectDeviceInfoFormVo) {
        LambdaQueryWrapper<ProjectDeviceInfo> byTypeQueryWrapper = findByTypeQueryWrapper(projectDeviceInfoFormVo);
        return baseMapper.selectList(byTypeQueryWrapper);
    }

    @Override
    public Page<ProjectDeviceInfoThirPartyVo> findPageByType(Page page,ProjectDeviceInfoFormVo projectDeviceInfoFormVo) {
        LambdaQueryWrapper<ProjectDeviceInfo> byTypeQueryWrapper = findByTypeQueryWrapper(projectDeviceInfoFormVo);
        Page<ProjectDeviceInfo> result = baseMapper.selectPage(page, byTypeQueryWrapper);
        List<ProjectDeviceInfoThirPartyVo> deviceThirPartyList = new ArrayList<>();

        for (ProjectDeviceInfo device : result.getRecords()) {
            ProjectDeviceInfoThirPartyVo deviceInfoThirPartyVo = new ProjectDeviceInfoThirPartyVo();
            BeanUtil.copyProperties(device, deviceInfoThirPartyVo);
            // 设置为可重启
            deviceInfoThirPartyVo.setIsResetDevice(1);
            deviceThirPartyList.add(deviceInfoThirPartyVo);
        }

        Page<ProjectDeviceInfoThirPartyVo> deviceInfoThirPartyVoPage = new Page<>();
        BeanUtil.copyProperties(result,deviceInfoThirPartyVoPage);
        deviceInfoThirPartyVoPage.setRecords(deviceThirPartyList);
        return deviceInfoThirPartyVoPage;
    }

    /**
     * 设备维护分页查询条件构造
     * @param projectDeviceInfoFormVo 传入表单
     * @return 条件构造器
     */
    private LambdaQueryWrapper<ProjectDeviceInfo> findByTypeQueryWrapper(ProjectDeviceInfoFormVo projectDeviceInfoFormVo) {
        LambdaQueryWrapper<ProjectDeviceInfo> queryWrapper = Wrappers.lambdaQuery();

        if (projectDeviceInfoFormVo.getTypes().size() > 0) {
            queryWrapper.in(ProjectDeviceInfo::getDeviceType, projectDeviceInfoFormVo.getTypes());
        }
        //区域
        if (StrUtil.isNotEmpty(projectDeviceInfoFormVo.getDeviceRegionId())) {
            queryWrapper.eq(ProjectDeviceInfo::getDeviceRegionId, projectDeviceInfoFormVo.getDeviceRegionId());
        }
        //楼栋
        if (StrUtil.isNotEmpty(projectDeviceInfoFormVo.getBuildingId())) {
            queryWrapper.eq(ProjectDeviceInfo::getBuildingId, projectDeviceInfoFormVo.getBuildingId());
        }
        //单元
        if (StrUtil.isNotEmpty(projectDeviceInfoFormVo.getUnitId())) {
            queryWrapper.eq(ProjectDeviceInfo::getUnitId, projectDeviceInfoFormVo.getUnitId());
        }
        //设备名称
        if (projectDeviceInfoFormVo.getDeviceName() != null && !"".equals(projectDeviceInfoFormVo.getDeviceName())) {
            queryWrapper.and(query -> query.like(ProjectDeviceInfo::getDeviceName, projectDeviceInfoFormVo.getDeviceName()).or().like(ProjectDeviceInfo::getDeviceDesc, projectDeviceInfoFormVo.getDeviceName()));
        }
        queryWrapper.ne(ProjectDeviceInfo::getStatus, "4");
        return queryWrapper;
    }

    @Override
    public List<ProjectIotDeviceInfoVo> findIotByType(ProjectDeviceInfoFormVo projectDeviceInfoFormVo) {
        if (DeviceTypeConstants.SMART_STREET_LIGHT.equals(projectDeviceInfoFormVo.getTypes().get(0))) {
            return baseMapper.getStreetLightList(projectDeviceInfoFormVo);
        }
        return baseMapper.getIotDeviceByType(projectDeviceInfoFormVo);
    }

    @Override
    public ProjectDeviceInfoVo getProjectDeviceInfoById(String id) {
        ProjectDeviceInfoVo projectDeviceInfoVo = baseMapper.getProjectDeviceInfoById(id);
        if (projectDeviceInfoVo == null) {
            return null;
        }
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
    public String getDeviceNoPreByDeviceId(String deviceId) {

        return this.baseMapper.getDeviceNoPreByDeviceId(deviceId);
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
     * @param thirdpartyCode
     * @param status
     * @return
     * @author: 王伟
     * @since 2020-08-07
     */
    @Override
    public boolean updateStatusByThirdPartyCode(String thirdpartyCode, String status) {

        List<ProjectDeviceInfo> deviceList =
                this.list(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getThirdpartyCode, thirdpartyCode));
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
     * @param thirdpartyCode
     * @return
     * @author: 王伟
     * @since 2020-08-07
     */
    @Override
    public ProjectDeviceInfo getByThirdPartyCode(String thirdpartyCode) {
        List<ProjectDeviceInfo> deviceList =
                this.list(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getThirdpartyCode, thirdpartyCode));
        if (CollectionUtil.isNotEmpty(deviceList)) {
            return deviceList.get(0);
        }
        return null;
    }

    @Override
    public R<String> getLiveUrl(String deviceId) {
//        List<ProjectDeviceAttrListVo> attrs =
//                projectDeviceAttrService.getDeviceAttrListVo(ProjectContextHolder.getProjectId(), CAMERA_TYPE, deviceId);

        List<ProjectDeviceAttr> list = projectDeviceAttrService.list(new LambdaQueryWrapper<ProjectDeviceAttr>().eq(ProjectDeviceAttr::getDeviceId, deviceId).in(ProjectDeviceAttr::getDeviceType, CAMERA_TYPE,"191"));
        ProjectDeviceInfo one = projectDeviceInfoProxyService.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getDeviceId, deviceId));
        if(one.getStatus().equals(AurineEdgeOnlineStatusEnum.OFFLINE.code)) {
           return   R.failed("设备已离线");
        }

        String username = one.getCompanyAccount();
        String password = one.getCompanyPasswd();
        String corpId = "";
        String gbId = "";
        String regionId = "";
        String videoUrl = "";
        for (ProjectDeviceAttr attr : list) {
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
                case "url":
                    videoUrl = attr.getAttrValue();
                case "videoUrl":
                    videoUrl = attr.getAttrValue();
                    break;
                default:
                    break;
            }
        }
        if(username !=null && password != null) {
            videoUrl = videoUrl.replace("rtsp://","rtsp://"+username+":"+password+"@");        }
        if(StringUtil.isNotEmpty(videoUrl)) {
            return R.ok(videoUrl);
        }

        // 获取地址 - 阿里云
//        return getDeviceLiveUrl(regionId, corpId, gbId);
        // 获取地址 - 宇视
        String liveUrl;
        try {
            liveUrl = getUniviewLiveUrl(corpId, Integer.parseInt(gbId));
        } catch (NumberFormatException e1) {
            e1.printStackTrace();
            ProjectDeviceInfoServiceImpl.log.error("设备配置参数异常，或未配置");
            return R.ok(null, "设备配置参数异常，或未配置");
//            throw new NumberFormatException("设备配置参数异常，或未配置");
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new RuntimeException(e2.getMessage(), e2.getCause());
        }
        return R.ok(liveUrl.replace("http://", "https://"));
    }

    @Override
    public R<JSONObject> rtspToUrl(String rtsp) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", rtsp);
        jsonObject.put("ffmpegFlag", 1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(jsonObject, headers);

        ResponseEntity<String> result = restTemplate.exchange(rtspServer, HttpMethod.POST, requestEntity, String.class);
        JSONObject json = JSONObject.parseObject(result.getBody());
        return R.ok(json);
    }


    @Override
    public String getVideoUrl(String deviceId, Long startTime, Long endTime) {
        List<ProjectDeviceAttrListVo> attrs =
                projectDeviceAttrService.getDeviceAttrListVo(ProjectContextHolder.getProjectId(), CAMERA_TYPE, deviceId);

        String corpId = "";
        String gbId = "";
        String regionId = "";
        String videoUrl = "";
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
            return true;
        }
        if (StringUtil.isNotEmpty(deviceId)) {
            return null == getOne(
                    new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getDeviceName, deviceName)
                            .eq(ProjectDeviceInfo::getProjectId, ProjectContextHolder.getProjectId())
                            .ne(ProjectDeviceInfo::getDeviceId, deviceId).last("limit 1"));
        }
        return null == getOne(
                new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getDeviceName, deviceName)
                        .eq(ProjectDeviceInfo::getProjectId, ProjectContextHolder.getProjectId()).last("limit 1"));
    }

    @Override
    public int countByProductId(String productId) {
        return this
                .count(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getProductId, productId));
    }

    @Override
    public Page<ProjectDeviceInfoPageVo> pageDeviceParam(Page page,
                                                         ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo) {
        /*LambdaQueryWrapper<ProjectDeviceInfo> wrapper = Wrappers.<ProjectDeviceInfo>lambdaQuery()
                .eq(ProjectDeviceInfo::getProductId, projectDeviceInfoPageFormVo.getProductId());
        if (StringUtil.isNotEmpty(projectDeviceInfoPageFormVo.getSn())) {
            wrapper.like(ProjectDeviceInfo::getSn, projectDeviceInfoPageFormVo.getSn());
        }
        if (StringUtil.isNotEmpty(projectDeviceInfoPageFormVo.getDeviceName())) {
            wrapper.and(qw -> qw.like(ProjectDeviceInfo::getDeviceName, projectDeviceInfoPageFormVo.getDeviceName())
                    .or().like(ProjectDeviceInfo::getDeviceAlias, projectDeviceInfoPageFormVo.getDeviceName()));
        }*/
//        return this.page(page, wrapper);
        return this.baseMapper.pageVo(page, projectDeviceInfoPageFormVo, ProjectContextHolder.getProjectId());
    }

    @Override
    public List<ProjectDeviceInfo> findRichByType(ProjectDeviceInfoFormVo projectDeviceInfoFormVo) {
        return baseMapper.findRichByType(projectDeviceInfoFormVo);
    }

    @Override
    public List<ProjectDeviceInfo> findFaceCapacityDoorDevice() {
        return baseMapper.findFaceCapacityDoorDevice();
    }
    @Override
    public Integer countBySn(String sn, String deviceId) {
        return baseMapper.countBySn(sn, deviceId);
    }

    @Override
    public void importExcel(MultipartFile file, DeviceExcelEnum deviceExcelEnum, Integer projectId) throws IOException {
        importExcel(file, deviceExcelEnum, projectId, false);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void importExcel(MultipartFile file, DeviceExcelEnum deviceExcelEnum, Integer projectId, boolean isCover) throws IOException {
        ProjectDeviceInfoServiceImpl projectDeviceInfoService = this;
        // 用于生成导入日志明细数据的输入流
        InputStream inputStreamInitDetail = file.getInputStream();
        // 用于实际进行导入的输入流
        InputStream inputStreamImportData = file.getInputStream();
        threadPoolTaskExecutor.execute(() -> {
            LocalDateTime beginTime = LocalDateTime.now();
            ProjectContextHolder.setProjectId(projectId);
            TenantContextHolder.setTenantId(1);
            //获取楼栋单元房间列表 xull@aurine.cn 2020/5/25 11:03
            List<ProjectFrameInfo> projectBuildingInfos = projectFrameInfoService.list(
                    Wrappers.lambdaQuery(ProjectFrameInfo.class).le(ProjectFrameInfo::getLevel, 3));
            List<ProjectDeviceRegionTreeVo> projectDeviceRegions = projectDeviceRegionService.findTree(null);

            List<ProjectFrameInfoTreeVo> tree = projectFrameInfoService.findTree("小区");
            ExcelResultVo excelResultVo = new ExcelResultVo();

            File tmpDir = null;
            File excelFile = null;
            String batchId = UUID.randomUUID().toString().replaceAll("-", "");
            ProjectDeviceLoadLog deviceLoadLog = new ProjectDeviceLoadLog(batchId, deviceExcelEnum.getCode(), LocalDateTime.now());
            try {
                projectDeviceLoadLogService.save(deviceLoadLog);
                List<ProjectDeviceLoadLogDetail> deviceLoadLogDetailList = new ArrayList<>();
                EasyExcel.read(inputStreamInitDetail, deviceExcelEnum.getClazz(),
                        new ProjectDeviceInfoInitListener(deviceLoadLog.getBatchId(), deviceLoadLogDetailList)).sheet().doRead();
                projectDeviceLoadLogDetailService.saveBatch(deviceLoadLogDetailList);

                EasyExcel.read(inputStreamImportData, deviceExcelEnum.getClazz(), new ProjectDeviceInfoListener(projectDeviceInfoService, projectDeviceLoadLogDetailService, projectDeviceAbnormalService, redisTemplate, deviceExcelEnum, batchId, isCover)).sheet().doRead();

                // 这里准备生成导入失败的Excel文件
                String fileName = "失败名单-" + deviceExcelEnum.getName();
                String fileDirPath = "temporary/" + UUID.randomUUID().toString().replaceAll("-", "");
                tmpDir = new File(fileDirPath);
                // 如果没有这个文件夹则新建一个
                if (!tmpDir.exists() || tmpDir.isFile()) {
                    tmpDir.mkdirs();
                }
                excelFile = new File(fileDirPath + "/" + fileName);
                excelFile.createNewFile();

                String dataString = redisTemplate.opsForValue().get(deviceLoadLog.getBatchId());

                List data = JSONUtil.toList(JSONUtil.parseArray(dataString), deviceExcelEnum.getClazz());
                if(CollUtil.isEmpty(data)){
                    data = new ArrayList();
                }
                String excelPath = DeviceExcelConstant.XLSX_PATH + deviceExcelEnum.getName();
                ClassPathResource classPathResource = new ClassPathResource(excelPath);
                EasyExcel.write(new FileOutputStream(excelFile), deviceExcelEnum.getClazz())
                        .withTemplate(classPathResource.getStream())
                        .sheet(0).doFill(data);

                ProjectInfo projectInfo = projectInfoService.getOne(new LambdaQueryWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId,
                        ProjectContextHolder.getProjectId()));
                String objectName = String.format("%s/%s/%s/%s/%s/%s", TenantContextHolder.getTenantId(), projectInfo.getCompanyId(),
                        projectInfo.getProjectGroupId(),
                        projectInfo.getProjectId(), UUID.randomUUID().toString().replaceAll("-", ""), fileName);

                // 这样写就不需要手动关闭文件流（否则临时文件无法删除）
                try (FileInputStream fileInputStream = new FileInputStream(excelFile)) {
                    minioTemplate.putObject(ExcelMinIOConstant.BUCKET_NAME, objectName, fileInputStream);
                    String objectURL = minioTemplate.getObjectURL(ExcelMinIOConstant.BUCKET_NAME, objectName, ExcelMinIOConstant.EXPIRES_ONE_DAY);
                    ProjectDeviceInfoServiceImpl.log.info("[设备导入] MinIO桶名:[{}] MinIO对象名:[{}] 临时分享链接:[{}]", ExcelMinIOConstant.BUCKET_NAME, objectName, objectURL);

                    deviceLoadLog.setOrginFile(objectName);
                    deviceLoadLog.setTempFile(objectURL);
                    deviceLoadLog.setLoadTime(beginTime);
                    deviceLoadLog.setDevCount(deviceLoadLogDetailList.size());
                    projectDeviceLoadLogService.updateById(deviceLoadLog);

                } catch (Exception e) {
                    e.printStackTrace();
                    ProjectDeviceInfoServiceImpl.log.error("导入失败模板保存失败");
                }

            } catch (IOException e) {
                e.printStackTrace();
                projectDeviceLoadLogDetailService.updateToFailed(deviceLoadLog.getBatchId(), "文件读取异常");
            } catch (Exception e) {
                e.printStackTrace();
                projectDeviceLoadLogDetailService.updateToFailed(deviceLoadLog.getBatchId(), e.getMessage());
            } finally {
                ProjectDeviceInfoServiceImpl.log.info("[设备导入] 删除临时文件");
                if (excelFile != null && excelFile.exists()) {
                    excelFile.delete();
                }
                if (tmpDir != null && tmpDir.exists()) {
                    tmpDir.delete();
                }
            }
            //增值服务设置
            List<String> ids = excelResultVo.getList();
            if (CollUtil.isNotEmpty(ids)) {
                List<ProjectServiceInfoVo> projectServiceInfoVos = projectServiceService.getHouseServiceByProjectId(ProjectContextHolder.getProjectId());

                projectServiceInfoVos.forEach(projectService -> {
                    ids.forEach(id -> {
                        IntercomFactoryProducer.getFactory(projectService.getServiceCode()).getIntercomService()
                                .addDevice(id, ProjectContextHolder.getProjectId());
                    });
                });
            }
        });
    }

    @Override
    public List<String> verifyCodeWithExcel(MultipartFile file, DeviceExcelEnum deviceExcelEnum) throws IOException {
        ProjectDeviceInfoServiceImpl projectDeviceInfoService = this;
        ProjectDeviceCodeVerifyListener projectDeviceCodeVerifyListener = new ProjectDeviceCodeVerifyListener(projectDeviceInfoService);
        InputStream inputStreamImportData = file.getInputStream();
        EasyExcel.read(inputStreamImportData, deviceExcelEnum.getClazz(), projectDeviceCodeVerifyListener).sheet().doRead();
        return projectDeviceCodeVerifyListener.getList();
    }

    @Override
    public void errorExcel(Integer projectId, String batchId, HttpServletResponse httpServletResponse) throws IOException {
        /*String dataString = redisTemplate.opsForValue().get(name);
        String[] keys = name.split("-");
        DeviceExcelEnum deviceExcelEnum = DeviceExcelEnum.getEnum(keys[0], keys[1]);
        List data = JSONUtil.toList(JSONUtil.parseArray(dataString), deviceExcelEnum.getClazz());
        String excelPath = DeviceExcelConstant.XLSX_PATH + deviceExcelEnum.getName();
        ClassPathResource classPathResource = new ClassPathResource(excelPath);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        EasyExcel.write(httpServletResponse.getOutputStream(), deviceExcelEnum.getClazz())
                .withTemplate(classPathResource.getStream())
                .sheet(0).doFill(data);*/
        ProjectContextHolder.setProjectId(projectId);
        ProjectDeviceLoadLog loadLog = projectDeviceLoadLogService.getOne(new LambdaQueryWrapper<ProjectDeviceLoadLog>().eq(ProjectDeviceLoadLog::getBatchId, batchId));
        InputStream object = minioTemplate.getObject(ExcelMinIOConstant.BUCKET_NAME, loadLog.getOrginFile());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        IOUtils.copy(object, httpServletResponse.getOutputStream());

    }


    /**
     * <p>
     * 获取指定设备的设备参数数据
     * </p>
     *
     * @param deviceId 所要获取设备参数数据的设备ID
     * @author: 王良俊
     */
    @Override
    public void initDeviceParamData(String deviceId) {
        ProjectDeviceInfo deviceInfo = this.getById(deviceId);
        if (deviceInfo == null) {
            return;
        }
        // 这里先删除这台设备原有的参数数据（如果有的话）
        //projectDeviceParamInfoService.remove(new QueryWrapper<ProjectDeviceParamInfo>().lambda().eq(ProjectDeviceParamInfo::getDeviceId, deviceId));
        /* // 20230704 V1.3.0.0（人行版）去除车场相关信息
        if (DeviceTypeConstants.VEHICLE_BARRIER_DEVICE.equals(deviceInfo.getDeviceType()) && DeviceStatusEnum.ONLINE.code.equals(deviceInfo.getStatus())) {
            *//*车道一体机添加后需要配置默认参数*//*
            boolean paramConfResult = DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(),
                    TenantContextHolder.getTenantId()).getDeviceService().configDefaultParam(deviceInfo.getDeviceId());
            if (!paramConfResult) {
                ProjectDeviceInfoServiceImpl.log.info("车场一体机默认参数配置失败：{}", JSON.toJSONString(deviceInfo));
            }
        } else {

        }*/
        List<SysProductService> productServiceList = sysProductServiceService.list(new QueryWrapper<SysProductService>().lambda()
                .eq(SysProductService::getProductId, deviceInfo.getProductId()));
        if (CollUtil.isNotEmpty(productServiceList)) {
            Set<String> serviceIdSet = productServiceList.stream().map(SysProductService::getServiceId).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(serviceIdSet)) {
                DeviceParamServiceFactory factory = DeviceParamFactoryProducer.getFactory(deviceId);
                if (factory != null) {
                    SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>()
                            .eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
                    DeviceParamDataService deviceParamDataService = factory.getParamDataService(productMap.getManufacture(), deviceInfo.getDeviceType());
                    if (deviceParamDataService != null) {
                        deviceParamDataService.requestDeviceParam(serviceIdSet, deviceId);
                    } else {
                        ProjectDeviceInfoServiceImpl.log.warn("未获取到当前设备参数服务 deviceId：{}", deviceId);
                    }
                } else {
                    ProjectDeviceInfoServiceImpl.log.warn("当前设备类型未对接中台 deviceId：{}", deviceId);
                }
            }
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveVo(ProjectDeviceInfoVo deviceInfo) {

        /**
         * 设备添加调用第三方平台注册接口
         * @author: 王伟
         * @since :2020-08-17
         */
        ProjectDeviceInfoProxyVo deviceInfoProxyVo = new ProjectDeviceInfoProxyVo();
        BeanUtils.copyProperties(deviceInfo, deviceInfoProxyVo);
        deviceInfoProxyVo.setProjectId(ProjectContextHolder.getProjectId());
        deviceInfoProxyVo.setTenantId(TenantContextHolder.getTenantId());

        //注册设备，返回第三方ID(针对同步添加的接口，异步添加接口返回空数据)
        String thirdCode = DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(),
                ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId())
                .getDeviceService().addDevice(deviceInfoProxyVo);
        if (StringUtils.isNotEmpty(thirdCode)) {
            deviceInfo.setThirdpartyCode(thirdCode);
            deviceInfo.setProductId(deviceInfoProxyVo.getProductId());
        }

        this.save(deviceInfo);

        List<ProjectDeviceLocation> locations = deviceInfo.getLocations();

        if (locations != null) {
            for (int i = 0; i < locations.size(); i++) {
                ProjectDeviceLocation location = locations.get(i);
                location.setDeviceId(deviceInfo.getDeviceId());
            }
            projectDeviceLocationService.saveBatch(locations);
        }


        // 添加设备拓展属性逻辑 xull@aurine.cn 2020年7月7日 11点13分
        ProjectDeviceAttrFormVo projectDeviceAttrFormVo = new ProjectDeviceAttrFormVo();
        projectDeviceAttrFormVo.setProjectDeviceAttrList(deviceInfo.getDeviceAttrs());
        projectDeviceAttrFormVo.setProjectId(ProjectContextHolder.getProjectId());
        projectDeviceAttrFormVo.setType(deviceInfo.getDeviceType());
        projectDeviceAttrFormVo.setDeviceId(deviceInfo.getDeviceId());
        projectDeviceAttrService.updateDeviceAttrList(projectDeviceAttrFormVo);

//        if (deviceInfo.getDeviceType().equals(DeviceTypeConstants.GATE_DEVICE) || deviceInfo.getDeviceType().equals(DeviceTypeConstants.LADDER_WAY_DEVICE)) {
//            projectPersonDeviceService.refreshAddDevice(deviceInfo);
//        }
        this.initDeviceParamData(deviceInfo.getDeviceId());
        return true;
    }

    @Override
    public DeviceReplaceResultEnum replaceDevice(String deviceId, String sn) {
        DeviceReplaceResultEnum resultEnum = DeviceReplaceResultEnum.REPLACE_FAILED;
        this.projectRightDeviceService.clearCerts(deviceId, CertmediaTypeEnum.Card);
        this.projectRightDeviceService.clearCerts(deviceId, CertmediaTypeEnum.Face);
        this.projectRightDeviceService.clearCerts(deviceId, CertmediaTypeEnum.Finger);
        this.projectRightDeviceService.clearCerts(deviceId, CertmediaTypeEnum.Password);
        List<ProjectDeviceInfo> deviceInfoList = this.list(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getDeviceId, deviceId));
        if (CollUtil.isNotEmpty(deviceInfoList)) {
            ProjectDeviceInfo deviceInfo = deviceInfoList.get(0);
            String oldSn = deviceInfo.getSn();
            try {
                // 这里将现有设备的数据备份到设备替换信息表中
                ProjectDeviceReplaceInfo deviceReplaceInfo = new ProjectDeviceReplaceInfo();
                deviceReplaceInfo.setReplaceReason("设备管理-设备替换");
                deviceReplaceInfo.setReplaceTime(LocalDateTime.now());
//            deviceReplaceInfo.setTenant_id(1);
                BeanUtil.copyProperties(deviceInfo, deviceReplaceInfo);
                projectDeviceReplaceInfoService.save(deviceReplaceInfo);

                // 这里获取到旧设备的产品ID
                String oldProductId = deviceInfo.getProductId();
                // 这里先对新设备进行绑定并更新新设备的部分信息到设备信息表
                boolean bindSuccess = bindNewDeviceAndUpdate(deviceInfo, sn);
                // 如果绑定成功再进行后续操作
                if (bindSuccess) {
                    // 对卡片和人脸进行重新下发操作
                    redownloadCertByDeviceId(deviceId);
                    // 这里如果旧设备和新设备产品ID一样则将原设备的参数重新下发给新的设备
                    if (deviceInfo.getProductId().equals(oldProductId)) {
                        DeviceParamDataVo deviceParamDataVo = projectDeviceParamInfoService.getParamByDeviceId(deviceId, oldProductId);
                        String paramData = deviceParamDataVo.getParamData();


                        // 这里进行设备参数（旧设备参数下发到新设备）下发
                        JsonNode jsonNode = objectMapper.readTree(paramData);
                        List<ProjectDeviceParamSetResultVo> resultVoList = this.setDeviceParam((ObjectNode) jsonNode, deviceId);
                        boolean isFailed = this.handleDeviceParamSetResult(resultVoList, false);
                        if (isFailed) {
                            // 这里返回的是参数设置失败（已经替换成功但是同步参数的时候失败了）
                            resultEnum = DeviceReplaceResultEnum.PARAM_SETTING_FILED;
                        } else {
                            resultEnum = DeviceReplaceResultEnum.REPLACE_SUCCESS;
                        }
                    } else {
                        // 这里返回的是替换设备后新设备的产品ID和原设备不一致（无法同步旧设备参数设置，这里要获取到新设备的参数数据替换原有旧设备的参数数据）
                        // 同时通知前端需要专门对新设备进行设置
                        // 获取新设备的参数数据（会先删除原有旧设备的参数数据）
                        this.initDeviceParamData(deviceId);
                        resultEnum = DeviceReplaceResultEnum.PRODUCT_ID_CHANGE;
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                // 这里如果出现异常则重新绑定回原来的设备
                this.bindNewDeviceAndUpdate(deviceInfo, oldSn);
                // 旧设备因为清空了介质这里需要重新下载回去
                this.redownloadCertByDeviceId(deviceId);
            }
        }
        return resultEnum;
    }

    /**
     * <p>
     * 这里对卡片和人脸两种介质进行重新下发
     * </p>
     *
     * @param deviceId 所要进行介质重新下发的设备ID
     * @author: 王良俊
     */
    private void redownloadCertByDeviceId(String deviceId) {
        projectRightDeviceService.redownloadCertByType(deviceId, CertmediaTypeEnum.Card.code);
        projectRightDeviceService.redownloadCertByType(deviceId, CertmediaTypeEnum.Face.code);
    }

    /**
     * <p>
     * 这里绑定新设备替换原有设备（没有进行原设备的业务转移因为事务的原因）
     * 会对入参设备信息对象进行更新
     * </p>
     *
     * @param deviceInfo 所要进行设备替换的旧设备信息对象
     * @param sn         新设备的SN
     * @author: 王良俊
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    protected boolean bindNewDeviceAndUpdate(ProjectDeviceInfo deviceInfo, String sn) {

        ProjectDeviceInfoProxyVo deviceInfoProxyVo = new ProjectDeviceInfoProxyVo();
        BeanUtils.copyProperties(deviceInfo, deviceInfoProxyVo);
        deviceInfoProxyVo.setTenantId(TenantContextHolder.getTenantId());
        deviceInfoProxyVo.setThirdpartyCode("");
        deviceInfoProxyVo.setSn(sn);
        // 这里根据项目ID获取，因为是新的设备用旧设备ID获取不知道会不会有问题
        // 这里先绑定新的设备再解绑旧的设备
        DeviceService deviceService = DeviceFactoryProducer.getFactory(ProjectContextHolder.getProjectId()).getDeviceService();
        String thirdpartyCode = deviceService.addDevice(deviceInfoProxyVo);

        if (StrUtil.isNotEmpty(thirdpartyCode)) {
            deviceInfo.setThirdpartyCode(thirdpartyCode);
            deviceInfo.setSn(deviceInfoProxyVo.getSn());
            deviceInfo.setProductId(deviceInfoProxyVo.getProductId());
            // 这里绑定成功之后更新设备信息
            return this.updateById(deviceInfo);
        } else {
            ProjectDeviceInfoServiceImpl.log.error("未获取到设备第三方编码");
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ProjectDeviceInfoVo saveDeviceVo(ProjectDeviceInfoVo deviceInfo) {
        //feat: 新增逻辑 双门、四门控制器校验入参必填和唯一性校验
        saveValid(deviceInfo);
        /**
         * 设备添加调用第三方平台注册接口
         * @author: 王伟
         * @since :2020-08-17
         */
        ProjectDeviceInfoProxyVo deviceInfoProxyVo = new ProjectDeviceInfoProxyVo();
        deviceInfo.setDeviceId(UUID.randomUUID().toString().replaceAll("-", ""));
        BeanUtils.copyProperties(deviceInfo, deviceInfoProxyVo);
        deviceInfoProxyVo.setProjectId(ProjectContextHolder.getProjectId());
        deviceInfoProxyVo.setTenantId(TenantContextHolder.getTenantId());
        if(CollUtil.isNotEmpty(deviceInfo.getDeviceAttrs())){
            Optional.of(deviceInfo.getDeviceAttrs()).ifPresent(d->{
                deviceInfoProxyVo.setDeviceAttrList(deviceInfo.getDeviceAttrs().stream()
                        .map(device->new ProjectDeviceAttrVo(device.getAttrCode(),device.getAttrValue(),device.getAttrName()))
                        .collect(Collectors.toList()));
            });
        }
        if(DeviceTypeConstants.ENVIRONMENTAL_MONITORING.equals(deviceInfo.getDeviceType())){
            //环境监测仪的话，重新赋值sn，方便共用中台的请求
            deviceInfo.setSn(deviceInfo.getDeviceCode());
        }
        //注册设备，返回第三方ID(针对同步添加的接口，异步添加接口返回空数据)
        String thirdCode = "";
        ProjectDeviceInfo oldDevice = null;
        switch (deviceInfo.getDeviceType()) {
            case DeviceTypeConstants.MONITOR_DEVICE:
                //TODO 监控设备新增接口对接
                thirdCode = DeviceFactoryProducer.getFactoryByProductId(deviceInfo.getProductId()).getDeviceService().addDevice(deviceInfoProxyVo);
                break;
            case DeviceTypeConstants.INDOOR_DEVICE:
                //TODO 室内机新增接口对接
            case DeviceTypeConstants.LADDER_WAY_DEVICE:
                //TODO 梯口机新增接口对接
            case DeviceTypeConstants.GATE_DEVICE:
                //TODO 区口机新增接口对接
            case DeviceTypeConstants.CENTER_DEVICE:
                //TODO 中心机新增接口对接
            case DeviceTypeConstants.ENCODE_DEVICE:
                //TODO 编码设备新增接口对接
            case DeviceTypeConstants.ALARM_HOST:
                //TODO 报警主机新增接口对接
            case DeviceTypeConstants.AI_BOX_DEVICE:
                //TODO AI盒子新增接口对接
            case DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE:
                //TODO 乘梯识别终端接口对接
            case DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE:
                //TODO 电梯分层控制器接口对接
            case DeviceTypeConstants.ELEVATOR_STATE_DETECTOR_DEVICE:
                //TODO 电梯状态检测器接口对接
                thirdCode = DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(),
                        TenantContextHolder.getTenantId()).getDeviceService().addDevice(deviceInfoProxyVo);
                break;
            case DeviceTypeConstants.SMART_WATER_METER:
                //TODO 智能水表新增接口对接
            case DeviceTypeConstants.LEVEL_GAUGE:
                //TODO 液位计新增接口对接
            case DeviceTypeConstants.SMOKE:
                //TODO 烟感新增接口对接
            case DeviceTypeConstants.ENVIRONMENTAL_MONITORING:
                //TODO 环境检测新增接口对接
            case DeviceTypeConstants.SMART_MANHOLE_COVER:
                //TODO 智能井盖新增接口对接
            case DeviceTypeConstants.SMART_STREET_LIGHT:
                //TODO 智能路灯新增接口对接
                SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>().eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()).last("limit 1"));
                thirdCode = DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(),
                        TenantContextHolder.getTenantId()).getDeviceService().addDevice(deviceInfoProxyVo, productMap.getProductCode());
                break;
            /*  // 20230704 V1.3.0.0（人行版）去除车场相关信息
            case DeviceTypeConstants.VEHICLE_BARRIER_DEVICE:
                //TODO 车道一体机对接
                //判断IP地址是否重复
                oldDevice = this.baseMapper.selectOne(new LambdaQueryWrapper<>(ProjectDeviceInfo.class)
                        .eq(ProjectDeviceInfo::getIpv4, deviceInfo.getIpv4())
                        .eq(ProjectDeviceInfo::getDeviceType,deviceInfo.getDeviceType()));
                if(oldDevice != null){
                    throw new RuntimeException("当前设备IP" + deviceInfo.getIpv4() + "已存在");
                }
                thirdCode = DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(),
                        TenantContextHolder.getTenantId()).getDeviceService().addDevice(deviceInfoProxyVo);
                break;*/

            default:
                break;
        }

        if (StrUtil.isNotEmpty(deviceInfo.getProductId())) {
            SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(Wrappers.lambdaQuery(SysDeviceProductMap.class)
                    .eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
            if (productMap != null) {
                String capabilities = Optional.of(productMap).map(SysDeviceProductMap::getCapability).orElse("");
                deviceInfo.setDeviceCapabilities(capabilities);
            }
        }


        if (StringUtils.isNotEmpty(thirdCode)) {
            deviceInfo.setThirdpartyCode(thirdCode);
            deviceInfo.setProductId(deviceInfoProxyVo.getProductId());
            deviceInfo.setStatus(deviceInfoProxyVo.getStatus());
            if (StrUtil.isNotEmpty(deviceInfoProxyVo.getSn())) {
                deviceInfo.setSn(deviceInfoProxyVo.getSn());
            }
            Integer deviceNum = projectDeviceInfoProxyService.countThirdPartyCode(thirdCode);
            if (deviceNum != 0) {
                throw new RuntimeException("当前设备编号" + thirdCode + "已存在");
            }
        }
        if(DeviceTypeConstants.ENVIRONMENTAL_MONITORING.equals(deviceInfo.getDeviceType())) {
            //环境监测仪的话，重新赋值sn，方便共用中台的请求
            deviceInfo.setSn(null);
        }
        this.save(deviceInfo);


        List<ProjectDeviceLocation> locations = deviceInfo.getLocations();

        if (locations != null) {
            for (int i = 0; i < locations.size(); i++) {
                ProjectDeviceLocation location = locations.get(i);
                location.setDeviceId(deviceInfo.getDeviceId());
            }
            projectDeviceLocationService.saveBatch(locations);
        }


        // 作为基础属性的扩展属性保存 hjj 2022-04-25 14:47:17
        List<ProjectDeviceAttrListVo> projectDeviceAttrListVos = this.projectDeviceAttrService.getDeviceAttrListVo(ProjectContextHolder.getProjectId(),deviceInfo.getDeviceType(),null);
        if(projectDeviceAttrListVos != null && deviceInfo.getDeviceType().equals(DeviceTypeEnum.INDOOR_DEVICE.getCode())){
            for (ProjectDeviceAttrListVo projectDeviceAttrListVo: projectDeviceAttrListVos) {
                Object value = ObjectPaseUtils.getFieldValueByName(projectDeviceAttrListVo.getAttrCode(),deviceInfo);
                if(value != null){
                    projectDeviceAttrListVo.setAttrValue(value.toString());
                }
            }
        }
//        if(deviceInfo.getDeviceAttrs() != null){
//            deviceInfo.getDeviceAttrs().addAll(projectDeviceAttrListVos);
//        }else {
//            deviceInfo.setDeviceAttrs(projectDeviceAttrListVos);
//        }
        if(CollectionUtil.isNotEmpty(projectDeviceAttrListVos)){
            if(CollectionUtil.isEmpty(deviceInfo.getDeviceAttrs())){
                deviceInfo.setDeviceAttrs(new ArrayList<>());
            }
            //去重 加入 取出表内配置和枚举配置并集
            List<String> proCodes = projectDeviceAttrListVos.stream().map(ProjectDeviceAttrListVo::getAttrCode).collect(Collectors.toList());
            List<String> deCodes = deviceInfo.getDeviceAttrs().stream().map(ProjectDeviceAttrListVo::getAttrCode).collect(Collectors.toList());
            log.info("【数据库配置】：拓展属性{}",proCodes.toString());
            log.info("【枚举配置】：拓展属性{}",deCodes.toString());
            for (String s : proCodes) {
                if(!deCodes.contains(s)){
                    projectDeviceAttrListVos.stream()
                            .filter(pro->pro.getAttrName().equals(s))
                            .findAny()
                            .ifPresent(i->{
                        deviceInfo.getDeviceAttrs().add(i);
                    });
                }
            }
//            projectDeviceAttrListVos.forEach(pro->{
//                ProjectDeviceAttrListVo v = deviceInfo.getDeviceAttrs().stream()
//                        .filter(de -> de.getAttrName().equals(pro.getAttrName())).findAny().orElse(null);
//                if(Objects.isNull(v)){
//                    deviceInfo.getDeviceAttrs().add(pro);
//                }
//            });
        }

        // 设备的拓展属性，新增或是要保存的设备拓展属性
        if(CollUtil.isNotEmpty(deviceInfoProxyVo.getDeviceAttrList())) {
            deviceInfoProxyVo.getDeviceAttrList().forEach(attr -> {
                projectDeviceAttrService.saveDeviceAttr(deviceInfo.getDeviceId(), attr.getAttrCode(), attr.getAttrName(), attr.getAttrValue());
            });
        }

        //报警主机根据通道数新增或者删除防区通道
        updateChannelCounts(deviceInfo);

//        if (deviceInfo.getDeviceType().equals(DeviceTypeConstants.GATE_DEVICE) || deviceInfo.getDeviceType().equals(DeviceTypeConstants.LADDER_WAY_DEVICE)) {
//            projectPersonDeviceService.refreshAddDevice(deviceInfo);
//        }
        /*if (deviceInfo.getDeviceType().equals(DeviceTypeConstants.SMART_STREET_LIGHT)) {
            String deviceId = UUID.randomUUID().toString().replaceAll("-", "");
            ProjectDeviceAttr projectDeviceAttr = new ProjectDeviceAttr();
            projectDeviceAttr.setProjectId(ProjectContextHolder.getProjectId());
            projectDeviceAttr.setDeviceId(deviceId);
            projectDeviceAttr.setDeviceType(DeviceTypeConstants.SMART_STREET_LIGHT);
            projectDeviceAttr.setAttrId(deviceInfo.getDeviceId());
            projectDeviceAttr.setAttrCode("controller");
            projectDevicinitDeviceParamDataeAttr.setAttrValue(deviceInfo.getController());
            projectDeviceAttrService.save(projectDeviceAttr);
            if (StringUtils.isNotEmpty(deviceInfo.getRoute())) {
                ProjectDeviceAttr route = new ProjectDeviceAttr();
                BeanUtils.copyProperties(projectDeviceAttr, route);
                route.setAttrCode("route");
                route.setAttrValue(deviceInfo.getRoute());
                projectDeviceAttrService.save(route);
            }
        }*/
       /* //添加异常设备
        if(deviceInfo.isResult()){
            ProjectDeviceAbnormal projectDeviceAbnormal = new ProjectDeviceAbnormal();
            BeanUtils.copyProperties(deviceInfoProxyVo,projectDeviceAbnormal);
            R<Boolean> date = projectDeviceModifyLogService.checkDeviceParam(deviceInfo.getIpv4(), deviceInfo.getDeviceCode(), deviceInfo.getMac(),null);
            String[] split = date.getMsg().split("/");
            projectDeviceAbnormal.setAbnormalCode(split[1]);
            projectDeviceAbnormal.setAbnormalDesc(split[0]);
            projectDeviceAbnormal.setDStatus(deviceInfo.getStatus());
            projectDeviceAbnormal.setDeviceId(deviceInfo.getDeviceId());
            projectDeviceAbnormal.setDevAddTime(LocalDateTime.now());
            projectDeviceAbnormalService.save(projectDeviceAbnormal);
        }*/
        //当前项目开启的增值服务列表
        List<ProjectServiceInfoVo> projectServiceInfoVos = projectServiceService.getHouseServiceByProjectId(ProjectContextHolder.getProjectId());

        projectServiceInfoVos.forEach(e -> {
            if (StringUtils.equals(e.getServiceType(), "YDJ")) {//如果服务类型未云对讲
                //当设备类型为梯口机、区口时调用云对讲接口
                if (StringUtils.equals(deviceInfo.getDeviceType(), DeviceTypeEnum.GATE_DEVICE.getCode()) || StringUtils.equals(deviceInfo.getDeviceType(), DeviceTypeEnum.LADDER_WAY_DEVICE.getCode())) {
                    IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().addDevice(deviceInfo.getDeviceId(), ProjectContextHolder.getProjectId());
                }
            }
        });
        return deviceInfo;
    }

    /**
     * 新增逻辑 双门、四门控制器校验入参必填和唯一性校验
     * @param deviceInfo
     */
    private void saveValid(ProjectDeviceInfoVo deviceInfo) {
        if(!DeviceTypeEnum.GATE_DEVICE.getCode().equals(deviceInfo.getDeviceType()) &&
                !DeviceTypeEnum.LADDER_WAY_DEVICE.getCode().equals(deviceInfo.getDeviceType())){
            return;
        }
        String doorNo="";
        AtomicReference<String> model= new AtomicReference<>(deviceInfo.getBrand());//设备型号
        if(StringUtil.isEmpty(model.get())){
            SysDeviceProductMap deviceProduct = sysDeviceProductMapService.lambdaQuery()
                    .eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()).one();
            Optional.of(deviceProduct).ifPresent(d-> model.set(d.getProductModel()));
        }
        DoorControllerEnum doorController = DoorControllerEnum.getByModel(model.get());
        if(Objects.isNull(doorController)){//查询不到对应型号的门控制器则直接返回
            return;
        }
        //通行密码 设备门号 ipv4 三方设备非空校验
        StringBuffer errorBuffer=new StringBuffer();
        if(StringUtil.isBlank(deviceInfo.getIpv4())){
            errorBuffer.append("【设备IPV4不可为空】");
        }
        if(StringUtils.isBlank(deviceInfo.getSn())){
            errorBuffer.append("【SN不可为空】");
        }
//        if(StringUtil.isBlank(deviceInfo.getThirdpartyCode())){
//            errorBuffer.append("【第三方编号不可为空】");
//        }
        if(Objects.isNull(deviceInfo.getPort())){
            errorBuffer.append("【网络端口号不可为空】");
        }

        //自定义参数校验
        for (AttrsConfig attrConf : doorController.getAttrConfs()) {
            ProjectDeviceAttrListVo attrListVo = deviceInfo.getDeviceAttrs().stream().filter(f -> f.getAttrCode().equals(attrConf.getAttrCode())).findAny().orElse(null);
            if(Objects.isNull(attrListVo) || StringUtils.isBlank(attrListVo.getAttrValue())){
                errorBuffer.append(String.format("【%s不可为空】",attrConf.getAttrName()));
            }else{
                //判断设备门号是否超出定义上限
                if(attrConf.getAttrCode().equals(DoorControllerEnum.DoorControllerAttrConf.DOOR_NO.getAttrCode())){
                    doorNo=attrListVo.getAttrValue();
                    if(StringUtils.isBlank(doorNo) || !NumberUtil.isNumber(doorNo)){
                        errorBuffer.append("【设备门号错误】");
                    }
                    if(NumberUtil.compare(doorController.getDoorCount(),Integer.valueOf(doorNo))<0){
                        errorBuffer.append(String.format("【%s不可超过%s】",DoorControllerEnum.DoorControllerAttrConf.DOOR_NO.getName(),doorController.getDoorCount()));
                    }
                }
            }

        }
        if(StringUtils.isNotBlank(errorBuffer)){
            throw new RuntimeException(errorBuffer.toString());
        }
        //第三方编码+门号做唯一性校验
        List<DeviceAttrDto> deviceAttrDtos = projectDeviceAttrService.getDeviceAttrKeyAndValue(deviceInfo.getSn(), DoorControllerEnum.DoorControllerAttrConf.DOOR_NO.getAttrCode(),doorNo);
        //修改->判断是否为相同deviceId
        if(CollectionUtil.isNotEmpty(deviceAttrDtos)){
            if(StringUtils.isNotBlank(deviceInfo.getDeviceId())){
                deviceAttrDtos.stream()
                        .filter(d->d.getDeviceId().equals(deviceInfo.getDeviceId()))
                        .findAny()
                        .orElseThrow(()->new RuntimeException("已有相同门号设备"));
            }else{
                throw new RuntimeException(String.format("已有相同门号设备"));
            }
        }

    }

    private void updateChannelCounts(ProjectDeviceInfoVo deviceInfo){
        //报警主机根据通道数新增或者删除防区通道
        if( DeviceTypeConstants.ALARM_HOST.equals(deviceInfo.getDeviceType())){
            int channelCountsInDB=projectPerimeterAlarmAreaService.count(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class)
                    .eq(ProjectPerimeterAlarmArea::getDeviceId,deviceInfo.getDeviceId()));
            ProjectDeviceAttrListVo attrVo=deviceInfo.getDeviceAttrs().stream()
                    .findFirst()
                    .filter(chan-> "channelCount".equals(chan.getAttrCode())).get();
            int channelCounts4Settting=Integer.parseInt( attrVo.getAttrValue());
            int channelCounts4Create=0;
            ////当设定的防区数量大于原有防区时,删除所有原防区通道,创建新的防区通道
            if(channelCountsInDB>channelCounts4Settting){
                List<ProjectPerimeterAlarmArea> lstProjectPerimeterAlarmArea= projectPerimeterAlarmAreaService.list(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class)
                        .eq(ProjectPerimeterAlarmArea::getDeviceId,deviceInfo.getDeviceId()));
                projectPerimeterAlarmAreaService.remove(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class)
                        .eq(ProjectPerimeterAlarmArea::getDeviceId,deviceInfo.getDeviceId()));
                if(lstProjectPerimeterAlarmArea.size()>0) {
                    remove(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                            .in (ProjectDeviceInfo::getDeviceId, lstProjectPerimeterAlarmArea.stream().map(a->a.getInfoUid()).collect(Collectors.toList())));
                }
                channelCounts4Create=channelCounts4Settting;
            }
            else{
                channelCounts4Create=channelCounts4Settting-channelCountsInDB;
            }
            List<ProjectPerimeterAlarmArea> lstProjectPerimeterAlarmArea=new ArrayList<>();
            List<ProjectDeviceInfo> lstProjectDeviceInfo=new ArrayList<>();
            for(int count=0;count<channelCounts4Create;count++){
                ProjectPerimeterAlarmArea projectPerimeterAlarmArea=new ProjectPerimeterAlarmArea();
                projectPerimeterAlarmArea.setDeviceId(deviceInfo.getDeviceId());
                String deviceId=UUID.randomUUID().toString().replace("-", "");
                projectPerimeterAlarmArea.setInfoUid(deviceId);
                lstProjectPerimeterAlarmArea.add(projectPerimeterAlarmArea);
                ProjectDeviceInfo projectDeviceInfo=new ProjectDeviceInfo();
                projectDeviceInfo.setDeviceName("设备防区");
                projectDeviceInfo.setDeviceType(DeviceTypeConstants.ALARM_EQUIPMENT);
                projectDeviceInfo.setDeviceId(deviceId);
                //projectDeviceInfo.setProjectId(ProjectContextHolder.getProjectId());
                lstProjectDeviceInfo.add(projectDeviceInfo);
            }

            projectPerimeterAlarmAreaService.saveBatch(lstProjectPerimeterAlarmArea);
            saveBatch(lstProjectDeviceInfo);

        }
    }

    @Override
    public ProjectDeviceInfoVo saveDeviceVoAndGetParam(ProjectDeviceInfoVo deviceInfo) {
        ProjectDeviceInfoVo deviceInfoVo = this.saveDeviceVo(deviceInfo);

        try {
            this.initDeviceParamData(deviceInfo.getDeviceId());
        } catch (Exception e) {
            this.removeDevice(deviceInfo.getDeviceId());
            throw new ExcelAnalysisException("网络问题/设备重启，无法完成设备添加");
        }
        return deviceInfoVo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateVo(ProjectDeviceInfoVo deviceInfoVo) {
        //feat: 新增逻辑 双门、四门控制器校验入参必填和唯一性校验
        saveValid(deviceInfoVo);
        // 编辑设备拓展属性  xull@aurine.cn 2020年7月7日 13点53分
        ProjectDeviceAttrFormVo projectDeviceAttrFormVo = new ProjectDeviceAttrFormVo();
        projectDeviceAttrFormVo.setProjectDeviceAttrList(deviceInfoVo.getDeviceAttrs());
        projectDeviceAttrFormVo.setProjectId(ProjectContextHolder.getProjectId());
        projectDeviceAttrFormVo.setType(deviceInfoVo.getDeviceType());
        projectDeviceAttrFormVo.setDeviceId(deviceInfoVo.getDeviceId());
        projectDeviceAttrService.updateDeviceAttrList(projectDeviceAttrFormVo);


        /*if (StringUtils.isNotEmpty(deviceInfoVo.getController())) {
            projectDeviceAttrService.update(Wrappers.lambdaUpdate(ProjectDeviceAttr.class)
                    .set(ProjectDeviceAttr::getAttrValue, deviceInfoVo.getController())
                    .eq(ProjectDeviceAttr::getAttrId, deviceInfoVo.getDeviceId())
                    .eq(ProjectDeviceAttr::getAttrCode, "controller"));
            if (StringUtils.isNotEmpty(deviceInfoVo.getRoute())) {
                projectDeviceAttrService.update(Wrappers.lambdaUpdate(ProjectDeviceAttr.class)
                        .set(ProjectDeviceAttr::getAttrValue, deviceInfoVo.getRoute())
                        .eq(ProjectDeviceAttr::getAttrId, deviceInfoVo.getDeviceId())
                        .eq(ProjectDeviceAttr::getAttrCode, "route"));
            }
        }*/

        //保存设备参数修改记录
        ProjectDeviceInfo projectDeviceInfo = new ProjectDeviceInfo();
        BeanUtils.copyProperties(deviceInfoVo, projectDeviceInfo);
        projectDeviceModifyLogService.saveDeviceModifyLog(deviceInfoVo.getDeviceId(), projectDeviceInfo);

        //添加异常设备
        /*if(deviceInfoVo.isResult()){
            ProjectDeviceAbnormal projectDeviceAbnormal = new ProjectDeviceAbnormal();
            BeanUtils.copyProperties(deviceInfoVo,projectDeviceAbnormal);
            R<Boolean> date = projectDeviceModifyLogService.checkDeviceParam(deviceInfoVo.getIpv4(), deviceInfoVo.getDeviceCode(), deviceInfoVo.getMac(),deviceInfoVo.getDeviceId());
            String[] split = date.getMsg().split("/");
            projectDeviceAbnormal.setAbnormalCode(split[1]);
            projectDeviceAbnormal.setAbnormalDesc(split[0]);
            projectDeviceAbnormal.setDStatus(deviceInfoVo.getStatus());
            projectDeviceAbnormal.setDeviceId(deviceInfoVo.getDeviceId());
            projectDeviceAbnormal.setDevAddTime(LocalDateTime.now());
            projectDeviceAbnormalService.save(projectDeviceAbnormal);
        }*/

        /**
         * 设备更新时将数据发送给硬件
         * @author: 王伟
         * @since 2020-11-26 18:02
         */
        ProjectDeviceInfoProxyVo deviceInfoProxyVo = new ProjectDeviceInfoProxyVo();
        BeanUtils.copyProperties(deviceInfoVo, deviceInfoProxyVo);
        deviceInfoProxyVo.setProjectId(ProjectContextHolder.getProjectId());
        deviceInfoProxyVo.setTenantId(TenantContextHolder.getTenantId());

        if (DeviceTypeConstants.MONITOR_DEVICE.equals(deviceInfoVo.getDeviceType())) {
            DeviceFactoryProducer.getFactoryByProductId(deviceInfoVo.getProductId()).getDeviceService().updateDevice(deviceInfoProxyVo);
            deviceInfoVo.setStatus(deviceInfoProxyVo.getStatus());
            deviceInfoVo.setThirdpartyCode(deviceInfoProxyVo.getThirdpartyCode());
        } else {
            Optional.ofNullable(deviceInfoVo.getDeviceAttrs()).ifPresent(d->{
                deviceInfoProxyVo.setDeviceAttrList(deviceInfoVo.getDeviceAttrs().stream()
                        .map(device->new ProjectDeviceAttrVo(device.getAttrCode(),device.getAttrValue(),device.getAttrName()))
                        .collect(Collectors.toList()));
            });
            DeviceFactoryProducer.getFactory(deviceInfoVo.getDeviceId()).getDeviceService().updateDevice(deviceInfoProxyVo);
        }

        updateChannelCounts(deviceInfoVo);
        return this.updateById(deviceInfoVo);
    }

    @Override
    public List<ProjectDeviceParamSetResultVo> setDeviceParam(ObjectNode paramsNode, String deviceId) {
        ProjectDeviceInfo deviceInfo = this.getById(deviceId);
        SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>()
                .eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
        DeviceParamServiceFactory paramServiceFactory = DeviceParamFactoryProducer.getFactory(deviceInfo.getProjectId(), deviceInfo.getDeviceType());
        if (paramServiceFactory == null) {
            throw new RuntimeException("该类型设备未配置对接中台");
        }
        DeviceParamConfigService paramService = paramServiceFactory.getParamConfigService(productMap.getManufacture(), deviceInfo.getDeviceType());
        return paramService.setDeviceParam(paramsNode, deviceId);

    }


    @Override
    public DevicesResultVo setDevicesParam(ObjectNode paramsNode, List<String> deviceIdList, List<String> serviceIdList) {
        ProjectDeviceInfo deviceInfo = this.getById(deviceIdList.get(0));
        SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>()
                .eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
        DeviceParamServiceFactory paramServiceFactory = DeviceParamFactoryProducer.getFactory(deviceInfo.getProjectId(), deviceInfo.getDeviceType());
        if (paramServiceFactory == null) {
            throw new RuntimeException("该类型设备未配置对接中台");
        }
        DeviceParamConfigService paramService = paramServiceFactory.getParamConfigService(productMap.getManufacture(), deviceInfo.getDeviceType());
        return paramService.setMultiDeviceParam(paramsNode, deviceIdList, serviceIdList);
    }


    @Override
    public boolean handleDeviceParamSetResult(List<ProjectDeviceParamSetResultVo> paramSetResultVoList, boolean isReset) {
        if (CollUtil.isNotEmpty(paramSetResultVoList)) {
            ProjectDeviceInfo deviceInfo = this.getOne(new QueryWrapper<ProjectDeviceInfo>().lambda()
                    .eq(ProjectDeviceInfo::getDeviceId, paramSetResultVoList.get(0).getDeviceId()));

            // 这里获取到参数设置成功的设备ID集合（一台设备多个参数多个结果）
            Set<String> failedDeviceIdSet = paramSetResultVoList.stream().filter(resultVo -> !resultVo.isSuccess())
                    .map(ProjectDeviceParamSetResultVo::getDeviceId).collect(Collectors.toSet());
            Set<String> successDeviceIdSet = paramSetResultVoList.stream().filter(ProjectDeviceParamSetResultVo::isSuccess)
                    .map(ProjectDeviceParamSetResultVo::getDeviceId).collect(Collectors.toSet());
            // 同台设备某个参数设置失败就算失败
            successDeviceIdSet.removeAll(failedDeviceIdSet);
            List<ProjectDeviceInfo> deviceInfoList = new ArrayList<>();
            deviceInfoList.add(deviceInfo);
            if (CollUtil.isNotEmpty(successDeviceIdSet)) {
                if (isReset) {
                    projectDeviceParamHisService.updateSuccessParamHis(new ArrayList<>(successDeviceIdSet), ProjectContextHolder.getProjectId());
                } else {
                    projectDeviceParamHisService.addSuccessParamHis(deviceInfoList, ProjectContextHolder.getProjectId());
                }
            }
            if (CollUtil.isNotEmpty(failedDeviceIdSet)) {
                projectDeviceParamHisService.addFailedParamHis(deviceInfoList, ProjectContextHolder.getProjectId());
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean removeDevice(String id) {

        List<String> infoUids = new ArrayList<>();
        ProjectDeviceInfo projectDeviceInfo = getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class).eq(ProjectDeviceInfo::getDeviceId, id));
        if (projectDeviceInfo != null && projectDeviceInfo.getDeviceType().equals(DeviceTypeConstants.ALARM_HOST)) {
            List<ProjectPerimeterAlarmArea> perimeterAlarmAreas = projectPerimeterAlarmAreaService.
                    list(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class).
                            eq(ProjectPerimeterAlarmArea::getDeviceId, projectDeviceInfo.getDeviceId()));
            perimeterAlarmAreas.forEach(e -> {
                infoUids.add(e.getInfoUid());
                projectPerimeterAlarmAreaService.remove(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class).eq(ProjectPerimeterAlarmArea::getDeviceId, e.getDeviceId()));
            });
            for (String infoUid : infoUids) {
                //remove(Wrappers.lambdaQuery(ProjectDeviceInfo.class).eq(ProjectDeviceInfo::getDeviceId, infoUid));
                // 删除需要通知第三方,所以要使用重写后的removeById
                removeById(infoUid);
            }
        }


        // 删除设备属性拓展 xull@aurine.cn 2020年7月7日 13点59分
        projectDeviceAttrService.remove(Wrappers.lambdaQuery(ProjectDeviceAttr.class)
                .eq(ProjectDeviceAttr::getDeviceId, id));
        /**
         * 区口机，梯口机 删除后，变更添加人员权限信息
         * 王伟 2020-06-19
         */
        projectPersonDeviceService.refreshDeleteDevice(id);
        /**
        * @Author 黄健杰
        * 电梯删除，后变更人员权限信息
        **/
        projectPersonLiftRelService.refreshDeleteDevice(id);

        //删除设备与监控设备的关联
        projectDeviceMonitorRelService.remove(Wrappers.lambdaQuery(ProjectDeviceMonitorRel.class).eq(ProjectDeviceMonitorRel::getDeviceId, id));

        /**
         * 删除设备，直接删除设备相关的通行凭证记录
         * 王伟 2020-09-03
         *
         */

        projectRightDeviceService.removeByDevice(id);

        // 删除设备时删除设备的参数数据
        projectDeviceParamInfoService.remove(new QueryWrapper<ProjectDeviceParamInfo>().lambda().eq(ProjectDeviceParamInfo::getDeviceId, id));

        // 删除设备从MongoDB中删除报警事件记录
        projectDeviceEventCallbackService.removeByDeviceId(id);

        // 从巡检点设备关系表中删除当前设备关联
        projectInspectPointDeviceRelService.remove(new LambdaQueryWrapper<ProjectInspectPointDeviceRel>().eq(ProjectInspectPointDeviceRel::getDeviceId, id));

        // 删除这台设备的异常记录
        projectDeviceAbnormalService.removeAbnormalByDeviceId(id);

        // 删除媒体广告关联关系
        projectMediaAdDevCfgService.removeRel(id);

        // 删除子设备
        List<ProjectDeviceInfoVo> cDevs = baseMapper.getDevicesByParDeviceId(id, null, null);
        if (cDevs != null) {
            cDevs.forEach(e -> {
                removeDevice(e.getDeviceId());
            });
            // 删除设备关系表
            projectDeviceRelService.removeByDeviceId(id);
        }

        // 删除聚集报警规则
        projectDeviceGatherAlarmRuleService.removeByDeviceId(id);
        /* // 20230704 V1.3.0.0（人行版）去除车场相关信息
        //当设备是车道闸一体机的时候才解绑车道
        if(projectDeviceInfo.getDeviceType().equals(DeviceTypeConstants.VEHICLE_BARRIER_DEVICE)){
            R<Boolean> response;
            // 解绑车场服务的车道
            try {
                response = remoteExitLaneService.unbindDevice(id);
            } catch (Exception e) {
                throw new RuntimeException("车场服务异常，车辆道闸一体机删除失败");
            }
            if (response.getCode() != R.ok().getCode() || !response.getData()) {
                throw new RuntimeException("与车道解绑失败");
            }
        }*/
        /**
         * 调用第三方接口处理
         * @author: 王伟
         * @since :2020-08-17
         */
        boolean delResult = DeviceFactoryProducer.getFactory(id).getDeviceService().delDevice(id);
        ProjectDeviceInfo deviceInfo = this.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getDeviceId, id));

        // 只有解绑成功了并且从设备组中删除才能从系统中删除设备
        if (delResult) {

            List<ProjectServiceInfoVo> projectServiceInfoVos = projectServiceService.getHouseServiceByProjectId(ProjectContextHolder.getProjectId());
            projectServiceInfoVos.forEach(e -> {
                IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().delDevice(id);
            });
            try {
                this.rebootDeviceById(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.removeById(id);
            return true;
        }
        return false;
    }


    private void rebootDeviceById(String deviceId) {
        List<ProjectDeviceInfo> deviceInfoList = this.list(new QueryWrapper<ProjectDeviceInfo>().lambda()
                .eq(ProjectDeviceInfo::getDeviceId, deviceId));
        if (CollUtil.isNotEmpty(deviceInfoList)) {
            ProjectDeviceInfo deviceInfo = deviceInfoList.get(0);
            DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(),
                    TenantContextHolder.getTenantId()).getDeviceService().reboot(deviceId);
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean removeAll(List<String> ids) {
        //批量删除设备相关附加属性 xull@aurine.cn 2020年7月8日 11点57分
        projectDeviceAttrService.remove(Wrappers.lambdaUpdate(ProjectDeviceAttr.class)
                .eq(ProjectDeviceAttr::getProjectId, ProjectContextHolder.getProjectId())
                .in(ProjectDeviceAttr::getDeviceId, ids));
        //批量更新人员权限信息 xull@aurine.cn 2020年7月8日 11点58分
        projectPersonDeviceService.refreshDeleteDeviceAll(ids);
        projectPersonLiftRelService.refreshDeleteDeviceAll(ids);

        List<ProjectServiceInfoVo> projectServiceInfoVos = projectServiceService.getHouseServiceByProjectId(ProjectContextHolder.getProjectId());
        projectServiceInfoVos.forEach(projectService -> {
            ids.forEach(id -> {
                IntercomFactoryProducer.getFactory(projectService.getServiceCode()).getIntercomService().delDevice(id);
            });
        });

        // 这里将设备移出设备组
        boolean removeResult = true;

        //批量删除设备 xull@aurine 2020年7月8日 11点58分
        return this.removeByIds(ids);
    }


    @Override
    public void modelExcel(String code, HttpServletResponse httpServletResponse, Integer projectId) throws IOException {
        String policeStatus = "0";
        //获取公安对接接口判断是否启用
        List<ProjectDeviceCollectListVo> ProjectDeviceCollectListVos = projectDeviceCollectService
                .getDeviceCollectListVo(projectId, DeviceCollectTypeEnum.POLICE.code, DeviceCollectConstant.POLICE_PARAM_NAME);

        if (ProjectDeviceCollectListVos != null && ProjectDeviceCollectListVos.size() > 0) {
            policeStatus = ProjectDeviceCollectListVos.get(0).getAttrValue();
            if (StringUtils.isBlank(policeStatus)) {
                policeStatus = "0";
            }
        }
        DeviceExcelEnum deviceExcelEnum = DeviceExcelEnum.getEnum(code, policeStatus);
        List data = Lists.newArrayList();
        String excelPath = DeviceExcelConstant.XLSX_PATH + deviceExcelEnum.getName();
        ClassPathResource classPathResource = new ClassPathResource(excelPath);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        String fileName = deviceExcelEnum.getName();
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(httpServletResponse.getOutputStream(), deviceExcelEnum.getClazz()).withTemplate(classPathResource.getStream()).sheet(0).doFill(data);

    }

    /**
     * 获取宇视直播视频
     *
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
     *
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

    @Override
    public List<ProjectDeviceInfo> getDdDeviceList() {
        return ddDeviceMapService.getDdDeviceList();
    }

    /**
     * 修改状态
     *
     * @param deviceId
     * @param name
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateByStatus(String deviceId, String name) {
        ProjectDeviceInfo projectDeviceInfo = getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class).eq(ProjectDeviceInfo::getDeviceId, deviceId));
        // 撤防布防类型
        String type = PerimeterStatusEnum.DISARM.getName().equals(name) ? PerimeterStatusEnum.DISARM.getType() : PerimeterStatusEnum.ARMED.getType();
        List<ProjectPerimeterAlarmRecord> recordList = new ArrayList<>();

        SysDeviceProductMap sysDeviceProductMap = sysDeviceProductMapService.getOne(Wrappers.lambdaQuery(SysDeviceProductMap.class)
                .eq(SysDeviceProductMap::getProductId, projectDeviceInfo.getProductId()));
        if (sysDeviceProductMap == null) {
            return false;
        }

        List<ProjectPerimeterAlarmArea> list = projectPerimeterAlarmAreaService.list(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class)
                .eq(ProjectPerimeterAlarmArea::getDeviceId, deviceId)
                .isNotNull(ProjectPerimeterAlarmArea::getModuleNo));

        PerimeterAlarmDeviceService perimeterAlarmDeviceService = DeviceFactoryProducer.getFactory(deviceId).getPerimeterAlarmDeviceService();

        ProjectPerimeterAlarmRecord.ProjectPerimeterAlarmRecordBuilder builder = ProjectPerimeterAlarmRecord.builder()
                .deviceId(projectDeviceInfo.getDeviceId()).deviceName(projectDeviceInfo.getDeviceName())
                .operateType(type).operator(SecurityUtils.getUser().getId());

        //区分长城和精华隆设备的操作
        if ("3Z99JHL01".equals(sysDeviceProductMap.getProductCode())) {
            boolean bool = true;
            if (PerimeterStatusEnum.DISARM.getName().equals(name)) {
                for (ProjectPerimeterAlarmArea projectPerimeterAlarmArea : list) {
                    projectPerimeterAlarmArea.setArmedStatus(PerimeterStatusEnum.DISARM.getType());
                    //有一个防区撤防失败就算全失败
                    bool = bool && perimeterAlarmDeviceService.channelRemoval(projectPerimeterAlarmArea);
                }
            } else {
                for (ProjectPerimeterAlarmArea projectPerimeterAlarmArea : list) {
                    projectPerimeterAlarmArea.setArmedStatus(PerimeterStatusEnum.ARMED.getType());
                    bool = bool && perimeterAlarmDeviceService.channelProtection(projectPerimeterAlarmArea);
                }
            }
            for (ProjectPerimeterAlarmArea projectPerimeterAlarmArea : list) {
                String regionName = projectDeviceRegionService.getById(projectPerimeterAlarmArea.getDeviceRegionId()).getRegionName();
                builder.moduleNo(projectPerimeterAlarmArea.getModuleNo()).channelNo(projectPerimeterAlarmArea.getChannelNo())
                        .channelName(projectPerimeterAlarmArea.getChannelName()).regionName(regionName).operateStatus(bool ? "1" : "0");
                recordList.add(builder.build());
            }
            projectPerimeterRecordAreaService.saveBatch(recordList);
            if (!bool) {
                return bool;
            }
            return projectPerimeterAlarmAreaService.updateBatchById(list);
        } else {
            boolean bool = true;
            if (PerimeterStatusEnum.DISARM.getName().equals(name)) {
                bool = perimeterAlarmDeviceService.channelRemoval(list.get(0));
            } else {
                bool = perimeterAlarmDeviceService.channelProtection(list.get(0));
            }
            for (ProjectPerimeterAlarmArea e : list) {
                e.setArmedStatus(type);
                String regionName = projectDeviceRegionService.getById(e.getDeviceRegionId()).getRegionName();
                recordList.add(builder.moduleNo(e.getModuleNo()).channelNo(e.getChannelNo())
                        .channelName(e.getChannelName()).operateStatus(bool ? "1" : "0")
                        .regionName(regionName).build());
            }
            projectPerimeterAlarmAreaService.updateBatchById(list);
            projectPerimeterRecordAreaService.saveBatch(recordList);
            return bool;
        }
    }

    /**
     * 设备关联
     *
     * @param projectDeviceMonitorRelVo
     * @return
     */
    @Override
    public R putDeviceRelevance(ProjectDeviceMonitorRelVo projectDeviceMonitorRelVo) {
        boolean flag = true;
        if (projectDeviceMonitorRelVo.getDeviceId() == null) {
            return R.failed();
        }
        List<ProjectDeviceMonitorRel> projectDeviceMonitorRel = projectDeviceMonitorRelService.list(Wrappers.lambdaQuery(ProjectDeviceMonitorRel.class)
                .eq(ProjectDeviceMonitorRel::getDeviceId, projectDeviceMonitorRelVo.getDeviceId()));
        if (projectDeviceMonitorRelVo.getMonitorDeviceId().length == 0) {
            if (CollectionUtils.isNotEmpty(projectDeviceMonitorRel)) {
                for (ProjectDeviceMonitorRel deviceMonitorRel : projectDeviceMonitorRel) {
                    flag = projectDeviceMonitorRelService.remove(Wrappers.lambdaQuery(ProjectDeviceMonitorRel.class)
                            .eq(ProjectDeviceMonitorRel::getMonitorDeviceId, deviceMonitorRel.getMonitorDeviceId()));
                    if (!flag) {
                        return R.failed(flag);
                    }
                }
            }
            return R.ok();
        } else {
            if (CollectionUtils.isNotEmpty(projectDeviceMonitorRel)) {
                for (ProjectDeviceMonitorRel deviceMonitorRel : projectDeviceMonitorRel) {
                    flag = projectDeviceMonitorRelService.remove(Wrappers.lambdaQuery(ProjectDeviceMonitorRel.class)
                            .eq(ProjectDeviceMonitorRel::getMonitorDeviceId, deviceMonitorRel.getMonitorDeviceId()));
                    if (!flag) {
                        return R.failed(flag);
                    }
                }
            }
            for (String monitorDeviceId : projectDeviceMonitorRelVo.getMonitorDeviceId()) {
                ProjectDeviceMonitorRel deviceMonitorRel = new ProjectDeviceMonitorRel();
                deviceMonitorRel.setDeviceId(projectDeviceMonitorRelVo.getDeviceId());
                deviceMonitorRel.setMonitorDeviceId(monitorDeviceId);
                flag = projectDeviceMonitorRelService.save(deviceMonitorRel);
            }
        }
        return R.ok(flag);
    }

    /**
     * 查询关联的设备
     *
     * @param deviceId
     * @return
     */
    @Override
    public List<ProjectDeviceInfoPageVo> getMonitoring(String deviceId) {
        return baseMapper.getMonitoring(deviceId);
    }


    /**
     * 报警主机添加设备调用同步接口
     */
    @Override
    public boolean reacquireDefenseArea(ProjectDeviceInfo projectDeviceInfo) {
        ProjectDeviceInfo deviceInfo = this.getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class).eq(ProjectDeviceInfo::getSn, projectDeviceInfo.getSn()));
        Boolean bool = projectPerimeterAlarmAreaService.reacquireDefenseArea(deviceInfo.getDeviceId());
        if (bool) {
            deviceInfo.setStatus("1");
            this.updateById(deviceInfo);
        } else {
            deviceInfo.setStatus("2");
            this.updateById(deviceInfo);
        }
        return bool;
    }

    /**
     * 查询品牌的型号
     *
     * @param deviceTypeId
     * @return
     */
    @Override
    public List<ProjectDeviceProductNameVo> getDeviceBrand(String deviceTypeId) {
        return baseMapper.getDeviceBrand(deviceTypeId,ProjectContextHolder.getProjectId());
    }

    @Override
    public ProjectDeviceProductNameVo getDeviceBrand(String deviceTypeId, String attrVal) {
        return baseMapper.getDeviceBrandByAttr(deviceTypeId, attrVal, ProjectContextHolder.getProjectId());
    }

    /**
     * 智能路灯添加控制器IMEL
     *
     * @param vo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveLightingIMEL(ProjectDeviceLightIMELVo vo) {

        ProjectDeviceAttr projectDeviceAttr = projectDeviceAttrService.getOne(Wrappers.lambdaQuery(ProjectDeviceAttr.class)
                .eq(ProjectDeviceAttr::getAttrCode, "sn")
                .eq(ProjectDeviceAttr::getAttrValue, vo.getSn()));

        if (projectDeviceAttr != null) {
            throw new RuntimeException("已存在IMEI");
        }
        boolean bool = false;
        String attrId = UUID.randomUUID().toString().replaceAll("-", "");
        String deviceId = UUID.randomUUID().toString().replaceAll("-", "");
        Field[] fields = vo.getClass().getDeclaredFields();
        List<String> list = new ArrayList<>();
        for (Field field : fields) {
            list.add(field.getName());
        }
        if (StringUtils.isEmpty(vo.getFrequency())) {
            list.remove("frequency");
        }
        for (String code : list) {
            try {
                ProjectDeviceAttr attr = new ProjectDeviceAttr();
                attr.setAttrCode(code);
                attr.setAttrId(attrId);
                attr.setDeviceId(deviceId);
                attr.setProjectId(ProjectContextHolder.getProjectId());
                attr.setDeviceType(DeviceTypeConstants.SMART_STREET_LIGHT);

                String firstLetter = code.substring(0, 1).toUpperCase();
                String getter = "get" + firstLetter + code.substring(1);
                Method method = vo.getClass().getMethod(getter);
                String value = method.invoke(vo).toString();

                attr.setAttrValue(value);
                bool = projectDeviceAttrService.save(attr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bool;
    }


    /**
     * 查询控制器IMEL
     *
     * @param deviceType
     * @return
     */
    @Override
    public List<ProjectDeviceProductNameVo> getControllerIMEL(String deviceType) {
        return baseMapper.getControllerIMEL(deviceType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean streetLightControl(StreetLightDeviceStatus deviceStatus, Integer projectId) {
        ProjectContextHolder.setProjectId(projectId);
        List<String> successDeviceIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(deviceStatus.getDeviceIdList())) {
            successDeviceIdList.addAll(DeviceFactoryProducer.getFactory(deviceStatus.getDeviceIdList().get(0)).getIotDeviceService().lightAdjustmentBatch(deviceStatus));
        } else {
            DeviceFactoryProducer.getFactory(deviceStatus.getDeviceId()).getIotDeviceService().lightAdjustment(deviceStatus);
            successDeviceIdList.add(deviceStatus.getDeviceId());
        }
        // 要保存或是更新的设备参数列表
        List<ProjectDeviceParamInfo> deviceParamInfoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(successDeviceIdList)) {
            projectDeviceParamInfoService.remove(new LambdaQueryWrapper<ProjectDeviceParamInfo>()
                    .in(ProjectDeviceParamInfo::getDeviceId, successDeviceIdList)
                    .eq(ProjectDeviceParamInfo::getServiceId, "LightDeviceStatus"));
            successDeviceIdList.forEach(deviceId -> {
                ProjectDeviceParamInfo projectDeviceParamInfo = new ProjectDeviceParamInfo();
                projectDeviceParamInfo.setServiceId("LightDeviceStatus");
                projectDeviceParamInfo.setDeviceId(deviceId);
                projectDeviceParamInfo.setProjectId(ProjectContextHolder.getProjectId());
                ObjectNode objectNode = objectMapper.createObjectNode();
                deviceStatus.setDeviceIdList(null);
                deviceStatus.setDeviceId("");
                objectNode.putPOJO("LightDeviceStatus", deviceStatus);
                projectDeviceParamInfo.setDeviceParam(objectNode.toString());
                deviceParamInfoList.add(projectDeviceParamInfo);
            });
            try {
                projectDeviceParamInfoService.saveBatch(deviceParamInfoList);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public String getProductIdByDeviceSn(String deviceSn) {
        return baseMapper.getProductIdByDeviceSn(deviceSn);
    }

    private static final String ABILITY_KEY_PRE = "ability_device_map_";

    @Override
    public boolean checkCapabilityByCertType(String deviceId, CertmediaTypeEnum certType) {
        if (certType == null) {
            return false;
        }
        switch (certType) {
            case Card:
                return this.checkDeviceHasCardCapability(deviceId);
            case Face:
                return this.checkDeviceHasFaceCapability(deviceId);
            default:
                return false;
        }
    }

    @Override
    public boolean checkDeviceHasFaceCapability(String deviceId) {
        return this.checkByCapability(deviceId, "face");
    }

    @Override
    public boolean checkDeviceHasCardCapability(String deviceId) {
        return this.checkByCapability(deviceId, "card");
    }

    /**
     * <p>检查设备是否有指定的能力</p>
     *
     * @param
     * @return
     * @author: 王良俊
     */
    private boolean checkByCapability(String deviceId, String capability) {

        ProjectDeviceInfo deviceInfo = this.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getDeviceId, deviceId).last("limit 1"));
        if (deviceInfo != null) {
            return StrUtil.isEmpty(deviceInfo.getDeviceCapabilities()) || deviceInfo.getDeviceCapabilities().contains(capability);
        }
        return false;
    }

    /**
     * 根据Id查询设备的楼栋和单元
     *
     * @param deviceId
     * @return
     */
    @Override
    public ProjectDeviceBuildingUnitNameVo getBuildingAndUnitByDeviceId(String deviceId) {
        return baseMapper.getBuildingAndUnitByDeviceId(deviceId);
    }

    @Override
    public String getModule(String deviceId) {
        Integer projectId = ProjectContextHolder.getProjectId();

        return baseMapper.getModule(deviceId, projectId);
    }

    @Override
    public Integer getAbnormalCount(String type, String deviceRegionId) {
        return baseMapper.getAbnormalCount(type,deviceRegionId);
    }

    @Override
    public ProjectNoticeDevice getDeviceNameAndBuildingNameAndUnitName(String deviceId) {
        return baseMapper.getDeviceNameAndBuildingNameAndUnitName(deviceId);
    }

    @Override
    public boolean setAccount(ProjectDeviceInfoVo deviceInfo) {
        ProjectDeviceInfoProxyVo deviceInfoProxyVo = new ProjectDeviceInfoProxyVo();
        BeanUtils.copyProperties(deviceInfo, deviceInfoProxyVo);
        deviceInfoProxyVo.setProjectId(ProjectContextHolder.getProjectId());
        deviceInfoProxyVo.setTenantId(TenantContextHolder.getTenantId());
        boolean success = DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(),
                TenantContextHolder.getTenantId()).getDeviceService().setAccount(deviceInfoProxyVo);
        return success;
    }

    @Override
    public boolean setGatherAlarmRule(String id, ProjectDeviceGatherAlarmRuleVo ruleVo) {
        if(ruleVo.getIsGlobal() != null && ruleVo.getIsGlobal().equals("1")){
            List<ProjectDeviceInfo> deviceInfos = baseMapper.listByDeviceType(ProjectContextHolder.getProjectId(),DeviceTypeConstants.AI_BOX_DEVICE_MONITOR);
            if(deviceInfos != null){
                // XXX 开启线程处理整个社区设备的规则设置，需要确认是否合理
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> {
                    RequestContextHolder.setRequestAttributes(servletRequestAttributes,true);//设置子线程共享
                    deviceInfos.forEach(deviceInfo ->{
                        setGatherAlarmRuleSingle(deviceInfo.getDeviceId(),ruleVo);
                    });
                });
            }
            return true;
        }else {
            return setGatherAlarmRuleSingle(id,ruleVo);
        }
    }

    @Override
    public boolean setGatherAlarmRuleSingle(String deviceId, ProjectDeviceGatherAlarmRuleVo ruleVo) {
        ProjectDeviceInfoProxyVo deviceInfoProxyVo = baseMapper.getByDeviceId(deviceId);
        if(deviceInfoProxyVo == null){
            return false;
        }
        ProjectDeviceInfoProxyVo parDevice = baseMapper.getParDeviceByDeviceId(deviceInfoProxyVo.getDeviceId());
        if(parDevice == null){
            return false;
        }
        ProjectDeviceGatherAlarmRuleVo oldRuleVo = projectDeviceGatherAlarmRuleService.getProjectDeviceGatherAlarmRuleVoByDeviceId(deviceId);
        ProjectDeviceGatherAlarmRule rule = new ProjectDeviceGatherAlarmRule();
        BeanUtils.copyProperties(ruleVo,rule);
        rule.setDeviceId(deviceId);
        if(oldRuleVo == null){
            projectDeviceGatherAlarmRuleService.save(rule);
        }else {
            projectDeviceGatherAlarmRuleService.updateByDeviceId(rule);
        }

        boolean success = DeviceFactoryProducer.getFactory(parDevice.getDeviceType(), ProjectContextHolder.getProjectId(),
                TenantContextHolder.getTenantId()).getDeviceService().setGatherAlarmRule(deviceInfoProxyVo,parDevice,ruleVo);
        return success;
    }

    @Override
    public boolean setDeviceAlias(String deviceId, String alias) {
        Integer num = baseMapper.updateAliasById(deviceId,alias);
        if(num != null && num > 0){
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ProjectDeviceInfoVo> relateChildDev(String id) {

        ProjectDeviceInfoProxyVo deviceInfoProxyVo = baseMapper.getByDeviceId(id);
        AurineEdgeDeviceInfoDTO deviceInfoDTO = DeviceFactoryProducer.getFactory(deviceInfoProxyVo.getDeviceType(), ProjectContextHolder.getProjectId(),
                TenantContextHolder.getTenantId()).getDeviceService().getChannel(deviceInfoProxyVo);
        if (deviceInfoDTO != null && deviceInfoDTO.getExtParam() != null) {

            // 库里的子设备
            List<ProjectDeviceInfoVo> oldDevs = baseMapper.getDevicesByParDeviceId(id, null, null);
            // 待删除的子设备
            List<String> delDevIds = new ArrayList<>();
            if (oldDevs != null) {
                for (ProjectDeviceInfoVo oldDev :
                        oldDevs) {
                    delDevIds.add(oldDev.getDeviceId());
                }
            }

            AurineEdgeDeviceInfoExtParamDTO extParamDTO = deviceInfoDTO.getExtParam();
            List<AurineEdgeChannelInfoDTO> channelInfoDTOs = extParamDTO.getChannelInfoList();
            if (channelInfoDTOs != null) {
                channelInfoDTOs.forEach(e -> {
                    boolean isNew = true;
                    ProjectDeviceInfoVo deviceInfoVo = new ProjectDeviceInfoVo();
                    if (oldDevs != null) {
                        for (ProjectDeviceInfoVo oldDev :
                                oldDevs) {
                            if (e.getChannelId().equals(oldDev.getChannel())) {
                                isNew = false;
                                delDevIds.remove(oldDev.getDeviceId());
                                BeanUtils.copyProperties(oldDev, deviceInfoVo);
                                deviceInfoVo.setProjectId(null);
                            }
                        }

                    }

                    deviceInfoVo.setDeviceName(e.getName());
                    if (StringUtils.isBlank(deviceInfoVo.getDeviceAlias())) {
                        deviceInfoVo.setDeviceAlias(e.getName());
                    }
                    deviceInfoVo.setDeviceDesc(e.getDescription());
                    deviceInfoVo.setChannel(e.getChannelId());
                    deviceInfoVo.setDeviceType(DeviceTypeConstants.AI_BOX_DEVICE_MONITOR);
                    if (e.getOnline() != null && e.getOnline().equals("true")) {
                        deviceInfoVo.setStatus(DeviceInfoConstant.ONLINE_STATUS);
                    } else {
                        deviceInfoVo.setStatus(DeviceInfoConstant.OUTLINE_STATUS);
                    }
                    //设置为手动接入
                    deviceInfoVo.setAccessMethod(DeviceAccessMethodEnum.AUTO.code);

                    try {
                        URL url = new URL(e.getUrl().replace("rtsp", "http"));
                        deviceInfoVo.setIpv4(url.getHost());
                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    //设置AI盒子摄像头产商账号密码
                    if(StrUtil.isNotBlank(e.getPassword()) && StrUtil.isNotBlank(e.getUsername())){
                        deviceInfoVo.setCompanyPasswd(e.getPassword());
                        deviceInfoVo.setCompanyAccount(e.getUsername());
                    }
                    //判断是否为新设备
                    if (isNew) {
                        this.save(deviceInfoVo);
                        // 保存设备关系
                        ProjectDeviceRel deviceRel = new ProjectDeviceRel();
                        deviceRel.setDeviceId(deviceInfoVo.getDeviceId());
                        deviceRel.setParDeviceId(deviceInfoProxyVo.getDeviceId());
                        this.projectDeviceRelService.save(deviceRel);
                    } else {
                        this.updateById(deviceInfoVo);
                    }


                    List<ProjectDeviceAttrListVo> deviceAttrs = this.projectDeviceAttrService
                            .getDeviceAttrListVo(deviceInfoVo.getProjectId(),
                                    deviceInfoVo.getDeviceType(), deviceInfoVo.getDeviceId());
                    if (deviceAttrs != null) {
                        deviceAttrs.forEach(f -> {
                            if (f.getAttrCode().equals("model")) {
                                f.setAttrValue(e.getModel());
                            } else if (f.getAttrCode().equals("url")) {
                                f.setAttrValue(e.getUrl());
                            }
                        });
                    }

                    // 添加设备拓展属性逻辑 xull@aurine.cn 2020年7月7日 11点13分
                    ProjectDeviceAttrFormVo projectDeviceAttrFormVo = new ProjectDeviceAttrFormVo();
                    projectDeviceAttrFormVo.setProjectDeviceAttrList(deviceAttrs);
                    projectDeviceAttrFormVo.setType(deviceInfoVo.getDeviceType());
                    projectDeviceAttrFormVo.setDeviceId(deviceInfoVo.getDeviceId());
                    projectDeviceAttrFormVo.setProjectId(ProjectContextHolder.getProjectId());
                    projectDeviceAttrService.updateDeviceAttrList(projectDeviceAttrFormVo);
                });
            }
            // 删除不存在的子设备
            if (delDevIds != null) {
                delDevIds.forEach(e -> {
                    removeDevice(e);
                });
            }
        }
        return this.baseMapper.getDevicesByParDeviceId(id, null, null);
    }

    @Override
    public List<ProjectDeviceInfoVo> getChildDev(String id, String deviceAlias, String ipv4) {
        return this.baseMapper.getDevicesByParDeviceId(id, deviceAlias, ipv4);
    }

    @Override
    public List<ProjectDeviceLiftVo> getLiftsWithFloor(String passPlanId, String personType, String personId) {
        //根据方案判断用户禁止修改的设备及楼层
        List<ProjectHouseDTO> houseDTOList = null;
        // 可通行楼栋
        List<String> buildingList = new ArrayList<>();
        // 可通行楼层
        Map<String, List<String>> floorLMap = new HashMap<>();
        boolean isAll = false;
        if(StringUtils.isNotBlank(personId) && StringUtils.isNotBlank(personType)){
            if(StringUtils.isNotBlank(passPlanId)){
                //如果是住户，获取住户可通行楼层
                if(personType.equals(PersonTypeEnum.PROPRIETOR.code)){
                    houseDTOList = projectHousePersonRelService.listHouseByPersonId(personId);
                    if(!houseDTOList.isEmpty()){
                        for (ProjectHouseDTO projectHouseDTO : houseDTOList) {
                            buildingList.add(projectHouseDTO.getBuildingId());
                            ProjectHouseInfoVo houseInfoVo = projectHouseInfoService.getVoById(projectHouseDTO.getHouseId());
                            if(houseInfoVo.getFloor() == null){
                                continue;
                            }
                            if(floorLMap.containsKey(projectHouseDTO.getBuildingId())){
                                floorLMap.get(projectHouseDTO.getBuildingId()).add(houseInfoVo.getFloor().toString());
                            }else {
                                floorLMap.put(projectHouseDTO.getBuildingId(),new ArrayList<>());
                                floorLMap.get(projectHouseDTO.getBuildingId()).add(houseInfoVo.getFloor().toString());
                            }
                        }
                    }

                }else if(personType.equals(PersonTypeEnum.STAFF.code)){
                    isAll = true;
                }
            }
        }
        // 设置设备类型
        ProjectDeviceInfoFormVo projectDeviceInfoFormVo = new ProjectDeviceInfoFormVo();
        List<String> types = new ArrayList<>();
        types.add(DeviceTypeConstants.ELEVATOR);
        projectDeviceInfoFormVo.setTypes(types);
        // 通过设备类型所有电梯设备(注意设备类型为列表类型)
        List<ProjectDeviceInfo> deviceList = this.findByType(projectDeviceInfoFormVo);

        List<ProjectDeviceLiftVo> liftList = new ArrayList<>();
        if(deviceList != null){
            for (ProjectDeviceInfo device : deviceList) {
                boolean isDisable = false;
                if(isAll){
                    isDisable = true;
                }
                ProjectDeviceLiftVo liftVo = new ProjectDeviceLiftVo();
                BeanUtil.copyProperties(device, liftVo);
                //根据设备关联的楼栋，查询楼层信息
                List<ProjectFloorVo> floorVos = new ArrayList<>();
                // 获取电梯设备关联的楼栋
                ProjectBuildingInfoVo buildingInfoVo = projectBuildingInfoService.getById(device.getBuildingId());
                if(buildingInfoVo == null){
                    continue;
                }
                //地下楼层
                /*if(buildingInfoVo.getFloorUnderground() != null && buildingInfoVo.getFloorUnderground() > 0){
                    for (int i = buildingInfoVo.getFloorUnderground(); i > 0; i--){
                        ProjectFloorVo floorVo = new ProjectFloorVo();
                        floorVo.setFloorNo(String.valueOf(-i));
                        floorVo.setIsPublic("0");
                        if(isDisable){
                            floorVo.setIsDisable("1");
                        }
                        floorVos.add(floorVo);
                    }
                }*/
                //架空楼层
                /*if(buildingInfoVo.getHasStiltFloor() != null && buildingInfoVo.getHasStiltFloor().equals("1")){
                    ProjectFloorVo floorVo = new ProjectFloorVo();
                    floorVo.setFloorNo(String.valueOf(0));
                    floorVo.setIsPublic("0");
                    if(isDisable){
                        floorVo.setIsDisable("1");
                    }
                    floorVos.add(floorVo);
                }*/
                // 获取楼层号
                List<Integer> floors = projectHouseInfoService.getFloorByBuildingId(device.getBuildingId());
                if(floors != null){
                    for(Integer floorNo:floors) {
                        ProjectFloorVo floorVo = new ProjectFloorVo();
                        floorVo.setFloorNo(String.valueOf(floorNo));
                        floorVo.setIsPublic("0");//不是公共层
                        if(isDisable){
                            floorVo.setIsDisable("1");// 是否禁止用户改变,1为选中不可变更,即员工
                        }else {
                            if(!buildingList.isEmpty() && !floorLMap.isEmpty()){
                                if(buildingList.contains(device.getBuildingId())){
                                    List<String> userFloors = floorLMap.get(device.getBuildingId());
                                    if(userFloors.contains(floorNo.toString())){
                                        floorVo.setIsDisable("1");
                                    }
                                }
                            }
                        }
                        floorVos.add(floorVo);
                    }
                }
                liftVo.setFloors(floorVos);
                liftList.add(liftVo);
            }
        }

        return liftList;
    }

    @Override
    public void exportExcel(String type,HttpServletResponse httpServletResponse) {
        List<IndoorDeviceExportExcel> excelData=new ArrayList<>();
        if(ExportExcelTypeEnum.NO_ACTIVE.code.equals(type)){
            excelData=baseMapper.getProjectDeviceInfoByStatue(type);

        }else if(ExportExcelTypeEnum.Active.code.equals(type)){
            excelData=baseMapper.getProjectDeviceInfoByStatue(type);

        }else{
            excelData=baseMapper.getProjectDeviceInfoByStatue("");

        }
        System.out.println("导出数据："+JSON.toJSONString(excelData));
        String fileName = "设备信息"+ DateUtil.format(DateUtil.date(),"yyyyMMddHHmmss");
        ExcelUtil excelUtil = new ExcelUtil();
        ExcelUtil.DefaultExportStrategy<IndoorDeviceExportExcel> deviceInfoExcelDefaultExportStrategy = excelUtil.new DefaultExportStrategy<IndoorDeviceExportExcel>(excelData);
        excelUtil.exportExcel(fileName, "设备信息", excelData, httpServletResponse, deviceInfoExcelDefaultExportStrategy,IndoorDeviceExportExcel.class);

    }

    @Override
    public DeviceNetworkInfoVo getDeviceNetWorkInfo(String mac) {
        try {
            mac = MacUtil.formatMac(mac);
            mac = MacUtil.formatMac(mac,"-");
            mac.toUpperCase();
        } catch (Exception e) {
            ProjectDeviceInfoServiceImpl.log.info("mac地址格式不正确",mac);
            e.printStackTrace();
        }
        List<ProjectDeviceInfoVo> deviceInfoVoList = this.baseMapper.getProjectDeviceInfoByMac(mac);
        if(deviceInfoVoList == null || deviceInfoVoList.size() != 1){
            return null;
        }
        ProjectDeviceInfoVo projectDeviceInfoVo = deviceInfoVoList.get(0);
        //设备已配置
        if(projectDeviceInfoVo.getConfigured().equals("1")){
            return null;
        }
        // 获取设备增值属性
        List<ProjectDeviceAttrListVo> projectDeviceAttrs =
                projectDeviceAttrService.getDeviceAttrListVo(ProjectContextHolder.getProjectId(),
                        projectDeviceInfoVo.getDeviceType(), projectDeviceInfoVo.getDeviceId());
        if (projectDeviceAttrs != null && projectDeviceAttrs.size() > 0) {
            projectDeviceInfoVo.setDeviceAttrs(projectDeviceAttrs);
        }
        DeviceNetworkInfoVo deviceNetworkInfoVo = new DeviceNetworkInfoVo();
        //网络参数
        DeviceNetworkParamsVo networkParamsVo = new DeviceNetworkParamsVo();
        networkParamsVo.setIp(projectDeviceInfoVo.getIpv4());
        List<ProjectDeviceAttrListVo> deviceAttrs = projectDeviceInfoVo.getDeviceAttrs();
        for (ProjectDeviceAttrListVo attrListVo:
        deviceAttrs) {
            if(attrListVo.getAttrCode().equals("subnet")){
                networkParamsVo.setMask(attrListVo.getAttrValue());
            }else if(attrListVo.getAttrCode().equals("gateway")){
                networkParamsVo.setGateway(attrListVo.getAttrValue());
            }else if(attrListVo.getAttrCode().equals("dns1")){
                networkParamsVo.setDns1(attrListVo.getAttrValue());
            }else if(attrListVo.getAttrCode().equals("dns2")){
                networkParamsVo.setDns2(attrListVo.getAttrValue());
            }else if(attrListVo.getAttrCode().equals("centerServerIP")){
                networkParamsVo.setCenterIp(attrListVo.getAttrValue());
            }
        }
        deviceNetworkInfoVo.setNetParams(networkParamsVo);
        //编号信息
        DeviceNetworkNoInfoVo networkNoInfoVo = new DeviceNetworkNoInfoVo();
        networkNoInfoVo.setAreaNo(1);//社区编号 TODO
        try {
            networkNoInfoVo.setNo(projectDeviceInfoVo.getDeviceCode());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
        if(projectDeviceInfoVo.getDeviceType().equals(DeviceTypeConstants.INDOOR_DEVICE)){
            networkNoInfoVo.setType(0x32);//设备类型 TODO
        }
        deviceNetworkInfoVo.setNoInfo(networkNoInfoVo);
        //编号规则
        DeviceNetworkNoRuleVo networkNoRuleVo = new DeviceNetworkNoRuleVo();
        networkNoRuleVo.setUseCellNo(1);//默认启用
        String subSection = "";
        String[] sectionDesc = new String[3];
        List<ProjectEntityLevelCfg> projectEntityLevelCfgList = projectEntityLevelCfgService.list(new QueryWrapper<ProjectEntityLevelCfg>().lambda().eq(ProjectEntityLevelCfg::getProjectId,projectDeviceInfoVo.getProjectId()));
        if(projectEntityLevelCfgList != null){
            for (ProjectEntityLevelCfg projectEntityLevelCfg:
            projectEntityLevelCfgList) {
                if(projectEntityLevelCfg.getLevel() == 1){
                    networkNoRuleVo.setRoomNoLen(projectEntityLevelCfg.getCodeRule());
                    sectionDesc[2] = projectEntityLevelCfg.getLevelDesc();
                }else if(projectEntityLevelCfg.getLevel() == 2){
                    networkNoRuleVo.setCellNoLen(projectEntityLevelCfg.getCodeRule());
                    sectionDesc[1] = projectEntityLevelCfg.getLevelDesc();
                }else if(projectEntityLevelCfg.getLevel() == 3){
                    networkNoRuleVo.setStairNoLen(projectEntityLevelCfg.getCodeRule());
                    sectionDesc[0] = projectEntityLevelCfg.getLevelDesc();
                }
            }
        }

        subSection = networkNoRuleVo.getStairNoLen().toString() + networkNoRuleVo.getCellNoLen().toString() + networkNoRuleVo.getRoomNoLen().toString();
        networkNoRuleVo.setStairNoLen(networkNoRuleVo.getCellNoLen() + networkNoRuleVo.getStairNoLen());
        networkNoRuleVo.setSubSection(subSection);
        deviceNetworkInfoVo.setNoRules(networkNoRuleVo);
        deviceNetworkInfoVo.setSectionDesc(sectionDesc);
        deviceNetworkInfoVo.setLanguage(2052);
        return deviceNetworkInfoVo;
    }

    @Override
    public Boolean updateConfigured(String mac) {
        try {
            mac = MacUtil.formatMac(mac);
            mac = MacUtil.formatMac(mac,"-");
            mac.toUpperCase();
        } catch (Exception e) {
            ProjectDeviceInfoServiceImpl.log.info("mac地址格式不正确",mac);
            e.printStackTrace();
        }
        List<ProjectDeviceInfo> deviceList =
                this.list(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getMac, mac));
        ProjectDeviceInfo projectDeviceInfo = null;

        if (CollectionUtil.isNotEmpty(deviceList)) {
            projectDeviceInfo = deviceList.get(0);
            projectDeviceInfo.setConfigured("1");
            return this.updateById(projectDeviceInfo);
        }
        return false;
    }



    @Override
    public List<ProjectVehicleBarrierDeviceInfoVo> listVehicleBarrierDevice(VehicleBarrierDeviceQuery query) {
        List<ProjectVehicleBarrierDeviceInfoVo> deviceList = this.baseMapper.listVehicleBarrierDevice(query);
        R<List<ParkingDeviceCertDlstatusCountVo>> response = projectPlateNumberDeviceService.countByProject();
        List<ParkingDeviceCertDlstatusCountVo> data = response.getData();
        Map<String, ParkingDeviceCertDlstatusCountVo> countMap = data.stream().collect(Collectors.toMap(ParkingDeviceCertDlstatusCountVo::getDeviceId, val -> val, (t, t2) -> t2));

        deviceList.forEach(item -> {
            ParkingDeviceCertDlstatusCountVo countInfo = countMap.get(item.getDeviceId());
            if (countInfo != null) {
                item.setSuccessNum(countInfo.getSuccessNum());
                item.setFailedNum(countInfo.getFailedNum());
                item.setDownloadingNum(countInfo.getDownloadingNum());
            }
        });
        return deviceList;
    }

    @Override
    public R saveVehicleBarrierDevice(ProjectDeviceInfo deviceInfo) {
        if (StrUtil.isEmpty(deviceInfo.getDeviceName())) {
            return R.failed("缺少设备名称");
        }
        if (StrUtil.isEmpty(deviceInfo.getThirdpartyCode())) {
            return R.failed("缺少设备第三方ID");
        }
        if (StrUtil.isEmpty(deviceInfo.getProductId())) {
            return R.failed("缺少产品ID");
        }
        SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>()
                .eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
        if (productMap == null) {
            return R.failed("平台未找到对应的产品信息");
        }
        if (StrUtil.isEmpty(deviceInfo.getDeviceRegionId())) {
            ProjectDeviceRegion deviceRegion = projectDeviceRegionService.getOne(new QueryWrapper<ProjectDeviceRegion>().lambda()
                    .eq(ProjectDeviceRegion::getIsDefault, "1")
                    .ne(ProjectDeviceRegion::getRegionId, "1000"));
            deviceInfo.setDeviceRegionId(deviceRegion.getRegionId());
        }
        deviceInfo.setBrand(productMap.getModelId());
        deviceInfo.setDeviceId(IdUtil.simpleUUID());
        this.save(deviceInfo);

        projectDeviceParamInfoService.setDefaultParam(deviceInfo);

        return R.ok(deviceInfo);
    }
    @SneakyThrows
    @Override
    public R setParkingDeviceParam(ParkingParamInfoVo deviceParamInfoVo) {
        String deviceId = deviceParamInfoVo.getDeviceId();
        String paramJson = deviceParamInfoVo.getParamJson();
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);

        JSONObject jsonObject = JSON.parseObject(paramJson, JSONObject.class);

        List<ProjectDeviceParamInfo> paramInfoList = projectDeviceParamInfoService.list(new LambdaQueryWrapper<ProjectDeviceParamInfo>().eq(ProjectDeviceParamInfo::getDeviceId, deviceId));
        Map<String, Integer> paramIdMap;
        if (CollUtil.isNotEmpty(paramInfoList)) {
            paramIdMap = paramInfoList.stream().collect(Collectors.toMap(ProjectDeviceParamInfo::getServiceId, ProjectDeviceParamInfo::getSeq));
        } else {
            paramIdMap= new HashMap<>();
        }
        List<ProjectDeviceParamInfo> successParamList = new ArrayList<>();
        ParkingDeviceParamInfoVo deviceParamInfoDto = new ParkingDeviceParamInfoVo();
        List<ParkingParamDataVo> paramDataVoList = new ArrayList<>();
        jsonObject.forEach((key, value) -> {
            ParkingParamDataVo parkingParamDataVo = new ParkingParamDataVo();
            parkingParamDataVo.setParam(JSON.parseObject(value.toString(), JSONObject.class));
            parkingParamDataVo.setServiceId(key);
            paramDataVoList.add(parkingParamDataVo);
        });

        deviceParamInfoDto.setParamList(paramDataVoList);
        deviceParamInfoDto.setDeviceId(deviceId);
        deviceParamInfoDto.setThirdPartyCode(deviceInfo.getThirdpartyCode());

        R<DeviceParamConfResultVo> result = remoteParkingDeviceParamConfService.configParam(deviceParamInfoDto, SecurityConstants.FROM_IN);

        DeviceParamConfResultVo data = result.getData();

        List<String> failedServiceId = data.getFailedServiceId();

        jsonObject.forEach((key, value) -> {
            // 生成参数信息对象
            if (CollUtil.isEmpty(failedServiceId) || !failedServiceId.contains(key)) {
                JSONObject paramData = new JSONObject();
                paramData.put(key, value);
                ProjectDeviceParamInfo paramInfo = new ProjectDeviceParamInfo();
                paramInfo.setDeviceId(deviceId);
                paramInfo.setServiceId(key);
                paramInfo.setDeviceParam(paramData.toString());
                paramInfo.setSeq(paramIdMap.get(key));
                successParamList.add(paramInfo);
            }
        });
        log.info("[车道一体机参数设置-{}] 失败参数：{}", deviceId, failedServiceId);
        List<ProjectDeviceParamInfo> saveList = successParamList.stream().filter(e -> e.getSeq() == null).collect(Collectors.toList());
        List<ProjectDeviceParamInfo> updateList = successParamList.stream().filter(e -> e.getSeq() != null).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(saveList)){
            projectDeviceParamInfoService.saveBatch(saveList);
        }
        if(CollUtil.isNotEmpty(updateList)){
            updateList.forEach(e->{
                projectDeviceParamInfoService.updateParamInfo(e.getDeviceId(),e.getServiceId(),e.getDeviceParam());
            });
        }
        //projectDeviceParamInfoService.saveOrUpdateBatch(successParamList);
        if (CollUtil.isNotEmpty(failedServiceId)) {
            projectDeviceParamHisService.addFailedParamHis(Arrays.asList(deviceInfo), ProjectContextHolder.getProjectId());
            return R.failed("参数设置失败");
        }
        projectDeviceParamHisService.addSuccessParamHis(Arrays.asList(deviceInfo), ProjectContextHolder.getProjectId());
        return R.ok("参数设置成功");

    }

    @SneakyThrows
    @Override
    public R<DevicesResultVo> setParkingDevicesParam(ParkingParamInfoVo deviceParamInfoVo) {
        List<String> deviceIdList = deviceParamInfoVo.getDeviceIdList();
        List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoProxyService.list(new LambdaQueryWrapper<ProjectDeviceInfo>()
                .in(ProjectDeviceInfo::getDeviceId, deviceIdList).select(ProjectDeviceInfo::getDeviceId, ProjectDeviceInfo::getThirdpartyCode));
        Map<String, String> thirdpartyCodeCache = deviceInfoList.stream().collect(Collectors.toMap(ProjectDeviceInfo::getDeviceId, ProjectDeviceInfo::getThirdpartyCode, (s, s2) -> s2));

        List<String> serviceIdList = deviceParamInfoVo.getServiceIdList();
        serviceIdList.add("displaySetting");
        JSONObject paramJSONObject = JSON.parseObject(deviceParamInfoVo.getParamJson(), JSONObject.class);
        List<String> removeServiceIdList = new ArrayList<>();
        paramJSONObject.forEach((key, o) -> {
            if (!serviceIdList.contains(key)) {
                removeServiceIdList.add(key);
            }
        });

        List<ParkingParamDataVo> paramDataVoList = new ArrayList<>();
        // 设置成功的参数要更新或是保存到参数信息表，这里先存到这个列表，方便后面保存
        List<ProjectDeviceParamInfo> paramInfoTemplateList = new ArrayList<>();
        paramJSONObject.forEach((key, value) -> {
            if (!removeServiceIdList.contains(key)) {
                ParkingParamDataVo paramDataVo = new ParkingParamDataVo();
                paramDataVo.setServiceId(key);
                paramDataVo.setParam(JSON.parseObject(value.toString(), JSONObject.class));
                paramDataVoList.add(paramDataVo);
                ProjectDeviceParamInfo deviceParamInfo = new ProjectDeviceParamInfo();
                JSONObject param = new JSONObject();
                param.put(key, value);
                deviceParamInfo.setDeviceParam(param.toString());
                deviceParamInfo.setServiceId(key);
                paramInfoTemplateList.add(deviceParamInfo);
            }
        });

        List<ParkingDeviceParamInfoVo> paramInfoDtoList = new ArrayList<>();

        deviceIdList.forEach(deviceId -> {
            ParkingDeviceParamInfoVo remoteParamInfo = new ParkingDeviceParamInfoVo();
            remoteParamInfo.setParamList(paramDataVoList);
            remoteParamInfo.setDeviceId(deviceId);
            remoteParamInfo.setThirdPartyCode(thirdpartyCodeCache.get(deviceId));
            paramInfoDtoList.add(remoteParamInfo);
        });
        R<DeviceParamBatchConfResultVo> confResultR = remoteParkingDeviceParamConfService.configDeviceParamBatch(paramInfoDtoList);
        DeviceParamBatchConfResultVo confResult = confResultR.getData();
        List<DeviceParamConfResultVo> resultInfoList = confResult.getResultInfoList();



        DevicesResultVo devicesResultVo = new DevicesResultVo();
        List<String> failedDeviceIdList = new ArrayList<>();
        List<String> successDeviceIdList = new ArrayList<>();
        AtomicInteger successNum = new AtomicInteger();
        AtomicInteger failedNum = new AtomicInteger();

        // 用于后面决定是新增还是更新参数数据
        List<ProjectDeviceParamInfo> deviceParamInfoList = projectDeviceParamInfoService.list(new LambdaQueryWrapper<ProjectDeviceParamInfo>()
                .in(ProjectDeviceParamInfo::getDeviceId, deviceIdList));
        Map<String, Integer> paramIdMap = deviceParamInfoList.stream().collect(Collectors.toMap(paramInfo -> paramInfo.getDeviceId() + paramInfo.getServiceId(), ProjectDeviceParamInfo::getSeq));
        List<ProjectDeviceParamInfo> successParamList = new ArrayList<>();

        resultInfoList.forEach(item -> {
            String deviceId = item.getDeviceId();
            List<String> failedServiceId = item.getFailedServiceId();
            paramDataVoList.forEach(parkingParamDataVo -> {
                if (!failedDeviceIdList.contains(parkingParamDataVo.getServiceId())) {
                    ProjectDeviceParamInfo paramInfo = new ProjectDeviceParamInfo();
                    paramInfo.setSeq(paramIdMap.get(deviceId + parkingParamDataVo.getServiceId()));
                    paramInfo.setDeviceId(deviceId);
                    paramInfo.setServiceId(parkingParamDataVo.getServiceId());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(parkingParamDataVo.getServiceId(), parkingParamDataVo.getParam());
                    paramInfo.setDeviceParam(jsonObject.toJSONString());
                    successParamList.add(paramInfo);
                }
            });
            if (CollUtil.isNotEmpty(failedServiceId)) {
                failedDeviceIdList.add(deviceId);
                failedNum.incrementAndGet();
            } else {
                successDeviceIdList.add(deviceId);
                successNum.incrementAndGet();
            }
        });
        projectDeviceParamInfoService.saveOrUpdateBatch(successParamList);
        if (CollUtil.isNotEmpty(failedDeviceIdList)) {
            List<ProjectDeviceInfo> failedDeviceList = this.list(new QueryWrapper<ProjectDeviceInfo>().lambda().in(ProjectDeviceInfo::getDeviceId, failedDeviceIdList));
            projectDeviceParamHisService.addFailedParamHis(failedDeviceList, ProjectContextHolder.getProjectId());
        } else {
            deviceIdList.removeAll(failedDeviceIdList);
            if (CollUtil.isNotEmpty(deviceIdList)) {
                // 这里判断是否是重配
                if (deviceParamInfoVo.isReset()) {
                    projectDeviceParamHisService.updateSuccessParamHis(deviceIdList, ProjectContextHolder.getProjectId());
                } else {
                    List<ProjectDeviceInfo> successDeviceInfoList = this.list(new QueryWrapper<ProjectDeviceInfo>().lambda()
                                    .in(ProjectDeviceInfo::getDeviceId, deviceIdList));
                    projectDeviceParamHisService.addSuccessParamHis(successDeviceInfoList, ProjectContextHolder.getProjectId());
                }
            }
        }
        devicesResultVo.setFailedDeviceIdList(failedDeviceIdList);
        devicesResultVo.setFailedNum(failedNum.get());
        devicesResultVo.setSuccessNum(successNum.get());
        devicesResultVo.setTotalNum(deviceIdList.size());
        List<ProjectDeviceInfo> failDeviceList = new ArrayList<>();
        if (CollUtil.isNotEmpty(failedDeviceIdList)) {
            failDeviceList = this.list(new LambdaQueryWrapper<ProjectDeviceInfo>().in(ProjectDeviceInfo::getDeviceId, failedDeviceIdList));
        }
        List<ProjectDeviceInfo> successDeviceList = new ArrayList<>();
        if (CollUtil.isNotEmpty(successDeviceIdList)) {
            successDeviceList = this.list(new LambdaQueryWrapper<ProjectDeviceInfo>().in(ProjectDeviceInfo::getDeviceId, successDeviceIdList));
        }


        projectDeviceParamHisService.addFailedParamHis(failDeviceList, ProjectContextHolder.getProjectId());
        projectDeviceParamHisService.addSuccessParamHis(successDeviceList, ProjectContextHolder.getProjectId());
        return R.ok(devicesResultVo);
    }

    @Override
    public List<SysDictItem> AlarmDeviceTypeList() {
        R<List<SysDictItem>> r = remotePigxUserService.getDictByType("device_type");
        List<SysDictItem> SysDictItemCollect = new ArrayList<>();
        if (r.getCode() == 0) {
            List<SysDictItem> deviceType =  r.getData();
            SysDictItemCollect = deviceType.stream().map(temp ->
                    "7".equals(temp.getValue()) || baseMapper.AlarmDeviceTypeList(temp.getValue()) == 0 ? null : temp
            ).filter(temp -> temp != null).collect(Collectors.toList());
        }
        return SysDictItemCollect;
    }

    @Override
    public List<String> childDeviceIdByLift(List<String> LiftIds){
        List<String> ids = new ArrayList<>();
        if(LiftIds != null){
            for (String LiftId:
                    LiftIds) {
                List<ProjectDeviceRelVo> deviceRelVoList = projectDeviceRelService.ListByDeviceIdAndDeviceType(LiftId, DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE);
                ids.addAll(deviceRelVoList.stream().map(ProjectDeviceRelVo::getDeviceId).collect(Collectors.toList()));
            }
        }
        return ids;
    }

    @Override
    public R<Boolean> callElevatorByHouseId(String houseId, String callType) {
        if(StrUtil.isBlank(houseId)){
            return R.failed("召梯失败，房屋不存在");
        }
        //默认被动召梯
        if(StrUtil.isBlank(callType)){
            callType = "1";
        }
        ProjectFrameInfo house = projectFrameInfoService.getOne(new LambdaQueryWrapper<ProjectFrameInfo>().eq(ProjectFrameInfo::getEntityId, houseId).last("limit 1"));
        ProjectHouseInfo houseInfo = projectHouseInfoService.getOne(new LambdaQueryWrapper<ProjectHouseInfo>().eq(ProjectHouseInfo::getHouseId, houseId));
        //楼层数据
        if(houseInfo.getFloor() == null){
            Integer floor = Integer.valueOf(house.getFrameNo().substring(0, 2));
            houseInfo.setFloor(floor);
        }
        List<ProjectDeviceInfo> ladderDeviceInfoList = this.list(new LambdaQueryWrapper<ProjectDeviceInfo>()
                .eq(ProjectDeviceInfo::getUnitId, house.getPuid())
                .eq(ProjectDeviceInfo::getStatus, DeviceStatusEnum.ONLINE.code)
                .eq(ProjectDeviceInfo::getDeviceType, DeviceTypeConstants.LADDER_WAY_DEVICE));
        List<ProjectDeviceInfo> projectDeviceInfoList = ladderDeviceInfoList.stream().filter(deviceInfo -> {
            if (StrUtil.isNotEmpty(deviceInfo.getDeviceCode())) {
                return deviceInfo.getDeviceCode().length() > 2;
//                    return deviceInfo.getDeviceCode().length() > 2 && "00".equals(deviceInfo.getDeviceCode().substring(deviceInfo.getDeviceCode().length() - 2));
            }
            return false;
        }).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(projectDeviceInfoList)) {
            log.info("[召梯命令下发] 下发给梯口机:{}", JSONObject.toJSONString(projectDeviceInfoList));
            for (ProjectDeviceInfo deviceInfo : projectDeviceInfoList) {
                boolean b = DeviceFactoryProducer.getFactory(deviceInfo.getDeviceId()).getDeviceService().callElevator(deviceInfo.getDeviceId(), house.getEntityCode(), houseInfo.getFloor().toString(), callType);
                if (b) {
                    return R.ok(true, "召梯命令下发成功");
                }
            }
            return R.failed("召梯命令下发失败");
        }
        return R.failed("召梯失败，未找到可进行召梯的设备");
    }


    @Override
    public void sendCertAndHouseRelation(ProjectDeviceInfo projectDeviceInfo, ProjectRightDevice projectRightDevice) {
        //员工暂时不处理
        if(projectRightDevice.getPersonType().equals(PersonTypeEnum.STAFF.code)){
            return;
        }

        String uid = UUID.randomUUID().toString().replaceAll("-", "");

        List<String> roomList = new ArrayList<>();
        List<Integer> stairNoList = new ArrayList<>();

        //凭证数组
        JSONArray passArray = new JSONArray();
        JSONObject passJSONObject = new JSONObject();
        passJSONObject.put("passType",projectRightDevice.getCertMedia().equals(CertmediaTypeEnum.Card.code) ? 1 : 3);
        passJSONObject.put("passNo",projectRightDevice.getCertMedia().equals(CertmediaTypeEnum.Card.code) ? projectRightDevice.getCertMediaInfo() : projectRightDevice.getCertMediaCode());
        passArray.add(passJSONObject);

        if(projectRightDevice.getPersonType().equals(PersonTypeEnum.PROPRIETOR.code)){
            roomList = baseMapper.getProprietorCertHouseRelationVo(projectRightDevice.getPersonId(),projectDeviceInfo.getUnitId());
            stairNoList = baseMapper.getProprietorStairNoList(projectRightDevice.getPersonId(),projectDeviceInfo.getBuildingId());

        }else if(projectRightDevice.getPersonType().equals(PersonTypeEnum.VISITOR.code)){
            roomList = baseMapper.getVisitorCertHouseRelationVo(projectRightDevice.getPersonId());
            stairNoList = baseMapper.getVisitorStairNoList(projectRightDevice.getPersonId(),projectDeviceInfo.getBuildingId());
        }
        //房屋和负楼层都为空 不做下发
        if(CollUtil.isEmpty(roomList) && CollUtil.isEmpty(stairNoList)){
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId",uid);
        jsonObject.put("passArray",passArray);
        jsonObject.put("roomNo",roomList);
        jsonObject.put("stairNo",stairNoList);

        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(DeviceTypeEnum.ELEVATOR_RECOGNIZER_DEVICE.getCode(), ProjectContextHolder.getProjectId(), 1, AurineEdgeConfigDTO.class);

        //发送给中台
        AurineEdgeRespondDTO respondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(VersionEnum.V1).objectManage(config,projectDeviceInfo.getThirdpartyCode(),projectDeviceInfo.getProductId(),jsonObject);

    }

    @Override
    public List<String> getLiftLadderList(String personId) {
        return baseMapper.getLiftLadderList(personId);
    }

    @Override
    public OpenApiProjectDeviceInfoManageDto getDeviceInfoManageDtoByDeviceId(String deviceId,String operateType) {
        OpenApiProjectDeviceInfoManageDto deviceInfoManageDto = new OpenApiProjectDeviceInfoManageDto();
        //设备信息填充
        ProjectDeviceInfo projectDeviceInfo = this.getOne(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getDeviceId, deviceId));
        if(projectDeviceInfo == null){
            log.info("[获取设备管理变更通知对象] - getDeviceInfoManageDtoByDeviceId 获取不到设备id: {} 的设备信息",deviceId);
            return null;
        }
        BeanUtil.copyProperties(projectDeviceInfo, deviceInfoManageDto);
        //查询楼栋号
        if(StrUtil.isNotBlank(projectDeviceInfo.getBuildingId())){
            ProjectFrameInfo building = projectFrameInfoService.getById(projectDeviceInfo.getBuildingId());
            if(building != null){
                deviceInfoManageDto.setBuildingNo(building.getFrameNo());
            }
        }
        //查询单元号
        if(StrUtil.isNotBlank(projectDeviceInfo.getUnitId())){
            ProjectFrameInfo unit = projectFrameInfoService.getById(projectDeviceInfo.getUnitId());
            if(unit != null){
                deviceInfoManageDto.setUnitNo(unit.getFrameNo());
            }
        }
        //查询房屋号
        if(StrUtil.isNotBlank(projectDeviceInfo.getHouseId())){
            ProjectFrameInfo house = projectFrameInfoService.getById(projectDeviceInfo.getHouseId());
            if(house != null){
                deviceInfoManageDto.setHouseCode(house.getEntityCode());
            }
        }

        deviceInfoManageDto.setOperateType(operateType);
        return deviceInfoManageDto;
    }

    @Override
    public void sendDeviceManageChangeNotice(OpenApiProjectDeviceInfoManageDto deviceInfoManageDto) {
        if (deviceInfoManageDto == null) {
            log.info("[设备管理变更通知] - sendDeviceManageChangeNotice 缺失参数,deviceInfoManageDto 为null");
            return;
        }
        log.info("[设备管理变更通知] - sendDeviceManageChangeNotice 通知对象:{}",JSONObject.toJSONString(deviceInfoManageDto));
        sendMsg(TopicConstant.OPEN_V2_DEVICE_MANGE, deviceInfoManageDto);
    }

    @Override
    public ProjectDeviceInfo getDriverByDeviceName(String deviceName) {
        return this.lambdaQuery().eq(ProjectDeviceInfo::getDeviceName,deviceName).eq(ProjectDeviceInfo::getDeviceType,DeviceTypeConstants.DEVICE_DRIVER).one();
    }

    /**
     * 发送消息到kafka
     *
     * @param topic   主题
     * @param message 内容体
     */
    private <T> void sendMsg(String topic, T message) {
        kafkaTemplate.send(topic, JSONObject.toJSONString(message));
    }
}
