package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.constant.enums.PerimeterStatusEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectPerimeterAlarmAreaMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.PerimeterAlarmDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgePerimeterDeviceParamsDTO;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceLocationVo;
import com.aurine.cloudx.estate.vo.ProjectPerimeterAlarmAreaVo;
import com.aurine.cloudx.wjy.entity.Project;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.injector.methods.DeleteBatchByIds;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 防区管理
 *
 * @author 邹宇
 * @date 2021-6-15 14:45:18
 */
@Service
public class ProjectPerimeterAlarmAreaServiceImpl extends ServiceImpl<ProjectPerimeterAlarmAreaMapper, ProjectPerimeterAlarmArea> implements ProjectPerimeterAlarmAreaService {

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    @Resource
    private ProjectDeviceLocationService projectDeviceLocationService;

    @Resource
    private ProjectPerimeterAlarmAreaService projectPerimeterAlarmAreaService;
    @Resource
    private  ProjectDeviceInfoProxyService projectDeviceInfProxyService;
    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;
    @Resource
    private ProjectPerimeterAlarmRecordService projectPerimeterRecordAreaService;
    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;

    private final String DEFENSE_AREA = "防区";


    /**
     * 分页查询
     *
     * @param projectPerimeterAlarmAreaVo
     * @return
     */
    @Override
    public Page<ProjectPerimeterAlarmAreaVo> findPage(Page page, ProjectPerimeterAlarmAreaVo projectPerimeterAlarmAreaVo) {
        return baseMapper.findPage(page, projectPerimeterAlarmAreaVo);
    }

    /**
     * 验证是否重名
     *
     * @param channelName
     * @param deviceId
     * @return
     */
    @Override
    public Boolean verifyDuplicateName(String channelName, String deviceId) {
        if (StringUtil.isEmpty(channelName)) {
            return true;
        }
        if (StringUtil.isNotEmpty(deviceId)) {
            return null == getOne(
                    new QueryWrapper<ProjectPerimeterAlarmArea>().lambda()
                            .eq(ProjectPerimeterAlarmArea::getChannelName, channelName)
                            .eq(ProjectPerimeterAlarmArea::getDeviceId, deviceId));
        }
        return null == getOne(
                new QueryWrapper<ProjectPerimeterAlarmArea>().lambda()
                        .eq(ProjectPerimeterAlarmArea::getChannelName, channelName)
                        .eq(ProjectPerimeterAlarmArea::getDeviceId, deviceId));
    }



