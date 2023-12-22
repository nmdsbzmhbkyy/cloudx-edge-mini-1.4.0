
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.MediaRepoTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectMediaRepo;
import com.aurine.cloudx.estate.mapper.ProjectMediaRepoMapper;
import com.aurine.cloudx.estate.service.ProjectMediaRepoService;

import com.aurine.cloudx.estate.vo.ProjectMediaRepoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.minio.service.MinioTemplate;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 项目媒体资源库
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:35:57
 */
@Service
public class ProjectMediaRepoServiceImpl extends ServiceImpl<ProjectMediaRepoMapper, ProjectMediaRepo> implements ProjectMediaRepoService {
    @Resource
    MinioTemplate minioTemplate;

    @Override
    public Page getList(Page page, ProjectMediaRepoVo projectMediaRepo) {
        LambdaQueryWrapper<ProjectMediaRepo> queryWrapper = Wrappers.lambdaQuery(ProjectMediaRepo.class).eq(ProjectMediaRepo::getVisible, projectMediaRepo.getVisible());
        if (StrUtil.isNotBlank(projectMediaRepo.getRepoType())) {
            queryWrapper.eq(ProjectMediaRepo::getRepoType, projectMediaRepo.getRepoType());
        }
        if (StrUtil.isNotBlank(projectMediaRepo.getResourceType())) {
            queryWrapper.eq(ProjectMediaRepo::getResourceType, projectMediaRepo.getResourceType());
        }
        if (StrUtil.isNotBlank(projectMediaRepo.getRepoName())) {
            queryWrapper.like(ProjectMediaRepo::getRepoName, projectMediaRepo.getRepoName());
        }
        if (StrUtil.isNotBlank(projectMediaRepo.getBeginTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            queryWrapper.ge(ProjectMediaRepo::getCreateTime, LocalDateTime.parse(projectMediaRepo.getBeginTimeString()+" 00:00:00", fmt));
        }
        if (StrUtil.isNotBlank(projectMediaRepo.getEndTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            queryWrapper.le(ProjectMediaRepo::getCreateTime, LocalDateTime.parse(projectMediaRepo.getEndTimeString()+" 23:59:59", fmt));
        }
        Page pageData = this.page(page, queryWrapper);
        List<ProjectMediaRepo> records = pageData.getRecords();
        List<ProjectMediaRepoVo> projectMediaRepoVos = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        records.forEach(value -> {
            ProjectMediaRepoVo projectMediaRepoVo = new ProjectMediaRepoVo();
            BeanUtils.copyProperties(value,projectMediaRepoVo);
            projectMediaRepoVo.setUploadDateTime(dateTimeFormatter.format(projectMediaRepoVo.getCreateTime()));
            if(projectMediaRepoVo.getRepoDuration() != null){
                projectMediaRepoVo.setRepoDurationCHS(formatDateTime(Long.parseLong(projectMediaRepoVo.getRepoDuration().toString())));
            }else{
                projectMediaRepoVo.setRepoDurationCHS("-");
            }
            projectMediaRepoVos.add(projectMediaRepoVo);
        });
        pageData.setRecords(projectMediaRepoVos);
        return pageData;
    }



    public static String formatDateTime(long mss) {
        long hours = (mss % ( 60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % ( 60 * 60)) /60;
        long seconds = mss % 60;
        StringBuilder sb = new StringBuilder();
        if(hours>0){
            sb.append(hours).append("小时").append(minutes).append("分").append(seconds).append("秒");
        }else if(minutes>0){
            sb.append("0小时").append(minutes).append("分").append(seconds).append("秒");
        }else{
            sb.append("0小时0分").append(seconds).append("秒");
        }
        return sb.toString();
    }

    @Override
    public List<ProjectMediaRepo> queryObj(String id) {
        return baseMapper.queryObj(id);
    }





    @Override
    public List<ProjectMediaRepo> listMediaRepoByAdSeq(Long adSeq) {
        return baseMapper.listMediaRepoByAdSeq(adSeq);
    }

    @Override
    public boolean removeResource(String repoId) {
        ProjectMediaRepo mediaRepo = this.getById(repoId);
        try {
            minioTemplate.removeObject(BUCKET_NAME, mediaRepo.getRepoUrl().substring(mediaRepo.getRepoUrl().lastIndexOf("/") +1 ));
            return this.removeById(repoId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
