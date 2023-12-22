package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceLocationVo;
import com.aurine.cloudx.open.origin.vo.ProjectPerimeterAlarmAreaVo;
import com.aurine.cloudx.open.origin.mapper.ProjectPerimeterAlarmAreaMapper;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceLocation;
import com.aurine.cloudx.open.origin.entity.ProjectPerimeterAlarmArea;
import com.aurine.cloudx.open.origin.service.ProjectDeviceInfoService;
import com.aurine.cloudx.open.origin.service.ProjectDeviceLocationService;
import com.aurine.cloudx.open.origin.service.ProjectPerimeterAlarmAreaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


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
//        //初始化数据，查询主机
//        ProjectDeviceInfo projectDeviceInfo;
//        ProjectDeviceInfo deviceInfo = baseMapper.reacquireDefenseArea(deviceId);
//        //获取防区列表
//        List<ProjectPerimeterAlarmArea> list = DeviceFactoryProducer.getFactory(deviceId).getPerimeterAlarmDeviceService().queryChannel(deviceInfo);
//        if(CollectionUtils.isEmpty(list)){
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return false;
//        }
//        //比对新增增量数据
//        if (CollectionUtils.isNotEmpty(channelNos)) {
//            for (int i = 0; i < channelNos.size(); i++) {
//                if (channelNos.get(i).equals(list.get(i).getChannelNo())) {
//                    list.remove(i);
//                    channelNos.remove(i);
//                    i--;
//                }
//            }
//        }
//
//        if (CollectionUtils.isNotEmpty(list)) {
//            for (ProjectPerimeterAlarmArea projectPerimeterAlarmArea : list) {
//
//                //判断deviceInfo里有没有这条数据
//                Integer count = baseMapper.comparison(projectPerimeterAlarmArea.getChannelNo());
//                String num = projectPerimeterAlarmArea.getChannelNo();
//                String number = num.substring(num.length() - 1, num.length());
//
//                if (count <= 0) {
//                    projectDeviceInfo = new ProjectDeviceInfo();
//                    String id = UUID.randomUUID().toString().replace("-", "");
//                    projectDeviceInfo.setDeviceId(id);
//                    projectDeviceInfo.setSn(projectPerimeterAlarmArea.getChannelNo());
//                    projectDeviceInfo.setDeviceName(DEFENSE_AREA + number);
//                    projectDeviceInfo.setDeviceType(DeviceTypeConstants.ALARM_EQUIPMENT);
//                    projectDeviceInfo.setDeviceRegionId(deviceInfo.getDeviceRegionId());
//                    //project_device_info表
//                    projectDeviceInfoService.save(projectDeviceInfo);
//                } else {
//                    projectDeviceInfo = projectDeviceInfoService.list(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
//                            .eq(ProjectDeviceInfo::getSn, projectPerimeterAlarmArea.getChannelNo())).get(0);
//                }
//                ProjectPerimeterAlarmArea alarmArea = getOne(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class)
//                        .eq(ProjectPerimeterAlarmArea::getInfoUid, projectDeviceInfo.getDeviceId()));
//                if (alarmArea == null) {
//                    //project_perimeter_alarm_area表
//                    projectPerimeterAlarmArea.setInfoUid(projectDeviceInfo.getDeviceId());
//                    projectPerimeterAlarmArea.setChannelName(DEFENSE_AREA + number);
//                    projectPerimeterAlarmArea.setModuleNo(deviceInfo.getDeviceCode());
//                    projectPerimeterAlarmArea.setChannelType("1");
//                    projectPerimeterAlarmArea.setArmedStatus(PerimeterStatusEnum.DISARM.getType());
//                    save(projectPerimeterAlarmArea);
//                    flag = updateStatusById(projectPerimeterAlarmArea.getInfoUid(), PerimeterStatusEnum.DISARM.getName());
//                }
//
//            }
//        }
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
        ProjectPerimeterAlarmArea projectPerimeterAlarmArea = getOne(Wrappers.<ProjectPerimeterAlarmArea>query().eq("infoUid", infoUid));
//        if (PerimeterStatusEnum.ARMED.getName().equals(name)) {
//            projectPerimeterAlarmArea.setArmedStatus(PerimeterStatusEnum.ARMED.getType());
//            boolean bool = DeviceFactoryProducer.getFactory(projectPerimeterAlarmArea.getDeviceId()).getPerimeterAlarmDeviceService().channelProtection(projectPerimeterAlarmArea);
//            if (!bool) {
//                return bool;
//            }
//        }
//        if (PerimeterStatusEnum.DISARM.getName().equals(name)) {
//            projectPerimeterAlarmArea.setArmedStatus(PerimeterStatusEnum.DISARM.getType());
//            boolean bool = DeviceFactoryProducer.getFactory(projectPerimeterAlarmArea.getDeviceId()).getPerimeterAlarmDeviceService().channelRemoval(projectPerimeterAlarmArea);
//            if (!bool) {
//                return bool;
//            }
//
//        }
        return updateById(projectPerimeterAlarmArea);
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
}
