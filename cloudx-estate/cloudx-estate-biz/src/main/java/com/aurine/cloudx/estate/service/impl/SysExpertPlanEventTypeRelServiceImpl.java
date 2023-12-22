package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.SysEventExpertPlanConf;
import com.aurine.cloudx.estate.entity.SysExpertPlanEventTypeRel;
import com.aurine.cloudx.estate.mapper.SysExpertPlanEventTypeRelMapper;
import com.aurine.cloudx.estate.service.SysEventExpertPlanConfService;
import com.aurine.cloudx.estate.service.SysEventTypeConfService;
import com.aurine.cloudx.estate.service.SysExpertPlanEventTypeRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author 谢泽毅
 */
@Service
@AllArgsConstructor
public class SysExpertPlanEventTypeRelServiceImpl extends ServiceImpl<SysExpertPlanEventTypeRelMapper, SysExpertPlanEventTypeRel> implements SysExpertPlanEventTypeRelService {

    @Resource
    private SysEventTypeConfService sysEventTypeConfService;

    @Resource
    private SysEventExpertPlanConfService sysEventExpertPlanConfService;

    /**
     * 修改预案关联事件类型
     * @param deviceType
     * @param eventTypeIdString
     * @param planId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateExpertPlanEventTypeRel(String deviceType, String eventTypeIdString, String planId) {
        //判断预案id是否存在
        SysEventExpertPlanConf sysExpertPlan = new SysEventExpertPlanConf();
        sysExpertPlan.setPlanId(planId);
        SysEventExpertPlanConf isExistSysExpertPlan = sysEventExpertPlanConfService.getExpertPlanContentById(sysExpertPlan);
        if (isExistSysExpertPlan==null){
            return R.failed("修改失败");
        }
        List eventTypeIdListByDeviceTypeList = sysEventTypeConfService.getEventTypeIdListByDeviceType(deviceType);
        eventTypeIdListByDeviceTypeList.forEach(System.out::println);
        if(eventTypeIdListByDeviceTypeList == null || eventTypeIdListByDeviceTypeList.size() ==0 ){
            return R.ok("修改成功");
        }
        if(eventTypeIdString == null || eventTypeIdString.trim().length() == 0){
            baseMapper.deleteExpertPlanEventTypeRel(eventTypeIdListByDeviceTypeList,planId);
            return R.ok("修改成功");
        }
        //判断设备类型获取的事件类型eventTypeId列表是否包含传来的事件类型eventTypeId列表
        List<String> eventTypeIdList = Arrays.asList(eventTypeIdString.split(","));
        if(eventTypeIdListByDeviceTypeList.containsAll(eventTypeIdList)){
            baseMapper.deleteExpertPlanEventTypeRel(eventTypeIdListByDeviceTypeList,planId);
            baseMapper.insertExpertPlanEventTypeRel(eventTypeIdString,planId,SecurityUtils.getUser().getId(),
                    TenantContextHolder.getTenantId());
            return R.ok("修改成功");
        }else {
            return R.failed("修改失败");
        }
    }
}
