package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectStaffNoticeObject;
import com.aurine.cloudx.estate.mapper.ProjectStaffNoticeObjectMapper;
import com.aurine.cloudx.estate.service.ProjectStaffNoticeObjectService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 员工通知下发对象设置
 *
 * @author guhl@aurine.cn
 * @date 2020-07-06 11:17:34
 */
@Service
@AllArgsConstructor
public class ProjectStaffNoticeObjectServiceImpl extends ServiceImpl<ProjectStaffNoticeObjectMapper, ProjectStaffNoticeObject> implements ProjectStaffNoticeObjectService {

    @Override
    public boolean updateNoticeStatus(String userId, List<String > noticeIds) {

        UpdateWrapper<ProjectStaffNoticeObject> updateWrapper = new UpdateWrapper<ProjectStaffNoticeObject>();
        updateWrapper.eq("userId", userId);
        
        if (noticeIds != null && noticeIds.size() > 0) {
            updateWrapper.in("noticeId",noticeIds);
        }
        
        updateWrapper.set("status", "1");
        
        this.update(updateWrapper);
        
        return true;
    }
}
