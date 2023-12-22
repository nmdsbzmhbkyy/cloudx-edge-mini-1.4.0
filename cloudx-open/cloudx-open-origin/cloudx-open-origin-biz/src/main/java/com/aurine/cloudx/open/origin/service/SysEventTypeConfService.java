package com.aurine.cloudx.open.origin.service;


import com.aurine.cloudx.open.origin.entity.SysEventTypeConf;
import com.aurine.cloudx.open.origin.entity.SysExpertPlanEventTypeRel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * 报警类型定义管理
 *
 * @author 谢泽毅
 * @date 2021-07-08 08:58:01
 */
public interface SysEventTypeConfService extends IService<SysEventTypeConf> {
    /**
     * 分页查询报警类型
     * @param page
     * @param sysEventTypeConf
     * @return
     */
    Page<SysEventTypeConf> pageAlarmType(Page page, SysEventTypeConf sysEventTypeConf);

    /**
     * 修改报警级别和报警分类
     * @param sysEventTypeConf
     * @return
     */
    R updateAlarmType(SysEventTypeConf sysEventTypeConf);

    /**
     * 通过预案id获取关联报警
     * @param page
     * @param sysExpertPlanEventTypeRel
     * @return
     */
    Page<SysEventTypeConf> pageAlarmTypeByPlanId(Page page, SysExpertPlanEventTypeRel sysExpertPlanEventTypeRel);

    List getEventTypeIdListByDeviceType(String deviceType);
}
