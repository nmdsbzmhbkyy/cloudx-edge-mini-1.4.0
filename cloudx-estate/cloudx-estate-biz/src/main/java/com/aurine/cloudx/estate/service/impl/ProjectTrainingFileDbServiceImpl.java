package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.entity.ProjectTrainingFile;
import com.aurine.cloudx.estate.service.ProjectTrainingFileService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.mapper.ProjectTrainingFileDbMapper;
import com.aurine.cloudx.estate.entity.ProjectTrainingFileDb;
import com.aurine.cloudx.estate.service.ProjectTrainingFileDbService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 培训资料库(ProjectTrainingFileDb)表服务实现类
 *
 * @author makejava
 * @since 2021-01-12 10:26:06
 */
@Service
public class ProjectTrainingFileDbServiceImpl extends ServiceImpl<ProjectTrainingFileDbMapper, ProjectTrainingFileDb> implements ProjectTrainingFileDbService {

    @Resource
    private ProjectTrainingFileService projectTrainingFileService;

    @Override
    public List<ProjectTrainingFileDb> selectDir(ProjectTrainingFileDb projectTrainingFileDb) {
        LambdaQueryWrapper<ProjectTrainingFileDb> wrapper = Wrappers.<ProjectTrainingFileDb>lambdaQuery()
                .eq(ProjectTrainingFileDb::getIsDir, "1");

        if (StrUtil.isNotEmpty(projectTrainingFileDb.getFileName())) {
            wrapper.like(ProjectTrainingFileDb::getFileName, projectTrainingFileDb.getFileName());
        }
        return list(wrapper);
    }

    @Override
    public Page<ProjectTrainingFileDb> selectFile(Page page, ProjectTrainingFileDb projectTrainingFileDb) {
        LambdaQueryWrapper<ProjectTrainingFileDb> wrapper = Wrappers.<ProjectTrainingFileDb>lambdaQuery()
                .eq(ProjectTrainingFileDb::getIsDir, "0")
                .eq(ProjectTrainingFileDb::getParId, projectTrainingFileDb.getParId());
        if (StrUtil.isNotEmpty(projectTrainingFileDb.getFileName())) {
            wrapper.like(ProjectTrainingFileDb::getFileName, projectTrainingFileDb.getFileName());
        }
        return page(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFile(String id) {
        int count = projectTrainingFileService.count(Wrappers.lambdaQuery(ProjectTrainingFile.class).eq(ProjectTrainingFile::getFileId, id));
        if (count != 0) {
            throw new RuntimeException("该资料已被使用，无法删除");
        }
        ProjectTrainingFileDb projectTrainingFileDb = getById(id);
        //如果是文件夹需要删除该文件夹下所有文件
        if ("1".equals(projectTrainingFileDb.getIsDir())) {
            List<String> fileIds = list(Wrappers.lambdaQuery(ProjectTrainingFileDb.class).eq(ProjectTrainingFileDb::getParId, id))
                    .stream().map(ProjectTrainingFileDb::getFileId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(fileIds)) {
                int count1 = projectTrainingFileService.count(Wrappers.lambdaQuery(ProjectTrainingFile.class).in(ProjectTrainingFile::getFileId, fileIds));
                if (count1 != 0) {
                    throw new RuntimeException("该文件夹下存在资料被使用，无法删除");
                } else {
                    removeByIds(fileIds);
                }
            }
        }
        return removeById(id);
    }
}