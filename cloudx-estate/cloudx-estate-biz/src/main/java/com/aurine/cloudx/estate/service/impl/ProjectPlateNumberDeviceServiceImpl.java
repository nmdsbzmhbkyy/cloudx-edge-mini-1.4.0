package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.excel.ProjectPlateNumberDeviceExcel;
import com.aurine.cloudx.estate.service.ProjectPlateNumberDeviceService;
import com.aurine.cloudx.estate.util.ExcelExportUtil;
import com.aurine.cloudx.estate.util.bean.BeanPropertyUtil;
import com.aurine.parking.entity.po.ProjectPlateNumberDevice;
import com.aurine.parking.entity.vo.ParkingDeviceCertDlstatusCountVo;
import com.aurine.parking.entity.vo.PlateNumberDeviceSearchCondition;
import com.aurine.parking.entity.vo.PlateNumberQueryVo;
import com.aurine.parking.entity.vo.ProjectPlateNumberDeviceVo;
import com.aurine.parking.feign.RemoteParkingCarRegisterService;
import com.aurine.parking.feign.RemotePlateNumberDeviceService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectPlateNumberDeviceServiceImpl implements ProjectPlateNumberDeviceService {

    @Resource
    RemotePlateNumberDeviceService remotePlateNumberDeviceService;

    @Resource
    RemoteParkingCarRegisterService remoteParkingCarRegisterService;

    @Override
    public R<Page<ProjectPlateNumberDevice>> selectPage(Page page, PlateNumberDeviceSearchCondition query) {
        PlateNumberQueryVo queryDto = new PlateNumberQueryVo();
        BeanPropertyUtil.copyProperty(queryDto, page).copyProperty(query);
        return remotePlateNumberDeviceService.page(queryDto);
    }

    @Override
    public R<List<ProjectPlateNumberDevice>> listByDeviceId(String deviceId) {
        return remotePlateNumberDeviceService.listByDeviceId(deviceId);
    }

    @Override
    public R<List<ParkingDeviceCertDlstatusCountVo>> countByProject() {
        return remotePlateNumberDeviceService.countByProjectId(ProjectContextHolder.getProjectId());
    }

    @SneakyThrows
    @Override
    public void exportExcel(String deviceId, String deviceName, HttpServletResponse httpServletResponse) {
        R<List<ProjectPlateNumberDeviceVo>> response = remotePlateNumberDeviceService.listVoByDeviceId(deviceId);
        List<ProjectPlateNumberDeviceVo> data = response.getData();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分ss秒");
        if (CollUtil.isNotEmpty(data)) {
            List<ProjectPlateNumberDeviceExcel> excelList = data.stream().map(item -> {
                ProjectPlateNumberDeviceExcel excel = new ProjectPlateNumberDeviceExcel();
                BeanPropertyUtil.copyProperty(excel, item);
                excel.setSendTime(format.format(item.getSendTime()));
                return excel;
            }).collect(Collectors.toList());
            ExcelExportUtil excelExportUtil = new ExcelExportUtil();
            excelExportUtil.exportExcel(deviceName + "-设备报表", ProjectPlateNumberDeviceExcel.class, excelList, httpServletResponse);

        }
    }

    @Override
    public R<Boolean> redownloadFailedPlateNumber(List<String> deviceIdList) {
        return remoteParkingCarRegisterService.plateNumberResend(deviceIdList);
    }
}
