package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.common.core.util.ObjectMapperUtil;
import com.aurine.cloudx.open.common.entity.vo.DeviceInfoVo;
import com.aurine.cloudx.open.origin.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.open.origin.constant.enums.DeviceReplaceResultEnum;
import com.aurine.cloudx.open.origin.constant.enums.DeviceStatusEnum;
import com.aurine.cloudx.open.origin.constant.enums.PerimeterStatusEnum;
import com.aurine.cloudx.open.origin.dto.ProjectParkingDeviceInfoDto;
import com.aurine.cloudx.open.origin.entity.*;
import com.aurine.cloudx.open.origin.mapper.ProjectDeviceInfoMapper;
import com.aurine.cloudx.open.origin.service.*;
import com.aurine.cloudx.open.origin.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.minio.service.MinioTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
public class ProjectDeviceInfoServiceImpl extends ServiceImpl<ProjectDeviceInfoMapper, ProjectDeviceInfo> implements ProjectDeviceInfoService {

    private final static String CAMERA_TYPE = "6";
    private static final String HOME_ROOT = "0";
    private static ObjectMapper objectMapper = ObjectMapperUtil.instance();
    private static Map<String, List<ProjectDeviceParamInfo>> deviceParamInfoMap = new HashMap<>();

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
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
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
    //    @Resource
//    private DdDeviceMapService ddDeviceMapService;
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


    @Value("${aliyun.vsc.accessKeyId:}")
    private String accessKeyId;
    @Value("${aliyun.vsc.accessSecret:}")
    private String accessSecret;
    @Value("${uniview.vsc.accessKeyId:25645212521}")
    private Long appId;
    @Value("${uniview.vsc.accessSecret:65a4f27ee572d23e3e977e19a341708f}")
    private String secretKey;


    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

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
            case DeviceTypeConstants.ALARM_HOST:
                return baseMapper.pageAlarmVo(page, projectDeviceInfoPageFormVo);
            case DeviceTypeConstants.SMART_WATER_METER:
            case DeviceTypeConstants.LEVEL_GAUGE:
            case DeviceTypeConstants.SMOKE:
            case DeviceTypeConstants.SMART_MANHOLE_COVER:
            case DeviceTypeConstants.SMART_STREET_LIGHT:
            case DeviceTypeConstants.DEVICE_DRIVER:
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
            queryWrapper.and(query -> query.like(ProjectDeviceInfo::getDeviceName, projectDeviceInfoFormVo.getDeviceName()).or().like(ProjectDeviceInfo::getDeviceDesc, projectDeviceInfoFormVo.getDeviceName()));
        }
        queryWrapper.ne(ProjectDeviceInfo::getStatus, "4");
        return baseMapper.selectList(queryWrapper);
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

