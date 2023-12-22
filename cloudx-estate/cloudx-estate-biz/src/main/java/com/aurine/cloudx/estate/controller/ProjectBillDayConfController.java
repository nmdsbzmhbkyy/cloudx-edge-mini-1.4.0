package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.estate.entity.ProjectBillDayConf;
import com.aurine.cloudx.estate.entity.ProjectBillingInfo;
import com.aurine.cloudx.estate.service.ProjectBillDayConfService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

/**
 * 账单日设置(ProjectBillDayConf)表控制层
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@RestController
@RequestMapping("projectBillDayConf")
@Api(value = "projectBillDayConf", tags = "账单日设置")
public class ProjectBillDayConfController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectBillDayConfService projectBillDayConfService;


    /**
     * 查询单条数据
     *
     * @return 单条数据
     */
    @GetMapping
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectBillDayConf单条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectBillDayConf> selectOne() {
        //因为projectId已经自动注入到查询语句里,故不做查询设置
        List<ProjectBillDayConf> dayConfs = this.projectBillDayConfService.list();
        if (dayConfs != null && dayConfs.size() > 0) {
            return R.ok(dayConfs.get(0));
        } else {
            return R.ok();
        }
    }

//    /**
//     * 新增数据
//     *
//     * @param projectBillDayConf 实体对象
//     * @return 新增结果
//     */
//    @PostMapping
//    @ApiOperation(value = "新增数据", notes = "新增projectBillDayConf数据")
//    public R insert(@RequestBody ProjectBillDayConf projectBillDayConf) {
//        return R.ok(this.projectBillDayConfService.save(projectBillDayConf));
//    }

    /**
     * 修改数据
     *
     * @param projectBillDayConf 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectBillDayConf数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R update( @RequestBody @Validated ProjectBillDayConf projectBillDayConf) {
        return R.ok(this.projectBillDayConfService.saveOrUpdate(projectBillDayConf));
    }
//    /**
//     * 删除数据
//     *
//     * @param idList 主键结合
//     * @return 删除结果
//     */
//    @DeleteMapping
//    @ApiOperation(value = "删除数据", notes = "通过id删除projectBillDayConf数据")
//    public R delete(@RequestParam("idList") List<Integer> idList) {
//        return R.ok(this.projectBillDayConfService.removeByIds(idList));
//    }

    /**
     * 关闭收费设置
     *
     * @param id 主键
     * @return 修改结果
     */
    @PutMapping("/off/{id}")
    @ApiOperation(value = "关闭收费设置", notes = "关闭收费设置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "费用id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R setOff(@PathVariable("id") String id) {

        return R.ok(this.projectBillDayConfService
                .update(Wrappers.lambdaUpdate(ProjectBillDayConf.class)
                        .set(ProjectBillDayConf::getStatus, DataConstants.FALSE)
                        .eq(ProjectBillDayConf::getRecordId, id)));
    }

    /**
     * 启用收费设置
     *
     * @param id 主键
     * @return 修改结果
     */
    @PutMapping("/on/{id}")
    @ApiOperation(value = "启用收费设置", notes = "启用收费设置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "费用id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R setOn(@PathVariable("id") String id) {
        ProjectBillDayConf projectBillDayConf = projectBillDayConfService.getById(id);
        if (ObjectUtil.isEmpty(projectBillDayConf)) {
            projectBillDayConf = new ProjectBillDayConf();
            projectBillDayConf.setBillDay("1");
        }
        projectBillDayConf.setStatus(DataConstants.TRUE);
        return R.ok(this.projectBillDayConfService.saveOrUpdate(projectBillDayConf));
    }
}