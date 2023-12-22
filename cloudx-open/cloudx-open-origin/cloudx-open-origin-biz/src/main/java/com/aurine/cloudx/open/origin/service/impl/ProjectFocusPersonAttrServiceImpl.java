package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.open.origin.mapper.ProjectFocusPersonAttrMapper;
import com.aurine.cloudx.open.origin.entity.ProjectFocusPersonAttr;
import com.aurine.cloudx.open.origin.service.ProjectFocusPersonAttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 重点人员拓展信息表(ProjectFocusPersonAttr)表服务实现类
 *
 * @author 王良俊
 * @since 2020-08-18 09:06:39
 */
@Service
public class ProjectFocusPersonAttrServiceImpl extends ServiceImpl<ProjectFocusPersonAttrMapper, ProjectFocusPersonAttr>
        implements ProjectFocusPersonAttrService {

    @Override
    public boolean saveOrUpdateFocusPersonAttrByPersonId(ProjectFocusPersonAttr focusPersonAttr) {
        // 这里先删除原来的重点人员信息
        this.remove(new QueryWrapper<ProjectFocusPersonAttr>().lambda()
                .eq(ProjectFocusPersonAttr::getPersonId, focusPersonAttr.getPersonId()));
        return this.save(focusPersonAttr);
    }

    @Override
    public boolean saveOrUpdateFocusPersonAttrByPersonId(List<ProjectFocusPersonAttr> focusPersonAttrList) {
        if (CollUtil.isNotEmpty(focusPersonAttrList)) {
            List<String> personIdList = focusPersonAttrList.stream().map(ProjectFocusPersonAttr::getPersonId).collect(Collectors.toList());
            // 这里先删除原来的重点人员信息
            this.remove(new QueryWrapper<ProjectFocusPersonAttr>().lambda().in(ProjectFocusPersonAttr::getPersonId, personIdList));
            List<ProjectFocusPersonAttr> list = new ArrayList<>();
            List<String> idList = new ArrayList<>();
            focusPersonAttrList.forEach(projectFocusPersonAttr -> {
                if (!idList.contains(projectFocusPersonAttr.getPersonId())) {
                    list.add(projectFocusPersonAttr);
                    idList.add(projectFocusPersonAttr.getPersonId());
                }
            });
            return this.saveBatch(list);
        }
        return true;
    }

    @Override
    public boolean removeFocusPersonAttrByPersonId(String personId) {
        return this.remove(new QueryWrapper<ProjectFocusPersonAttr>().lambda().eq(ProjectFocusPersonAttr::getPersonId, personId));
    }

    @Override
    public boolean removeFocusPersonAttrByPersonId(List<String> personIdList) {
        if (CollUtil.isNotEmpty(personIdList)) {
            return this.remove(new QueryWrapper<ProjectFocusPersonAttr>().lambda().in(ProjectFocusPersonAttr::getPersonId, personIdList));
        }
        return true;
    }

    @Override
    public ProjectFocusPersonAttr getFocusPersonAttrByPersonId(String personId) {
        List<ProjectFocusPersonAttr> focusPersonAttrList = this.list(new QueryWrapper<ProjectFocusPersonAttr>().lambda()
                .eq(ProjectFocusPersonAttr::getPersonId, personId));
        if (CollUtil.isNotEmpty(focusPersonAttrList)){
            return focusPersonAttrList.get(0);
        } else {
            return new ProjectFocusPersonAttr();
        }
    }
}