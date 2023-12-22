package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.mapper.ProjectDeviceLoadLogDetailMapper;
import com.aurine.cloudx.open.origin.constant.LoadStatusConstants;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceLoadLogDetail;
import com.aurine.cloudx.open.origin.service.ProjectDeviceLoadLogDetailService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 设备导入日志明细(ProjectDeviceLoadLogDetail)表服务实现类
 *
 * @author 王良俊
 * @since 2021-06-03 10:49:52
 */
@Service
public class ProjectDeviceLoadLogDetailServiceImpl extends ServiceImpl<ProjectDeviceLoadLogDetailMapper, ProjectDeviceLoadLogDetail> implements ProjectDeviceLoadLogDetailService {

    @Override
    public void updateToFailed(String batchId, String errorMessage) {
        this.update(new LambdaUpdateWrapper<ProjectDeviceLoadLogDetail>()
                .eq(ProjectDeviceLoadLogDetail::getBatchId, batchId)
                .ne(ProjectDeviceLoadLogDetail::getLoadStatus, LoadStatusConstants.IMPORT_SUCCESS)
                .ne(ProjectDeviceLoadLogDetail::getLoadStatus, LoadStatusConstants.IMPORT_FAILED)
                .set(ProjectDeviceLoadLogDetail::getLoadStatus, LoadStatusConstants.IMPORT_FAILED)
                .set(ProjectDeviceLoadLogDetail::getErrorMsg, errorMessage));
    }

}
