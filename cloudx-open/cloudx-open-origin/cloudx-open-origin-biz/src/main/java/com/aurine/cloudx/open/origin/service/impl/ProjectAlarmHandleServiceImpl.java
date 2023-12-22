
package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.common.entity.vo.AlarmHandleVo;
import com.aurine.cloudx.open.origin.entity.ProjectAlarmHandle;
import com.aurine.cloudx.open.origin.mapper.ProjectAlarmHandleMapper;
import com.aurine.cloudx.open.origin.service.ProjectAlarmHandleService;
import com.aurine.cloudx.open.origin.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    //确认类
    private static final String DETERMINE_CLASS = "1";

    @Override
    public boolean save(ProjectEntranceAlarmEventVo vo) {
        ProjectAlarmHandle projectAlarmHandle = new ProjectAlarmHandle();
        //vo转po
        BeanUtils.copyProperties(vo, projectAlarmHandle);
        projectAlarmHandle.setPicUrl(null);
        projectAlarmHandle.setOperator(vo.getHandleOperator());
        //保存当前时间为事件开始处理时间
        projectAlarmHandle.setHandleBeginTime(LocalDateTime.now());
        if (DETERMINE_CLASS.equals(vo.getEventCategory())) {
            projectAlarmHandle.setHandleEndTime(LocalDateTime.now());
        }
        return this.save(projectAlarmHandle);
    }

    @Override
    public boolean updateById(ProjectEntranceAlarmEventVo vo) {
        ProjectAlarmHandle projectAlarmHandle = new ProjectAlarmHandle();
        //vo转po

        BeanUtils.copyProperties(vo, projectAlarmHandle);
        projectAlarmHandle.setSeq(vo.getHandleSeq());
        projectAlarmHandle.setPicUrl(vo.getLivePic());
        projectAlarmHandle.setOperator(vo.getHandleOperator());
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

    @Override
    public Page<AlarmHandleVo> page(Page page, AlarmHandleVo vo) {
        ProjectAlarmHandle po = new ProjectAlarmHandle();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }
}