//    @Override
//    public R<String> getLiveUrl(String deviceId) {
//        List<ProjectDeviceAttrListVo> attrs =
//                projectDeviceAttrService.getDeviceAttrListVo(ProjectContextHolder.getProjectId(), CAMERA_TYPE, deviceId);
//        String corpId = "";
//        String gbId = "";
//        String regionId = "";
//
//        for (ProjectDeviceAttrListVo attr : attrs) {
//            switch (attr.getAttrCode()) {
//                case "corpId":
//                    corpId = attr.getAttrValue();
//                    break;
//                case "gbId":
//                    gbId = attr.getAttrValue();
//                    break;
//                case "regionId":
//                    regionId = attr.getAttrValue();
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        // 获取地址 - 阿里云
////        return getDeviceLiveUrl(regionId, corpId, gbId);
//        // 获取地址 - 宇视
//        String liveUrl;
//        try {
//            liveUrl = getUniviewLiveUrl(corpId, Integer.parseInt(gbId));
//        } catch (NumberFormatException e1) {
//            e1.printStackTrace();
//            log.error("设备配置参数异常，或未配置");
//            return R.ok(null, "设备配置参数异常，或未配置");
////            throw new NumberFormatException("设备配置参数异常，或未配置");
//        } catch (Exception e2) {
//            e2.printStackTrace();
//            throw new RuntimeException(e2.getMessage(), e2.getCause());
//        }
//        return R.ok(liveUrl.replace("http://", "https://"));
//    }
//
//    @Override
//    public String getVideoUrl(String deviceId, Long startTime, Long endTime) {
//        List<ProjectDeviceAttrListVo> attrs =
//                projectDeviceAttrService.getDeviceAttrListVo(ProjectContextHolder.getProjectId(), CAMERA_TYPE, deviceId);
//
//        String corpId = "";
//        String gbId = "";
//        String regionId = "";
//
//        for (ProjectDeviceAttrListVo attr : attrs) {
//            switch (attr.getAttrCode()) {
//                case "corpId":
//                    corpId = attr.getAttrValue();
//                    break;
//                case "gbId":
//                    gbId = attr.getAttrValue();
//                    break;
//                case "regionId":
//                    regionId = attr.getAttrValue();
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        // 获取地址 - 阿里云
////        return getDeviceVideoUrl(regionId, corpId, gbId, startTime, endTime);
//        // 获取地址 - 宇视
//        String liveUrl;
//        try {
//            liveUrl = getUniviewRecordUrl(corpId, Integer.parseInt(gbId), startTime / 1000, endTime / 1000);
//        } catch (NumberFormatException e1) {
//            e1.printStackTrace();
//            throw new NumberFormatException("设备配置参数异常，或未配置");
//        } catch (Exception e2) {
//            e2.printStackTrace();
//            throw new RuntimeException(e2.getMessage(), e2.getCause());
//        }
//
//        return liveUrl.replace("http://", "https://");
//    }
//
//    /**
//     * 获取直播地址，后期做成工具包
//     *
//     * @param regionId
//     * @param corpId
//     * @param gbId
//     * @return
//     */
//    private String getDeviceLiveUrl(String regionId, String corpId, String gbId) {
//        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessSecret);
//        IAcsClient client = new DefaultAcsClient(profile);
//
//        GetDeviceLiveUrlRequest request = new GetDeviceLiveUrlRequest();
//        request.setRegionId(regionId);
//        request.setCorpId(corpId);
//        request.setGbId(gbId);
//        request.setOutProtocol("httpshls");
//
//        try {
//            GetDeviceLiveUrlResponse response = client.getAcsResponse(request);
//
//            return response.getUrl();
//        } catch (ServerException e) {
//            e.printStackTrace();
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
//
//        return "";
//    }
//
//    private String getDeviceVideoUrl(String regionId, String corpId, String gbId, Long startTime, Long endTime) {
//        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessSecret);
//        IAcsClient client = new DefaultAcsClient(profile);
//
//        GetDeviceVideoUrlRequest request = new GetDeviceVideoUrlRequest();
//        request.setRegionId(regionId);
//        request.setCorpId(corpId);
//        request.setGbId(gbId);
//        request.setStartTime(startTime);
//        request.setEndTime(endTime);
//        request.setOutProtocol("httpshls");
//
//        try {
//            GetDeviceVideoUrlResponse response = client.getAcsResponse(request);
//
//            return response.getUrl();
//        } catch (ServerException e) {
//            e.printStackTrace();
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
//
//        return "";
//    }

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
    public Integer countBySn(String sn, String deviceId) {
        return baseMapper.countBySn(sn, deviceId);
    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void importExcel(MultipartFile file, DeviceExcelEnum deviceExcelEnum, Integer projectId) throws IOException {
//        ProjectDeviceInfoServiceImpl projectDeviceInfoService = this;
//        // 用于生成导入日志明细数据的输入流
//        InputStream inputStreamInitDetail = file.getInputStream();
//        // 用于实际进行导入的输入流
//        InputStream inputStreamImportData = file.getInputStream();
//        threadPoolTaskExecutor.execute(() -> {
//            LocalDateTime beginTime = LocalDateTime.now();
//            ProjectContextHolder.setProjectId(projectId);
//            //获取楼栋单元房间列表 xull@aurine.cn 2020/5/25 11:03
//            List<ProjectFrameInfo> projectBuildingInfos = projectFrameInfoService.list(
//                    Wrappers.lambdaQuery(ProjectFrameInfo.class).le(ProjectFrameInfo::getLevel, 3));
//            List<ProjectDeviceRegionTreeVo> projectDeviceRegions = projectDeviceRegionService.findTree(null);
//
//            List<ProjectFrameInfoTreeVo> tree = projectFrameInfoService.findTree("小区");
//            ExcelResultVo excelResultVo = new ExcelResultVo();
//
//            File tmpDir = null;
//            File excelFile = null;
//            String batchId = UUID.randomUUID().toString().replaceAll("-", "");
//            ProjectDeviceLoadLog deviceLoadLog = new ProjectDeviceLoadLog(batchId, deviceExcelEnum.getCode(), LocalDateTime.now());
//            try {
//                projectDeviceLoadLogService.save(deviceLoadLog);
//                List<ProjectDeviceLoadLogDetail> deviceLoadLogDetailList = new ArrayList<>();
//                EasyExcel.read(inputStreamInitDetail, deviceExcelEnum.getClazz(),
//                        new ProjectDeviceInfoInitListener(deviceLoadLog.getBatchId(), deviceLoadLogDetailList)).sheet().doRead();
//                projectDeviceLoadLogDetailService.saveBatch(deviceLoadLogDetailList);
//
//                EasyExcel.read(inputStreamImportData, deviceExcelEnum.getClazz(), new ProjectDeviceInfoListener(projectDeviceInfoService, projectDeviceLoadLogDetailService, redisTemplate, deviceExcelEnum, batchId)).sheet().doRead();
//
//                // 这里准备生成导入失败的Excel文件
//                String fileName = "失败名单-" + deviceExcelEnum.getName();
//                String fileDirPath = "temporary/" + UUID.randomUUID().toString().replaceAll("-", "");
//                tmpDir = new File(fileDirPath);
//                // 如果没有这个文件夹则新建一个
//                if (!tmpDir.exists() || tmpDir.isFile()) {
//                    tmpDir.mkdirs();
//                }
//                excelFile = new File(fileDirPath + "/" + fileName);
//                excelFile.createNewFile();
//
//                String dataString = redisTemplate.opsForValue().get(deviceLoadLog.getBatchId());
//
//                List data = JSONUtil.toList(JSONUtil.parseArray(dataString), deviceExcelEnum.getClazz());
//                String excelPath = DeviceExcelConstant.XLSX_PATH + deviceExcelEnum.getName();
//                ClassPathResource classPathResource = new ClassPathResource(excelPath);
//                EasyExcel.write(new FileOutputStream(excelFile), deviceExcelEnum.getClazz())
//                        .withTemplate(classPathResource.getStream())
//                        .sheet(0).doFill(data);
//
//                ProjectInfo projectInfo = projectInfoService.getOne(new LambdaQueryWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId,
//                        ProjectContextHolder.getProjectId()));
//                String objectName = String.format("%s/%s/%s/%s/%s/%s", TenantContextHolder.getTenantId(), projectInfo.getCompanyId(),
//                        projectInfo.getProjectGroupId(),
//                        projectInfo.getProjectId(), UUID.randomUUID().toString().replaceAll("-", ""), fileName);
//
//                // 这样写就不需要手动关闭文件流（否则临时文件无法删除）
//                try (FileInputStream fileInputStream = new FileInputStream(excelFile)) {
//                    minioTemplate.putObject(ExcelMinIOConstant.BUCKET_NAME, objectName, fileInputStream);
//                    String objectURL = minioTemplate.getObjectURL(ExcelMinIOConstant.BUCKET_NAME, objectName, ExcelMinIOConstant.EXPIRES_ONE_DAY);
//                    log.info("[设备导入] MinIO桶名:[{}] MinIO对象名:[{}] 临时分享链接:[{}]", ExcelMinIOConstant.BUCKET_NAME, objectName, objectURL);
//
//                    deviceLoadLog.setOrginFile(objectName);
//                    deviceLoadLog.setTempFile(objectURL);
//                    deviceLoadLog.setLoadTime(beginTime);
//                    deviceLoadLog.setDevCount(deviceLoadLogDetailList.size());
//                    projectDeviceLoadLogService.updateById(deviceLoadLog);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    log.error("导入失败模板保存失败");
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                projectDeviceLoadLogDetailService.updateToFailed(deviceLoadLog.getBatchId(), "文件读取异常");
//            } catch (Exception e) {
//                e.printStackTrace();
//                projectDeviceLoadLogDetailService.updateToFailed(deviceLoadLog.getBatchId(), e.getMessage());
//            } finally {
//                log.info("[设备导入] 删除临时文件");
//                if (excelFile != null && excelFile.exists()) {
//                    excelFile.delete();
//                }
//                if (tmpDir != null && tmpDir.exists()) {
//                    tmpDir.delete();
//                }
//            }
//            //增值服务设置
//            List<String> ids = excelResultVo.getList();
//            if (CollUtil.isNotEmpty(ids)) {
//                List<ProjectServiceInfoVo> projectServiceInfoVos = projectServiceService.getHouseServiceByProjectId(ProjectContextHolder.getProjectId());
//
//                projectServiceInfoVos.forEach(projectService -> {
//                    ids.forEach(id -> {
//                        IntercomFactoryProducer.getFactory(projectService.getServiceCode()).getIntercomService()
//                                .addDevice(id, ProjectContextHolder.getProjectId());
//                    });
//                });
//            }
//        });
//    }
//
//    @Override
//    public void errorExcel(Integer projectId, String batchId, HttpServletResponse httpServletResponse) throws IOException {
//        /*String dataString = redisTemplate.opsForValue().get(name);
//        String[] keys = name.split("-");
//        DeviceExcelEnum deviceExcelEnum = DeviceExcelEnum.getEnum(keys[0], keys[1]);
//        List data = JSONUtil.toList(JSONUtil.parseArray(dataString), deviceExcelEnum.getClazz());
//        String excelPath = DeviceExcelConstant.XLSX_PATH + deviceExcelEnum.getName();
//        ClassPathResource classPathResource = new ClassPathResource(excelPath);
//        httpServletResponse.setCharacterEncoding("UTF-8");
//        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//        EasyExcel.write(httpServletResponse.getOutputStream(), deviceExcelEnum.getClazz())
//                .withTemplate(classPathResource.getStream())
//                .sheet(0).doFill(data);*/
//        ProjectContextHolder.setProjectId(projectId);
//        ProjectDeviceLoadLog loadLog = projectDeviceLoadLogService.getOne(new LambdaQueryWrapper<ProjectDeviceLoadLog>().eq(ProjectDeviceLoadLog::getBatchId, batchId));
//        InputStream object = minioTemplate.getObject(ExcelMinIOConstant.BUCKET_NAME, loadLog.getOrginFile());
//        httpServletResponse.setCharacterEncoding("UTF-8");
//        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//        IOUtils.copy(object, httpServletResponse.getOutputStream());
//
//    }


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
        // 这里先删除这台设备原有的参数数据（如果有的话）
        projectDeviceParamInfoService.remove(new QueryWrapper<ProjectDeviceParamInfo>().lambda().eq(ProjectDeviceParamInfo::getDeviceId, deviceId));
//        ProjectDeviceInfo deviceInfo = this.getById(deviceId);
//
//        List<SysProductService> productServiceList = sysProductServiceService.list(new QueryWrapper<SysProductService>().lambda()
//                .eq(SysProductService::getProductId, deviceInfo.getProductId()));
//        if (CollUtil.isNotEmpty(productServiceList)) {
//            List<SysServiceParamConf> existServiceList = sysServiceParamConfService.list(new QueryWrapper<SysServiceParamConf>().lambda()
//                    .in(SysServiceParamConf::getServiceId, productServiceList.stream().map(SysProductService::getServiceId).collect(Collectors.toSet())));
//
//            if (CollUtil.isNotEmpty(existServiceList)) {
//                Set<String> serviceIdSet = existServiceList.stream().map(SysServiceParamConf::getServiceId).collect(Collectors.toSet());
//                DeviceParamServiceFactory factory = DeviceParamFactoryProducer.getFactory(deviceId);
//                if (factory != null) {
//                    SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>()
//                            .eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
//                    DeviceParamService deviceParamService = factory.getInstance(productMap.getManufacture(), deviceInfo.getDeviceType());
//                    if (deviceParamService != null) {
//                        deviceParamService.requestDeviceParam(serviceIdSet, deviceId);
//                    } else {
//                        log.warn("未获取到当前设备参数服务 deviceId：{}", deviceId);
//                    }
//                } else {
//                    log.warn("当前设备类型未对接中台 deviceId：{}", deviceId);
//                }
//                  /*  // 这里获取到的是设备可以进行获取的参数数据
//                    Set<String> existServiceIdSet = existServiceList.stream().map(SysServiceParamConf::getServiceId).collect(Collectors.toSet());
//                    existServiceIdSet.forEach(serviceId -> {
////                    int count = sysServiceParamConfService.count(new QueryWrapper<SysServiceParamConf>().lambda().eq(SysServiceParamConf::getServiceId, serviceId));
//                        // 这里是设备回调参数数据而不是直接同步获取
//                        DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId()).getDeviceService()
//                                .getDeviceParam(DeviceParamEnum.getObjName(serviceId), deviceInfo.getThirdpartyCode(), deviceInfo.getDeviceId());
//                    });*/
//            }
//            // 获取设备信息
//               /* DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(),
//                        ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId()).getDeviceService()
//                        .getDeviceInfo(deviceId, deviceInfo.getThirdpartyCode());*/
//        }
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
//        String thirdCode = DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(),
//                ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId())
//                .getDeviceService().addDevice(deviceInfoProxyVo);
//        if (StringUtils.isNotEmpty(thirdCode)) {
//            deviceInfo.setThirdpartyCode(thirdCode);
//            deviceInfo.setProductId(deviceInfoProxyVo.getProductId());
//        }

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
//            try {
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


//                        // 这里进行设备参数（旧设备参数下发到新设备）下发
//                        JsonNode jsonNode = objectMapper.readTree(paramData);
//                        List<ProjectDeviceParamSetResultVo> resultVoList = this.setDeviceParam((ObjectNode) jsonNode, deviceId);
//                        boolean isFailed = this.handleDeviceParamSetResult(resultVoList, false);
//                        if (isFailed) {
//                            // 这里返回的是参数设置失败（已经替换成功但是同步参数的时候失败了）
//                            resultEnum = DeviceReplaceResultEnum.PARAM_SETTING_FILED;
//                        } else {
//                            resultEnum = DeviceReplaceResultEnum.REPLACE_SUCCESS;
//                        }
                } else {
                    // 这里返回的是替换设备后新设备的产品ID和原设备不一致（无法同步旧设备参数设置，这里要获取到新设备的参数数据替换原有旧设备的参数数据）
                    // 同时通知前端需要专门对新设备进行设置
                    // 获取新设备的参数数据（会先删除原有旧设备的参数数据）
                    this.initDeviceParamData(deviceId);
                    resultEnum = DeviceReplaceResultEnum.PRODUCT_ID_CHANGE;
                }
            }
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//                // 这里如果出现异常则重新绑定回原来的设备
//                this.bindNewDeviceAndUpdate(deviceInfo, oldSn);
//                // 旧设备因为清空了介质这里需要重新下载回去
//                this.redownloadCertByDeviceId(deviceId);
//            }
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
//        // 这里根据项目ID获取，因为是新的设备用旧设备ID获取不知道会不会有问题
//        // 这里先绑定新的设备再解绑旧的设备
//        DeviceService deviceService = DeviceFactoryProducer.getFactory(ProjectContextHolder.getProjectId()).getDeviceService();
//        String thirdpartyCode = deviceService.addDevice(deviceInfoProxyVo);
//
//        if (StrUtil.isNotEmpty(thirdpartyCode)) {
//            deviceInfo.setThirdpartyCode(thirdpartyCode);
//            deviceInfo.setSn(deviceInfoProxyVo.getSn());
//            deviceInfo.setProductId(deviceInfoProxyVo.getProductId());
//            // 这里绑定成功之后更新设备信息
//            return this.updateById(deviceInfo);
//        } else {
//            log.error("未获取到设备第三方编码");
//        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ProjectDeviceInfoVo saveDeviceVo(ProjectDeviceInfoVo deviceInfo) {
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
        String thirdCode = "";
        switch (deviceInfo.getDeviceType()) {
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
            case DeviceTypeConstants.MONITOR_DEVICE:
                //TODO 监控设备新增接口对接
            case DeviceTypeConstants.ALARM_HOST:
                //TODO 报警主机新增接口对接
//                thirdCode = DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(),
//                        TenantContextHolder.getTenantId()).getDeviceService().addDevice(deviceInfoProxyVo);
                break;
            case DeviceTypeConstants.SMART_WATER_METER:
                //TODO 智能水表新增接口对接
            case DeviceTypeConstants.LEVEL_GAUGE:
                //TODO 液位计新增接口对接
            case DeviceTypeConstants.SMOKE:
                //TODO 烟感新增接口对接
            case DeviceTypeConstants.SMART_MANHOLE_COVER:
                //TODO 智能井盖新增接口对接
            case DeviceTypeConstants.SMART_STREET_LIGHT:
                //TODO 智能路灯新增接口对接
//                SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>().eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()).last("limit 1"));
//                thirdCode = DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(),
//                        TenantContextHolder.getTenantId()).getDeviceService().addDevice(deviceInfoProxyVo, productMap.getProductCode());
                break;
            default:
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
            Integer deviceNum = projectDeviceInfoProxyService.countThirdPartyCode(thirdCode);
            if (deviceNum != 0) {
                throw new RuntimeException("当前设备编号" + thirdCode + "已存在");
            }
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
        if (deviceInfo.getDeviceType().equals(DeviceTypeConstants.SMART_STREET_LIGHT)) {
            String deviceId = UUID.randomUUID().toString().replaceAll("-", "");
            ProjectDeviceAttr projectDeviceAttr = new ProjectDeviceAttr();
            projectDeviceAttr.setProjectId(ProjectContextHolder.getProjectId());
            projectDeviceAttr.setDeviceId(deviceId);
            projectDeviceAttr.setDeviceType(DeviceTypeConstants.SMART_STREET_LIGHT);
            projectDeviceAttr.setAttrId(deviceInfo.getDeviceId());
            projectDeviceAttr.setAttrCode("controller");
            projectDeviceAttr.setAttrValue(deviceInfo.getController());
            projectDeviceAttrService.save(projectDeviceAttr);
            if (StringUtils.isNotEmpty(deviceInfo.getRoute())) {
                ProjectDeviceAttr route = new ProjectDeviceAttr();
                BeanUtils.copyProperties(projectDeviceAttr, route);
                route.setAttrCode("route");
                route.setAttrValue(deviceInfo.getRoute());
                projectDeviceAttrService.save(route);
            }
        }
//        //当前项目开启的增值服务列表
//        List<ProjectServiceInfoVo> projectServiceInfoVos = projectServiceService.getHouseServiceByProjectId(ProjectContextHolder.getProjectId());
//
//        projectServiceInfoVos.forEach(e -> {
//            if (StringUtils.equals(e.getServiceType(), "YDJ")) {//如果服务类型未云对讲
//                //当设备类型为梯口机、区口时调用云对讲接口
//                if (StringUtils.equals(deviceInfo.getDeviceType(), DeviceTypeEnum.GATE_DEVICE.getCode()) || StringUtils.equals(deviceInfo.getDeviceType(), DeviceTypeEnum.LADDER_WAY_DEVICE.getCode())) {
//                    IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().addDevice(deviceInfo.getDeviceId(), ProjectContextHolder.getProjectId());
//                }
//            }
//        });
        return deviceInfo;
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
        // 编辑设备拓展属性  xull@aurine.cn 2020年7月7日 13点53分
        ProjectDeviceAttrFormVo projectDeviceAttrFormVo = new ProjectDeviceAttrFormVo();
        projectDeviceAttrFormVo.setProjectDeviceAttrList(deviceInfoVo.getDeviceAttrs());
        projectDeviceAttrFormVo.setProjectId(ProjectContextHolder.getProjectId());
        projectDeviceAttrFormVo.setType(deviceInfoVo.getDeviceType());
        projectDeviceAttrFormVo.setDeviceId(deviceInfoVo.getDeviceId());
        projectDeviceAttrService.updateDeviceAttrList(projectDeviceAttrFormVo);


        if (StringUtils.isNotEmpty(deviceInfoVo.getController())) {
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
        }

        //保存设备参数修改记录
        ProjectDeviceInfo projectDeviceInfo = new ProjectDeviceInfo();
        BeanUtils.copyProperties(deviceInfoVo, projectDeviceInfo);
        projectDeviceModifyLogService.saveDeviceModifyLog(deviceInfoVo.getDeviceId(), projectDeviceInfo);


        /**
         * 设备更新时将数据发送给硬件
         * @author: 王伟
         * @since 2020-11-26 18:02
         */
        ProjectDeviceInfoProxyVo deviceInfoProxyVo = new ProjectDeviceInfoProxyVo();
        BeanUtils.copyProperties(deviceInfoVo, deviceInfoProxyVo);
        deviceInfoProxyVo.setProjectId(ProjectContextHolder.getProjectId());
        deviceInfoProxyVo.setTenantId(TenantContextHolder.getTenantId());

//        DeviceFactoryProducer.getFactory(deviceInfoVo.getDeviceId()).getDeviceService().updateDevice(deviceInfoProxyVo);
        return this.updateById(deviceInfoVo);
    }

