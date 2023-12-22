

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectAlarmHandle;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 报警事件处理
 *
 * @author 黄阳光
 * @date 2020-06-04 08:31:21
 */
public interface ProjectAlarmHandleService extends IService<ProjectAlarmHandle> {

    boolean save(ProjectEntranceAlarmEventVo vo);

    boolean updateById(ProjectEntranceAlarmEventVo vo);

}
