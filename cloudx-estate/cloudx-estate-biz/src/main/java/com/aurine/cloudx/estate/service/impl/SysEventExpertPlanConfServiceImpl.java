package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.SysEventExpertPlanConf;
import com.aurine.cloudx.estate.mapper.SysEventExpertPlanConfMapper;
import com.aurine.cloudx.estate.service.SysEventExpertPlanConfService;
import com.aurine.cloudx.estate.vo.SysEventExpertPlanConfVo;
import com.aurine.cloudx.estate.vo.SysEventTypeConfVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author 谢泽毅
 */
@Service
@AllArgsConstructor
public class SysEventExpertPlanConfServiceImpl extends ServiceImpl<SysEventExpertPlanConfMapper, SysEventExpertPlanConf> implements SysEventExpertPlanConfService {

    @Override
    public R saveReturnId(SysEventExpertPlanConf sysExpertPlan) {
        if(sysExpertPlan.getPlanContent().trim() == null || sysExpertPlan.getPlanContent().trim().length() == 0
                || sysExpertPlan.getPlanName().trim() == null || sysExpertPlan.getPlanName().trim().length() == 0){
            return R.failed("预案内容不可为空，请填写");
        }else if(baseMapper.selectOne(new QueryWrapper<SysEventExpertPlanConf>().lambda().
                eq(SysEventExpertPlanConf::getPlanName, sysExpertPlan.getPlanName().trim())) != null){
            return R.failed("预案名称已存在");
        }else {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            sysExpertPlan.setPlanId(uuid);
            sysExpertPlan.setOperator(SecurityUtils.getUser().getId());
            sysExpertPlan.setTenant_Id(TenantContextHolder.getTenantId());
            return R.ok(baseMapper.insert(sysExpertPlan));
        }
    }

    @Override
    public Page<SysEventExpertPlanConfVo> pageExpertPlan(SysEventExpertPlanConfVo sysExpertPlan, Page page) {
        return baseMapper.pageExpertPlan(sysExpertPlan,page);
    }

    @Override
    public List<SysEventTypeConfVo> getExpertPlanEventTypeRelList(SysEventTypeConfVo sysEventTypeConfVo) {
        return baseMapper.getExpertPlanEventTypeRelList(sysEventTypeConfVo);
    }

    @Override
    public SysEventExpertPlanConf getExpertPlanContentById(SysEventExpertPlanConf sysExpertPlan) {
        return baseMapper.selectOne(new QueryWrapper<SysEventExpertPlanConf>().lambda().eq(SysEventExpertPlanConf::getPlanId, sysExpertPlan.getPlanId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByPlanId(String planId) {
        baseMapper.deleteRel(planId);
        return baseMapper.delete(new QueryWrapper<SysEventExpertPlanConf>().lambda().eq(SysEventExpertPlanConf::getPlanId, planId)) > -1;

    }

    @Override
    public R updataExpertPlanById(SysEventExpertPlanConf sysExpertPlan) {
        if(sysExpertPlan.getPlanContent().trim() == null || sysExpertPlan.getPlanContent().trim().length() == 0
                || sysExpertPlan.getPlanName().trim() == null || sysExpertPlan.getPlanName().trim().length() == 0){
            return R.failed("预案内容不可为空，请填写");
        }

        SysEventExpertPlanConf checkSysExpertPlan = baseMapper.selectOne(new QueryWrapper<SysEventExpertPlanConf>().lambda()
                .eq(SysEventExpertPlanConf::getPlanName, sysExpertPlan.getPlanName().trim())
                .ne(SysEventExpertPlanConf::getPlanId,sysExpertPlan.getPlanId()));

        if (checkSysExpertPlan != null){
            return R.failed("预案名称已存在");
        } else {
            LambdaUpdateWrapper<SysEventExpertPlanConf> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(SysEventExpertPlanConf::getPlanId,sysExpertPlan.getPlanId());
            updateWrapper.set(SysEventExpertPlanConf::getPlanName,sysExpertPlan.getPlanName());
            updateWrapper.set(SysEventExpertPlanConf::getPlanContent,sysExpertPlan.getPlanContent());
            updateWrapper.set(SysEventExpertPlanConf::getOperator,SecurityUtils.getUser().getId());
            baseMapper.update(null,updateWrapper);
            return R.ok("修改成功");
        }
    }

    @Override
    public List<SysEventExpertPlanConf> getExpertPlanListByEventTypeId(String eventTypeId) {
        //通过事件类型id(eventTypeId)获取关联的预案id(planId)
        //通过预案id获取匹配的列表

        return baseMapper.getExpertPlanListByEventTypeId(eventTypeId);
    }

}