//    @Override
//    public List<ProjectDeviceParamSetResultVo> setDeviceParam(ObjectNode paramsNode, String deviceId) {
//        ProjectDeviceInfo deviceInfo = this.getById(deviceId);
//        SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>()
//                .eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
//        DeviceParamServiceFactory paramServiceFactory = DeviceParamFactoryProducer.getFactory(deviceInfo.getProjectId(), deviceInfo.getDeviceType());
//        if (paramServiceFactory == null) {
//            throw new RuntimeException("该类型设备未配置对接中台");
//        }
//        DeviceParamService paramService = paramServiceFactory.getInstance(productMap.getManufacture(), deviceInfo.getDeviceType());
//        return paramService.setParamSingleDevice(paramsNode, deviceId);
//
//    }
//
//
//    @Override
//    public DevicesResultVo setDevicesParam(ObjectNode paramsNode, List<String> deviceIdList, List<String> serviceIdList) {
//        ProjectDeviceInfo deviceInfo = this.getById(deviceIdList.get(0));
//        SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>()
//                .eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
//        DeviceParamServiceFactory paramServiceFactory = DeviceParamFactoryProducer.getFactory(deviceInfo.getProjectId(), deviceInfo.getDeviceType());
//        if (paramServiceFactory == null) {
//            throw new RuntimeException("该类型设备未配置对接中台");
//        }
//        DeviceParamService paramService = paramServiceFactory.getInstance(productMap.getManufacture(), deviceInfo.getDeviceType());
//        return paramService.setParamMultiDevice(paramsNode, deviceIdList, serviceIdList);
//    }


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
                remove(Wrappers.lambdaQuery(ProjectDeviceInfo.class).eq(ProjectDeviceInfo::getDeviceId, infoUid));
            }
        }


        // 删除设备属性拓展 xull@aurine.cn 2020年7月7日 13点59分
        projectDeviceAttrService.remove(Wrappers.lambdaUpdate(ProjectDeviceAttr.class)
                .eq(ProjectDeviceAttr::getDeviceId, id)
                .eq(ProjectDeviceAttr::getProjectId, ProjectContextHolder.getProjectId()));
        /**
         * 区口机，梯口机 删除后，变更添加人员权限信息
         * 王伟 2020-06-19
         */
        projectPersonDeviceService.refreshDeleteDevice(id);

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

