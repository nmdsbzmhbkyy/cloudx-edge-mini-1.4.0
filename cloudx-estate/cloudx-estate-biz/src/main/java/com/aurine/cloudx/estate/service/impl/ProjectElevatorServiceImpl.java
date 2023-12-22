package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.cert.util.UUID;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectDeviceInfoMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectElevatorServiceImpl extends ServiceImpl<ProjectDeviceInfoMapper, ProjectDeviceInfo> implements ProjectElevatorService {

    @Resource
    ProjectDeviceRelService projectDeviceRelService;
    @Resource
    ProjectFrameInfoService projectFrameInfoService;
    @Resource
    ProjectDeviceRegionService projectDeviceRegionService;
    @Resource
    ProjectPersonLiftRelService projectPersonLiftRelService;
    @Resource
    ProjectDeviceParamInfoService projectDeviceParamInfoService;
    @Resource
    ProjectDeviceInfoService projectDeviceInfoService;

    @Override
    public Page<ProjectElevatorDeviceVo> pageElevator(Page page, ElevatorPageFormVo elevatorPageFormVo) {
        //TODO: 后面要加上对楼层设置判断是否异常的查询
        return this.baseMapper.pageElevator(page, elevatorPageFormVo);
    }

    @Override
    public Page<ProjectLayerDeviceInfoVo> pageLayerDevice(Page page, String unitId, ProjectDeviceInfoPageFormVo formVo) {
        List<String> layerControlDeviceIdList = new ArrayList<>();
        if (StrUtil.isNotEmpty(formVo.getDeviceId())) {
            ProjectElevatorFormVo elevatorById = this.getElevatorById(formVo.getDeviceId());
            if (elevatorById != null) {
                layerControlDeviceIdList = elevatorById.getLayerControlDeviceIdList();
            }
        }
        System.out.println(formVo);
        return this.baseMapper.pageLayerDevice(page, unitId, formVo, layerControlDeviceIdList);
    }

    @Override
    public Page<ProjectDeviceInfoVo> pageRecognizerControlDevice(Page page, String unitId, ProjectDeviceInfoPageFormVo formVo) {
        return this.baseMapper.pageRecognizerControlDevice(page, unitId, formVo, new ArrayList<>());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R creatElevator(ProjectElevatorFormVo formVo) {
        String deviceRegionId = formVo.getDeviceRegionId();
        String buildingId = formVo.getBuildingId();
        String unitId = formVo.getUnitId();
        String deviceName = formVo.getDeviceName();
        String deviceId = UUID.randomUUID().toString().replaceAll("-", "");
        List<String> layerControlDeviceIdList = formVo.getLayerControlDeviceIdList();
        List<String> recognizerDeviceIdList = formVo.getRecognizerDeviceIdList();
        if (StrUtil.isEmpty(deviceRegionId) || StrUtil.isEmpty(buildingId) || StrUtil.isEmpty(unitId) || StrUtil.isEmpty(deviceName)) {
            return R.failed("必填项未填写");
        }
        if (CollUtil.isEmpty(layerControlDeviceIdList)) {
            return R.failed("未选择分层控制设备");
        }
        int bindCount = projectDeviceRelService.count(new LambdaQueryWrapper<ProjectDeviceRel>().in(ProjectDeviceRel::getDeviceId, layerControlDeviceIdList));
        if (bindCount > 0) {
            return R.failed("已有分层控制器绑定其他电梯，请刷新页面重新创建电梯");
        }
        int deviceNameCount = projectDeviceInfoService.count(new LambdaQueryWrapper<ProjectDeviceInfo>().in(ProjectDeviceInfo::getDeviceName, deviceName));
        if (deviceNameCount > 0) {
            return R.failed("电梯组名称不能与其他电梯组名称重复");
        }

        ProjectDeviceInfo deviceInfo = new ProjectDeviceInfo();
        deviceInfo.setDeviceId(deviceId);
        deviceInfo.setDeviceName(deviceName);
        deviceInfo.setDeviceType(DeviceTypeConstants.ELEVATOR);
        deviceInfo.setDeviceRegionId(deviceRegionId);
        deviceInfo.setBuildingId(buildingId);
        deviceInfo.setUnitId(unitId);
        deviceInfo.setActive("1");
        deviceInfo.setStatus("1");
        deviceInfo.setAccessMethod("2");
        this.save(deviceInfo);
        int layerDeviceNum = layerControlDeviceIdList.size();
        int recognizerDeviceNum = recognizerDeviceIdList.size();
        List<ProjectDeviceRel> deviceRelList = new ArrayList<>();
        for (int i = 0; i < Integer.max(layerDeviceNum, recognizerDeviceNum); i++) {
            if (layerDeviceNum > i) {
                deviceRelList.add(new ProjectDeviceRel(layerControlDeviceIdList.get(i), deviceId));
            }
            if (recognizerDeviceNum > i) {
                deviceRelList.add(new ProjectDeviceRel(recognizerDeviceIdList.get(i), deviceId));
            }
        }
        //TODO: 这里要下发参数到设备上，并修改设备的楼栋、单元、区域ID
        projectDeviceRelService.saveBatch(deviceRelList);
        //下载设备权限
        projectPersonLiftRelService.refreshAddDevice(deviceInfo);
        return R.ok("电梯创建成功");
    }

    @Override
    public ProjectElevatorFormVo getElevatorById(String deviceId) {
        ProjectDeviceInfo elevatorInfo = this.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getDeviceId, deviceId).last("limit 1"));
        ProjectElevatorFormVo projectElevatorFormVo = new ProjectElevatorFormVo();
        projectElevatorFormVo.setBuildingId(elevatorInfo.getBuildingId());
        projectElevatorFormVo.setUnitId(elevatorInfo.getUnitId());
        projectElevatorFormVo.setDeviceName(elevatorInfo.getDeviceName());
        projectElevatorFormVo.setDeviceRegionId(elevatorInfo.getDeviceRegionId());

        ProjectDeviceRegion deviceRegion = projectDeviceRegionService.getOne(new LambdaQueryWrapper<ProjectDeviceRegion>().eq(ProjectDeviceRegion::getRegionId, elevatorInfo.getDeviceRegionId()));

        String buildingName = projectFrameInfoService.getFrameNameById(elevatorInfo.getBuildingId());

        ProjectFrameInfo unit = projectFrameInfoService.getOne(new LambdaQueryWrapper<ProjectFrameInfo>().eq(ProjectFrameInfo::getEntityId, elevatorInfo.getUnitId()));

        projectElevatorFormVo.setBuildingName(buildingName);
        projectElevatorFormVo.setDeviceRegionName(deviceRegion.getRegionName());
        projectElevatorFormVo.setUnitName(unit.getEntityName());

        List<ProjectDeviceRelVo> projectDeviceRelVoList = projectDeviceRelService.ListByDeviceId(deviceId);

        List<String> layerControlDeviceIdList = new ArrayList<>();
        List<String> recognizerDeviceIdList = new ArrayList<>();

        projectDeviceRelVoList.forEach(item -> {
            if (DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE.equals(item.getDeviceType())) {
                layerControlDeviceIdList.add(item.getDeviceId());
            } else if (DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE.equals(item.getDeviceType())) {
                recognizerDeviceIdList.add(item.getDeviceId());
            }
        });
        projectElevatorFormVo.setLayerControlDeviceIdList(layerControlDeviceIdList);
        projectElevatorFormVo.setRecognizerDeviceIdList(recognizerDeviceIdList);
        return projectElevatorFormVo;
    }

    @Override
    public R updateElevatorById(ProjectElevatorFormVo data) {
        String deviceId = data.getDeviceId();
        if (StrUtil.isEmpty(deviceId)) {
            return R.failed("无法修改电梯，请刷新页面重试");
        }
        if (StrUtil.isEmpty(data.getDeviceRegionId()) || StrUtil.isEmpty(data.getBuildingId()) || StrUtil.isEmpty(data.getUnitId()) || StrUtil.isEmpty(data.getDeviceName())) {
            return R.failed("必填项未填写");
        }
        if (CollUtil.isEmpty(data.getLayerControlDeviceIdList())) {
            return R.failed("未选择分层控制设备");
        }
        int bindCount = projectDeviceRelService.count(new LambdaQueryWrapper<ProjectDeviceRel>()
                .ne(ProjectDeviceRel::getParDeviceId, data.getDeviceId())
                .in(ProjectDeviceRel::getDeviceId, data.getLayerControlDeviceIdList()));
        if (bindCount > 0) {
            return R.failed("已有分层控制器绑定其他电梯，请刷新页面重新选择");
        }
        projectDeviceRelService.remove(new LambdaQueryWrapper<ProjectDeviceRel>().eq(ProjectDeviceRel::getParDeviceId, data.getDeviceId()));
        ProjectDeviceInfo projectDeviceInfo = new ProjectDeviceInfo();
        projectDeviceInfo.setDeviceId(data.getDeviceId());
        projectDeviceInfo.setDeviceRegionId(data.getDeviceRegionId());
        projectDeviceInfo.setBuildingId(data.getBuildingId());
        projectDeviceInfo.setUnitId(data.getUnitId());
        projectDeviceInfo.setDeviceName(data.getDeviceName());
        this.updateById(projectDeviceInfo);

        int layerDeviceNum = data.getLayerControlDeviceIdList().size();
        int recognizerDeviceNum = data.getRecognizerDeviceIdList().size();
        List<ProjectDeviceRel> deviceRelList = new ArrayList<>();
        for (int i = 0; i < Integer.max(layerDeviceNum, recognizerDeviceNum); i++) {
            if (layerDeviceNum > i) {
                deviceRelList.add(new ProjectDeviceRel(data.getLayerControlDeviceIdList().get(i), deviceId));
            }
            if (recognizerDeviceNum > i) {
                deviceRelList.add(new ProjectDeviceRel(data.getRecognizerDeviceIdList().get(i), deviceId));
            }
        }
        projectDeviceRelService.saveBatch(deviceRelList);
        return R.ok("更新成功");
    }

    @Override
    public R removeElevatorById(String deviceId) {
        ProjectDeviceInfo deviceInfo = this.getById(deviceId);
        if (deviceInfo == null) {
            return R.ok("删除成功");
        }
        if (!DeviceTypeConstants.ELEVATOR.equals(deviceInfo.getDeviceType())) {
            return R.failed("只能删除电梯设备");
        }
        //TODO: 删除电梯前面要先删除下发到设备的凭证
        projectDeviceRelService.remove(new LambdaQueryWrapper<ProjectDeviceRel>().eq(ProjectDeviceRel::getParDeviceId, deviceId));
        this.removeById(deviceId);
        return R.ok("删除成功");
    }

    @Override
    public List<ProjectDeviceInfoVo> getLayerControlDeviceByElevatorId(String deviceId) {
        return this.baseMapper.getDeviceListByType(deviceId, DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE);
    }

    @Override
    public List<ProjectDeviceInfoVo> getRecognizerDeviceByElevatorId(String deviceId) {
        return this.baseMapper.getDeviceListByType(deviceId, DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE);
    }

    @Override
    public DeviceParamDataVo getRecognizerDeviceParamByElevatorId(String deviceId) {
        List<ProjectDeviceInfoVo> list = this.getLayerControlDeviceByElevatorId(deviceId);
        // 没有分层控制器
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return projectDeviceParamInfoService.getParamByDeviceId(list.get(0).getDeviceId(),list.get(0).getProductId());
    }
}
