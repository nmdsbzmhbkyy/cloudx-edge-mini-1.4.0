

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceGatherAlarmRule;
import com.aurine.cloudx.estate.vo.ProjectDeviceGatherAlarmRuleVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 设备拓展属性表
 *
 * @author 黄健杰
 * @date 2022-02-07
 */
public interface ProjectDeviceGatherAlarmRuleService extends IService<ProjectDeviceGatherAlarmRule> {
    ProjectDeviceGatherAlarmRuleVo getProjectDeviceGatherAlarmRuleVoByDeviceId(String deviceId);
    void removeByDeviceId(String deviceId);
    Integer updateByDeviceId(ProjectDeviceGatherAlarmRule rule);
}
