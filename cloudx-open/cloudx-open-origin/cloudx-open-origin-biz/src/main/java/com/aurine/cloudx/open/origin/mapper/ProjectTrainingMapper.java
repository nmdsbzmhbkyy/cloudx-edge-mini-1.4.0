package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectTrainingStaffDetailPageVo;
import com.aurine.cloudx.open.origin.entity.ProjectTraining;
import com.aurine.cloudx.open.origin.vo.ProjectTrainingFileDbVo;
import com.aurine.cloudx.open.origin.vo.ProjectTrainingPageVo;
import com.aurine.cloudx.open.origin.vo.TrainingStaffDetailVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工培训主表(ProjectTraining)表数据库访问层
 *
 * @author makejava
 * @since 2021-01-13 14:16:54
 */
@Mapper
public interface ProjectTrainingMapper extends BaseMapper<ProjectTraining> {

    /**
     * 分页查询培训设置信息
     *
     * @param page
     * @param projectTrainingPageVo
     * @return
     */
    Page<ProjectTrainingPageVo> pageTrain(Page page, @Param("query") ProjectTrainingPageVo projectTrainingPageVo);

    /**
     * 分页查询培训设置信息(小程序)
     *
     * @param trainingStaffDetailVo
     * @return
     */
    List<TrainingStaffDetailVo> listTrainByStaff(@Param("query") TrainingStaffDetailVo trainingStaffDetailVo);
    /**
     * 分页查询培训设置信息(小程序)
     *
     * @param trainingStaffDetailVo
     * @return
     */
    Page<TrainingStaffDetailVo> pageTrainByStaff(Page page, @Param("query") TrainingStaffDetailVo trainingStaffDetailVo);
    /**
     * 分页查询培训分析信息
     * @param page
     * @param projectTrainingPageVo
     * @return
     */
    Page<ProjectTrainingPageVo> pageTrainDetail(Page page, @Param("query") ProjectTrainingPageVo projectTrainingPageVo);

    /**
     * 分页查询培训分析详情页
     * @param page
     * @param trainingId
     * @return
     */
    Page<ProjectTrainingStaffDetailPageVo> pageTrainStaffDetail(Page page, @Param("trainingId") String trainingId);

    /**
     * 员工资料读取进度查看
     * @param page
     * @param staffId
     * @return
     */
    Page<ProjectTrainingFileDbVo> pageDetailProgress(Page page, @Param("staffId") String staffId, @Param("trainingId") String trainingId);

}