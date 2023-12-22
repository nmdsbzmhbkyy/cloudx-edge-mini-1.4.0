package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.util.NoticeUtil;
import com.aurine.cloudx.estate.constant.VisitorIsLeaveConstant;
import com.aurine.cloudx.estate.constant.enums.DataOriginExEnum;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.ProjectVisitorHis;
import com.aurine.cloudx.estate.mapper.ProjectVisitorHisMapper;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.service.ProjectVisitorHisService;
import com.aurine.cloudx.estate.vo.ProjectVisitorVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 访客记录
 *
 * @author 王伟
 * @date 2020-06-03 19:43:06
 */
@Service
public class ProjectVisitorHisServiceImpl extends ServiceImpl<ProjectVisitorHisMapper, ProjectVisitorHis> implements ProjectVisitorHisService {
    @Resource
    private NoticeUtil noticeUtil;

    @Resource
    private ProjectPersonInfoService projectPersonInfoService;

    @Override
    public boolean saveHistory(ProjectVisitorHis projectVisitorHis) {
        return this.saveOrUpdate(projectVisitorHis);
    }

    @Override
    public boolean checkVisitorById(String visitorId) {
        int count = this.count(new QueryWrapper<ProjectVisitorHis>().lambda().eq(ProjectVisitorHis::getVisitorId, visitorId).eq(ProjectVisitorHis::getIsLeave, "0"));
        /*如果有未签离的返回true*/
        return count != 0;
    }

    @Override
    public boolean checkVisitorById(String visitorId, String visitId) {
        int count = this.count(new QueryWrapper<ProjectVisitorHis>().lambda()
                .eq(ProjectVisitorHis::getVisitorId, visitorId).eq(ProjectVisitorHis::getIsLeave, "0")
                .notIn(ProjectVisitorHis::getVisitId, visitId));
        /*如果有未签离的返回true*/
        return count != 0;
    }

    /**
     * 获取当前访客数量
     *
     * @return
     * @author: 王伟
     * @since ：2020-09-03
     */
    @Override
    public Integer countCurrVisitor() {
        return this.baseMapper.countVisitor();
    }

    /**
     * 获取30天内访客人数最多的一天访客数量和所在日期
     *
     * @return
     */
    @Override
    public Map<String, Object> count30DayMostestVisitorAndDate() {
        return this.baseMapper.count30DayVisitor();
    }

    @Override
    public String saveVisitTime(String visitorId, LocalDateTime visitTime) {
        if (StrUtil.isNotBlank(visitorId)) {
            List<ProjectVisitorHis> visitorHisList = this.list(new QueryWrapper<ProjectVisitorHis>().lambda()
                    .eq(ProjectVisitorHis::getVisitId, visitorId).eq(ProjectVisitorHis::getIsLeave, VisitorIsLeaveConstant.UNLEAVE));
            if (CollUtil.isNotEmpty(visitorHisList)) {
                ProjectVisitorHis projectVisitorHis = visitorHisList.get(0);
                if (projectVisitorHis.getVisitTime() == null) {
                    projectVisitorHis.setVisitTime(visitTime);
                    this.updateById(projectVisitorHis);
                }
                return projectVisitorHis.getVisitId();
            }
        }
        return "";
    }

    @Override
    public List<String> getAllUnSendCertList(String time) {
        return baseMapper.getCurrentUnSendCertList(time);
    }

    /**
     * 更新审核超时的访客数据,并发送推送消息
     *
     * @return
     */
    @Override
    public void getTimeOutVisitor() {
        List<ProjectVisitorVo> projectVisitorHis = baseMapper.getTimeOutVisitor();

        for (ProjectVisitorVo projectVisitor : projectVisitorHis) {

            if (DataOriginExEnum.YZ.code.equals(projectVisitor.getOriginEx())) {
                ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(projectVisitor.getOperator());
                noticeUtil.send(false, "访客超期未审核", "您邀请的访客" + projectVisitor.getPersonName() + "审核超时未通过", projectPersonInfo.getPersonId());
            }

        }
        baseMapper.updateAuditStatusTimeOut();
    }

}