    /**
     * 重新获取防区
     *
     * @param deviceId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean reacquireDefenseArea(String deviceId) {

        List<String> channelNos = new ArrayList<>();
        List<ProjectPerimeterAlarmArea> projectPerimeterAlarmAreas = list(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class)
                .eq(ProjectPerimeterAlarmArea::getDeviceId, deviceId));
        projectPerimeterAlarmAreas.forEach(e -> {
            channelNos.add(e.getChannelNo());
        });

        boolean flag = true;
        //初始化数据，查询主机
        ProjectDeviceInfo projectDeviceInfo;
        ProjectDeviceInfo deviceInfo = baseMapper.reacquireDefenseArea(deviceId);
        //获取防区列表 
        List<ProjectPerimeterAlarmArea> list = DeviceFactoryProducer.getFactory(deviceId).getPerimeterAlarmDeviceService().queryChannel(deviceInfo);
        if(CollectionUtils.isEmpty(list)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        //比对新增增量数据
        if (CollectionUtils.isNotEmpty(channelNos)) {
            for (int i = 0; i < channelNos.size(); i++) {
                if (channelNos.get(i).equals(list.get(i).getChannelNo())) {
                    list.remove(i);
                    channelNos.remove(i);
                    i--;
                }
            }
        }

        if (CollectionUtils.isNotEmpty(list)) {
            for (ProjectPerimeterAlarmArea projectPerimeterAlarmArea : list) {

                //判断deviceInfo里有没有这条数据
                Integer count = baseMapper.comparison(projectPerimeterAlarmArea.getChannelNo());
                String num = projectPerimeterAlarmArea.getChannelNo();
                String number = num.substring(num.length() - 1, num.length());

                if (count <= 0) {
                    projectDeviceInfo = new ProjectDeviceInfo();
                    String id = UUID.randomUUID().toString().replace("-", "");
                    projectDeviceInfo.setDeviceId(id);
                    projectDeviceInfo.setSn(projectPerimeterAlarmArea.getChannelNo());
                    projectDeviceInfo.setDeviceName(DEFENSE_AREA + number);
                    projectDeviceInfo.setDeviceType(DeviceTypeConstants.ALARM_EQUIPMENT);
                    projectDeviceInfo.setDeviceRegionId(deviceInfo.getDeviceRegionId());
                    //project_device_info表
                    projectDeviceInfoService.save(projectDeviceInfo);
                } else {
                    projectDeviceInfo = projectDeviceInfoService.list(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                            .eq(ProjectDeviceInfo::getSn, projectPerimeterAlarmArea.getChannelNo())).get(0);
                }
                ProjectPerimeterAlarmArea alarmArea = getOne(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class)
                        .eq(ProjectPerimeterAlarmArea::getInfoUid, projectDeviceInfo.getDeviceId()));
                if (alarmArea == null) {
                    //project_perimeter_alarm_area表
                    projectPerimeterAlarmArea.setInfoUid(projectDeviceInfo.getDeviceId());
                    projectPerimeterAlarmArea.setChannelName(DEFENSE_AREA + number);
                    projectPerimeterAlarmArea.setModuleNo(deviceInfo.getDeviceCode());
                    projectPerimeterAlarmArea.setChannelType("1");
                    projectPerimeterAlarmArea.setArmedStatus(PerimeterStatusEnum.DISARM.getType());
                    save(projectPerimeterAlarmArea);
                    flag = updateStatusById(projectPerimeterAlarmArea.getInfoUid(), PerimeterStatusEnum.DISARM.getName());
                }

            }
        }
        return flag;

    }



    /**
     * 修改状态
     *
     * @param infoUid
     * @param name
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateStatusById(String infoUid, String name) {
        //查询出防区
        ProjectPerimeterAlarmArea projectPerimeterAlarmArea = getOne(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class).eq(ProjectPerimeterAlarmArea::getInfoUid, infoUid));
        // 撤防布防类型
        String type = PerimeterStatusEnum.DISARM.getName().equals(name) ? PerimeterStatusEnum.DISARM.getType() : PerimeterStatusEnum.ARMED.getType();

        //根据防区查询出报警主机
        ProjectDeviceInfo projectDeviceInfo = projectDeviceInfoService.getOne(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                .eq(ProjectDeviceInfo::getDeviceId, projectPerimeterAlarmArea.getDeviceId()));

        //查询出报警主机的产品
        SysDeviceProductMap sysDeviceProductMap = sysDeviceProductMapService.getOne(Wrappers.lambdaQuery(SysDeviceProductMap.class)
                .eq(SysDeviceProductMap::getProductId, projectDeviceInfo.getProductId()));
        if (sysDeviceProductMap == null) {
            return false;
        }

        PerimeterAlarmDeviceService perimeterAlarmDeviceService = DeviceFactoryProducer.getFactory(projectPerimeterAlarmArea.getDeviceId()).getPerimeterAlarmDeviceService();

        ProjectPerimeterAlarmRecord.ProjectPerimeterAlarmRecordBuilder builder = ProjectPerimeterAlarmRecord.builder()
                .deviceId(projectDeviceInfo.getDeviceId()).deviceName(projectDeviceInfo.getDeviceName())
                .operateType(type).operator(SecurityUtils.getUser().getId());
        //根据产品判断 是长城还是精华隆厂商
        if ("3Z99JHL01".equals(sysDeviceProductMap.getProductCode())) {
            boolean bool = true;
            if (PerimeterStatusEnum.ARMED.getName().equals(name)) {
                projectPerimeterAlarmArea.setArmedStatus(PerimeterStatusEnum.ARMED.getType());
                bool = perimeterAlarmDeviceService.channelProtection(projectPerimeterAlarmArea);
            }
            if (PerimeterStatusEnum.DISARM.getName().equals(name)) {
                projectPerimeterAlarmArea.setArmedStatus(PerimeterStatusEnum.DISARM.getType());
                bool = perimeterAlarmDeviceService.channelRemoval(projectPerimeterAlarmArea);
            }

            String regionName = projectDeviceRegionService.getById(projectPerimeterAlarmArea.getDeviceRegionId()).getRegionName();
            builder.moduleNo(projectPerimeterAlarmArea.getModuleNo()).channelNo(projectPerimeterAlarmArea.getChannelNo())
                    .channelName(projectPerimeterAlarmArea.getChannelName()).regionName(regionName).operateStatus(bool ? "1" : "0");
            projectPerimeterRecordAreaService.save(builder.build());
            if (!bool) {
                return bool;
            }
            return updateById(projectPerimeterAlarmArea);
        } else {
            //查询出所有设置了设备号的防区
            List<ProjectPerimeterAlarmArea> list = projectPerimeterAlarmAreaService.list(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class)
                    .eq(ProjectPerimeterAlarmArea::getDeviceId, projectDeviceInfo.getDeviceId())
                    .isNotNull(ProjectPerimeterAlarmArea::getModuleNo));
            List<ProjectPerimeterAlarmRecord> recordList = new ArrayList<>();
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
     * 更改区域定位
     *
     * @param projectDeviceLocationVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean putPoint(ProjectDeviceLocationVo projectDeviceLocationVo) {
        boolean flag;
        flag = projectDeviceInfoService.update(Wrappers.lambdaUpdate(ProjectDeviceInfo.class)
                .set(ProjectDeviceInfo::getDeviceRegionId,projectDeviceLocationVo.getDeviceRegionId())
                .eq(ProjectDeviceInfo::getDeviceId,projectDeviceLocationVo.getDeviceId()));
        if(!flag){
            return false;
        }


        ProjectDeviceLocation location = projectDeviceLocationService.getOne(Wrappers.lambdaQuery(ProjectDeviceLocation.class)
                .eq(ProjectDeviceLocation::getDeviceId, projectDeviceLocationVo.getDeviceId())
                .eq(ProjectDeviceLocation::getPicId, projectDeviceLocationVo.getPicId()));
        if(projectDeviceLocationVo.getLon()!=null && projectDeviceLocationVo.getLat() != null) {
            if (location == null) {
                ProjectDeviceLocation projectDeviceLocation = new ProjectDeviceLocationVo();
                projectDeviceLocation.setLon(projectDeviceLocationVo.getLon());
                projectDeviceLocation.setLat(projectDeviceLocationVo.getLat());
                projectDeviceLocation.setDeviceId(projectDeviceLocationVo.getDeviceId());
                projectDeviceLocation.setPicId(projectDeviceLocationVo.getPicId());
                flag = projectDeviceLocationService.save(projectDeviceLocation);
            } else {
                flag = projectDeviceLocationService.update(Wrappers.lambdaUpdate(ProjectDeviceLocation.class)
                        .set(ProjectDeviceLocation::getLat, projectDeviceLocationVo.getLat())
                        .set(ProjectDeviceLocation::getLon, projectDeviceLocationVo.getLon())
                        .set(ProjectDeviceLocation::getDeviceId, projectDeviceLocationVo.getDeviceId())
                        .set(ProjectDeviceLocation::getPicId, projectDeviceLocationVo.getPicId())
                        .eq(ProjectDeviceLocation::getSeq, location.getSeq()));
            }
        }
        return flag;
    }

    /**
     * 批量修改防区信息
     * @param lstProjectPerimeterAlarmAreaVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean putMultiDefenseAreaInfo(String deviceId,List<ProjectPerimeterAlarmAreaVo> lstProjectPerimeterAlarmAreaVo) {
//        List<ProjectPerimeterAlarmArea> lstProjectPerimeterAlarmArea=new ArrayList<>();
//
//        for (ProjectPerimeterAlarmAreaVo vo: lstProjectPerimeterAlarmAreaVo){
//            ProjectPerimeterAlarmArea projectPerimeterAlarmArea = getOne(Wrappers.<ProjectPerimeterAlarmArea>query().eq("infoUid", vo.getInfoUid()));
//            projectPerimeterAlarmArea.setChannelNo(vo.getChannelNo());
//            projectPerimeterAlarmArea.setDeviceId(vo.getDeviceId());
//            projectPerimeterAlarmArea.setChannelName(vo.getChannelName());
//            lstProjectPerimeterAlarmArea.add(projectPerimeterAlarmArea);
//        }
        //从数据库中查询完整数据,若已经存在则修改,不存在则添加
        ////交集(修改)
        List<ProjectPerimeterAlarmArea> lstProjectPerimeterAlarmArea4Update=null;
        List<ProjectDeviceInfo> lstProjectDeviceInfo4Update=null;
        if(lstProjectPerimeterAlarmAreaVo.size()>0) {
            lstProjectPerimeterAlarmArea4Update =
                    list(Wrappers.<ProjectPerimeterAlarmArea>query().in("infoUid", lstProjectPerimeterAlarmAreaVo.stream().map(p -> p.getInfoUid()).collect(Collectors.toList())));
            lstProjectPerimeterAlarmArea4Update.forEach(
                    p -> {
                        ProjectPerimeterAlarmAreaVo vo = lstProjectPerimeterAlarmAreaVo.stream().filter(v -> v.getInfoUid().equals(p.getInfoUid())).findFirst().get();
                        p.setChannelNo(vo.getChannelNo());
                        p.setDeviceId(vo.getDeviceId());
                        p.setChannelName(vo.getChannelName());
                    }
            );
            lstProjectDeviceInfo4Update = projectDeviceInfoService.list(Wrappers.<ProjectDeviceInfo>query().in("deviceId", lstProjectPerimeterAlarmAreaVo.stream().map(p -> p.getInfoUid()).collect(Collectors.toList())));
            lstProjectDeviceInfo4Update.forEach(
                    dev -> {
                        ProjectPerimeterAlarmAreaVo vo = lstProjectPerimeterAlarmAreaVo.stream().filter(v -> v.getInfoUid().equals(dev.getDeviceId())).findFirst().get();
                        dev.setDeviceName(vo.getChannelName());
                    }
            );
        }


        ////数据库中的数据与传入数据的差集(删除)
        List<Integer> lstProjectPerimeterAlarmArea4Delete=
                list(Wrappers.<ProjectPerimeterAlarmArea>query().in("deviceId", deviceId))
                        .stream()
                        .filter(item->!lstProjectPerimeterAlarmAreaVo.stream().map(vo->vo.getInfoUid()).collect(Collectors.toList()).contains(item.getInfoUid()))
                        .map(item->item.getSeq())
                        .collect(Collectors.toList());
//        LambdaQueryWrapper<ProjectPerimeterAlarmArea> wrapper4Dele=new LambdaQueryWrapper();
//        wrapper4Dele.in(ProjectPerimeterAlarmArea::getInfoUid,lstProjectPerimeterAlarmArea4Delete);

        List<String> lstProjectPerimeterAlarmArea4DeleteInfoUid=
                list(Wrappers.<ProjectPerimeterAlarmArea>query().in("deviceId", deviceId))
                        .stream()
                        .filter(item->!lstProjectPerimeterAlarmAreaVo.stream().map(vo->vo.getInfoUid()).collect(Collectors.toList()).contains(item.getInfoUid()))
                        .map(item->item.getInfoUid())
                        .collect(Collectors.toList());
        List<String> lstProjectDeviceInfo4Delete =null;
        if(lstProjectPerimeterAlarmArea4DeleteInfoUid.size()>0) {
            lstProjectDeviceInfo4Delete =
                    projectDeviceInfoService.list(Wrappers.<ProjectDeviceInfo>query().in("deviceId", lstProjectPerimeterAlarmArea4DeleteInfoUid))
                            .stream()
                            .map(item -> item.getDeviceId())
                            .collect(Collectors.toList());
        }


        ////传入数据与数据库数据的差集(添加)
        /////获取已存在的通道
        Map<String, ProjectPerimeterAlarmAreaVo> addMap = new HashMap<>();
        List<ProjectDeviceInfo> lstProjectDeviceInfo4Add =new ArrayList<>();
        if(lstProjectPerimeterAlarmAreaVo.size()>0) {

            List<ProjectDeviceInfo> channelsInDB = projectDeviceInfoService.list(Wrappers.<ProjectDeviceInfo>query().in("deviceId", lstProjectPerimeterAlarmAreaVo.stream().map(p -> p.getInfoUid()).collect(Collectors.toList())));
            lstProjectDeviceInfo4Add =
                    lstProjectPerimeterAlarmAreaVo
                            .stream()
                            .filter(item -> !channelsInDB.stream().map(dbo -> dbo.getDeviceId()).collect(Collectors.toList()).contains(item.getInfoUid()))//通道的infoUid对应设备的deviceId
                            .map(vo -> {
                                ProjectDeviceInfo projectDeviceInfo4Add = new ProjectDeviceInfo();
                                String deviceId4AlarmArea = UUID.randomUUID().toString().replace("-", "");
                                projectDeviceInfo4Add.setDeviceId(deviceId4AlarmArea);
                                projectDeviceInfo4Add.setDeviceName(vo.getChannelName());
                                projectDeviceInfo4Add.setDeviceType(DeviceTypeConstants.ALARM_EQUIPMENT);
                                addMap.put(deviceId4AlarmArea, vo);
                                return projectDeviceInfo4Add;
                            })
                            .collect(Collectors.toList());
        }


        ProjectDeviceInfo deviceInfo = baseMapper.reacquireDefenseArea(deviceId);

//        List<ProjectPerimeterAlarmArea> lstProjectPerimeterAlarmArea4Add=
//                lstProjectPerimeterAlarmAreaVo
//                        .stream()
//                        .filter(item->!channelsInDB.stream().map(dbo->dbo.getDeviceId()).collect(Collectors.toList()).contains(item.getInfoUid()))
//                        .map(vo->{
//                            ProjectPerimeterAlarmArea projectPerimeterAlarmArea4Add=new ProjectPerimeterAlarmArea();
//                            projectPerimeterAlarmArea4Add.setChannelName(vo.getChannelName());
//                            projectPerimeterAlarmArea4Add.setDeviceId(vo.getDeviceId());
//                            projectPerimeterAlarmArea4Add.setChannelNo(vo.getChannelNo());
//                            projectPerimeterAlarmArea4Add.setModuleNo(vo.getModuleNo());
//                            projectPerimeterAlarmArea4Add.setInfoUid(vo.getInfoUid());
//                            projectPerimeterAlarmArea4Add.setArmedStatus("1");
//                            return projectPerimeterAlarmArea4Add;
//                        })
//                        .collect(Collectors.toList());
        List<ProjectPerimeterAlarmArea> lstProjectPerimeterAlarmArea4Add=
                addMap.keySet().stream()
                .map(key->{
                            ProjectPerimeterAlarmArea projectPerimeterAlarmArea4Add=new ProjectPerimeterAlarmArea();
                            ProjectPerimeterAlarmAreaVo vo=addMap.get(key);
                            projectPerimeterAlarmArea4Add.setChannelName(vo.getChannelName());
                            projectPerimeterAlarmArea4Add.setDeviceId(deviceId);
                            projectPerimeterAlarmArea4Add.setChannelNo(vo.getChannelNo());
                            projectPerimeterAlarmArea4Add.setModuleNo(vo.getModuleNo());
                            projectPerimeterAlarmArea4Add.setInfoUid(key);
                            projectPerimeterAlarmArea4Add.setArmedStatus("1");
                            return projectPerimeterAlarmArea4Add;
                        }
                ).collect(Collectors.toList());



        //组织数据,对驱动进行下发
        List<String> channelsName=lstProjectPerimeterAlarmAreaVo.stream().map(vo->{
            return String.format("Module%03dZone%03d",Integer.parseInt(vo.getModuleNo()),Integer.parseInt(vo.getChannelNo()));
        }).collect(Collectors.toList());

        AurineEdgePerimeterDeviceParamsDTO aurineEdgePerimeterDeviceParamsDTO=new AurineEdgePerimeterDeviceParamsDTO();
        aurineEdgePerimeterDeviceParamsDTO.setChannelNameList(channelsName);
        aurineEdgePerimeterDeviceParamsDTO.setParamDevId(deviceInfo.getDeviceCode());
        aurineEdgePerimeterDeviceParamsDTO.setPasswd(deviceInfo.getCompanyPasswd());

        if(DeviceFactoryProducer.getFactory(deviceId).getPerimeterAlarmDeviceService().setDevParams(deviceInfo,aurineEdgePerimeterDeviceParamsDTO)) {
            if(lstProjectPerimeterAlarmArea4Update!=null) {
                updateBatchById(lstProjectPerimeterAlarmArea4Update);
            }
            if(lstProjectDeviceInfo4Update!=null) {
                projectDeviceInfoService.updateBatchById(lstProjectDeviceInfo4Update);
            }

            removeByIds(lstProjectPerimeterAlarmArea4Delete);
            projectDeviceInfoService.removeByIds(lstProjectDeviceInfo4Delete);

            saveBatch(lstProjectPerimeterAlarmArea4Add);
            projectDeviceInfoService.saveBatch(lstProjectDeviceInfo4Add);

            return true;

        }else {
            return false;
        }

    }

    @Override
    public Boolean putDefenseAreaInfo(ProjectPerimeterAlarmAreaVo projectPerimeterAlarmAreaVo) {
        //设置当前防区参数
        ProjectPerimeterAlarmArea projectPerimeterAlarmArea= getOne(Wrappers.<ProjectPerimeterAlarmArea>query().eq("InfoUid", projectPerimeterAlarmAreaVo.getInfoUid()));
        projectPerimeterAlarmArea.setModuleNo(projectPerimeterAlarmAreaVo.getModuleNo());
        projectPerimeterAlarmArea.setChannelNo(projectPerimeterAlarmAreaVo.getChannelNo());
        projectPerimeterAlarmArea.setChannelName(projectPerimeterAlarmAreaVo.getChannelName());
        projectPerimeterAlarmArea.setDeviceRegionId(projectPerimeterAlarmAreaVo.getDeviceRegionId());


        //查询出报警主机
        ProjectDeviceInfo deviceInfo = baseMapper.reacquireDefenseArea(projectPerimeterAlarmArea.getDeviceId());

        //组织数据,对驱动进行下发
        List<ProjectPerimeterAlarmArea> projectPerimeterAlarmAreaList = list(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class)
                .eq(ProjectPerimeterAlarmArea::getDeviceId, projectPerimeterAlarmArea.getDeviceId())
                .isNotNull(ProjectPerimeterAlarmArea::getChannelNo));
        projectPerimeterAlarmAreaList.add(projectPerimeterAlarmArea);

        List<String> channelsName = new ArrayList<>();
        if(CollUtil.isNotEmpty(projectPerimeterAlarmAreaList) && projectPerimeterAlarmAreaList.size() > 0){
            channelsName = projectPerimeterAlarmAreaList.stream().map(channel->{
                if(channel.getInfoUid().equals(projectPerimeterAlarmAreaVo.getInfoUid())){
                    return String.format("Module%03dZone%03d"
                            ,Integer.parseInt(projectPerimeterAlarmAreaVo.getModuleNo())
                            ,Integer.parseInt(projectPerimeterAlarmAreaVo.getChannelNo())
                    );
                }else{
                    return String.format("Module%03dZone%03d"
                            ,Integer.parseInt(channel.getModuleNo())
                            ,Integer.parseInt(channel.getChannelNo())
                    );
                }
            }).collect(Collectors.toList());
        }
        AurineEdgePerimeterDeviceParamsDTO aurineEdgePerimeterDeviceParamsDTO=new AurineEdgePerimeterDeviceParamsDTO();
        aurineEdgePerimeterDeviceParamsDTO.setChannelNameList(channelsName);
        aurineEdgePerimeterDeviceParamsDTO.setParamDevId(deviceInfo.getDeviceCode());
        aurineEdgePerimeterDeviceParamsDTO.setPasswd(deviceInfo.getCompanyPasswd());

        if(DeviceFactoryProducer.getFactory(deviceInfo.getDeviceId()).getPerimeterAlarmDeviceService().setDevParams(deviceInfo,aurineEdgePerimeterDeviceParamsDTO)) {
            SysDeviceProductMap sysDeviceProductMap = sysDeviceProductMapService.getOne(Wrappers.lambdaQuery(SysDeviceProductMap.class)
                    .eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
            if(sysDeviceProductMap == null){
                return false;
            }
            if("3Z99JHL01".equals(sysDeviceProductMap.getProductCode())) {
                //当前防区默认设置为布防
                projectPerimeterAlarmArea.setArmedStatus(PerimeterStatusEnum.ARMED.getType());
                projectPerimeterAlarmAreaService.saveOrUpdate(projectPerimeterAlarmArea);
            } else {
                projectPerimeterAlarmAreaList.forEach(e-> e.setArmedStatus(PerimeterStatusEnum.ARMED.getType()));
                projectPerimeterAlarmAreaService.updateBatchById(projectPerimeterAlarmAreaList);
            }
            DeviceFactoryProducer.getFactory(projectPerimeterAlarmArea.getDeviceId()).getPerimeterAlarmDeviceService().channelProtection(projectPerimeterAlarmArea);
            return true;
        } else {
            return false;
        }

    }
}