//        /**
//         * 调用第三方接口处理
//         * @author: 王伟
//         * @since :2020-08-17
//         */
//        boolean delResult = DeviceFactoryProducer.getFactory(id).getDeviceService().delDevice(id);
//        ProjectDeviceInfo deviceInfo = this.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getDeviceId, id));
//
//        // 只有解绑成功了并且从设备组中删除才能从系统中删除设备
//        if (delResult) {
//
//            List<ProjectServiceInfoVo> projectServiceInfoVos = projectServiceService.getHouseServiceByProjectId(ProjectContextHolder.getProjectId());
//            projectServiceInfoVos.forEach(e -> {
//                IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().delDevice(id);
//            });
//            try {
//                this.rebootDeviceById(id);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            this.removeById(id);
//            return true;
//        }
        return false;
    }

//    private void rebootDeviceById(String deviceId) {
//        List<ProjectDeviceInfo> deviceInfoList = this.list(new QueryWrapper<ProjectDeviceInfo>().lambda()
//                .eq(ProjectDeviceInfo::getDeviceId, deviceId));
//        if (CollUtil.isNotEmpty(deviceInfoList)) {
//            ProjectDeviceInfo deviceInfo = deviceInfoList.get(0);
//            DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(),
//                    TenantContextHolder.getTenantId()).getDeviceService().reboot(deviceId);
//        }
//    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean removeAll(List<String> ids) {
        //批量删除设备相关附加属性 xull@aurine.cn 2020年7月8日 11点57分
        projectDeviceAttrService.remove(Wrappers.lambdaUpdate(ProjectDeviceAttr.class)
                .eq(ProjectDeviceAttr::getProjectId, ProjectContextHolder.getProjectId())
                .in(ProjectDeviceAttr::getDeviceId, ids));
        //批量更新人员权限信息 xull@aurine.cn 2020年7月8日 11点58分
        projectPersonDeviceService.refreshDeleteDeviceAll(ids);

