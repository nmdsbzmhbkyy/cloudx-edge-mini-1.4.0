package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectTraining;
import com.aurine.cloudx.estate.entity.ProjectTrainingFile;
import com.aurine.cloudx.estate.entity.ProjectTrainingStaff;
import com.aurine.cloudx.estate.entity.ProjectTrainingStaffDetail;
import com.aurine.cloudx.estate.mapper.ProjectTrainingMapper;
import com.aurine.cloudx.estate.service.ProjectTrainingFileService;
import com.aurine.cloudx.estate.service.ProjectTrainingService;
import com.aurine.cloudx.estate.service.ProjectTrainingStaffDetailService;
import com.aurine.cloudx.estate.service.ProjectTrainingStaffService;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 员工培训主表(ProjectTraining)表服务实现类
 *
 * @author makejava
 * @since 2021-01-13 14:16:57
 */
@Service
public class ProjectTrainingServiceImpl extends ServiceImpl<ProjectTrainingMapper, ProjectTraining> implements ProjectTrainingService {

    @Resource
    private ProjectTrainingFileService projectTrainingFileService;
    @Resource
    private ProjectTrainingStaffService projectTrainingStaffService;
    @Resource
    ProjectTrainingStaffDetailService projectTrainingStaffDetailService;

    @Override
    public Page<ProjectTrainingPageVo> pageTrain(Page<ProjectTrainingPageVo> page, ProjectTrainingPageVo projectTrainingPageVo) {
        return baseMapper.pageTrain(page, projectTrainingPageVo);
    }

    @Override
    public List<TrainingStaffDetailVo> listTrainByStaff(TrainingStaffDetailVo trainingStaffDetailVo) {
        return baseMapper.listTrainByStaff(trainingStaffDetailVo);
    }

    @Override
    public Page<TrainingStaffDetailVo> pageTrainByStaff(Page page, TrainingStaffDetailVo trainingStaffDetailVo) {
        return baseMapper.pageTrainByStaff(page, trainingStaffDetailVo);
    }

    @Override
    public Page<ProjectTrainingPageVo> pageTrainDetail(Page<ProjectTrainingPageVo> page, ProjectTrainingPageVo projectTrainingPageVo) {
        return baseMapper.pageTrainDetail(page, projectTrainingPageVo);
    }

    @Override
    public Page<ProjectTrainingStaffDetailPageVo> pageTrainStaffDetail(Page<ProjectTrainingStaffDetailPageVo> page, String trainingId) {
        return baseMapper.pageTrainStaffDetail(page, trainingId);
    }

