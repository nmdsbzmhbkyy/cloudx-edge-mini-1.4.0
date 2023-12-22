

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectDeviceGatherAlarmRule;
import com.aurine.cloudx.estate.vo.ProjectDeviceGatherAlarmRuleVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备聚集报警规则表
 *
 * @author 黄健杰
 * @date 2022-01-27 09:28:42
 */
@Mapper
public interface ProjectDeviceGatherAlarmRuleMapper extends BaseMapper<ProjectDeviceGatherAlarmRule> {

    ProjectDeviceGatherAlarmRuleVo getProjectDeviceGatherAlarmRuleVoByDeviceId(@Param("id")String deviceId);

    Integer updateByDeviceId(@Param("param") ProjectDeviceGatherAlarmRule rule);

}
