

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectHouseDesign;
import com.aurine.cloudx.estate.service.ProjectHouseBatchAddTemplateService;
import com.aurine.cloudx.estate.service.ProjectHouseDesignService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


/**
 * 项目户型配置表
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @date 2020-05-06 15:22:42
 */
@RestController
@AllArgsConstructor
@RequestMapping("/baseHousedesign")
@Api(value = "housedesign", tags = "项目户型配置表管理")
public class ProjectHouseDesignController {

    private final ProjectHouseDesignService projectHouseDesignService;
    private final ProjectHouseBatchAddTemplateService projectHouseBatchAddTemplateService;

    /**
     * 分页查询
     *
     * @param page       分页对象
     * @param desginDesc 项目户型配置表
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getHouseDesignPage(Page page, String desginDesc) {
//        System.err.println(houseDesign);
//        return R.ok(houseDesignService.page(page, Wrappers.query(houseDesign)));
        return R.ok(projectHouseDesignService.page(page,
                new QueryWrapper<ProjectHouseDesign>().lambda().eq(ProjectHouseDesign::getProjectId, ProjectContextHolder.getProjectId()).like(ProjectHouseDesign::getDesginDesc, desginDesc)));
    }

//    return R.ok(houseDesignService.page(page,
//            new QueryWrapper<HouseDesign>().lambda().like(HouseDesign::getDesginDesc, desginDesc)));

    /**
     * 通过id查询房屋
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(projectHouseDesignService.getById(id));
    }


    /**
     * 新增项目户型配置表
     *
     * @param projectHouseDesign 项目户型配置表
     * @return R
     */
    @ApiOperation(value = "新增项目户型配置表", notes = "新增项目户型配置表")
    @SysLog("新增项目户型配置表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('housedesign_add')")
    public R save(@RequestBody ProjectHouseDesign projectHouseDesign) {
        projectHouseDesign.setProjectId(ProjectContextHolder.getProjectId());
        String uid = UUID.randomUUID().toString().replace("-", "");
        projectHouseDesign.setDesignId(uid);
        Boolean flag = projectHouseDesignService.add(projectHouseDesign);
        if (flag) {
            return R.ok(uid);
        } else {
            return R.failed();
        }
    }

    /**
     * 户型查询
     * @param desginDesc 项目户型配置表
     * @return R
     */
    /*@ApiOperation(value = "通过户型查询", notes = "通过户型查询")
    @SysLog("通过户型查询" )
    @GetMapping("/desginDesc")
    public R getBydesginDesc(String desginDesc) {
        QueryWrapper<HouseDesign> query = new QueryWrapper<HouseDesign>();
        query.like("desginDesc", desginDesc);
        return R.ok(houseDesignService.getBaseMapper().selectOne(query));
    }*/

    /**
     * 通过id删除项目户型配置表
     *
     * @param designId uuid
     * @return R
     */
    @ApiOperation(value = "通过id删除项目户型配置表", notes = "通过id删除项目户型配置表")
    @SysLog("通过id删除项目户型配置表")
    @DeleteMapping("/{designId}")
    @PreAuthorize("@pms.hasPermission('housedesign_del')")
    public R removeById(@PathVariable String designId) {
        boolean isUsing = projectHouseBatchAddTemplateService.checkIsUsing(designId);
        if (isUsing) {
            throw new RuntimeException("该户型正在被模板使用中无法删除");
        }
        return R.ok(projectHouseDesignService.delete(designId));
    }

    /**
     * 修改房型
     *
     * @param projectHouseDesign
     * @return R
     */
    @ApiOperation(value = "修改房型", notes = "修改房型")
    @SysLog("通过id修改房型")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('housedesign_edit')")
    public R updateById(@RequestBody ProjectHouseDesign projectHouseDesign) {
        return R.ok(projectHouseDesignService.update(projectHouseDesign));
    }

    /**
     * 查询全部户型
     *
     * @return
     */
    @ApiOperation(value = "查询全部户型", notes = "查询全部户型")
    @GetMapping("/list")
    public R list() {
        return R.ok(projectHouseDesignService.list(new QueryWrapper<ProjectHouseDesign>().lambda().eq(ProjectHouseDesign::getProjectId, ProjectContextHolder.getProjectId())));
    }

    /**
     * 查询全部户型
     *
     * @return
     */
    @ApiOperation(value = "根据项目ID查找户型", notes = "根据项目ID查找户型")
    @GetMapping("/getByProjectId/{projectId}")
    public R getByProjectId(@PathVariable("projectId") Integer projectId) {
        return R.ok(projectHouseDesignService.getTopOne(projectId));
    }
}
