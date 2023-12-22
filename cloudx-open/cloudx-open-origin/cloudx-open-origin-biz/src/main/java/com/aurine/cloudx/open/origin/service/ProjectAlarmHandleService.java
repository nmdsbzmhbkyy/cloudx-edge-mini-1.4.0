

package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.common.entity.vo.AlarmHandleVo;
import com.aurine.cloudx.open.origin.entity.ProjectAlarmHandle;
import com.aurine.cloudx.open.origin.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 分页查询
     *
     * @param page
     * @param vo
     * @return
     */
    Page<AlarmHandleVo> page(Page page, AlarmHandleVo vo);

}
