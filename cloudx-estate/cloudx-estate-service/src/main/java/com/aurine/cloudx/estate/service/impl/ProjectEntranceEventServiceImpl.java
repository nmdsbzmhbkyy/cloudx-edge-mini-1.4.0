
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceRegion;
import com.aurine.cloudx.estate.entity.ProjectEntranceEvent;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.mapper.ProjectEntranceEventMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectDeviceRegionService;
import com.aurine.cloudx.estate.service.ProjectEntranceEventService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.vo.ProjectAppEventVo;
import com.aurine.cloudx.estate.vo.ProjectEventSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectEventVo;
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
public class ProjectEntranceEventServiceImpl extends ServiceImpl<ProjectEntranceEventMapper, ProjectEntranceEvent> implements ProjectEntranceEventService {

    @Resource
    private ProjectEntranceEventMapper projectEntranceEventMapper;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;

    @Override
    public IPage<ProjectEventVo> page(Page page, ProjectEventSearchCondition param) {
        return projectEntranceEventMapper.select(page, param);
    }

    @Override
    public List<ProjectEventVo> getEventTypeNum(Integer projectId, Integer tenantId) {
        return projectEntranceEventMapper.findNum(projectId, tenantId);
    }

    @Override
    public boolean add(ProjectEventVo vo) {
        vo.setProjectId(ProjectContextHolder.getProjectId());
        vo.setTenantId(TenantContextHolder.getTenantId());
        //初始化事件描述
        String eventDesc = "";
        //初始化人员类型
        Map<String, String> personType = new HashMap<>();
        personType.put("1", "住户");
        personType.put("2", "员工");
        personType.put("3", "游客");
        //初始化认证介质
        Map<String, String> certMedia = new HashMap<>();
        certMedia.put("1", "指纹");
        certMedia.put("2", "人脸");
        certMedia.put("3", "卡");
        certMedia.put("4", "密码");
        //初始化出入方向
        Map<String, String> entranceType = new HashMap<>();
        entranceType.put("1", "进门");
        entranceType.put("2", "出门");
        //构成事件描述
        if (vo.getEventDesc() == null) {
            eventDesc = personType.get(vo.getPersonType()) + vo.getPersonName() + "在" + vo.getDeviceName() +
                    certMedia.get(vo.getCertMedia()) + "开门" + entranceType.get(vo.getEntranceType());
        }
        vo.setEventDesc(eventDesc);

        //判断是否为报警事件
        if (vo.getEventType().equals("3")) {
            //为报警事件则将处理情况变更为未处理
            vo.setEventStatus("0");
        }
        ProjectEntranceEvent projectEntranceEvent = new ProjectEntranceEvent();
        BeanUtils.copyProperties(vo, projectEntranceEvent);
        boolean result = baseMapper.insert(projectEntranceEvent) == 1;
        if (!result) {
            throw new RuntimeException("添加事件失败,未知错误，请联系管理员");
        }
        return result;
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
        ProjectEntranceEvent projectEntranceEvent = new ProjectEntranceEvent();
        BeanUtils.copyProperties(vo, projectEntranceEvent);
        if (personInfo != null) {
            projectEntranceEvent.setPersonName(personInfo.getPersonName());
        }
        if (deviceInfo != null) {
            projectEntranceEvent.setDeviceName(deviceInfo.getDeviceName());
            ProjectDeviceRegion region = projectDeviceRegionService.getById(deviceInfo.getDeviceRegionId());
            projectEntranceEvent.setDeviceRegionName(region.getRegionName());
        }
        return this.save(projectEntranceEvent);
    }

    @Override
    public boolean updateByEventStatus(ProjectEventVo vo) {
        //调用时为0则视为开始修改,为1时提交修改
        if (vo.getEventStatus().equals("0")) {
            vo.setEventStatus("1");
        } else if (vo.getEventStatus().equals("1")) {
            vo.setEventStatus("2");
        }
        vo.setEventTime(LocalDateTime.now());

        return false;
    }

    /**
     * 根据人员类型获取当天的访问数量
     *
     * @param personTypeEnum
     * @return
     */
    @Override
    public Integer countOneDayByPersonType(PersonTypeEnum personTypeEnum) {
        return this.baseMapper.countOneDayByPersonType(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId(), personTypeEnum == null ? null : personTypeEnum.code);
    }

    @Override
    public IPage<ProjectAppEventVo> getPageByPersonId(Page page, String personId, String date) {
        return this.baseMapper.getPageByPersonId(page, personId, date);
    }

    @Override
    public List<ProjectAppEventVo> getListByPersonId(String personId, String date) {
        return this.baseMapper.getListByPersonId(personId, date);
    }
}
