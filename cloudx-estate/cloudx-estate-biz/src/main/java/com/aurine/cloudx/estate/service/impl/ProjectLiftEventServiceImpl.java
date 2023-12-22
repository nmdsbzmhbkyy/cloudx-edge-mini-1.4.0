
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectEntranceEventMapper;
import com.aurine.cloudx.estate.mapper.ProjectLiftEventMapper;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectAppEventVo;
import com.aurine.cloudx.estate.vo.ProjectEventSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectEventVo;
import com.aurine.cloudx.estate.vo.ProjectLiftEventVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通行事件记录
 *
 * @author 黄阳光 code generator
 * @date 2020-05-20 13:27:59
 */
@Service
public class ProjectLiftEventServiceImpl extends ServiceImpl<ProjectLiftEventMapper, ProjectLiftEvent> implements ProjectLiftEventService {

    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;


    @Override
    public IPage<ProjectLiftEventVo> page(Page page, ProjectEventSearchCondition param) {
        return this.baseMapper.select(page, param);
    }

    /**
     * 添加事件
     *
     * @param vo
     * @return
     * @author: 王伟
     */
    @Override
    public boolean addEvent(ProjectEventVo vo) {
        ProjectPersonInfo personInfo = projectPersonInfoService.getById(vo.getPersonId());
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(vo.getDeviceId());
        ProjectLiftEvent projectLiftEvent = new ProjectLiftEvent();
        BeanUtils.copyProperties(vo, projectLiftEvent);
        if (personInfo != null) {
            projectLiftEvent.setPersonName(personInfo.getPersonName());
        }
        if (deviceInfo != null) {
            projectLiftEvent.setDeviceName(deviceInfo.getDeviceName());
            ProjectDeviceRegion region = projectDeviceRegionService.getById(deviceInfo.getDeviceRegionId());
            projectLiftEvent.setDeviceRegionName(region.getRegionName());
        }
        return this.save(projectLiftEvent);
    }


}
