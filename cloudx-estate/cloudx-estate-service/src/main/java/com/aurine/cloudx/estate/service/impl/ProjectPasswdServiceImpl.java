package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.PassRightTokenStateEnum;
import com.aurine.cloudx.estate.entity.ProjectPasswd;
import com.aurine.cloudx.estate.mapper.ProjectPasswdMapper;
import com.aurine.cloudx.estate.service.ProjectPasswdService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目密码库
 *
 * @author 王良俊
 * @date 2020-06-04 18:16:17
 */
@Service
public class ProjectPasswdServiceImpl extends ServiceImpl<ProjectPasswdMapper, ProjectPasswd> implements ProjectPasswdService {

    @Lazy
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Override
    public boolean saveOrUpdatePassword(ProjectPasswd projectPasswd) {
        if (BeanUtil.isNotEmpty(projectPasswd)) {
            boolean hasPersonId = StrUtil.isNotBlank(projectPasswd.getPersonId());
            if (StrUtil.isBlank(projectPasswd.getPasswd())) {
                return false;
            }
            int count = this.count(new QueryWrapper<ProjectPasswd>().lambda()
                    .eq(ProjectPasswd::getPasswd, projectPasswd.getPasswd())
                    .eq(hasPersonId, ProjectPasswd::getPersonId, projectPasswd.getPersonId()));
            if (count == 0) {
                return this.saveOrUpdate(projectPasswd);
            }else{
                throw new RuntimeException("已存在相同密码");
            }
        }
        return false;
    }

    @Override
    public List<ProjectPasswd> listByPersonId(String personId) {
        return this.list(new QueryWrapper<ProjectPasswd>().lambda().eq(ProjectPasswd::getPersonId, personId));
    }

    @Override
    public List<ProjectPasswd> listByPersonId(List<String> personIdList) {
        if (CollUtil.isNotEmpty(personIdList)) {
            return this.list(new QueryWrapper<ProjectPasswd>().lambda().in(ProjectPasswd::getPersonId, personIdList));
        }
        return new ArrayList<>();
    }

    @Override
    public boolean removeByPersonId(String personId) {
        return this.remove(new QueryWrapper<ProjectPasswd>().lambda().eq(ProjectPasswd::getPersonId, personId));
    }


    /**
     * 根据第三方id和项目编号，获取数据
     *
     * @param code
     * @param projectId
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    @Override
    public ProjectPasswd getByCode(String code, int projectId) {
        return this.baseMapper.getByCode(projectId,code);
    }

}
