package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.ProjectEpidemicEvent;
import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmEvent;
import com.aurine.cloudx.estate.entity.ProjectVisitorHis;
import com.aurine.cloudx.estate.mapper.ProjectEpidemicEventMapper;
import com.aurine.cloudx.estate.service.ProjectEpidemicEventService;
import com.aurine.cloudx.estate.service.ProjectHousePersonRelService;
import com.aurine.cloudx.estate.service.ProjectVisitorHisService;
import com.aurine.cloudx.estate.vo.ProjectEpidemicEventVo;
import com.aurine.cloudx.estate.vo.ProjectEventVo;
import com.aurine.cloudx.estate.vo.ProjectHousePersonRelRecordVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * 疫情记录
 *
 * @author 邹宇
 * @date 2021-6-7 11:08:18
 */

@Service
@Slf4j
public class ProjectEpidemicEventServiceImpl extends ServiceImpl<ProjectEpidemicEventMapper, ProjectEpidemicEvent> implements ProjectEpidemicEventService {

    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private ProjectVisitorHisService projectVisitorHisService;

    /**
     * 分页查询
     *
     * @param projectEpidemicEventVo
     * @return
     */
    @Override
    public Page pageEpidemicEvent(ProjectEpidemicEventVo projectEpidemicEventVo) {
        if (projectEpidemicEventVo.getSize() != null && projectEpidemicEventVo.getCurrent() != null) {
            projectEpidemicEventVo.setCurrent((projectEpidemicEventVo.getCurrent() - 1 )* projectEpidemicEventVo.getSize() );
        }

        List<ProjectEpidemicEvent> list=baseMapper.selectAll(projectEpidemicEventVo);
        Page<ProjectEpidemicEvent> projectEpidemicEventPage = new Page<ProjectEpidemicEvent>();
        projectEpidemicEventPage.setRecords(list);
        projectEpidemicEventPage.setTotal(baseMapper.getCount(projectEpidemicEventVo));
        return projectEpidemicEventPage;
    }

    @Override
    public void saveEpidemicEvent(ProjectEventVo eventVo, String temperature) {
        ProjectEpidemicEvent projectEpidemicEvent = new ProjectEpidemicEvent();
        projectEpidemicEvent.setPersonName(eventVo.getPersonName());
        projectEpidemicEvent.setEventId(UUID.randomUUID().toString().replaceAll("-",""));
        projectEpidemicEvent.setMobileNo(eventVo.getTelephone());
        projectEpidemicEvent.setPersonType(eventVo.getPersonType());
        if(eventVo.getPersonType().equals(PersonTypeEnum.PROPRIETOR.code)){
            StringBuilder stringBuilder = new StringBuilder();
            List<ProjectHouseDTO> projectHouseDTOList = projectHousePersonRelService.listHouseByPersonId(eventVo.getPersonId());
            if (CollUtil.isNotEmpty(projectHouseDTOList)){
                projectHouseDTOList.forEach(e->{
                    if(StrUtil.isEmpty(stringBuilder)){
                        stringBuilder.append(e.getBuildingName()).append("-").append(e.getUnitName()).append("-").append(e.getHouseName()).append("房");
                    }else{
                        stringBuilder.append(",").append(e.getBuildingName()).append("-").append(e.getUnitName()).append("-").append(e.getHouseName()).append("房");
                    }
                });
            }
            projectEpidemicEvent.setHouseName(stringBuilder.toString());
        } else if (eventVo.getPersonType().equals(PersonTypeEnum.VISITOR.code)) {
            ProjectVisitorHis projectVisitorHis = projectVisitorHisService.getOne(Wrappers.lambdaQuery(ProjectVisitorHis.class).eq(ProjectVisitorHis::getVisitId, eventVo.getPersonId()).last("limit 1"));
            if (projectVisitorHis != null && StrUtil.isNotEmpty(projectVisitorHis.getVisitHouseId())) {
                projectEpidemicEvent.setHouseName(projectHousePersonRelService.getVisitorHouseName(projectVisitorHis.getVisitHouseId()));
            } else {
                projectEpidemicEvent.setHouseName(eventVo.getAddrDesc());
            }
        } else {
            projectEpidemicEvent.setHouseName(eventVo.getAddrDesc());
        }
        projectEpidemicEvent.setEventTime(eventVo.getEventTime());
        projectEpidemicEvent.setSnapshotPic(eventVo.getPicUrl());
        projectEpidemicEvent.setTemperature(Double.valueOf(temperature));

        save(projectEpidemicEvent);
    }


    /**
     * 查询红码数量
     *
     * @return
     */
    @Override
    public Integer countRedCode() {
        return count(new QueryWrapper<ProjectEpidemicEvent>().eq("codeStatus", "红码"));
    }


}
