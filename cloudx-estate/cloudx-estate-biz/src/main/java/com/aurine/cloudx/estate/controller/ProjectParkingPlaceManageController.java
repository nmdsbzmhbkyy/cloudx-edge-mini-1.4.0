

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PlaceRelTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.service.ProjectParkingInfoService;
import com.aurine.cloudx.estate.service.ProjectParkingPlaceManageService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageRecordVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceManageVoMore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>车位归属管理</p>
 *
 * @ClassName: ProjectParkingPlaceManageController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/11 11:57
 * @Copyright:
 */
@RestController
@RequestMapping("/baseParkingManage")
@Api(value = "baseParkingManage", tags = "车位归属管理")
public class ProjectParkingPlaceManageController {
    @Resource
    private ProjectParkingPlaceManageService projectParkingPlaceManageService;
    @Resource
    private ProjectParkingInfoService projectParkingInfoService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;

    /**
     * 分页查询
     *
     * @param page              分页对象
     * @param searchConditionVo 车位查询条件
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectParkingPlacePage(Page page, ProjectParkingPlaceManageSearchConditionVo searchConditionVo) {
        int count = projectParkingInfoService.count(new QueryWrapper<ProjectParkingInfo>().lambda()
                .eq(ProjectParkingInfo::getProjectId, ProjectContextHolder.getProjectId()));
        if (count > 0) {
            return R.ok(projectParkingPlaceManageService.findPage(page, searchConditionVo));
        }
        return R.ok(new Page<ProjectParkingPlaceManageRecordVo>());
    }


    /**
     * 通过id查询车位
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(projectParkingPlaceManageService.getVoById(id));
    }

    /**
     * 获取自有和租赁车位数
     *
     * @return R
     */
    @ApiOperation(value = "获取自有和租赁车位数", notes = "获取自有和租赁车位数")
    @GetMapping("/getParkRelNum")
    public R getParkRelNum() {
        Map<String, Integer> resultMap = new HashMap<>();
        int propertyRightNum = projectParkingPlaceManageService.count(new QueryWrapper<ProjectParkingPlace>().lambda()
                .eq(ProjectParkingPlace::getRelType, PlaceRelTypeEnum.PROPERTYRIGHT.code)
                .ne(ProjectParkingPlace::getPersonId, ""));
        int rentNum = projectParkingPlaceManageService.count(new QueryWrapper<ProjectParkingPlace>().lambda()
                .eq(ProjectParkingPlace::getRelType, PlaceRelTypeEnum.RENT.code)
                .ne(ProjectParkingPlace::getPersonId, ""));
        resultMap.put("propertyRightNum", propertyRightNum);
        resultMap.put("rentNum", rentNum);
        return R.ok(resultMap);
    }

    /**
     * 通过id查询更多信息
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/more/{id}")
    public R getMoreInfoById(@PathVariable("id") String id) {
        ProjectParkingPlaceManageVo projectParkingPlaceManageVo = projectParkingPlaceManageService.getVoById(id);
        ProjectParkingPlaceManageVoMore moreVo = new ProjectParkingPlaceManageVoMore();
        BeanUtils.copyProperties(projectParkingPlaceManageVo, moreVo);
        return R.ok(moreVo);
    }

    /**
     * 迁入车位
     *
     * @param projectParkingPlaceManageVo 车位VO
     * @return R
     */
    @ApiOperation(value = "迁入车位", notes = "迁入车位")
    @SysLog("迁入车位")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_projectparkingplace_add')")
    public R save(@RequestBody ProjectParkingPlaceManageVo projectParkingPlaceManageVo) {


        /**
         * 设置租赁结束事件为23：59：
         * @since 2021-04-01
         * @auther 王伟
         */
        if(projectParkingPlaceManageVo.getExpTime() != null){
            LocalDateTime time = projectParkingPlaceManageVo.getExpTime();
            projectParkingPlaceManageVo.setExpTime(LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 23, 59, 59));
        }

        return R.ok(projectParkingPlaceManageService.save(projectParkingPlaceManageVo));
    }

    /**
     * 修改车位
     *
     * @param projectParkingPlaceManageVo 车位
     * @return R
     */
    @ApiOperation(value = "修改车位", notes = "修改车位")
    @SysLog("修改车位")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_projectparkingplace_edit')")
    public R updateById(@RequestBody ProjectParkingPlaceManageVo projectParkingPlaceManageVo) {
        /**
         * 设置租赁结束事件为23：59：
         * @since 2021-04-01
         * @auther 王伟
         */
        if(projectParkingPlaceManageVo.getExpTime() != null){
            LocalDateTime time = projectParkingPlaceManageVo.getExpTime();
            projectParkingPlaceManageVo.setExpTime(LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 23, 59, 59));
        }

        return R.ok(projectParkingPlaceManageService.updateById(projectParkingPlaceManageVo));
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
    @PreAuthorize("@pms.hasPermission('estate_projectparkingplace_del')")
    public R removeById(@PathVariable String id) {
        ProjectParkingPlace parkingPlace = projectParkingPlaceManageService.getById(id);
        boolean result = projectParkingPlaceManageService.removeParkingPlaceManageById(id);
        if (parkingPlace != null) {
            projectPersonInfoService.checkPersonAssets(parkingPlace.getPersonId());
        }
        return R.ok(result);
    }

    /**
     * 用户公共车位分配
     *
     * @param parkId     车场id
     * @param personId   人员id
     * @param personName 人员姓名
     * @return R
     */
    @ApiOperation(value = "给用户分配一个公共车位", notes = "给用户分配一个公共车位")
    @SysLog("给用户分配一个公共车位")
    @GetMapping("/allocationPersonPublicParkingPlace/{parkId}/{personId}/{personName}")
    @PreAuthorize("@pms.hasPermission('estate_projectparkingplace_del')")
    public R removeById(@PathVariable String parkId, @PathVariable String personId, @PathVariable String personName) {
        return R.ok(projectParkingPlaceManageService.allocationPersonPublicParkingPlace(parkId, personId, personName));
    }

    /**
     * <p>
     * 导入Excel文件
     * </p>
     *
     * @param file Excel文件对象
     * @param type 类型是系统的还是公安的
     * @author: 王良俊
     */
    @ApiOperation(value = "导入Excel文件", notes = "导入Excel文件")
    @SysLog("导入Excel文件")
    @PostMapping("/importExcel/{type}")
    public R importExcel(@PathVariable("type") String type, @RequestParam("file") MultipartFile file) {
        return projectParkingPlaceManageService.importExcel(file, type);
    }

    /**
     * <p>
     * 获取错误Excel
     * </p>
     *
     * @param name 文件名
     * @author: 许亮亮
     */
    @GetMapping("/errorExcel/{name}")
    @ApiModelProperty(value = "获取导入失败列表", notes = "获取导入失败列表")
    @Inner(false)
    public void errorExcel(@PathVariable("name") String name, HttpServletResponse httpServletResponse) throws IOException {
        projectParkingPlaceManageService.errorExcel(name, httpServletResponse);
    }

    @GetMapping("/modelExcel/{type}")
    @ApiModelProperty(value = "获取导入模板", notes = "获取导入模板")
    @Inner(false)
    public void modelExcel(@PathVariable("type") String type, HttpServletResponse httpServletResponse) throws IOException {
        projectParkingPlaceManageService.modelExcel(type, httpServletResponse);
    }

}
