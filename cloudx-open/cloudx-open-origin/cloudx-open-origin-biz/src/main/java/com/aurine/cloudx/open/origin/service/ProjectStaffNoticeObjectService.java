package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.ProjectStaffNoticeObject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 员工通知下发对象设置
 *
 * @author guhl@aurine.cn
 * @date 2020-07-06 11:17:34
 */
public interface ProjectStaffNoticeObjectService extends IService<ProjectStaffNoticeObject> {
    /**
     * 消息已读
     *
     * @param userId
     * @param noticeIds
     * @return
     */
    boolean updateNoticeStatus(String userId, List<String> noticeIds);
}
