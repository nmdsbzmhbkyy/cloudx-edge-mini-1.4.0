package com.aurine.cloudx.estate.controller;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.*;
import com.aurine.cloudx.estate.dto.ProjectHousePersonRelSearchConditionDTO;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.feign.RemoteDockModuleService;
import com.aurine.cloudx.estate.feign.RemoteFaceResourcesService;
import com.aurine.cloudx.estate.feign.RemoteHousePersonRelService;
import com.aurine.cloudx.estate.feign.RemoteWebSocketService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 住户管理 (ProjectHousePersonRelController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/3 10:13
 */
@RestController
@RequestMapping("/baseHousePersonRel")
@Api(value = "baseHousePersonRel", tags = "住户管理")
@Slf4j
public class ProjectHousePersonRelController {
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectHouseInfoService projectHouseInfoService;

    @Resource
    private RemoteHousePersonRelService remoteHousePersonRelService;

    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;

    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;

    @Resource
    private RemoteFaceResourcesService remoteFaceResourcesService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectisWrService projectisWrService;


    @Resource
    private RemoteDockModuleService remoteDockModuleService;



    /**
     * 分页查询住户信息(物业)
     *
     * @param page            分页对象
     * @param searchCondition 查询条件
     * @return
     */
    @ApiOperation(value = "分页查询住户信息(物业)", notes = "分页查询住户信息(物业)")
    @GetMapping("/person/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<IPage<ProjectHousePersonRelRecordVo>> getProjectHousePersonRelPage(Page page, ProjectHousePersonRelSearchConditionVo searchCondition) {
        return R.ok(projectHousePersonRelService.findPage(page, searchCondition));
    }

    /**
     * 获取业主车位和车辆信息表
     *
     * @param relaId 房屋人员关系id
     * @return
     */
    @ApiOperation(value = "获取业主车位和车辆信息表(物业)", notes = "获取业主车位和车辆信息表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "relaId", value = "房屋人员关系id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/info/{relaId}")
    public R<ProjectHouseParkPlaceInfoVo> getInfo(@PathVariable("relaId") String relaId) {

        return R.ok(projectHousePersonRelService.getInfo(relaId));
    }


    /**
     * 业主分页查询关联的房屋信息
     *
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = "业主分页查询关联的房屋信息(业主)", notes = "业主分页查询关联的房屋信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/page")
    public R<IPage<ProjectHousePersonRelRecordVo>> getProjectHousePersonRelPage(Page page) {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.ok(page);
        }
        ProjectHousePersonRelSearchConditionDTO projectHousePersonRelSearchConditionDTO = new ProjectHousePersonRelSearchConditionDTO();
        projectHousePersonRelSearchConditionDTO.setCurrent(page.getCurrent());
        projectHousePersonRelSearchConditionDTO.setSize(page.getSize());
        projectHousePersonRelSearchConditionDTO.setPersonId(projectPersonInfo.getPersonId());
        ProjectHousePersonRelSearchConditionVo projectHousePersonRelSearchConditionVo = new ProjectHousePersonRelSearchConditionVo();
        projectHousePersonRelSearchConditionVo.setPersonId(projectPersonInfo.getPersonId());

        return R.ok(projectHousePersonRelService.findPage(page, projectHousePersonRelSearchConditionVo));

    }

    /**
     * 获取一个房屋下的所有住户 :TODO
     *
     * @param id 房屋ID
     * @return
     */
    @ApiOperation(value = "分页查询(业主、物业)", notes = "获取某个房屋所有住户的分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "房屋id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/house/{id}")
    public R<List<ProjectHousePersonRelRecordVo>> getPersonRelByHouseId(@PathVariable("id") String id) {
        return R.ok(projectHousePersonRelService.listByHouseId(id));
    }

    /**
     * 查询该房屋的所有住户 :TODO
     *
     * @param houseId id
     * @return
     * @throws
     */
    @ApiOperation(value = "房屋住户查询(业主、物业)")
    @SysLog("房屋住户信息查看")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "houseId", value = "房屋id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = false, paramType = "param")
    })
    @GetMapping("/getHouseResident/{houseId}")
    public R<List<ProjectHouseResidentVo>> getHouseResident(@PathVariable("houseId") String houseId,
                                                            @RequestParam(value = "phone", required = false) String phone) {
        return R.ok(projectHouseInfoService.getHouseResidentsWithoutStatus(houseId, phone));
    }

    /**
     * 通过id查询住户房屋信息
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询住户(业主、物业)", notes = "通过id查询住户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "房屋关系id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/{id}")
    public R<ProjectHousePersonRelVo> getById(@PathVariable("id") String id) {
        return R.ok(projectHousePersonRelService.getVoById(id));
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
    @GetMapping("/checkHouseRel/{houseId}")
    @ApiOperation(value = "查看是否存在业主或家属", notes = "查看是否存在业主或家属")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "houseId", value = "房屋id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personName", value = "人员名称", required = true, paramType = "query"),
            @ApiImplicitParam(name = "houseHoldType", value = "房屋关系类型", required = true, paramType = "query"),
            @ApiImplicitParam(name = "personId", value = "人员id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectHousePersonRelVo> checkHouseRel(@PathVariable("houseId") String houseId,
                                                    @RequestParam("personName") String personName,
                                                    @RequestParam("houseHoldType") String houseHoldType,
                                                    @RequestParam("personId") String personId) {
        return R.ok(projectHousePersonRelService.checkHouseRel(houseId, personName, houseHoldType, personId));
    }

    /**
     * 通过id查询更多信息
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询更多信息(业主、物业)", notes = "通过id查询更多信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "房屋关系id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/more/{id}")
    public R<ProjectHousePersonRelVo> getMoreInfoById(@PathVariable("id") String id) {
        ProjectHousePersonRelVo projectHousePersonRelVo = projectHousePersonRelService.getVoById(id);
        return R.ok(projectHousePersonRelVo);
    }

    /**
     * 房间内是否已经有业主
     *
     * @param houseId houseId
     * @return R
     */
    @ApiOperation(value = "房间内是否已经有业主(业主、物业)", notes = "房间内是否已经有业主")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "houseId", value = "房屋id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/owner/{houseId}")
    public R<Boolean> haveOwner(@PathVariable("houseId") String houseId) {
        return R.ok(projectHousePersonRelService.haveOwner(houseId));
    }

    /**
     * 查询业主信息
     *
     * @param name
     * @return
     */
    @ApiOperation(value = "查询业主信息(业主、物业、访客)", notes = "查询业主信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/info")
    public R<List<ProjectHouseHisRecordVo>> findPerson(@Param("name") String name) {

        return R.ok(projectHousePersonRelService.findByName(name));
    }

    /**
     * 住户新增房屋申请
     *
     * @param projectHousePersonRel 住户
     * @return R
     */
    @ApiOperation(value = "住户新增房屋申请(业主)", notes = "住户新增房屋申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @SysLog("住户新增房屋申请")
    @PostMapping
    public R save(@RequestBody ProjectHousePersonRelRequestVo projectHousePersonRel) {
        //默认添加手机号
        if (StringUtils.isEmpty(projectHousePersonRel.getTelephone())) {
            projectHousePersonRel.setTelephone(SecurityUtils.getUser().getPhone());

        }
        projectHousePersonRel.setOrigin(DataOriginEnum.WECHAT.code);
        projectHousePersonRel.setOriginEx(DataOriginExEnum.YZ.code);
        projectHousePersonRel.setCheckInTime(LocalDateTime.now());
        ProjectPersonInfo personInfo = projectPersonInfoService.getPersonByOwner();
        if (!ObjectUtil.isEmpty(personInfo)) {
            projectHousePersonRel.setRemark(personInfo.getPersonId());
        }
        return R.ok(projectHousePersonRelService.request(projectHousePersonRel));
    }


    /**
     * 住户重新审核申请
     *
     * @param projectHousePersonRelRequestAgainVo 住户
     * @return R
     */
    @ApiOperation(value = "住户重新审核申请(业主)", notes = "住户新增房屋申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @SysLog("住户重新审核申请")
    @PutMapping("/requestAgain")
    public R requestAgain(@RequestBody ProjectHousePersonRelRequestAgainVo projectHousePersonRelRequestAgainVo) {

        return R.ok(projectHousePersonRelService.requestAgain(projectHousePersonRelRequestAgainVo));
    }

    /**
     * 修改住户
     *
     * @param projectHousePersonRel 住户
     * @return R
     */
    @ApiOperation(value = "修改住户(物业)", notes = "修改住户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @SysLog("修改住户")
    @PutMapping
    public R updateById(@RequestBody ProjectHousePersonRelVo projectHousePersonRel) {
        return R.ok(projectHousePersonRelService.updateById(projectHousePersonRel));
    }


    /**
     * 通过id迁出住户
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id迁出住户(业主、物业)", notes = "通过id迁出住户")
    @SysLog("通过id迁出住户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "房屋关系id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @DeleteMapping("/{id}")
    public R removeById(@PathVariable String id) {
        return R.ok(remoteHousePersonRelService.removeById(id));
    }


    /**
     * 取消申请
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "取消申请(业主、物业)", notes = "取消申请")
    @SysLog("取消申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "房屋关系id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @DeleteMapping("/rel/{id}")
    public R removeRelById(@PathVariable("id") String id) {
        remoteHousePersonRelService.removeRelById(id);
        return R.ok();
    }

    /**
     * 通过id批量迁出住户
     *
     * @param ids
     * @return R
     */
    @ApiOperation(value = "通过id列表批量删除住户(物业)", notes = "通过id列表批量删除住户")
    @SysLog("通过id批量删除住户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "房屋关系id列表", required = true, paramType = "body"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @DeleteMapping("/removeAll")
    public R removeById(@RequestBody List<String> ids) {
        if (ids.size() > 0) {
            return R.ok(remoteHousePersonRelService.removeAll(ids));
        } else {
            return R.ok(false);
        }
    }


    /**
     * 身份认证分页
     *
     * @param page            分页对象
     * @param searchCondition 查询条件
     * @return
     */
    @ApiOperation(value = "身份认证分页(物业)", notes = "身份认证分页(物业)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/pageIdentity")
    public R<IPage<ProjectHousePersonRelRecordVo>> pageIdentity(Page page, ProjectHousePersonRelSearchConditionVo searchCondition) {
        return R.ok(projectHousePersonRelService.pageIdentity(page, searchCondition));
    }

    @PutMapping("/verify")
    @ApiModelProperty(value = "身份认证审核", notes = "身份认证审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @SysLog("身份认证审核")
    public R verify(@RequestBody ProjectHousePersonRelVo projectHousePersonRel) {

        return R.ok(remoteHousePersonRelService.verify(projectHousePersonRel));
    }

    /**
     * 小程序新增住户
     *
     * @param projectHousePersonRel 住户
     * @return R
     */
    @ApiOperation(value = "新增住户", notes = "新增住户")
    @SysLog("小程序新增住户")
    @PostMapping("/byStaff")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<String> save(@RequestBody ProjectHousePersonRelVo projectHousePersonRel) {
        /**
         * 初始化住户、员工权限凭证，避免首次访问无通行方案
         * 如果用户已有方案，获取方案id，如果没有，分配默认方案，并返回方案id
         */
        String personId = projectPersonInfoService.getPersonId(projectHousePersonRel);
        projectPersonDeviceService.initDefaultPassRightPlan(PersonTypeEnum.PROPRIETOR, personId);
        ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(personId);
        //添加通行配置判断通行方法必须启用且未过期
        if (ObjectUtil.isNotEmpty(personPlanRel) &&
                DataConstants.ROOT.equals(personPlanRel.getIsActive()) &&
                LocalDateTime.now().isBefore(personPlanRel.getExpTime())) {
            projectHousePersonRel.setPersonId(personId);
            projectHousePersonRel.setOrigin(DataOriginEnum.WECHAT.code);
            projectHousePersonRel.setOriginEx(DataOriginExEnum.WY.code);
            projectHousePersonRel.setCheckInTime(LocalDateTime.now());
            projectHousePersonRel.setRentStartTime(LocalDateTime.now());
            projectHousePersonRel.setAuditStatus(AuditStatusEnum.pass.code);
            R<ProjectHousePersonRel> projectHousePersonRelR = remoteHousePersonRelService.saveRel(projectHousePersonRel);

            Boolean flag = remoteDockModuleService.isWr20(ProjectContextHolder.getProjectId()).getData();
            if (flag && flag !=null) {
                log.info("发送消息队列开始请求");

                KafkaSaveFaceVo map = new KafkaSaveFaceVo();
                map.setRelaId(projectHousePersonRelR.getData().getRelaId());
                map.setPersonId(personId);
                map.setPicUrl(projectHousePersonRel.getPicUrl());
                remoteHousePersonRelService.sendGeneratedFaceMessage(map);
       /*         try {


                    projectisWrService.findSaveFace(projectHousePersonRelR.getData().getRelaId(), personId, projectHousePersonRel.getPicUrl(),ProjectContextHolder.getProjectId());

                } catch (Exception exception) {
                    exception.printStackTrace();
                }*/
                //返回结果
                return R.ok("用户保存成功.人脸等待下发中");
            }else {
                ProjectFaceResources projectFaceResources = new ProjectFaceResources();
                projectFaceResources.setPersonId(personId);
                projectFaceResources.setPersonType(PersonTypeEnum.PROPRIETOR.code);
                projectFaceResources.setOrigin(DataOriginEnum.WECHAT.code);
                projectFaceResources.setPicUrl(projectHousePersonRel.getPicUrl());
                projectFaceResources.setStatus("1");
                return remoteFaceResourcesService.saveFaceByApp(projectFaceResources);
            }

        }


        return R.failed("该住户通行权限已被禁用，请启用后再进行配置");
    }
}
