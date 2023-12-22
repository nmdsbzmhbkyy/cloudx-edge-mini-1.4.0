package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.entity.ProjectPatrolDetail;
import com.aurine.cloudx.estate.mapper.ProjectPatrolDetailMapper;
import com.aurine.cloudx.estate.service.ProjectPatrolDetailService;
import com.aurine.cloudx.estate.service.ProjectPatrolPersonService;
import com.aurine.cloudx.estate.vo.ProjectPatrolDetaiInfolVo;
import com.aurine.cloudx.estate.vo.ProjectPatrolDetailVo;
import com.aurine.cloudx.estate.vo.ProjectPatrolPersonVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 巡更明细表(ProjectPatrolDetail)表服务实现类
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-11 11:57:27
 */
@Service
public class ProjectPatrolDetailServiceImpl extends ServiceImpl<ProjectPatrolDetailMapper, ProjectPatrolDetail> implements ProjectPatrolDetailService {

    @Resource
    ProjectPatrolDetailService projectPatrolDetailService;
    @Resource
    ProjectPatrolPersonService projectPatrolPersonService;


    @Override
    public ProjectPatrolDetaiInfolVo getDetailListByPatrolId(String patrolId) {
        List<ProjectPatrolDetailVo> patrolDetailVoList = new ArrayList<>();
        List<ProjectPatrolDetail> patrolDetailList = projectPatrolDetailService.list(new QueryWrapper<ProjectPatrolDetail>().lambda()
                .eq(ProjectPatrolDetail::getPatrolId, patrolId));
        if (CollUtil.isNotEmpty(patrolDetailList)) {
            patrolDetailList.forEach(patrolDetail -> {
                ProjectPatrolDetailVo patrolDetailVo = new ProjectPatrolDetailVo();
                List<ProjectPatrolPersonVo> patrolPersonVoList = projectPatrolPersonService.listByDetailId(patrolDetail.getPatrolDetailId());
                BeanUtil.copyProperties(patrolDetail, patrolDetailVo);
                patrolDetailVo.setPatrolPersonVoList(patrolPersonVoList);
                patrolDetailVoList.add(patrolDetailVo);
            });
        }
        // 未巡数
        Integer unpatrolNum = projectPatrolPersonService.countUnpatrolByPatrolId(patrolId);
        // 已巡数
        Integer patrolNum = projectPatrolPersonService.countPatrolByPatrolId(patrolId);
        // 巡更结果正常数
        Integer normalNum = projectPatrolPersonService.countNormalByPatrolId(patrolId, "1");
        // 巡更结果异常数
        Integer unNormalNum = projectPatrolPersonService.countNormalByPatrolId(patrolId, "2");
        // 签到正常数
        Integer signNormalNum = projectPatrolPersonService.countTimeOutByPatrolId(patrolId, "1");
        // 签到异常数
        Integer signUnNormalNum = projectPatrolPersonService.countTimeOutByPatrolId(patrolId, "0");

        if (unpatrolNum == null) {
            unpatrolNum = 0;
        }
        if (patrolNum == null) {
            patrolNum = 0;
        }
        ProjectPatrolDetaiInfolVo patrolDetaiInfolVo = new ProjectPatrolDetaiInfolVo();
        patrolDetaiInfolVo.setUnpatrolNum(unpatrolNum);
        patrolDetaiInfolVo.setPatrolNum(patrolNum);
        patrolDetaiInfolVo.setNormalNum(normalNum);
        patrolDetaiInfolVo.setUnNormalNum(unNormalNum);
        patrolDetaiInfolVo.setSignNormalNum(signNormalNum);
        patrolDetaiInfolVo.setSignUnNormalNum(signUnNormalNum);

        patrolDetaiInfolVo.setPatrolDetailVoList(patrolDetailVoList);
        return patrolDetaiInfolVo;
    }
}