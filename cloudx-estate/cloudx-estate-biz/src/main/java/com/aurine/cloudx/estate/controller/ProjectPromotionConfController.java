package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.estate.constant.FeeConstant;
import com.aurine.cloudx.estate.constant.enums.FeeCycleTypeEnum;
import com.aurine.cloudx.estate.constant.enums.FeeTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectFeeConf;
import com.aurine.cloudx.estate.service.ProjectFeeConfService;
import com.aurine.cloudx.estate.vo.ProjectFeeConfVo;
import com.aurine.cloudx.estate.vo.ProjectPromotionConfFormVo;
import com.aurine.cloudx.estate.vo.ProjectPromotionConfPageVo;
import com.aurine.cloudx.estate.vo.ProjectPromotionConfVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.core.util.R;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aurine.cloudx.estate.entity.ProjectPromotionConf;
import com.aurine.cloudx.estate.service.ProjectPromotionConfService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠活动设置(ProjectPromotionConf)表控制层
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@RestController
@RequestMapping("projectPromotionConf")
@Api(value = "projectPromotionConf", tags = "优惠活动设置")
public class ProjectPromotionConfController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectPromotionConfService projectPromotionConfService;

    @Resource
    private ProjectFeeConfService projectFeeConfService;

    /**
     * 分页查询所有数据
     *
     * @param page                 分页对象
     * @param projectPromotionConf 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectPromotionConf所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<IPage<ProjectPromotionConfPageVo>> selectAll(Page<ProjectPromotionConfPageVo> page, ProjectPromotionConfFormVo projectPromotionConf) {
        //前端无法传递时间类型 故做转换 xull@aurine.cn 2020/5/22 18:40
        if (projectPromotionConf.getBeginTimeString() != null && !"".equals(projectPromotionConf.getBeginTimeString())) {
            projectPromotionConf.setBeginTime(LocalDate.parse(projectPromotionConf.getBeginTimeString(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if (projectPromotionConf.getEndTimeString() != null && !"".equals(projectPromotionConf.getEndTimeString())) {
            projectPromotionConf.setEndTime(LocalDate.parse(projectPromotionConf.getEndTimeString(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        return R.ok(this.projectPromotionConfService.pageByForm(page, projectPromotionConf));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectPromotionConf单条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "优惠活动设置id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R selectOne(@PathVariable String id) {
        return R.ok(this.projectPromotionConfService.getPromotion(id));
    }

    /**
     * 新增数据
     *
     * @param projectPromotionConfVo 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectPromotionConf数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R insert(@RequestBody ProjectPromotionConfVo projectPromotionConfVo) {
        if (dataCheck(projectPromotionConfVo)) {
            return R.failed("请选择参与费用");
        }
        Integer countData = this.projectPromotionConfService.count(Wrappers.lambdaQuery(ProjectPromotionConf.class)
                .eq(ProjectPromotionConf::getPromotionName, projectPromotionConfVo.getPromotionName()));
        if (countData != null && countData > 0) {
            return R.failed("优惠活动名称不允许重复");
        }
        return this.projectPromotionConfService.savePromotionConf(projectPromotionConfVo);
    }

    /**
     * 修改数据
     *
     * @param projectPromotionConfVo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectPromotionConf数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R update(@RequestBody ProjectPromotionConfVo projectPromotionConfVo) {
        if (dataCheck(projectPromotionConfVo)) {
            return R.failed("请选择参与费用");
        }
        Integer countData = this.projectPromotionConfService.count(Wrappers.lambdaQuery(ProjectPromotionConf.class)
                .eq(ProjectPromotionConf::getPromotionName, projectPromotionConfVo.getPromotionName())
                .notIn(ProjectPromotionConf::getPromotionId, projectPromotionConfVo.getPromotionId()));
        if (countData != null && countData > 0) {
            return R.failed("优惠活动名称不允许重复");
        }
        return this.projectPromotionConfService.updatePromotionConf(projectPromotionConfVo);
    }

    private boolean dataCheck(@RequestBody ProjectPromotionConfVo projectPromotionConfVo) {
        // 如果是全选则获取查询获取所有费用配置
        if (DataConstants.TRUE.equals(projectPromotionConfVo.getSelectType())) {
            //查询启用的费用
            List<ProjectFeeConfVo> list = projectFeeConfService.listFee(projectPromotionConfVo.getPromotionType(), "1");
            List<String> feeConfIds = list.stream().map(ProjectFeeConf::getFeeId).collect(Collectors.toList());
            projectPromotionConfVo.setFeeIds(feeConfIds);
        }

        if (projectPromotionConfVo.getFeeIds() == null || projectPromotionConfVo.getFeeIds().size() == 0) {
            return true;
        }
        return false;
    }


    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除数据", notes = "通过id删除projectPromotionConf数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "优惠活动设置id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R delete(@PathVariable("id") String id) {
        return R.ok(this.projectPromotionConfService.removePromotionConf(id));
    }
}