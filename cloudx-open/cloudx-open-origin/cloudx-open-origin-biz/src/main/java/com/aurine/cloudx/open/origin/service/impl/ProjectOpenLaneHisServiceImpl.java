package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.open.origin.entity.ProjectOpenLaneHis;
import com.aurine.cloudx.open.origin.vo.OpenLaneHisQueryVo;
import com.aurine.cloudx.open.origin.vo.ProjectOpenLaneHisVo;
import com.aurine.cloudx.open.origin.mapper.ProjectOpenLaneHisMapper;
import com.aurine.cloudx.open.origin.service.ProjectOpenLaneHisService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 车道开闸记录service
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/27 10:47
 */
@Slf4j
@Service
public class ProjectOpenLaneHisServiceImpl extends ServiceImpl<ProjectOpenLaneHisMapper, ProjectOpenLaneHis> implements ProjectOpenLaneHisService {

    @Override
    public R<IPage<ProjectOpenLaneHisVo>> fetchList(Page page, OpenLaneHisQueryVo query) {
        return R.ok(baseMapper.fetchList(page, query));
    }

    @Override
    public void updateImage(String parkId, String laneId, String localImageUrl) {
        List<ProjectOpenLaneHis> plateNumberList = this.list(new LambdaQueryWrapper<ProjectOpenLaneHis>()
                .eq(ProjectOpenLaneHis::getParkId, parkId)
                .eq(ProjectOpenLaneHis::getLaneId, laneId)
                .eq(ProjectOpenLaneHis::getType, '1')
                .orderByDesc(ProjectOpenLaneHis::getSeq)
                .last("limit 1")
        );
        if (CollUtil.isNotEmpty(plateNumberList)) {
            ProjectOpenLaneHis openLaneHis = plateNumberList.get(0);
            if (StrUtil.isEmpty(openLaneHis.getPicUrl())) {
                openLaneHis.setPicUrl(localImageUrl);
                this.updateById(openLaneHis);
            }
        }
    }

}
