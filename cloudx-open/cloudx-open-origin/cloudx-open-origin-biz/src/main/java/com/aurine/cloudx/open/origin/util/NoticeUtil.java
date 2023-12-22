package com.aurine.cloudx.open.origin.util;

import cn.hutool.core.collection.ListUtil;
import com.aurine.cloudx.open.origin.service.ProjectStaffNoticeService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * (NoticeUtil)人员消息工具
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/4 15:37
 */
@Component
public class NoticeUtil
//        implements ApplicationContextAware
{

    @Resource
    private ProjectStaffNoticeService projectStaffNoticeService;

    /**
     * 发送消息
     *
     * @param noticeTitle 标题
     * @param content     内容
     * @param userIds     人员id列表
     */

    public void send(boolean isStaff, String noticeTitle, String content, List<String> userIds) {
        try {
            projectStaffNoticeService.sendMessage(isStaff, noticeTitle, content, userIds);
        } catch (Exception e) {
            e.printStackTrace();
        }

        }

    /**
     * 发送消息
     *
     * @param noticeTitle 标题
     * @param content     内容
     * @param userIds     人员id
     */

    public void send(boolean isStaff, String noticeTitle, String content, String userIds) {
        send(isStaff, noticeTitle, content, ListUtil.toList(userIds));
    }

    public void send(String noticeId,boolean isStaff, String noticeTitle, String content, String userId) {
        projectStaffNoticeService.sendMessageOnId(noticeId,isStaff,noticeTitle,content,userId);
    }

//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        projectStaffNoticeService = applicationContext.getBean(ProjectStaffNoticeService.class);
//    }
}
