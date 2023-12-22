package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectThirdpartyInfo;
import com.aurine.cloudx.estate.mapper.ProjectThirdpartyInfoMapper;
import com.aurine.cloudx.estate.service.EdgeCloudRequestService;
import com.aurine.cloudx.estate.service.ProjectThirdpartyInfoService;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.bean.BeanPropertyUtil;
import com.aurine.cloudx.estate.vo.ProjectInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p></p>
 *
 * @author : 王良俊
 * @date : 2021-12-08 11:05:06
 */
@Slf4j
@Service
public class ProjectThirdpartyInfoServiceImpl extends ServiceImpl<ProjectThirdpartyInfoMapper, ProjectThirdpartyInfo> implements ProjectThirdpartyInfoService {

    @Resource
    EdgeCloudRequestService edgeCloudRequestService;

    @Override
    public void saveOrUpdateThirdpartyInfo(ProjectInfoVo projectInfoVo, Integer projectId, String requestId) {
        this.remove(new LambdaQueryWrapper<ProjectThirdpartyInfo>().eq(ProjectThirdpartyInfo::getRequestId, requestId));
        ProjectThirdpartyInfo thirdpartyInfo = new ProjectThirdpartyInfo();
        BeanPropertyUtil.copyProperty(thirdpartyInfo, projectInfoVo);
        thirdpartyInfo.setProjectId(projectId);
        thirdpartyInfo.setRequestId(requestId);
        thirdpartyInfo.setTenantId(1);

        this.save(thirdpartyInfo);
    }

}
