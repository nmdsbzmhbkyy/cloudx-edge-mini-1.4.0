
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.date.DateUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectPersonDeviceDTO;
import com.aurine.cloudx.estate.entity.ProjectPersonDevice;
import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.mapper.ProjectPersonDeviceMapper;
import com.aurine.cloudx.estate.service.ProjectPersonDeviceService;
import com.aurine.cloudx.estate.service.ProjectPersonPlanRelService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.aurine.cloudx.estate.service.ProjectStaffDeviceProxyService;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceRecordVo;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectStaffDeviceVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    @Autowired
    private ProjectPersonDeviceService projectPersonDeviceService;

    @Autowired
    private ProjectPersonPlanRelService projectPersonPlanRelService;

    @Autowired
    private ProjectRightDeviceService projectRightDeviceService;



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
