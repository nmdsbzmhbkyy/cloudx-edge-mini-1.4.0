package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.mapper.ProjectDeviceReplaceInfoMapper;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceReplaceInfo;
import com.aurine.cloudx.open.origin.service.ProjectDeviceReplaceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 设备变更历史表，用于存储被替换掉的设备信息(ProjectDeviceReplaceInfo)表服务实现类
 *
 * @author 王良俊
 * @since 2021-01-11 11:18:42
 */
@Service
public class ProjectDeviceReplaceInfoServiceImpl extends ServiceImpl<ProjectDeviceReplaceInfoMapper, ProjectDeviceReplaceInfo> implements ProjectDeviceReplaceInfoService {

}