//        List<ProjectServiceInfoVo> projectServiceInfoVos = projectServiceService.getHouseServiceByProjectId(ProjectContextHolder.getProjectId());
//        projectServiceInfoVos.forEach(projectService -> {
//            ids.forEach(id -> {
//                IntercomFactoryProducer.getFactory(projectService.getServiceCode()).getIntercomService().delDevice(id);
//            });
//        });

        // 这里将设备移出设备组
        boolean removeResult = true;

        //批量删除设备 xull@aurine 2020年7月8日 11点58分
        return this.removeByIds(ids);
    }


//    @Override
//    public void modelExcel(String code, HttpServletResponse httpServletResponse, Integer projectId) throws IOException {
//        String policeStatus = "0";
//        //获取公安对接接口判断是否启用
//        List<ProjectDeviceCollectListVo> ProjectDeviceCollectListVos = projectDeviceCollectService
//                .getDeviceCollectListVo(projectId, DeviceCollectTypeEnum.POLICE.code, DeviceCollectConstant.POLICE_PARAM_NAME);
//
//        if (ProjectDeviceCollectListVos != null && ProjectDeviceCollectListVos.size() > 0) {
//            policeStatus = ProjectDeviceCollectListVos.get(0).getAttrValue();
//            if (StringUtils.isBlank(policeStatus)) {
//                policeStatus = "0";
//            }
//        }
//        DeviceExcelEnum deviceExcelEnum = DeviceExcelEnum.getEnum(code, policeStatus);
//        List data = Lists.newArrayList();
//        String excelPath = DeviceExcelConstant.XLSX_PATH + deviceExcelEnum.getName();
//        ClassPathResource classPathResource = new ClassPathResource(excelPath);
//        httpServletResponse.setCharacterEncoding("UTF-8");
//        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//        String fileName = deviceExcelEnum.getName();
//        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
//        EasyExcel.write(httpServletResponse.getOutputStream(), deviceExcelEnum.getClazz()).withTemplate(classPathResource.getStream()).sheet(0).doFill(data);
//
//    }

