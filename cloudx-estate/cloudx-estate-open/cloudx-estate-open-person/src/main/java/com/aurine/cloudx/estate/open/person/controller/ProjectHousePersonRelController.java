package com.aurine.cloudx.estate.open.person.controller;

import com.aurine.cloudx.estate.open.person.bean.ProjectHousePersonRelSearchConditionVoPage;
import com.aurine.cloudx.estate.open.person.fegin.RemoteProjectHousePersonRelService;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 住户管理 (ProjectHousePersonRelController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/3 10:13
 */
@RestController
@RequestMapping("/person")
@Api(value = "person", tags = "住户管理")
public class ProjectHousePersonRelController {
    @Resource
    private RemoteProjectHousePersonRelService projectHousePersonRelService;

    /**
     * 分页查询住户信息(物业)
     *
     * @param page            分页对象 查询条件
     * @return
     */
    @ApiOperation(value = "分页查询住户信息", notes = "分页查询住户信息")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('person:get:page')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<IPage<ProjectHousePersonRelRecordVo>> getProjectHousePersonRelPage(ProjectHousePersonRelSearchConditionVoPage page) {
        return projectHousePersonRelService.getProjectHousePersonRelPage(page);
    }

    /**
     * 获取业主车位和车辆信息表
     *
     * @param relaId 房屋人员关系id
     * @return
     */
    @ApiOperation(value = "获取业主车位和车辆信息表", notes = "获取业主车位和车辆信息表")
    @GetMapping("/info/{relaId}")
    @PreAuthorize("@pms.hasPermission('person:get:info')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "relaId", value = "房屋人员关系id", required = true, paramType = "path")
    })
    public R<ProjectHouseParkPlaceInfoVo> getInfo(@PathVariable("relaId") String relaId) {

        return projectHousePersonRelService.getInfo(relaId);
    }

//    /**
//     * 业主分页查询关联的房屋信息
//     *
//     * @param page 分页对象
//     * @return
//     */
//    @ApiOperation(value = "业主分页查询关联的房屋信息", notes = "业主分页查询关联的房屋信息")
//    @GetMapping("/resident/page")
//    public R<IPage<ProjectHousePersonRelRecordVo>> getProjectHousePersonRelPage(Page page) {
//
//        return projectHousePersonRelService.getProjectHousePersonRelPage(page);
//    }

    /**
     * 获取一个房屋下的所有住户
     *
     * @param id 房屋ID
     * @return
     */
    @ApiOperation(value = "分页查询(业主、物业)", notes = "获取某个房屋所有住户的分页列表")
    @GetMapping("/house/{houseId}")
    @PreAuthorize("@pms.hasPermission('person:get:house')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "houseId", value = "房屋id", required = true, paramType = "path")
    })
    public R<List<ProjectHousePersonRelRecordVo>> getPersonRelByHouseId(@PathVariable("houseId") String id) {
        return projectHousePersonRelService.getPersonRelByHouseId(id);
    }

//    /**
//     * 查询该房屋的所有住户
//     *
//     * @param houseId id
//     * @return
//     * @throws
//     */
//    @ApiOperation(value = "房屋住户查询(业主、物业)")
//    @SysLog("房屋住户信息查看")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "houseId", value = "房屋id", required = true, paramType = "path"),
//            @ApiImplicitParam(name = "phone", value = "手机号", required = false, paramType = "param")
//    })
//    @GetMapping("/resident/{houseId}")
//    public R<List<ProjectHouseResidentVo>> getHouseResident(@PathVariable("houseId") String houseId,
//                                    @RequestParam(value = "phone", required = false) String phone) {
//        return projectHousePersonRelService.getHouseResident(houseId, phone);
//    }

    /**
     * 通过id查询住户房屋信息
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询住户", notes = "通过id查询住户")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('person:get:house')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "houseId", value = "房屋id", required = true, paramType = "path")
    })
    public R<ProjectHousePersonRelVo> getById(@PathVariable("id") String id) {
        return projectHousePersonRelService.getById(id);
    }

    /**
     * 查看是否存在业主或家属
     *
     * @param houseId
     * @param personName
     * @param houseHoldType
     * @param personId
     * @return
     */
    @GetMapping("/check/{houseId}")
    @ApiOperation(value = "查看是否存在业主或家属", notes = "查看是否存在业主或家属")
    @PreAuthorize("@pms.hasPermission('person:get:check')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "houseId", value = "房屋id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personName", value = "人员名称", required = true, paramType = "query"),
            @ApiImplicitParam(name = "houseHoldType", value = "房屋关系类型", required = true, paramType = "query"),
            @ApiImplicitParam(name = "personId", value = "人员id", required = true, paramType = "query"),
    })
    public R<ProjectHousePersonRelVo> checkHouseRel(@PathVariable("houseId") String houseId,
                                                    @RequestParam("personName") String personName,
                                                    @RequestParam("houseHoldType") String houseHoldType,
                                                    @RequestParam("personId") String personId) {
        return projectHousePersonRelService.checkHouseRel(houseId, personName, houseHoldType, personId);
    }

