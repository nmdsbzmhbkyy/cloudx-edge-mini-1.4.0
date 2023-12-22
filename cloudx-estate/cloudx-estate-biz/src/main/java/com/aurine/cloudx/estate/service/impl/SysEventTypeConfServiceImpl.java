package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.SysEventTypeConf;
import com.aurine.cloudx.estate.entity.SysExpertPlanEventTypeRel;
import com.aurine.cloudx.estate.mapper.SysEventTypeConfMapper;
import com.aurine.cloudx.estate.service.SysEventTypeConfService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 谢泽毅
 */
@Service
@AllArgsConstructor
public class SysEventTypeConfServiceImpl extends ServiceImpl<SysEventTypeConfMapper, SysEventTypeConf> implements SysEventTypeConfService {

    @Override
    public Page<SysEventTypeConf> pageAlarmType(Page page, SysEventTypeConf sysEventTypeConf) {
        return baseMapper.pageAlarmType(page, sysEventTypeConf);
    }

    @Override
    public R updateAlarmType(SysEventTypeConf sysEventTypeConf) {
        LambdaUpdateWrapper<SysEventTypeConf> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysEventTypeConf::getEventTypeId,sysEventTypeConf.getEventTypeId());
        updateWrapper.eq(SysEventTypeConf::getDeviceType,sysEventTypeConf.getDeviceType());
        updateWrapper.set(SysEventTypeConf::getEventCategory,sysEventTypeConf.getEventCategory());
        updateWrapper.set(SysEventTypeConf::getEventLevel,sysEventTypeConf.getEventLevel());
        updateWrapper.set(SysEventTypeConf::getOperator,SecurityUtils.getUser().getId());
        int count = baseMapper.update(null, updateWrapper);
        System.out.println(count);
        if(count > 0){
           return R.ok("修改成功");
        }else{
            return R.ok("修改失败");
        }
    }

    @Override
    public Page<SysEventTypeConf> pageAlarmTypeByPlanId(Page page, SysExpertPlanEventTypeRel sysExpertPlanEventTypeRel) {
        return baseMapper.pageAlarmTypeByPlanId(page, sysExpertPlanEventTypeRel);
    }

    @Override
    public List getEventTypeIdListByDeviceType(String deviceType) {
        QueryWrapper<SysEventTypeConf> sysEventTypeConfQueryWrapper = Wrappers.query();
        sysEventTypeConfQueryWrapper.select("eventTypeId").eq("deviceType",deviceType);
        return baseMapper.selectObjs(sysEventTypeConfQueryWrapper);
    }
}
