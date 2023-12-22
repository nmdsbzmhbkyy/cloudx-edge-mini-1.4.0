package com.aurine.cloudx.estate.controller;
import com.aurine.cloudx.estate.vo.ProjectStaffRotaDetailFromVo;
import com.pig4cloud.pigx.common.core.util.R;


import com.aurine.cloudx.estate.service.ProjectStaffRotaDetailService;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import javax.annotation.Resource;


/**
 * 值班明细(ProjectStaffRotaDetail)表控制层
 *
 * @author guhl@aurine.cn
 * @since 2020-08-03 11:49:09
 */
@RestController
@RequestMapping("projectStaffRotaDetail")
@Api(value="projectStaffRotaDetail",tags="值班明细")
public class ProjectStaffRotaDetailController  {
    /**
     * 服务对象
     */
    @Resource
    private ProjectStaffRotaDetailService projectStaffRotaDetailService;

    /**
     * 通过主键查询单条数据
     *
     * @param rotaDetailId 主键
     * @return 单条数据
     */
    @GetMapping("{rotaDetailId}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询值班明细单条数据")
    public R selectOne(@PathVariable("rotaDetailId") String rotaDetailId) {
        return R.ok(this.projectStaffRotaDetailService.getById(rotaDetailId));
    }

    /**
     * 新增数据
     *
     * @param projectStaffRotaDetailFromVo 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增值班明细数据")
    public R insert(@RequestBody ProjectStaffRotaDetailFromVo projectStaffRotaDetailFromVo) {
        return R.ok(projectStaffRotaDetailService.saveProjectStaffRotaDetail(projectStaffRotaDetailFromVo));
    }

    /**
     * 修改数据
     *
     * @param projectStaffRotaDetailFromVo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改值班明细数据")
    public R update(@RequestBody ProjectStaffRotaDetailFromVo projectStaffRotaDetailFromVo) {
        return R.ok(this.projectStaffRotaDetailService.updateProjectStaffRotaDetail(projectStaffRotaDetailFromVo));
    }
    /**
     * 删除数据
     *
     * @param rotaDetailId 主键结合
     * @return 删除结果
     */
    @DeleteMapping("/{rotaDetailId}")
    @ApiOperation(value = "删除数据", notes = "通过id删除值班明细数据")
    public R removeById(@PathVariable("rotaDetailId") String rotaDetailId) {
        return R.ok(this.projectStaffRotaDetailService.removeById(rotaDetailId));
    }
}