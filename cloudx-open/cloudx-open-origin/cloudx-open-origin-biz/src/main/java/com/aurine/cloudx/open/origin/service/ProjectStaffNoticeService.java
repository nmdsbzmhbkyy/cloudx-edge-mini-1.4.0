package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.vo.ProjectStaffNoticeAddVo;
import com.aurine.cloudx.open.origin.vo.ProjectStaffNoticeVo;
import com.aurine.cloudx.open.origin.entity.ProjectStaffNotice;
import com.aurine.cloudx.open.origin.vo.ProjectPersonNoticeAddVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 员工通知发布
 *
 * @author guhl@aurine.cn
 * @date 2020-07-06 11:16:25
 */
public interface ProjectStaffNoticeService extends IService<ProjectStaffNotice> {


    

    /**
     * 保存并发送消息
     *
     * @param projectStaffNoticeAddVo
     * @return
     */
    Integer saveByUserIds(ProjectStaffNoticeAddVo projectStaffNoticeAddVo);

    /**
     * 保存业主消息
     * @param projectStaffNoticeAddVo
     * @return
     */
    void saveByPersonIds(ProjectPersonNoticeAddVo projectStaffNoticeAddVo);

    /**
     * 检索员工通知
     * @param vo
     * @return
     */
    Page<ProjectStaffNoticeVo> getByStaff(Page page, ProjectStaffNoticeVo vo);
    
    /**
     * 统计未读消息
     * @return
     */
    Integer countUnRead();

    /**
     * 根据用户查询消息(微信相关接口)
     * @param page
     * @param userId
     * @return
     */
    Page<ProjectStaffNoticeVo> pageByPerson(Page page, String userId);


    /**
     * 消息未读数量
     * @param personId
     * @return
     */
    Integer countByPersonId(String personId);

    Integer countByStaffId(String personId);

    /**
     * 异步保存消息
     */
    void sendMessage(boolean isStaff, String noticeTitle, String content, List<String> userIds);



    /**
     * 根据主键发送消息内容
     * @param noticeId
     * @param isStaff
     * @param noticeTitle
     * @param content
     * @param userId
     */
    void sendMessageOnId(String noticeId, boolean isStaff, String noticeTitle, String content, String userId);
}
