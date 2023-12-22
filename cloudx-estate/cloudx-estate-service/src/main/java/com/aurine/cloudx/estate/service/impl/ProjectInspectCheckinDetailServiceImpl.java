package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.entity.ProjectInspectCheckinDetail;
import com.aurine.cloudx.estate.mapper.ProjectInspectCheckinDetailMapper;
import com.aurine.cloudx.estate.service.ProjectInspectCheckinDetailService;
import com.aurine.cloudx.estate.service.ProjectInspectTaskService;
import com.aurine.cloudx.estate.vo.ProjectInspectCheckinDetailVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 巡检点签到明细(ProjectInspectCheckinDetail)表服务实现类
 *
 * @author 王良俊
 * @since 2020-08-04 10:08:52
 */
@Service
public class ProjectInspectCheckinDetailServiceImpl extends ServiceImpl<ProjectInspectCheckinDetailMapper,
        ProjectInspectCheckinDetail> implements ProjectInspectCheckinDetailService {

    @Autowired
    private ProjectInspectTaskService projectInspectTaskService;

    @Override
    public boolean removeByDetailIdList(List<String> detailIdList) {
        if (CollUtil.isNotEmpty(detailIdList)) {
            return this.remove(new QueryWrapper<ProjectInspectCheckinDetail>().lambda().in(ProjectInspectCheckinDetail::getDetailId, detailIdList));
        }
        return true;
    }

    @Override
    public boolean checkin(ProjectInspectCheckinDetailVo checkinDetailVo) {
        ProjectInspectCheckinDetail checkinDetail = new ProjectInspectCheckinDetail();
        BeanUtil.copyProperties(checkinDetailVo, checkinDetail);
        boolean save = this.save(checkinDetail);
        int unCheckinNum = baseMapper.countUnCheckin(checkinDetailVo.getTaskId());
        // 如果为0说明所有巡检点都签到了 这就就要更改任务的完成状态
        if (unCheckinNum == 0) {
            projectInspectTaskService.completeTask(checkinDetailVo.getTaskId());
        }
        return save;
    }


}