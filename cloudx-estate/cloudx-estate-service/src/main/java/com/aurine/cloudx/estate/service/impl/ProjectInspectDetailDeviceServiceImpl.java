package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.entity.ProjectInspectDetailDevice;
import com.aurine.cloudx.estate.entity.ProjectInspectTaskDetail;
import com.aurine.cloudx.estate.mapper.ProjectInspectDetailDeviceMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectInspectDetailCheckItemService;
import com.aurine.cloudx.estate.service.ProjectInspectDetailDeviceService;
import com.aurine.cloudx.estate.vo.ProjectInspectDetailDeviceVo;
import com.aurine.cloudx.estate.vo.ProjectPointDeviceVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 巡检任务明细设备列表(ProjectInspectDetailDevice)表服务实现类
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:31
 */
@Service
public class ProjectInspectDetailDeviceServiceImpl extends ServiceImpl<ProjectInspectDetailDeviceMapper,
        ProjectInspectDetailDevice> implements ProjectInspectDetailDeviceService {

    @Resource
    ProjectInspectDetailDeviceMapper projectInspectDetailDeviceMapper;
    @Resource
    ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    ProjectInspectDetailCheckItemService projectInspectDetailCheckItemService;

    private final Map<String, List<ProjectPointDeviceVo>> deviceVoListMap = new HashMap<>();

    @Override
    public boolean initDetailDevice(List<ProjectInspectTaskDetail> detailList) {
        deviceVoListMap.clear();
        List<ProjectInspectDetailDevice> detailDeviceList = new ArrayList<>();
        // 遍历巡检任务明细根据明细id和巡检点id生成巡检任务明细数据
        detailList.forEach(taskDetail -> {
            String detailId = taskDetail.getDetailId();
            String pointId = taskDetail.getPointId();
            List<ProjectPointDeviceVo> pointDeviceVoList = getProjectPointDeviceVoList(pointId);
            pointDeviceVoList.forEach(vo -> {
                ProjectInspectDetailDevice detailDevice = new ProjectInspectDetailDevice();
                BeanUtil.copyProperties(vo, detailDevice);
                detailDevice.setDeviceTypeId(vo.getDeviceType());
                detailDevice.setDetailId(detailId);
                detailDevice.setOperator(0);
                detailDeviceList.add(detailDevice);
            });
        });
        boolean saveBatch = this.saveBatch(detailDeviceList);
        projectInspectDetailCheckItemService.initDetailCheckItem(detailDeviceList);
        return saveBatch;
    }

    @Override
    public boolean removeDetailDeviceByDetailId(List<String> detailIdList) {
        LambdaQueryWrapper<ProjectInspectDetailDevice> removeQuery = new QueryWrapper<ProjectInspectDetailDevice>().lambda()
                .in(ProjectInspectDetailDevice::getDetailId, detailIdList);
        List<ProjectInspectDetailDevice> detailDeviceList = this.list(removeQuery);
        if (CollUtil.isNotEmpty(detailDeviceList)) {
            List<String> detailDeviceIdList = detailDeviceList.stream().map(ProjectInspectDetailDevice::getDeviceDetailId).collect(Collectors.toList());
            projectInspectDetailCheckItemService.removeDetailCheckItemByDetailDeviceIdList(detailDeviceIdList);
            return this.remove(removeQuery);
        }
        return true;
    }

    @Override
    public List<ProjectInspectDetailDeviceVo> listDetailDeviceByDetailId(String detailId) {
        return projectInspectDetailDeviceMapper.listDetailDeviceByDetailId(detailId);
    }

    private List<ProjectPointDeviceVo> getProjectPointDeviceVoList(String pointId) {
        List<ProjectPointDeviceVo> deviceVoList = deviceVoListMap.get(pointId);
        if (CollUtil.isEmpty(deviceVoList)) {
            deviceVoList = projectDeviceInfoService.listDeviceByPointId(pointId);
            deviceVoListMap.put(pointId, deviceVoList);
        }
        return deviceVoList;
    }

}