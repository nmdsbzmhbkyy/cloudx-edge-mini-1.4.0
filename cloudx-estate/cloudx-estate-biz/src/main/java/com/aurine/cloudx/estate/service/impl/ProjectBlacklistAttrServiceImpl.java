package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectBlacklistAttr;
import com.aurine.cloudx.estate.mapper.ProjectBlacklistAttrMapper;
import com.aurine.cloudx.estate.service.ProjectBlacklistAttrService;
import com.aurine.cloudx.estate.vo.BlacklistAttrSearchCondition;
import com.aurine.cloudx.estate.vo.CarPreRegisterSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectBlacklistAttrVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 黑名单属性(ProjectBlacklistAttr)表服务实现类
 *
 * @author 顾文豪
 * @since 2023-11-9 16:43:48
 */
@Service
public class ProjectBlacklistAttrServiceImpl extends ServiceImpl<ProjectBlacklistAttrMapper, ProjectBlacklistAttr> implements ProjectBlacklistAttrService {
    @Override
    public ProjectBlacklistAttr getByFaceId(String faceId) {
        LambdaQueryWrapper<ProjectBlacklistAttr> queryWrapper = new QueryWrapper<ProjectBlacklistAttr>().lambda()
                .eq(ProjectBlacklistAttr::getFaceId,faceId)
                .last("limit 1");
        return getOne(queryWrapper);
    }

    @Override
    public Page<ProjectBlacklistAttrVo> fetchList(Page page, BlacklistAttrSearchCondition query) {
        return baseMapper.fetchList(page,query);
    }
}
