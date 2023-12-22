package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.estate.entity.ProjectFeeConf;
import com.aurine.cloudx.estate.service.ProjectFeeConfService;
import com.aurine.cloudx.estate.vo.ProjectFeeConfVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 费用设置(ProjectFeeConf)表控制层
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@RestController
@RequestMapping("projectFeeConf")
@Api(value = "projectFeeConf", tags = "费用设置")
public class ProjectFeeConfController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectFeeConfService projectFeeConfService;

    /**
     * 分页查询所有数据
     *
     * @param page           分页对象
     * @param projectFeeConf 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectFeeConf所有数据")
    public R selectAll(Page<ProjectFeeConfVo> page, ProjectFeeConf projectFeeConf) {
        return R.ok(this.projectFeeConfService.pageFee(page,projectFeeConf));
    }

    /**
     * 查询所有数据列表
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有数据列表", notes = "查询projectFeeConf所有数据")
    public R selectList(@RequestParam(value = "type",required=false)String type) {
        return R.ok(this.projectFeeConfService.listFee(type,"1"));
    }




    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectFeeConf单条数据")
    public R selectOne(@PathVariable String  id) {
        return R.ok(this.projectFeeConfService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param projectFeeConf 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectFeeConf数据")
    public R insert(@RequestBody ProjectFeeConf projectFeeConf) {
        Integer countData = projectFeeConfService.count(Wrappers.lambdaQuery(ProjectFeeConf.class)
                .eq(ProjectFeeConf::getFeeName, projectFeeConf.getFeeName()));
        if (countData != null && countData > 0) {
            return R.failed("费用名称不允许重复");
        }
        return R.ok(this.projectFeeConfService.save(projectFeeConf));
    }

    /**
     * 修改数据
     *
     * @param projectFeeConf 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectFeeConf数据")
    public R update(@RequestBody ProjectFeeConf projectFeeConf) {
        Integer countData = projectFeeConfService.count(Wrappers.lambdaQuery(ProjectFeeConf.class)
                .eq(ProjectFeeConf::getFeeName, projectFeeConf.getFeeName())
                .notIn(ProjectFeeConf::getFeeId,projectFeeConf.getFeeId()));
        if (countData != null && countData > 0) {
            return R.failed("费用名称不允许重复");
        }
        return R.ok(this.projectFeeConfService.updateById(projectFeeConf));
    }


    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除数据", notes = "通过id删除projectFeeConf数据")
    public R delete(@PathVariable("id") String id) {
        String status=this.projectFeeConfService.getOne(Wrappers.lambdaQuery(ProjectFeeConf.class)
                .eq(ProjectFeeConf::getFeeId, id).select(ProjectFeeConf::getStatus)).getStatus();
        if (DataConstants.TRUE.equals(status)) {
            return R.failed("无法删除已启用的费用配置");
        }
        return R.ok(this.projectFeeConfService.removeById(id));
    }

    /**
     * 关闭收费设置
     *
     * @param id 主键
     * @return 修改结果
     */
    @PutMapping("/off/{id}")
    @ApiOperation(value = "关闭收费设置", notes = "关闭收费设置")
    public R setOff(@PathVariable("id") String id) {

        return R.ok(this.projectFeeConfService
                .update(Wrappers.lambdaUpdate(ProjectFeeConf.class)
                        .set(ProjectFeeConf::getStatus, DataConstants.FALSE)
                        .eq(ProjectFeeConf::getFeeId, id)));
    }

    /**
     * 启用收费设置
     *
     * @param id 主键
     * @return 修改结果
     */
    @PutMapping("/on/{id}")
    @ApiOperation(value = "启用收费设置", notes = "启用收费设置")
    public R setOn(@PathVariable("id")String id) {

        return R.ok(this.projectFeeConfService
                .update(Wrappers.lambdaUpdate(ProjectFeeConf.class)
                        .set(ProjectFeeConf::getStatus, DataConstants.TRUE)
                        .eq(ProjectFeeConf::getFeeId, id)));
    }

}