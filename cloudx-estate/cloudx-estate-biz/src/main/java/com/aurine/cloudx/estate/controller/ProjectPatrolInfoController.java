package com.aurine.cloudx.estate.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.entity.ProjectPatrolPerson;
import com.aurine.cloudx.estate.service.ProjectPatrolInfoService;
import com.aurine.cloudx.estate.service.ProjectPatrolPersonService;
import com.aurine.cloudx.estate.vo.ProjectPatrolAssignVo;
import com.aurine.cloudx.estate.vo.ProjectPatrolInfoSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectPatrolInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目巡更记录(ProjectPatrolInfo)表控制层
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-09 08:55:33
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectPatrolInfo")
@Api(value = "projectPatrolInfo", tags = "项目巡更记录")
public class ProjectPatrolInfoController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectPatrolInfoService projectPatrolInfoService;
    @Resource
    private ProjectPatrolPersonService projectPatrolPersonService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param vo   查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询巡更记录", notes = "分页查询巡更记录")
    public R selectAll(Page<ProjectPatrolInfoVo> page, ProjectPatrolInfoSearchCondition vo) {
        return R.ok(projectPatrolInfoService.pageByPatrolInfo(page, vo));
    }

    /**
     * 通过巡更记录Id查询巡更记录
     *
     * @param patrolId 主键
     * @return 单条数据
     */
    @GetMapping("/{patrolId}")
    @ApiOperation(value = "通过巡更记录Id查询巡更记录", notes = "通过巡更记录Id查询巡更记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patrolId", value = "巡更记录Id", paramType = "query", required = true)
    })
    public R selectOne(@PathVariable String patrolId) {
        return R.ok(projectPatrolInfoService.getById(patrolId));
    }

//    @GetMapping("/timeCheck")
//    @ApiOperation(value = "判断巡更记录是否过期", notes = "判断巡更记录是否过期")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
//    })
//    public R timeOutCheck() {
    //无用代码去除 为防止前端异常 暂不删除该controller
//        return R.ok(projectPatrolInfoService.timeOutCheck());
//        return R.ok();
//    }

    /**
     * 新增巡更记录
     *
     * @param
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增巡更记录", notes = "新增巡更记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R insert() {
        return R.ok(projectPatrolInfoService.savePatrolInfo());
    }

    /**
     * 修改数据
     *
     * @param projectPatrolInfo 实体对象
     * @return 修改结果
     */
    /*@PutMapping
    public R update(@RequestBody ProjectPatrolInfo projectPatrolInfo) {
        return success(this.projectPatrolInfoService.updateById(projectPatrolInfo));
    }*/

    /**
     * 删除数据
     *
     * @param
     * @return 删除结果
     */
    @DeleteMapping("/{patrolId}")
    @ApiOperation(value = "删除巡更记录", notes = "删除巡更记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patrolId", value = "巡更记录Id", paramType = "query", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R delete(@PathVariable String patrolId) {
        return R.ok(projectPatrolInfoService.deleteById(patrolId));
    }


    /**
     * 通过id批量删除巡更记录
     *
     * @param ids
     * @return R
     */
    /*@ApiOperation(value = "通过巡更记录id批量删除巡更记录", notes = "通过巡更记录id批量删除巡更记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "巡更记录Id", paramType = "body"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @SysLog("通过id批量删除巡更记录")
    @DeleteMapping("/removeAll")
    public R removeById(@RequestBody List<String> ids) {
        if (ids.size() > 0) {
            return R.ok(projectPatrolInfoService.batchRemove(ids));
        } else {
            return R.ok(false);
        }
    }*/

    /**
     * <p>
     * 指派人员
     * </p>
     *
     * @param projectPatrolAssignVo 巡更指派VO对象 包含巡更记录ID和巡更指派人员对象列表
     * @return R
     * @author: 王良俊
     */
    @PostMapping("/assignPerson")
    @ApiOperation(value = "指派人员", notes = "指派人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R assignPerson(@RequestBody ProjectPatrolAssignVo projectPatrolAssignVo) {
        return R.ok(projectPatrolPersonService.assignPerson(projectPatrolAssignVo.getPatrolId(), projectPatrolAssignVo.getStaffList()));
    }

    /**
     * <p>
     * 获取已指派人员ID集合
     * </p
     *
     * @param patrolId 巡更记录ID
     * @return R
     * @author: 王良俊
     */
    @GetMapping("/getAssignStaffIdListByPatrolId/{patrolId}")
    @ApiOperation(value = "获取已指派人员ID集合", notes = "获取已指派人员ID集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patrolId", value = "巡更记录Id", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getAssignStaffIdListByPatrolId(@PathVariable("patrolId") String patrolId) {
        List<String> staffIdList = new ArrayList<>();
        if (StrUtil.isNotBlank(patrolId)) {
            List<ProjectPatrolPerson> personList = projectPatrolPersonService.list(new QueryWrapper<ProjectPatrolPerson>()
                    .lambda().eq(ProjectPatrolPerson::getPatrolId, patrolId).eq(ProjectPatrolPerson::getStaffType, "2"));
            if (CollUtil.isNotEmpty(personList)) {
                staffIdList = personList.stream().map(ProjectPatrolPerson::getStaffId).collect(Collectors.toList());
            }
        }
        return R.ok(staffIdList);
    }
}