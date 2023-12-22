package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.SysExpertPlanEventTypeRel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * @author 谢泽毅
 */
public interface SysExpertPlanEventTypeRelService extends IService<SysExpertPlanEventTypeRel> {

    /**
     * 修改预案关联事件类型
     * @param deviceType
     * @param eventTypeList
     * @param planId
     * @return
     */
    R updateExpertPlanEventTypeRel(String deviceType, String eventTypeList, String planId);
}
