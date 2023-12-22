package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.entity.ProjectTrainingFileDb;
import com.aurine.cloudx.estate.entity.ProjectTrainingStaffDetail;
import com.aurine.cloudx.estate.service.ProjectTrainingFileDbService;
import com.aurine.cloudx.estate.service.ProjectTrainingService;
import com.aurine.cloudx.estate.service.ProjectTrainingStaffService;
import com.aurine.cloudx.estate.vo.ProjectTrainingFormVo;
import com.aurine.cloudx.estate.vo.TrainingStaffDetailVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//import com.aurine.cloudx.estate.feign.RemoteProjectTrainingService;

/**
 * 员工培训主表(ProjectTraining)表控制层
 *
 * @author makejava
 * @since 2021-01-13 14:16:58
 */
@RestController
@RequestMapping("/training")
@Api(value = "/training", tags = "员工培训")
public class ProjectTrainingController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectTrainingService projectTrainingService;
    @Resource
    private ProjectTrainingStaffService projectTrainingStaffService;
    @Resource
    private ProjectTrainingFileDbService projectTrainingFileDbService;
//    @Resource
//    private RemoteProjectTrainingService remoteProjectTrainingService;


    /**
     * 查询员工培训信息
     *
     * @param trainingStaffDetailVo 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "获取员工培训列表（物业）", notes = "获取员工培训列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "staffId", value = "员工ID", required = true, paramType = "param"),
            @ApiImplicitParam(name = "status", value = "状态 (0 未培训 1 培训中 2 已完成 ) ", required = false, paramType = "param"),
            @ApiImplicitParam(name = "timeOrderBy", value = "时间排序 (0升序 1降序 ) ", required = false, paramType = "param")
    })
    public R<Page<TrainingStaffDetailVo>> listTrainByStaff(Page page, TrainingStaffDetailVo trainingStaffDetailVo) {
        return R.ok(projectTrainingService.pageTrainByStaff(page, trainingStaffDetailVo));
    }

    /**
     * <p>
     *  根据培训ID获取培训资料
     * </p>
     *
     * @param trainingId 培训ID
     */
    @GetMapping({"/{staffId}/{trainingId}"})
    @ApiOperation(value = "根据培训ID获取培训资料（物业）", notes = "根据培训ID获取培训资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "staffId", value = "员工ID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "trainingId", value = "培训ID", required = true, paramType = "path")
    })
    public R<ProjectTrainingFormVo> getTraining(@PathVariable("staffId") String staffId, @PathVariable("trainingId") String trainingId) {
        ProjectTrainingFormVo trainingFormVo = projectTrainingService.getVoById(trainingId);
        trainingFormVo.setDoneCount(projectTrainingService.countReadByStaffId(staffId, trainingId));
        return R.ok(trainingFormVo);
//        return remoteProjectTrainingService.getTraining(trainingId);
    }


    /**
     * 根据文件id批量查询
     * @param projectTrainingFormVo
     * @return 所有数据
     */
    @GetMapping({"/file/list"})
    @ApiOperation(value = "根据文件id列表获取文件信息（物业）", notes = "根据文件id列表获取文件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "fileIds", value = "文件id列表", required = true, paramType = "param")
    })
    public R<List<ProjectTrainingFileDb>> selectFileByIds(ProjectTrainingFormVo projectTrainingFormVo) {

        return R.ok(projectTrainingFileDbService.listByIds(projectTrainingFormVo.getFileIds()));
    }




    /**
     * <p>
     *  已读资料
     * </p>
     *
     * @param projectTrainingStaffDetail
     */
    @PutMapping({"/file/info"})
    @ApiOperation(value = "获取文件信息把文件从未读状态改为已读状态（物业）", notes = "获取文件信息并不把文件从未读状态改为已读状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Boolean> setProgress(@RequestBody ProjectTrainingStaffDetail projectTrainingStaffDetail){
        return  R.ok(this.projectTrainingService.setProgress(projectTrainingStaffDetail.getTrainingId(),
                projectTrainingStaffDetail.getStaffId(), projectTrainingStaffDetail.getFileId()));
//        return remoteProjectTrainingService.setProgress(projectTrainingStaffDetail);
    }

    /**
     * 获取员工已读资料数
     * @param trainingId
     * @return
     */
    @GetMapping({"/count/{staffId}/{trainingId}"})
    @ApiOperation(value = "获取员工已读文件数（物业）", notes = "获取员工已读文件数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "staffId", value = "员工ID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "trainingId", value = "培训ID", required = true, paramType = "path")
    })
    public R<Integer> countReadByStaffId(@PathVariable("staffId") String staffId, @PathVariable("trainingId") String trainingId) {
        return R.ok(projectTrainingService.countReadByStaffId(staffId, trainingId));
//        return remoteProjectTrainingService.countReadByStaffId(staffId, trainingId);
    }

}