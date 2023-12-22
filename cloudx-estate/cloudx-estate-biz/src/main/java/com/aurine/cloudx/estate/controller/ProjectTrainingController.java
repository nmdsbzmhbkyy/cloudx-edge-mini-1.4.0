package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.entity.ProjectTraining;
import com.aurine.cloudx.estate.entity.ProjectTrainingStaffDetail;
import com.aurine.cloudx.estate.service.ProjectTrainingService;
import com.aurine.cloudx.estate.service.ProjectTrainingStaffService;
import com.aurine.cloudx.estate.util.NewDateUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 员工培训主表(ProjectTraining)表控制层
 *
 * @author makejava
 * @since 2021-01-13 14:16:58
 */
@RestController
@RequestMapping("projectTraining")
@Api(value = "projectTraining", tags = "员工培训主表")
public class ProjectTrainingController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectTrainingService projectTrainingService;
    @Resource
    private ProjectTrainingStaffService projectTrainingStaffService;

    /**
     * 分页查询培训设置信息
     *
     * @param page                  分页对象
     * @param projectTrainingPageVo 查询实体
     * @return 所有数据
     */
    @GetMapping("/pageTrain")
    @ApiOperation(value = "分页查询培训设置信息", notes = "分页查询培训设置信息")
    public R pageTrain(Page<ProjectTrainingPageVo> page, ProjectTrainingPageVo projectTrainingPageVo) {

        projectTrainingPageVo.setNowTime(NewDateUtil.getNewDate(new Date()));

        if (StrUtil.isNotEmpty(projectTrainingPageVo.getEndTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            projectTrainingPageVo.setEndTime(LocalDateTime.parse(projectTrainingPageVo.getEndTimeString(), fmt));
        }
        if (StrUtil.isNotEmpty(projectTrainingPageVo.getBeginTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            projectTrainingPageVo.setBeginTime(LocalDateTime.parse(projectTrainingPageVo.getBeginTimeString(), fmt));
        }

        return R.ok(this.projectTrainingService.pageTrain(page, projectTrainingPageVo));
    }

    /**
     * 分页查询培训分析信息
     *
     * @param page                  分页对象
     * @param projectTrainingPageVo 查询实体
     * @return 所有数据
     */
    @GetMapping("/pageTrainDetail")
    @ApiOperation(value = "分页查询培训分析信息", notes = "分页查询培训分析信息")
    public R pageTrainDetail(Page<ProjectTrainingPageVo> page, ProjectTrainingPageVo projectTrainingPageVo) {

        projectTrainingPageVo.setNowTime(NewDateUtil.getNewDate(new Date()));

        if (StrUtil.isNotEmpty(projectTrainingPageVo.getEndTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            projectTrainingPageVo.setEndTime(LocalDateTime.parse(projectTrainingPageVo.getEndTimeString(), fmt));
        }
        if (StrUtil.isNotEmpty(projectTrainingPageVo.getBeginTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            projectTrainingPageVo.setBeginTime(LocalDateTime.parse(projectTrainingPageVo.getBeginTimeString(), fmt));
        }

        return R.ok(this.projectTrainingService.pageTrainDetail(page, projectTrainingPageVo));
    }

    /**
     * 分页查询培训分析详情信息
     *
     * @param page            分页对象
     * @param projectTraining 查询实体
     * @return 所有数据
     */
    @GetMapping("/pageTrainStaffDetail")
    @ApiOperation(value = "分页查询培训分析信息", notes = "分页查询培训分析信息")
    public R pageTrainStaffDetail(Page<ProjectTrainingStaffDetailPageVo> page, ProjectTraining projectTraining) {
        return R.ok(this.projectTrainingService.pageTrainStaffDetail(page, projectTraining.getTrainingId()));
    }

    /**
     * 员工进度查看
     *
     * @param page                       分页对象
     * @param projectTrainingStaffDetail 查询实体
     * @return 所有数据
     */
    @GetMapping("/pageDetailProgress")
    @ApiOperation(value = "员工进度查看", notes = "员工进度查看")
    public R pageDetailProgress(Page<ProjectTrainingStaffDetailPageVo> page, ProjectTrainingStaffDetail projectTrainingStaffDetail) {
        return R.ok(this.projectTrainingService.pageDetailProgress(page, projectTrainingStaffDetail.getStaffId(), projectTrainingStaffDetail.getTrainingId()));
    }

    /**
     * 获取参与培训的总人数
     *
     * @param projectTrainingFileTotalVo 查询实体
     * @return 所有数据
     */
    @GetMapping("/staffTotalCount")
    @ApiOperation(value = "获取参与培训的总人数", notes = "获取参与培训的总人数")
    public R count(ProjectTrainingFileTotalVo projectTrainingFileTotalVo) {
        return R.ok(projectTrainingStaffService.staffTotalCount(projectTrainingFileTotalVo.getTrainingIds()));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param trainingId 主键
     * @return 单条数据
     */
    @GetMapping("{trainingId}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectTraining单条数据")
    public R selectOne(@PathVariable("trainingId") String trainingId) {
        return R.ok(this.projectTrainingService.getVoById(trainingId));
    }

    /**
     * 新增数据
     *
     * @param projectTrainingFormVo 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectTraining数据")
    public R insert(@RequestBody ProjectTrainingFormVo projectTrainingFormVo) {
        return R.ok(this.projectTrainingService.saveTrain(projectTrainingFormVo));
    }

    /**
     * 修改数据
     *
     * @param projectTrainingFormVo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectTraining数据")
    public R update(@RequestBody ProjectTrainingFormVo projectTrainingFormVo) {
        return R.ok(this.projectTrainingService.updateTrain(projectTrainingFormVo));
    }

    /**
     * 删除数据
     *
     * @param trainingId 主键
     * @return 删除结果
     */
    @DeleteMapping("{trainingId}")
    @ApiOperation(value = "删除数据", notes = "通过id删除projectTraining数据")
    public R delete(@PathVariable("trainingId") String trainingId) {
        return R.ok(this.projectTrainingService.removeTrain(trainingId));
    }

    /**
     * 查询员工培训信息
     *
     * @param trainingStaffDetailVo 查询实体
     * @return 所有数据
     */
    @GetMapping("/listTrainByStaff")
    @ApiOperation(value = "分页查询培训设置信息", notes = "分页查询培训设置信息")
    public R listTrainByStaff(TrainingStaffDetailVo trainingStaffDetailVo) {
        return R.ok(projectTrainingService.listTrainByStaff(trainingStaffDetailVo));
    }

    /**
     * 查询员工已读资料数
     *
     * @param staffId 查询实体
     * @return 所有数据
     */
    @GetMapping("/countReadByStaffId/{staffId}/{trainingId}")
    @ApiOperation(value = "查询员工已读资料数", notes = "查询员工已读资料数")
    public R countReadByStaffId(@PathVariable("staffId") String staffId, @PathVariable("trainingId") String trainingId) {
        return R.ok(projectTrainingService.countReadByStaffId(staffId, trainingId));
    }

    /**
     * 已读资料
     *
     * @param projectTrainingStaffDetail
     * @return 单条数据
     */
    @PutMapping("/setProgress")
    @ApiOperation(value = "已读资料", notes = "已读资料")
    public R setProgress(@RequestBody ProjectTrainingStaffDetail projectTrainingStaffDetail) {
        return R.ok(this.projectTrainingService.setProgress(projectTrainingStaffDetail.getTrainingId(),
                projectTrainingStaffDetail.getStaffId(), projectTrainingStaffDetail.getFileId()));
    }
}