    @Override
    public Page<ProjectTrainingFileDbVo> pageDetailProgress(Page<ProjectTrainingStaffDetailPageVo> page, String staffId, String trainId) {
        return baseMapper.pageDetailProgress(page, staffId, trainId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTrain(ProjectTrainingFormVo projectTrainingFormVo) {
        Integer count = count(Wrappers.lambdaQuery(ProjectTraining.class).eq(ProjectTraining::getTitle, projectTrainingFormVo.getTitle()));
        if (count != 0) {
            throw new RuntimeException("培训主题已存在");
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //员工id集合
        List<String> staffIds = projectTrainingFormVo.getStaffIds();
        //文件id集合
        List<String> fileIds = projectTrainingFormVo.getFileIds();

        //参与培训的员工集合
        List<ProjectTrainingStaff> projectTrainingStaffs = new ArrayList<>();
        //培训资料集合
        List<ProjectTrainingFile> projectTrainingFiles = new ArrayList<>();
        //员工培训明细集合
        List<ProjectTrainingStaffDetail> projectTrainingStaffDetails = new ArrayList<>();
        staffIds.forEach(e -> {
            ProjectTrainingStaff projectTrainingStaff = new ProjectTrainingStaff();
            projectTrainingStaff.setStaffId(e);
            projectTrainingStaff.setTrainingId(uuid);
            projectTrainingStaff.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            projectTrainingStaffs.add(projectTrainingStaff);
        });
        fileIds.forEach(e -> {
            ProjectTrainingFile projectTrainingFile = new ProjectTrainingFile();
            projectTrainingFile.setTrainingId(uuid);
            projectTrainingFile.setFileId(e);
            projectTrainingFile.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            projectTrainingFiles.add(projectTrainingFile);
        });

        projectTrainingFiles.forEach(file -> {
            projectTrainingStaffs.forEach(staff -> {
                ProjectTrainingStaffDetail projectTrainingStaffDetail = new ProjectTrainingStaffDetail();
                projectTrainingStaffDetail.setDetailId(UUID.randomUUID().toString().replaceAll("-", ""));
                projectTrainingStaffDetail.setFileId(file.getFileId());
                projectTrainingStaffDetail.setStaffId(staff.getStaffId());
                projectTrainingStaffDetail.setTrainingId(uuid);
                projectTrainingStaffDetails.add(projectTrainingStaffDetail);
            });
        });
        //批量保存此培训需要的文件
        projectTrainingFileService.saveBatch(projectTrainingFiles);
        //批量保存参与此培训的员工
        projectTrainingStaffService.saveBatch(projectTrainingStaffs);
        //批量保存员工培训明细
        projectTrainingStaffDetailService.saveBatch(projectTrainingStaffDetails);

        ProjectTraining projectTraining = new ProjectTraining();
        BeanUtils.copyProperties(projectTrainingFormVo, projectTraining);
        projectTraining.setTrainingId(uuid);
        return save(projectTraining);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeTrain(String trainingId) {
        projectTrainingStaffDetailService.remove(Wrappers.lambdaQuery(ProjectTrainingStaffDetail.class)
                .eq(ProjectTrainingStaffDetail::getTrainingId, trainingId));
        projectTrainingStaffService.remove(Wrappers.lambdaQuery(ProjectTrainingStaff.class)
                .eq(ProjectTrainingStaff::getTrainingId, trainingId));
        projectTrainingFileService.remove(Wrappers.lambdaQuery(ProjectTrainingFile.class)
                .eq(ProjectTrainingFile::getTrainingId, trainingId));
        return removeById(trainingId);
    }

    @Override
    public ProjectTrainingFormVo getVoById(String trainingId) {
        ProjectTraining projectTraining = getById(trainingId);
        List<String> staffIds = projectTrainingStaffService.list(Wrappers.lambdaQuery(ProjectTrainingStaff.class)
                .eq(ProjectTrainingStaff::getTrainingId, trainingId))
                .stream().map(ProjectTrainingStaff::getStaffId).collect(Collectors.toList());
        List<String> fileIds = projectTrainingFileService.list(Wrappers.lambdaQuery(ProjectTrainingFile.class)
                .eq(ProjectTrainingFile::getTrainingId, trainingId))
                .stream().map(ProjectTrainingFile::getFileId).collect(Collectors.toList());
        ProjectTrainingFormVo projectTrainingFormVo = new ProjectTrainingFormVo();
        BeanUtils.copyProperties(projectTraining, projectTrainingFormVo);
        projectTrainingFormVo.setStaffIds(staffIds);
        projectTrainingFormVo.setFileIds(fileIds);
        return projectTrainingFormVo;
    }

    @Override
    public boolean updateTrain(ProjectTrainingFormVo projectTrainingFormVo) {
        String trainingId = projectTrainingFormVo.getTrainingId();

        Integer count = count(Wrappers.lambdaQuery(ProjectTraining.class)
                .eq(ProjectTraining::getTitle, projectTrainingFormVo.getTitle())
                .ne(ProjectTraining::getTrainingId, trainingId));
        if (count != 0) {
            throw new RuntimeException("培训主题已存在");
        }
        //先删除原先的关联数据，再做添加 达到修改的效果
        projectTrainingStaffDetailService.remove(Wrappers.lambdaQuery(ProjectTrainingStaffDetail.class)
                .eq(ProjectTrainingStaffDetail::getTrainingId, trainingId));
        projectTrainingStaffService.remove(Wrappers.lambdaQuery(ProjectTrainingStaff.class)
                .eq(ProjectTrainingStaff::getTrainingId, trainingId));
        projectTrainingFileService.remove(Wrappers.lambdaQuery(ProjectTrainingFile.class)
                .eq(ProjectTrainingFile::getTrainingId, trainingId));

        //员工id集合
        List<String> staffIds = projectTrainingFormVo.getStaffIds();
        //文件id集合
        List<String> fileIds = projectTrainingFormVo.getFileIds();

        //参与培训的员工集合
        List<ProjectTrainingStaff> projectTrainingStaffs = new ArrayList<>();
        //培训资料集合
        List<ProjectTrainingFile> projectTrainingFiles = new ArrayList<>();
        //员工培训明细集合
        List<ProjectTrainingStaffDetail> projectTrainingStaffDetails = new ArrayList<>();
        staffIds.forEach(e -> {
            ProjectTrainingStaff projectTrainingStaff = new ProjectTrainingStaff();
            projectTrainingStaff.setStaffId(e);
            projectTrainingStaff.setTrainingId(trainingId);
            projectTrainingStaff.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            projectTrainingStaffs.add(projectTrainingStaff);
        });
        fileIds.forEach(e -> {
            ProjectTrainingFile projectTrainingFile = new ProjectTrainingFile();
            projectTrainingFile.setTrainingId(trainingId);
            projectTrainingFile.setFileId(e);
            projectTrainingFile.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            projectTrainingFiles.add(projectTrainingFile);
        });

        projectTrainingFiles.forEach(file -> {
            projectTrainingStaffs.forEach(staff -> {
                ProjectTrainingStaffDetail projectTrainingStaffDetail = new ProjectTrainingStaffDetail();
                projectTrainingStaffDetail.setDetailId(UUID.randomUUID().toString().replaceAll("-", ""));
                projectTrainingStaffDetail.setFileId(file.getFileId());
                projectTrainingStaffDetail.setStaffId(staff.getStaffId());
                projectTrainingStaffDetail.setTrainingId(trainingId);
                projectTrainingStaffDetails.add(projectTrainingStaffDetail);
            });
        });

        //批量保存此培训需要的文件
        projectTrainingFileService.saveBatch(projectTrainingFiles);
        //批量保存参与此培训的员工
        projectTrainingStaffService.saveBatch(projectTrainingStaffs);
        //批量保存员工培训明细
        projectTrainingStaffDetailService.saveBatch(projectTrainingStaffDetails);

        ProjectTraining projectTraining = new ProjectTraining();
        BeanUtils.copyProperties(projectTrainingFormVo, projectTraining);
        return updateById(projectTraining);
    }

    @Override
    public boolean setProgress(String trainingId, String staffId, String fileId) {
        ProjectTraining projectTraining = getById(trainingId);
        //LocalDateTime beginTime = projectTraining.getBeginTime();
        LocalDateTime endTime = projectTraining.getEndTime();
        //判断培训是否已经结束，结束不更新状态
        if (LocalDateTime.now().isAfter(endTime)) {
            return true;
        }
        ProjectTrainingStaffDetail detail = projectTrainingStaffDetailService.getOne(Wrappers.lambdaQuery(ProjectTrainingStaffDetail.class)
                .eq(ProjectTrainingStaffDetail::getTrainingId, trainingId)
                .eq(ProjectTrainingStaffDetail::getStaffId, staffId)
                .eq(ProjectTrainingStaffDetail::getFileId, fileId));
        detail.setCompleteTime(LocalDateTime.now());
        detail.setProgress("1");
        return projectTrainingStaffDetailService.updateById(detail);
    }

    @Override
    public Integer countReadByStaffId(String staffId, String trainingId) {
        return projectTrainingStaffDetailService.count(Wrappers.lambdaQuery(ProjectTrainingStaffDetail.class)
                .eq(ProjectTrainingStaffDetail::getStaffId, staffId)
                .eq(ProjectTrainingStaffDetail::getTrainingId, trainingId)
                .eq(ProjectTrainingStaffDetail::getProgress, "1"));
    }
}