//    /**
//     * 获取宇视直播视频
//     *
//     * @param deviceSerial
//     * @param channelNo
//     * @return
//     */
//    private String getUniviewLiveUrl(String deviceSerial, Integer channelNo) throws Exception {
//        UniviewVcsClient client = null;
//        try {
//            client = UniviewVcsClient.getInstance(appId, secretKey);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        GetLiveUrlVo getLiveUrlVo = new GetLiveUrlVo();
//        getLiveUrlVo.setDeviceSerial(deviceSerial);
//        getLiveUrlVo.setChannelNo(channelNo);
//        getLiveUrlVo.setStreamIndex(0);
//        getLiveUrlVo.setType(GetVideoResponse.STREAM_TYPE_HLS);
//
//        return client.getLiveUrl(getLiveUrlVo);
//    }
//
//    /**
//     * 获取宇视回放视频
//     *
//     * @param deviceSerial
//     * @param channelNo
//     * @return
//     */
//    private String getUniviewRecordUrl(String deviceSerial, Integer channelNo, Long startTime, Long endTime) throws Exception {
//        UniviewVcsClient client = null;
//        try {
//            client = UniviewVcsClient.getInstance(appId, secretKey);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        GetRecordUrlVo getRecordUrlVo = new GetRecordUrlVo();
//        getRecordUrlVo.setDeviceSerial(deviceSerial);
//        getRecordUrlVo.setChannelNo(channelNo);
//        getRecordUrlVo.setStreamIndex(0);
//        getRecordUrlVo.setType(GetVideoResponse.STREAM_TYPE_HLS);
//        getRecordUrlVo.setStartTime(startTime);
//        getRecordUrlVo.setEndTime(endTime);
//        getRecordUrlVo.setRecordTypes(0);
//
//        return client.getRecordUrl(getRecordUrlVo);
//    }

