package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.date.DateUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectPersonDeviceDTO;
import com.aurine.cloudx.estate.entity.ProjectPersonDevice;
import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.mapper.ProjectPersonDeviceMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectPersonDeviceService;
import com.aurine.cloudx.estate.util.delay.constants.DelayTaskTopicEnum;
import com.aurine.cloudx.estate.util.delay.TaskUtil;
import com.aurine.cloudx.estate.util.delay.entity.PersonDelayTask;
import com.aurine.cloudx.estate.vo.ProjectDeviceLiftVo;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceRecordVo;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>员工设备权限代理实现类</p>
 *
 * @ClassName: ProjectStaffDeviceProxyServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 10:22
 * @Copyright:
 */
@Service
public class ProjectStaffDeviceProxyServiceImpl extends ServiceImpl<ProjectPersonDeviceMapper, ProjectPersonDevice> implements ProjectStaffDeviceProxyService {
    //    @Autowired
//    private WebProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private AbstractProjectPersonDeviceService abstractWebProjectPersonDeviceService;

    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;

    @Resource
    private TaskUtil taskUtil;

    /**
     * 保存员工权限配置
     *
     * @param projectStaffDeviceVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ProjectStaffDeviceVo projectStaffDeviceVo) {

        //删除老旧方案
        //projectPersonPlanRelService.deleteByPersonId(projectStaffDeviceVo.getPersonId());
        ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(projectStaffDeviceVo.getPersonId());
//        if(personPlanRel != null){
//            projectPersonPlanRelService.removeById(personPlanRel);
//        }

        //保存该用户使用的方案
        ProjectPersonPlanRel projectPersonPlanRel = new ProjectPersonPlanRel();
        if(personPlanRel != null){
            projectPersonPlanRel.setSeq(personPlanRel.getSeq());
        }
        projectPersonPlanRel.setPersonType(PersonTypeEnum.STAFF.code);
        BeanUtils.copyProperties(projectStaffDeviceVo, projectPersonPlanRel);

        if (projectPersonPlanRel.getExpTime() == null) {
            projectPersonPlanRel.setExpTime(DateUtil.toLocalDateTime(DateUtil.parse("2199-01-01", "yyyy-MM-dd")));
        }
        projectPersonPlanRelService.saveOrUpdate(projectPersonPlanRel);

        List<String> deviceIdList = new ArrayList<>();
        Collections.addAll(deviceIdList, projectStaffDeviceVo.getDeviceIdArray());
//        projectRightDeviceService.authPersonCertmdiaDevice(deviceIdList, projectStaffDeviceVo.getPersonId());

        //保存该用户可用的通行设备
        ProjectPersonDeviceDTO projectPersonDeviceDTO = new ProjectPersonDeviceDTO();
        BeanUtils.copyProperties(projectStaffDeviceVo, projectPersonDeviceDTO);
        //电梯集合
        List<String> liftIdList;
        if(projectStaffDeviceVo.getLifts() != null){
            liftIdList = projectStaffDeviceVo.getLifts().stream().map(ProjectDeviceLiftVo::getDeviceId).collect(Collectors.toList());
            projectPersonDeviceDTO.setLiftIdList(liftIdList);
        }
//        projectPersonDeviceService.savePersonDevice(projectPersonDeviceDTO);
        abstractWebProjectPersonDeviceService.savePersonDevice(projectPersonDeviceDTO);
        if (TaskUtil.isToday(projectPersonDeviceDTO.getExpTime())) {
            taskUtil.addDelayTask(new PersonDelayTask(ProjectContextHolder.getProjectId(), projectPersonDeviceDTO.getPersonId(),
                    projectPersonDeviceDTO.getExpTime(), PersonTypeEnum.STAFF, DelayTaskTopicEnum.householderExp));

            /*DelayTaskUtil.instance().addDelayTask(new DelayTask(projectPersonDeviceDTO.getExpTime(),
                    ProjectContextHolder.getProjectId(), DelayTaskTopicEnum.expHouseholder));*/
        }
        return true;
    }

    /**
     * 关闭通行权限
     *
     * @param personId 人员ID
     * @return
     */
    @Override
    public boolean disablePassRight(String personId) {
//        return projectPersonDeviceService.disablePassRight(PersonTypeEnum.STAFF.code, personId);
        return abstractWebProjectPersonDeviceService.disablePassRight(PersonTypeEnum.STAFF.code, personId);
    }

    /**
     * 开启通行权限
     *
     * @param personId
     * @return
     */
    @Override
    public boolean enablePassRight(String personId) {
        return abstractWebProjectPersonDeviceService.enablePassRight(PersonTypeEnum.STAFF.code, personId);
//        return projectPersonDeviceService.enablePassRight(PersonTypeEnum.STAFF.code, personId);
    }


    /**
     * 获取员工权限配置
     *
     * @param personId
     * @return
     */
    @Override
    public ProjectStaffDeviceVo getVo(String personId) {
        ProjectPersonDeviceDTO dto = projectPersonDeviceService.getDTOByPersonId(personId);
        ProjectStaffDeviceVo vo = new ProjectStaffDeviceVo();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }


    /**
     * 员工门禁权限查询
     *
     * @param page
     * @param searchCondition
     * @return
     */
    @Override
    public IPage<ProjectStaffDeviceRecordVo> findPage(IPage<ProjectStaffDeviceRecordVo> page, ProjectStaffDeviceSearchConditionVo searchCondition) {
        searchCondition.setProjectId(ProjectContextHolder.getProjectId());
        return this.baseMapper.findStaffDevicePage(page, searchCondition);
    }

}
