package com.aurine.cloudx.estate.open.parking.controller;

import com.aurine.cloudx.estate.open.parking.bean.ProjectParkingPlaceManageSearchConditionVoPage;
import com.aurine.cloudx.estate.open.parking.fegin.RemoteProjectParkingPlaceManageService;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageRecordVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>车位归属管理</p>
 *
 * @ClassName: ProjectParkingPlaceManageController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/11 11:57
 * @Copyright:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/parking-manage")
@Api(value = "parking-manage", tags = "车位归属管理")
public class ProjectParkingPlaceManageController {
    private RemoteProjectParkingPlaceManageService projectParkingPlaceManageService;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('parking-manage:get:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectParkingPlaceManageRecordVo>> getProjectParkingPlacePage(ProjectParkingPlaceManageSearchConditionVoPage page) {
        return projectParkingPlaceManageService.getProjectParkingPlacePage(page);
    }

    /**
     * 通过id查询车位
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询车位归属", notes = "通过id查询车位归属")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('parking-manage:get:info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "车位id", required = true, paramType = "path"),
    })
    public R<ProjectParkingPlaceManageVo> getById(@PathVariable("id") String id) {
        return projectParkingPlaceManageService.getById(id);
    }

//    /**
//     * 获取自有和租赁车位数
//     *
//     * @return R
//     */
//    @ApiOperation(value = "获取自有和租赁车位数", notes = "获取自有和租赁车位数")
//    @GetMapping("/getParkRelNum")
//    public R getParkRelNum() {
//        return projectParkingPlaceManageService.getParkRelNum();
//    }

//    /**
//     * 通过id查询更多信息
//     *
//     * @param id id
//     * @return R
//     */
//    @ApiOperation(value = "通过id查询", notes = "通过id查询")
//    @GetMapping("/more/{id}")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "车位id", required = true, paramType = "path"),
//    })
//    public R getMoreInfoById(@PathVariable("id") String id) {
//        return projectParkingPlaceManageService.getMoreInfoById(id);
//
//    }

    /**
     * 迁入车位
     *
     * @param projectParkingPlaceManageVo 车位VO
     * @return R
     */
    @ApiOperation(value = "迁入车位", notes = "迁入车位")
    @SysLog("迁入车位")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('parking-manage:post:save')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    public R<String> save(@RequestBody ProjectParkingPlaceManageVo projectParkingPlaceManageVo) {

        return projectParkingPlaceManageService.save(projectParkingPlaceManageVo);

    }

    /**
     * 修改车位
     *
     * @param projectParkingPlaceManageVo 车位
     * @return R
     */
    @ApiOperation(value = "修改车位归属", notes = "修改车位归属")
    @SysLog("修改车位归属")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('parking-manage:put:info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    public R<Boolean> updateById(@RequestBody ProjectParkingPlaceManageVo projectParkingPlaceManageVo) {

        return projectParkingPlaceManageService.updateById(projectParkingPlaceManageVo);

    }

    /**
     * 通过id迁出车位
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id删除车位", notes = "通过id迁出车位")
    @SysLog("通过id迁出车位")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('parking-manage:delete:info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "车位id", required = true, paramType = "path"),
    })
    public R<Boolean> removeById(@PathVariable String id) {

        return projectParkingPlaceManageService.removeById(id);
    }
//
//    /**
//     * 用户公共车位分配
//     *
//     * @param parkId     车场id
//     * @param personId   人员id
//     * @param personName 人员姓名
//     * @return R
//     */
//    @ApiOperation(value = "给用户分配一个公共车位", notes = "给用户分配一个公共车位")
//    @SysLog("给用户分配一个公共车位")
//    @PreAuthorize("@pms.hasPermission('parking-manage:get:info')")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "id", value = "车位id", required = true, paramType = "path"),
//    })
//    @GetMapping("/allocationPersonPublicParkingPlace/{parkId}/{personId}/{personName}")
//    public R removeById(@PathVariable String parkId, @PathVariable String personId, @PathVariable String personName) {
//        return projectParkingPlaceManageService.removeById(parkId, personId, personName);
//    }
}
