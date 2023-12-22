package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.vo.ProjectEmployerInfoFromVo;
import com.aurine.cloudx.estate.vo.ProjectEmployerInfoPageVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aurine.cloudx.estate.entity.ProjectEmployerInfo;
import com.aurine.cloudx.estate.service.ProjectEmployerInfoService;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目实有单位信息(ProjectEmployerInfo)表控制层
 *
 * @author guhl@aurine.cn
 * @since 2020-08-25 14:58:44
 */
@RestController
@RequestMapping("projectEmployerInfo")
@Api(value = "projectEmployerInfo", tags = "项目实有单位信息")
public class ProjectEmployerInfoController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectEmployerInfoService projectEmployerInfoService;

    /**
     * 分页查询所有数据
     *
     * @param page                      分页对象
     * @param projectEmployerInfoPageVo 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectEmployerInfo所有数据")
    public R selectAll(Page<ProjectEmployerInfo> page, ProjectEmployerInfoPageVo projectEmployerInfoPageVo) {
        return R.ok(this.projectEmployerInfoService.pageEmployer(page, projectEmployerInfoPageVo));
    }

    /**
     * 通过房屋id查询单条数据
     *
     * @param houseId 房屋id
     * @return 单条数据
     */
    @GetMapping("{houseId}")
    @ApiOperation(value = "通过房屋id查询", notes = "通过房屋id查询projectEmployerInfo单条数据")
    public R selectOne(@PathVariable("houseId") String houseId) {
        return R.ok(this.projectEmployerInfoService.getByHouseId(houseId));
    }

    /**
     * 新增数据
     *
     * @param projectEmployerInfoFromVo 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectEmployerInfo数据")
    public R insert(@RequestBody ProjectEmployerInfoFromVo projectEmployerInfoFromVo) {
        return R.ok(this.projectEmployerInfoService.saveEmployerInfo(projectEmployerInfoFromVo));
    }

    /**
     * 修改数据
     *
     * @param projectEmployerInfoFromVo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectEmployerInfo数据")
    public R update(@RequestBody ProjectEmployerInfoFromVo projectEmployerInfoFromVo) {
        return R.ok(this.projectEmployerInfoService.updateEmployerInfo(projectEmployerInfoFromVo));
    }

    /**
     * 迁出
     *
     * @param houseId 主键结合
     * @return 删除结果
     */
    @DeleteMapping("{houseId}")
    @ApiOperation(value = "迁出", notes = "迁出")
    public R delete(@PathVariable("houseId") String houseId) {
        return R.ok(this.projectEmployerInfoService.removeByHouseId(houseId));
    }

    /**
     * 通过统一社会信用代码查询实有单位
     *
     * @param socialCreditCode
     * @return 单条数据
     */
    @GetMapping("/getBySocialCreditCode")
    @ApiOperation(value = "通过统一社会信用代码查询实有单位", notes = "通过统一社会信用代码查询实有单位")
    public R getBySocialCreditCode(@RequestParam(value = "socialCreditCode", required = false) String socialCreditCode,
                                   @RequestParam(value = "employerId", required = false) String employerId) {
        return R.ok(projectEmployerInfoService.getBySocialCreditCode(socialCreditCode, employerId));
    }
}