//    @Override
//    public List<ProjectDeviceInfo> getDdDeviceList() {
//        return ddDeviceMapService.getDdDeviceList();
//    }

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
        List<ProjectPerimeterAlarmArea> list = projectPerimeterAlarmAreaService.list(new QueryWrapper<ProjectPerimeterAlarmArea>().lambda()
                .eq(ProjectPerimeterAlarmArea::getDeviceId, deviceId));
        for (ProjectPerimeterAlarmArea projectPerimeterAlarmArea : list) {
            if (projectPerimeterAlarmArea != null) {
                if (PerimeterStatusEnum.DISARM.getName().equals(name)) {
                    projectPerimeterAlarmArea.setArmedStatus(PerimeterStatusEnum.DISARM.getType());
//                    boolean bool = DeviceFactoryProducer.getFactory(deviceId).getPerimeterAlarmDeviceService().channelRemoval(projectPerimeterAlarmArea);
//                    if (!bool) {
//                        return bool;
//                    }
                }
                if (PerimeterStatusEnum.ARMED.getName().equals(name)) {
                    projectPerimeterAlarmArea.setArmedStatus(PerimeterStatusEnum.ARMED.getType());
//                    boolean bool = DeviceFactoryProducer.getFactory(deviceId).getPerimeterAlarmDeviceService().channelProtection(projectPerimeterAlarmArea);
//                    if (!bool) {
//                        return bool;
//                    }
                }
                projectPerimeterAlarmAreaService.updateById(projectPerimeterAlarmArea);
            }
        }
        return true;
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
        return baseMapper.getDeviceBrand(deviceTypeId);
    }

