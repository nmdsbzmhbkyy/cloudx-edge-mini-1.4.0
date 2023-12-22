package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.common.entity.vo.VisitorHisVo;
import com.aurine.cloudx.open.origin.entity.ProjectVisitorHis;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 访客记录
 *
 * @author 王伟
 * @date 2020-06-03 19:43:06
 */
public interface ProjectVisitorHisService extends IService<ProjectVisitorHis> {

    boolean saveHistory(ProjectVisitorHis projectVisitorHis);

    /**
     * <p>
     * 如果有未签离的返回false
     * </p>
     *
     * @param visitorId 访客id
     * @return 有未签离返回true
     * @author: 王良俊
     */
    boolean checkVisitorById(String visitorId);

    /**
     * <p>
     * 判断是否可以进行重新申请操作
     * </p>
     *
     * @param visitorId 访客id
     * @param visitId   访客申请id
     * @return 重新申请时有其他非本次申请的申请返回true
     * @author: 王良俊
     */
    boolean checkVisitorById(String visitorId, String visitId);

    /**
     * 获取当前访客数量
     *
     * @return
     * @author: 王伟
     * @since ：2020-09-03
     */
    Integer countCurrVisitor();

    /**
     * 获取30天内访客人数最多的一天访客数量和所在日期
     *
     * @return
     */
    Map<String, Object> count30DayMostestVisitorAndDate();


    /**
     * 根据传入的访客ID和访问时间对访客的访问时间进行更新
     * 已有访问时间则不会进行更新
     *
     * @param visitorId 访客ID
     * @param visitTime 访问时间 如：2020-09-07 10:48:12
     * @return 访客记录ID visitId
     * @author: 王良俊
     */
    String saveVisitTime(String visitorId, LocalDateTime visitTime);

    /**
     * <p>
     * 根据时间获取这个时间能进行下发的任务
     * </p>
     *
     * @param time 时间 必须精确到分
     */
    List<String> getAllUnSendCertList(String time);

    void getTimeOutVisitor();

    Integer countByOff();

    /**
     * 分页查询
     *
     * @param page
     * @param vo
     * @return
     */
    Page<VisitorHisVo> page(Page page, VisitorHisVo vo);

}
