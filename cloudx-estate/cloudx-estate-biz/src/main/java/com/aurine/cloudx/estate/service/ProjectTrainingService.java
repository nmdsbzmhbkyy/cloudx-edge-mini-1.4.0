package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectTraining;
import com.aurine.cloudx.estate.entity.ProjectTrainingStaffDetail;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * 员工培训主表(ProjectTraining)表服务接口
 *
 * @author makejava
 * @since 2021-01-13 14:16:56
 */
public interface ProjectTrainingService extends IService<ProjectTraining> {

    /**
     * 分页查询培训设置信息
     *
     * @param projectTrainingPageVo
     *
     * @return
     */
    Page<ProjectTrainingPageVo> pageTrain(Page<ProjectTrainingPageVo> page, @Param("query") ProjectTrainingPageVo projectTrainingPageVo);

    /**
     * 分页查询培训设置信息(小程序)
     *
     * @param trainingStaffDetailVo
     *
     * @return
     */
    List<TrainingStaffDetailVo> listTrainByStaff(@Param("query") TrainingStaffDetailVo trainingStaffDetailVo);
    /**
     * 分页查询培训设置信息(小程序)
     *
     * @param trainingStaffDetailVo
     *
     * @return
     */
    Page<TrainingStaffDetailVo> pageTrainByStaff(Page page, TrainingStaffDetailVo trainingStaffDetailVo);

    /**
     * 分页查询培训分析信息
     *
     * @param projectTrainingPageVo
     *
     * @return
     */
    Page<ProjectTrainingPageVo> pageTrainDetail(Page<ProjectTrainingPageVo> page, @Param("query") ProjectTrainingPageVo projectTrainingPageVo);

    /**
     * 分页查询培训分析详情页
     *
     * @param trainingId
     *
     * @return
     */
    Page<ProjectTrainingStaffDetailPageVo> pageTrainStaffDetail(Page<ProjectTrainingStaffDetailPageVo> page, @Param("trainingId") String trainingId);

    /**
     * 员工资料读取进度查看
     *
     * @param staffId
     *
     * @return
     */
    Page<ProjectTrainingFileDbVo> pageDetailProgress(Page<ProjectTrainingStaffDetailPageVo> page, @Param("staffId") String staffId, @Param("trainingId") String trainingId);

    /**
     * 保存培训设置
     * @param projectTrainingFormVo
     * @return
     */
    boolean saveTrain(ProjectTrainingFormVo projectTrainingFormVo);

    /**
     * 删除培训设置
     * @param trainingId
     * @return
     */
    boolean removeTrain(String trainingId);

    /**
     * 根据培训id获取培训设置vo
     *
     * @param trainingId
     * @return
     */
    ProjectTrainingFormVo getVoById(String trainingId);

    /**
     * 更新培训设置
     *
     * @param projectTrainingFormVo
     * @return
     */
    boolean updateTrain(ProjectTrainingFormVo projectTrainingFormVo);

    /**
     * 设置为已读
     *
     * @param trainingId
     * @param staffId
     * @param fileId
     * @return
     */
    boolean setProgress(String trainingId,String staffId, String fileId);

    /**
     * 获取员工已读资料数
     * @param staffId
     * @return
     */
    Integer countReadByStaffId(String staffId, String trainingId);
}