//    /**
//     * 智能路灯添加控制器IMEL
//     *
//     * @param vo
//     * @return
//     */
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public boolean saveLightingIMEL(ProjectDeviceLightIMELVo vo) {
//
//        ProjectDeviceAttr projectDeviceAttr = projectDeviceAttrService.getOne(Wrappers.lambdaQuery(ProjectDeviceAttr.class)
//                .eq(ProjectDeviceAttr::getAttrCode, "sn")
//                .eq(ProjectDeviceAttr::getAttrValue, vo.getSn()));
//
//        if (projectDeviceAttr != null) {
//            throw new RuntimeException("已存在IMEI");
//        }
//        boolean bool = false;
//        String attrId = UUID.randomUUID().toString().replaceAll("-", "");
//        String deviceId = UUID.randomUUID().toString().replaceAll("-", "");
//        Field[] fields = vo.getClass().getDeclaredFields();
//        List<String> list = new ArrayList<>();
//        for (Field field : fields) {
//            list.add(field.getName());
//        }
//        if (StringUtils.isEmpty(vo.getFrequency())) {
//            list.remove("frequency");
//        }
//        for (String code : list) {
//            try {
//                ProjectDeviceAttr attr = new ProjectDeviceAttr();
//                attr.setAttrCode(code);
//                attr.setAttrId(attrId);
//                attr.setDeviceId(deviceId);
//                attr.setProjectId(ProjectContextHolder.getProjectId());
//                attr.setDeviceType(DeviceTypeConstants.SMART_STREET_LIGHT);
//
//                String firstLetter = code.substring(0, 1).toUpperCase();
//                String getter = "get" + firstLetter + code.substring(1);
//                Method method = vo.getClass().getMethod(getter);
//                String value = method.invoke(vo).toString();
//
//                attr.setAttrValue(value);
//                bool = projectDeviceAttrService.save(attr);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return bool;
//    }
//
//
//    /**
//     * 查询控制器IMEL
//     *
//     * @param deviceType
//     * @return
//     */
//    @Override
//    public List<ProjectDeviceProductNameVo> getControllerIMEL(String deviceType) {
//        return baseMapper.getControllerIMEL(deviceType);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean streetLightControl(StreetLightDeviceStatus deviceStatus, Integer projectId) {
//        ProjectContextHolder.setProjectId(projectId);
//        List<String> successDeviceIdList = new ArrayList<>();
//        if (CollUtil.isNotEmpty(deviceStatus.getDeviceIdList())) {
//            successDeviceIdList.addAll(DeviceFactoryProducer.getFactory(deviceStatus.getDeviceIdList().get(0)).getIotDeviceService().lightAdjustmentBatch(deviceStatus));
//        } else {
//            DeviceFactoryProducer.getFactory(deviceStatus.getDeviceId()).getIotDeviceService().lightAdjustment(deviceStatus);
//            successDeviceIdList.add(deviceStatus.getDeviceId());
//        }
//        // 要保存或是更新的设备参数列表
//        List<ProjectDeviceParamInfo> deviceParamInfoList = new ArrayList<>();
//        if (CollUtil.isNotEmpty(successDeviceIdList)) {
//            projectDeviceParamInfoService.remove(new LambdaQueryWrapper<ProjectDeviceParamInfo>()
//                    .in(ProjectDeviceParamInfo::getDeviceId, successDeviceIdList)
//                    .eq(ProjectDeviceParamInfo::getServiceId, "LightDeviceStatus"));
//            successDeviceIdList.forEach(deviceId -> {
//                ProjectDeviceParamInfo projectDeviceParamInfo = new ProjectDeviceParamInfo();
//                projectDeviceParamInfo.setServiceId("LightDeviceStatus");
//                projectDeviceParamInfo.setDeviceId(deviceId);
//                projectDeviceParamInfo.setProjectId(ProjectContextHolder.getProjectId());
//                ObjectNode objectNode = objectMapper.createObjectNode();
//                deviceStatus.setDeviceIdList(null);
//                deviceStatus.setDeviceId("");
//                objectNode.putPOJO("LightDeviceStatus", deviceStatus);
//                projectDeviceParamInfo.setDeviceParam(objectNode.toString());
//                deviceParamInfoList.add(projectDeviceParamInfo);
//            });
//            try {
//                projectDeviceParamInfoService.saveBatch(deviceParamInfoList);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        return true;
//    }

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
        return baseMapper.getAbnormalCount(type, deviceRegionId);
    }

    @Override
    public ProjectNoticeDevice getDeviceNameAndBuildingNameAndUnitName(String deviceId) {
        return baseMapper.getDeviceNameAndBuildingNameAndUnitName(deviceId);
    }

    @Override
    public Page<DeviceInfoVo> page(Page page, DeviceInfoVo vo) {
        ProjectDeviceInfo po = new ProjectDeviceInfo();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }

    @Override
    public Page<ProjectParkingDeviceInfoDto> pageDeviceInfo(Page<ProjectParkingDeviceInfoDto> page, String laneId, ProjectParkingDeviceInfoDto projectParkingDeviceInfoDto) {
        return this.baseMapper.pageDeviceInfo(page, ProjectContextHolder.getProjectId(),laneId, projectParkingDeviceInfoDto);
    }

    @Override
    public ProjectParkingDeviceInfoDto getByDeviceId(String deviceId) {
        ProjectDeviceInfo one = super.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>()
                .eq(ProjectDeviceInfo::getDeviceId, deviceId)
                .last("LIMIT 1")
        );

        if (one == null) return null;
        ProjectParkingDeviceInfoDto dto = new ProjectParkingDeviceInfoDto();
        BeanUtils.copyProperties(one, dto);

        return dto;
    }
}
