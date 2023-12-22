package com.aurine.cloudx.estate.service.impl;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.ExcelMinIOConstant;
import com.aurine.cloudx.estate.entity.ProjectDeviceLoadLog;
import com.aurine.cloudx.estate.mapper.ProjectDeviceLoadLogMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceLoadLogService;
import com.aurine.cloudx.estate.vo.ProjectDeviceLoadLogVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.api.client.util.IOUtils;
import com.pig4cloud.pigx.common.minio.service.MinioTemplate;
import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 设备导入日志(ProjectDeviceLoadLog)表服务实现类
 *
 * @author 王良俊
 * @since 2021-06-03 10:49:35
 */
@Service
public class ProjectDeviceLoadLogServiceImpl extends ServiceImpl<ProjectDeviceLoadLogMapper, ProjectDeviceLoadLog> implements ProjectDeviceLoadLogService {

    @Resource
    private MinioTemplate minioTemplate;

    @Override
    public Page<ProjectDeviceLoadLogVo> fetchList(Page<ProjectDeviceLoadLogVo> page, String deviceType) {
        return this.baseMapper.fetchList(page, deviceType);
    }

    @Override
    public void getFailedExcelLink(String batchId, HttpServletResponse response) {
        ProjectDeviceLoadLog deviceLoadLog = this.getOne(new LambdaUpdateWrapper<ProjectDeviceLoadLog>().eq(ProjectDeviceLoadLog::getBatchId, batchId));
        String orginFile = deviceLoadLog.getOrginFile();
        String filename = orginFile.substring(68);
        InputStream object = minioTemplate.getObject(ExcelMinIOConstant.BUCKET_NAME, deviceLoadLog.getOrginFile());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            IOUtils.copy(object, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*LocalDateTime updateTime = deviceLoadLog.getUpdateTime();
        Duration between = Duration.between(updateTime, LocalDateTime.now());
        String url;
        if (between.getSeconds() >= ExcelMinIOConstant.EXPIRES_ONE_DAY - 60 && StrUtil.isNotEmpty(deviceLoadLog.getTempFile())) {
            url = minioTemplate.getObjectURL(ExcelMinIOConstant.BUCKET_NAME, deviceLoadLog.getOrginFile(), ExcelMinIOConstant.EXPIRES_ONE_DAY);
            deviceLoadLog.setTempFile(url);
            deviceLoadLog.setUpdateTime(LocalDateTime.now());
            this.updateById(deviceLoadLog);
        } else {
            url = deviceLoadLog.getTempFile();
        }
        return url;*/
    }
}
