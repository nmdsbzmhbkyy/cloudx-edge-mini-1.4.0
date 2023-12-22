package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PassMacroIdEnum;
import com.aurine.cloudx.estate.mapper.ProjectPersonDeviceMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (WebProjectRightDeviceServiceImpl)
 * 当前类用于解决因@Async注解引起的循环引用问题
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/8 13:57
 */
@Service
@Slf4j
public class ProjectRightDeviceServiceProxyImpl extends ProjectRightDeviceServiceImpl {

    @Resource
    private ProjectPassPlanPolicyRelService projectPassPlanPolicyRelService;
    @Resource
    private ProjectPersonDeviceMapper projectPersonDeviceMapper;
    @Resource
    @Lazy
    private ProjectPersonLiftRelService projectPersonLiftRelService;
    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;



    /**
     * <p>
     * 在配置了不同新的设备列表后更新介质和设备的关系
     * </p>
     *
     * @param personId 人员id
     * @return 处理结果
     * @author: 王良俊
     */
    @Override
    @Async
    public void saveRelationshipProxy(String planId, String personId ,Integer projectId) {
        ProjectContextHolder.setProjectId(projectId);
        //电梯方案修改时没有门禁通行方案id更具personId查询
        if(planId == null){
            planId = projectPersonPlanRelService.getPlanIdByPersonId(personId);
        }
        List<String> macroList = projectPassPlanPolicyRelService.listMacroIdListByPlanId(planId);
        List<ProjectPassDeviceVo> newDeviceByPlanList = projectPersonDeviceMapper.listByPerson(
                personId, null,
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.GATE.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.FRAME_GATE.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.BUILDING_LADDER.name())),
                macroList.stream().anyMatch(e -> e.equals(PassMacroIdEnum.UNIT_LADDER.name())),
                false
        );
        List<String> deviceIdList = newDeviceByPlanList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());

        //兼顾电梯方案关联的设备，否者电梯方案关联的设备会被删除
        List<ProjectPassDeviceVo> newLiftByPlanList = projectPersonLiftRelService.listLiftByPersonId(personId, planId);
        List<String> LiftIds = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(newLiftByPlanList)){
            LiftIds = newLiftByPlanList.stream().map(ProjectPassDeviceVo::getDeviceId).collect(Collectors.toList());
        }
        List<String> liftChildDevice;
        liftChildDevice = projectDeviceInfoService.childDeviceIdByLift(LiftIds);
        if(CollectionUtil.isNotEmpty(liftChildDevice)){
            deviceIdList.addAll(liftChildDevice);
        }
        super.saveRelationship(deviceIdList, personId, false);
    }
}
