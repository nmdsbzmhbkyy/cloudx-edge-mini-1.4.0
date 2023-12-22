
package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.open.common.entity.vo.PersonPlanRelVo;
import com.aurine.cloudx.open.origin.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.open.origin.mapper.ProjectPersonPlanRelMapper;
import com.aurine.cloudx.open.origin.service.ProjectPersonPlanRelService;
import com.aurine.cloudx.open.origin.service.ProjectRightDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>人员方案关系管理实现</p>
 *
 * @ClassName: ProjectPersonPlanRelServiceImpl
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 10:02
 * @Copyright:
 */
@Service
@Slf4j
public class ProjectPersonPlanRelServiceImpl extends ServiceImpl<ProjectPersonPlanRelMapper, ProjectPersonPlanRel> implements ProjectPersonPlanRelService {

    @Autowired
    private ProjectRightDeviceService projectRightDeviceService;
//    @Autowired
//    private TaskUtil taskUtil;

//    @Resource
//    private AbstractProjectPersonDeviceService abstractWebProjectPersonDeviceService;

    /**
     * 根据personId，保存或更新方案
     *
     * @param projectPersonPlanRel
     * @return
     */
    @Override
    public boolean saveOrUpdateByPersonId(ProjectPersonPlanRel projectPersonPlanRel) {
        List<ProjectPersonPlanRel> relList = this.list(new QueryWrapper<ProjectPersonPlanRel>().lambda().eq(ProjectPersonPlanRel::getPersonId, projectPersonPlanRel.getPersonId()));

        if (CollUtil.isNotEmpty(relList)) {
            //修改
            return true;
        } else {
            return this.save(projectPersonPlanRel);
        }
    }

    /**
     * 验证所选通行方案是否需要变更
     *
     * @param personId
     * @param planId
     * @return
     */
    @Override
    public boolean checkChange(String personId, String planId) {
        List<ProjectPersonPlanRel> list = this.list(new QueryWrapper<ProjectPersonPlanRel>()
                .lambda()
                .eq(ProjectPersonPlanRel::getPersonId, personId));

        if (CollectionUtil.isNotEmpty(list)) {
            if (list.size() > 0) {
                //未改变
                // 方案变更
                return planId.equals(list.get(0).getPlanId());
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 根据personId获取该人员使用的方案
     *
     * @param personId
     * @return
     */
    @Override
    public String getPlanIdByPersonId(String personId) {
        List<ProjectPersonPlanRel> list = this.list(new QueryWrapper<ProjectPersonPlanRel>().lambda().eq(ProjectPersonPlanRel::getPersonId, personId));
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0).getPlanId();
        } else {
            return "";
        }
    }


    /**
     * 返回当前项目过期的人员
     *
     * @return
     */
    @Override
    public List<ProjectPersonPlanRel> listPersonPlanRelExpDate() {
        return this.baseMapper.getExpList();
    }

    /**
     * 根据personId获取该人员使用的方案
     *
     * @param personId
     * @return
     */
    @Override
    public ProjectPersonPlanRel getPoByPersonId(String personId) {
        List<ProjectPersonPlanRel> list = this.list(new QueryWrapper<ProjectPersonPlanRel>().lambda().eq(ProjectPersonPlanRel::getPersonId, personId));
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据PersonId删除人员方案关系
     *
     * @param personId
     * @return
     */
    @Override
    public boolean deleteByPersonId(String personId) {
        return this.remove(new QueryWrapper<ProjectPersonPlanRel>().lambda().eq(ProjectPersonPlanRel::getPersonId, personId));
    }

    /**
     * 根据方案ID,查询到使用方案的用户
     *
     * @param planId
     * @return
     */
    @Override
    public List<ProjectPersonPlanRel> listByPlanId(String planId) {
        return this.list(new QueryWrapper<ProjectPersonPlanRel>().lambda().eq(ProjectPersonPlanRel::getPlanId, planId));
    }

    /**
     * 核对方案是否已经被使用
     *
     * @param planId
     * @return
     */
    @Override
    public boolean checkUsed(String planId) {
        int count = this.count(new QueryWrapper<ProjectPersonPlanRel>().lambda().eq(ProjectPersonPlanRel::getPlanId, planId));
        return count != 0;
    }

    /**
     * 启用person通行
     *
     * @param personId
     * @return
     */
    @Override
    public boolean enablePerson(String personId) {
        return changePersonActive(personId, "1");
    }

    /**
     * 禁用person通行
     *
     * @param personId
     * @return
     */
    @Override
    public boolean disablePerson(String personId) {
        return changePersonActive(personId, "0");
    }

    @Override
    public void initTodayTask() {
//        List<ProjectPersonPlanRel> todayExpList = baseMapper.getTodayExpList();
//        todayExpList.forEach(projectPersonPlanRel -> {
//            if (TaskUtil.isToday(projectPersonPlanRel.getExpTime())) {
//                taskUtil.addDelayTaskAsync(new PersonDelayTask(ProjectContextHolder.getProjectId(),
//                        projectPersonPlanRel.getPersonId(), projectPersonPlanRel.getExpTime(),
//                        PersonTypeEnum.getEnum(projectPersonPlanRel.getPersonType()),
//                        DelayTaskTopicEnum.householderExp));
//            }
//        });

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleAllTimeOutRight(String timeStr) {
        log.info("自动到期停用：{}", timeStr);
        List<String> personIdList = baseMapper.getAllTimeOutRightPerson(timeStr);
        List<String> staffIdList = baseMapper.getAllTimeOutRightStaff(timeStr);
        personIdList.addAll(staffIdList);

//        //调用停用接口
//        if (CollUtil.isNotEmpty(personIdList)) {
//            for (String personId : personIdList) {
//                abstractWebProjectPersonDeviceService.disablePassRight(PersonTypeEnum.PROPRIETOR.code, personId);
//            }
//        }
//
//        if (CollUtil.isNotEmpty(staffIdList)) {
//            for (String personId : personIdList) {
//                abstractWebProjectPersonDeviceService.disablePassRight(PersonTypeEnum.STAFF.code, personId);
//            }
//        }

//        if (CollUtil.isNotEmpty(personIdList)) {
//                List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>()
//                        .lambda().in(ProjectRightDevice::getPersonId, personIdList));
//                projectRightDeviceService.delCerts(rightDeviceList);
//        }
    }

    @Override
    public Page<PersonPlanRelVo> page(Page page, PersonPlanRelVo vo) {
        ProjectPersonPlanRel po = new ProjectPersonPlanRel();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }


    /**
     * 激活或禁用人员权限关系
     *
     * @param personId
     * @param active
     * @return
     */
    private boolean changePersonActive(String personId, String active) {
        List<ProjectPersonPlanRel> personPlanRelList = this.list(new QueryWrapper<ProjectPersonPlanRel>().lambda().eq(ProjectPersonPlanRel::getPersonId, personId));

        if (CollUtil.isNotEmpty(personPlanRelList)) {
            personPlanRelList.get(0).setIsActive(active);
            return this.updateById(personPlanRelList.get(0));
        } else {
            return true;
        }
    }

}
