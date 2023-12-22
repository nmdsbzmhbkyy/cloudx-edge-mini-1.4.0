package com.aurine.cloudx.estate.service;


import com.aurine.cloudx.estate.entity.SysEventExpertPlanConf;
import com.aurine.cloudx.estate.vo.SysEventExpertPlanConfVo;
import com.aurine.cloudx.estate.vo.SysEventTypeConfVo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * @author 谢泽毅
 */
public interface SysEventExpertPlanConfService extends IService<SysEventExpertPlanConf> {

    /**
     * 新增预案
     * @param sysExpertPlan
     * @return
     */
    R saveReturnId(SysEventExpertPlanConf sysExpertPlan);

    /**
     * 分页查询预案
     * @param sysEventExpertPlanConfVo
     * @param page
     * @return
     */
    Page<SysEventExpertPlanConfVo> pageExpertPlan(SysEventExpertPlanConfVo sysEventExpertPlanConfVo, Page page);

    /**
     * 预案设备关联展示
     * @param sysEventTypeConfVo
     * @return
     */
    List<SysEventTypeConfVo> getExpertPlanEventTypeRelList(SysEventTypeConfVo sysEventTypeConfVo);

    /**
     * 预案内容展示
     * @param sysExpertPlan
     * @return
     */
    SysEventExpertPlanConf getExpertPlanContentById(SysEventExpertPlanConf sysExpertPlan);

    /**
     * 删除预案
     * @param planId
     * @return
     */
    Boolean removeByPlanId(String planId);

    /**
     * 通过预案id修改预案信息
     * @param sysEventExpertPlanConf
     * @return
     */
    R updataExpertPlanById(SysEventExpertPlanConf sysEventExpertPlanConf);

    /**
     * 通过事件类型id(eventTypeId)获取关联的预案
     * @param eventTypeId
     * @return
     */
    List<SysEventExpertPlanConf> getExpertPlanListByEventTypeId(String eventTypeId);

}
