package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectDeviceLocation;
import com.aurine.cloudx.estate.service.ProjectDeviceLocationService;
import com.aurine.cloudx.estate.service.ProjectFloorPicService;
import com.aurine.cloudx.estate.vo.ProjectFloorPicSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectFloorPicVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

/**
 * @ClassName: ProjectFloorPicController
 * @author: 王良俊
 * @date: 2020年05月07日 下午05:42:43
 * @Copyright:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/baseFloorPlan")
@Api(value = "plan", tags = "平面图模块")
public class ProjectFloorPicController {

    @Autowired
    private final ProjectFloorPicService projectFloorPicServiceImpl;
    @Autowired
    private final ProjectDeviceLocationService projectDeviceLocationService;

    /**
     * <p>
     * 增加平面图
     * </p>
     *
     * @param vo
     * @return
     */
    @PostMapping
    @ApiOperation(value = "增加平面图")
    public R add(@RequestBody ProjectFloorPicVo vo) {
        return R.ok(projectFloorPicServiceImpl.savePlan(vo));
    }

    /**
     * <p>
     * 修改平面图
     * </p>
     *
     * @param vo
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改平面图")
    public R update(@RequestBody ProjectFloorPicVo vo) {
        return R.ok(projectFloorPicServiceImpl.update(vo));
    }

    /**
     * <p>
     * 分页检索列表数据
     * </p>
     *
     * @param projectFloorPicSearchCondition
     * @param page
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @GetMapping("findList")
    @ApiOperation(value = "获取平面图列表")
    public R findList(ProjectFloorPicSearchCondition projectFloorPicSearchCondition, Page page) {
        projectFloorPicSearchCondition.setProjectId(ProjectContextHolder.getProjectId());
        return R.ok(projectFloorPicServiceImpl.findPage(page, projectFloorPicSearchCondition));
    }


    /**
     * 获取设备打点的平面图
     *
     * @param projectFloorPicSearchCondition
     * @param page
     * @return
     */
    @GetMapping("/getPicLocation")
    @ApiOperation(value = "获取设备打点的平面图")
    public R<IPage<ProjectFloorPicVo>> getPicLocation(ProjectFloorPicSearchCondition projectFloorPicSearchCondition , Page page) {
        Integer projectId = ProjectContextHolder.getProjectId();
        return R.ok(projectFloorPicServiceImpl.getPicLocation(page, projectFloorPicSearchCondition.getDeviceId(),projectFloorPicSearchCondition.getRegionId(),projectId));
    }





    /**
     * <p>
     * 获取平面图
     * </p>
     *
     * @param picId
     * @return
     */
    @GetMapping("/{picId}")
    @ApiOperation(value = "获取平面图")
    public R get(@PathVariable("picId") String picId) {
        return R.ok(this.projectFloorPicServiceImpl.getById(picId));
    }

    /**
     * <p>
     * 删除平面图
     * </p>
     *
     * @param picId
     * @return
     */
    @DeleteMapping("/{picId}")
    @ApiOperation(value = "删除平面图")
    public R delete(@PathVariable("picId") String picId) {
        projectDeviceLocationService.remove(new QueryWrapper<ProjectDeviceLocation>().lambda().eq(ProjectDeviceLocation::getPicId,picId));
        return R.ok(projectFloorPicServiceImpl.removeById(picId));
    }
}