//    /**
//     * 通过id查询更多信息
//     *
//     * @param id id
//     * @return R
//     */
//    @ApiOperation(value = "通过id查询更多信息(业主、物业)", notes = "通过id查询更多信息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "房屋关系id", required = true, paramType = "path"),
//    })
//    @GetMapping("/more/{id}")
//    public R<ProjectHousePersonRelVo> getMoreInfoById(@PathVariable("id") String id) {
//
//        return projectHousePersonRelService.getMoreInfoById(id);
//    }

//    /**
//     * 房间内是否已经有业主
//     *
//     * @param houseId houseId
//     * @return R
//     */
//    @ApiOperation(value = "房间内是否已经有业主(业主、物业)", notes = "房间内是否已经有业主")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "houseId", value = "房屋id", required = true, paramType = "path"),
//    })
//    @GetMapping("/check/owner/{houseId}")
//    public R<Boolean> haveOwner(@PathVariable("houseId") String houseId) {
//        return projectHousePersonRelService.haveOwner(houseId);
//    }

    /**
     * 查询业主信息
     *
     * @param name
     * @return
     */
    @ApiOperation(value = "查询业主信息(业主、物业、访客)", notes = "查询业主信息")
    @GetMapping("/info")
    @PreAuthorize("@pms.hasPermission('person:get:findPerson')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, paramType = "query"),
    })
    public R<List<ProjectHouseHisRecordVo>> findPerson(@Param("name") String name) {

        return projectHousePersonRelService.findPerson(name);
    }

    /**
     * 住户新增房屋申请
     *
     * @param projectHousePersonRel 住户
     * @return R
     */
    @ApiOperation(value = "新增住户", notes = "新增住户")
    @SysLog("新增住户")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('person:post:save')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    public R save(@RequestBody ProjectHousePersonRelRequestVo projectHousePersonRel) {
        return projectHousePersonRelService.save(projectHousePersonRel);
    }

//    /**
//     * 住户重新审核申请
//     *
//     * @param projectHousePersonRelRequestAgainVo 住户
//     * @return R
//     */
//    @ApiOperation(value = "住户重新审核申请(业主)", notes = "住户新增房屋申请")
//    @SysLog("住户重新审核申请")
//    @PutMapping("/request")
//    public R requestAgain(@RequestBody ProjectHousePersonRelRequestAgainVo projectHousePersonRelRequestAgainVo) {
//
//        return projectHousePersonRelService.requestAgain(projectHousePersonRelRequestAgainVo);
//    }

    /**
     * 修改住户
     *
     * @param projectHousePersonRel 住户
     * @return R
     */
    @ApiOperation(value = "修改住户(物业)", notes = "修改住户")
    @SysLog("修改住户")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('person:put:update')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    public R updateById(@RequestBody ProjectHousePersonRelVo projectHousePersonRel) {
        return projectHousePersonRelService.updateById(projectHousePersonRel);
    }

    /**
     * 通过id迁出住户
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id迁出住户(业主、物业)", notes = "通过id迁出住户")
    @SysLog("通过id迁出住户")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('person:delete:removeById')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "房屋关系id", required = true, paramType = "path"),
    })
    public R removeById(@PathVariable("id") String id) {
        return projectHousePersonRelService.removeById(id);
    }

//    /**
//     * 取消申请
//     *
//     * @param id
//     * @return R
//     */
//    @ApiOperation(value = "取消申请(业主、物业)", notes = "取消申请")
//    @SysLog("取消申请")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "房屋关系id", required = true, paramType = "path"),
//    })
//    @DeleteMapping("/rel/{id}")
//    public R removeRelById(@PathVariable("id") String id) {
//        return  projectHousePersonRelService.removeRelById(id);
//
//    }

//    /**
//     * 通过id批量迁出住户
//     *
//     * @param ids
//     * @return R
//     */
//    @ApiOperation(value = "通过id列表批量删除住户(物业)", notes = "通过id列表批量删除住户")
//    @SysLog("通过id批量删除住户")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "ids", value = "房屋关系id列表", required = true, paramType = "body"),
//    })
//    @DeleteMapping("/removeAll")
//    public R removeById(@RequestBody List<String> ids) {
//
//      return   projectHousePersonRelService.removeById(ids);
//    }

//    /**
//     * 身份认证分页
//     *
//     * @param page            分页对象
//     * @return
//     */
//    @ApiOperation(value = "身份认证分页(物业)", notes = "身份认证分页(物业)")
//    @GetMapping("/identity/page")
//    public R<IPage<ProjectHousePersonRelRecordVo>> pageIdentity(ProjectHousePersonRelSearchConditionVoPage page) {
//        return projectHousePersonRelService.pageIdentity(page);
//    }

//    @PutMapping("/verify")
//    @ApiModelProperty(value = "身份认证审核", notes = "身份认证审核")
//    @SysLog("身份认证审核")
//    public R verify(@RequestBody ProjectHousePersonRelVo projectHousePersonRel) {
//
//        return projectHousePersonRelService.verify(projectHousePersonRel);
//    }
}
