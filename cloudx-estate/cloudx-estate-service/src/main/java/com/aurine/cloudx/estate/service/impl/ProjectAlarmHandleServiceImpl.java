
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectAlarmHandle;
import com.aurine.cloudx.estate.mapper.ProjectAlarmHandleMapper;
import com.aurine.cloudx.estate.service.ProjectAlarmHandleService;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 报警事件处理
 *
 * @author 黄阳光
 * @date 2020-06-04 08:31:21
 */
@Service
public class ProjectAlarmHandleServiceImpl extends ServiceImpl<ProjectAlarmHandleMapper, ProjectAlarmHandle> implements ProjectAlarmHandleService {

    @Override
    public boolean save(ProjectEntranceAlarmEventVo vo) {
        ProjectAlarmHandle projectAlarmHandle = new ProjectAlarmHandle();
        //vo转po
        BeanUtils.copyProperties(vo, projectAlarmHandle);
        projectAlarmHandle.setPicUrl(null);
        //保存当前时间为事件开始处理时间
        projectAlarmHandle.setHandleBeginTime(LocalDateTime.now());
        return this.save(projectAlarmHandle);
    }

    @Override
    public boolean updateById(ProjectEntranceAlarmEventVo vo) {
        ProjectAlarmHandle projectAlarmHandle = new ProjectAlarmHandle();
        //vo转po

        BeanUtils.copyProperties(vo, projectAlarmHandle);
        projectAlarmHandle.setSeq(vo.getHandleSeq());
        projectAlarmHandle.setPicUrl(vo.getLivePic());
        //若时间结束处理时间为null则保存当前时间为事件结束处理时间
        if (projectAlarmHandle.getHandleEndTime() == null) {
            projectAlarmHandle.setHandleEndTime(LocalDateTime.now());
            //计算处理开始到处理结束使用时间，单位为分钟
            Duration duration = Duration.between(projectAlarmHandle.getHandleBeginTime(), projectAlarmHandle.getHandleEndTime());
            projectAlarmHandle.setDealDuration(duration.toMinutes() + "");
        }
        //结束处理时间不为null则表示再次处理
        return super.updateById(projectAlarmHandle);
    }
}
