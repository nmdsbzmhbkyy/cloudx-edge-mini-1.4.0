package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.KafkaConstant;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.constant.enums.DataOriginEnum;
import com.aurine.cloudx.estate.constant.enums.DataOriginExEnum;
import com.aurine.cloudx.estate.constant.enums.IntoCloudStatusEnum;
import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.EdgeCloudRequest;
import com.aurine.cloudx.estate.entity.OpenApiEntity;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCommandTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.openapi.service.OpenApiMessageService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectHousePersonRelService;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.intercom.factory.IntercomFactoryProducer;
import com.aurine.cloudx.estate.util.WebSocketNotifyUtil;
import com.aurine.cloudx.estate.vo.ProjectHousePersonRelSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectHousePersonRelVo;
import com.aurine.cloudx.estate.vo.ProjectHousePersonRelVoMore;
import com.aurine.cloudx.estate.vo.ProjectHouseServiceInfoVo;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 住户
 *
 * @author pigx code generator
 * @date 2020-05-11 08:17:43
 */
@RestController
@RequestMapping("/baseHousePersonRel")
@Api(value = "baseHousePersonRel", tags = "住户管理")
@Slf4j
public class ProjectHousePersonRelController {

    @Resource
    private ProjectHouseServiceService projectHouseServiceService;

    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;

    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private AbstractProjectHousePersonRelService adapterProjectHousePersonRelServiceImpl;
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Resource
    private ProjectWebSocketService projectWebSocketService;

    /**
     * 分页查询
     *
     * @param page            分页对象
     * @param searchCondition 查询条件
     * @return
     * @author: 王良俊
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectHousePersonRelPage(Page page, ProjectHousePersonRelSearchConditionVo searchCondition) {
        return R.ok(projectHousePersonRelService.findPage(page, searchCondition));
    }

    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/pageAll")
    public R getProjectHousePersonRelPageAll(Page page, ProjectHousePersonRelSearchConditionVo searchCondition) {
        return R.ok(projectHousePersonRelService.findPageAll(page, searchCondition));
    }

    /**
     * 获取一个房屋下的所有住户
     *
     * @param id 房屋ID
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "获取某个房屋所有住户的分页列表")
    @GetMapping("/inner/house/{id}")
    @Inner(false)
    public R innerGetPersonRelByHouseId(@PathVariable("id") String id) {
        return R.ok(projectHousePersonRelService.list(new QueryWrapper<ProjectHousePersonRel>().eq("houseId", id)));
    }


    /**
     * 通过id查询住户
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(projectHousePersonRelService.getVoById(id));
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
        ProjectHousePersonRelVo projectHousePersonRelVo = projectHousePersonRelService.getVoById(id);
        ProjectHousePersonRelVoMore moreVo = new ProjectHousePersonRelVoMore();
        BeanUtils.copyProperties(projectHousePersonRelVo, moreVo);
        return R.ok(moreVo);
    }

    /**
     * 房间内是否已经有业主
     *
     * @param houseId houseId
     * @return R
     */
    @ApiOperation(value = "房间内是否已经有业主", notes = "房间内是否已经有业主")
    @GetMapping("/owner/{houseId}")
    public R haveOwner(@PathVariable("houseId") String houseId) {
        return R.ok(projectHousePersonRelService.haveOwner(houseId));
    }


    /**
     * 新增住户
     *
     * @param projectHousePersonRelVo 住户
     * @return R
     */
    @ApiOperation(value = "新增住户", notes = "新增住户")
    @SysLog("新增住户")
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('estate_housepersonrel_add')")
    public R save(@RequestBody ProjectHousePersonRelVo projectHousePersonRelVo) {

        /**
         * 设置租赁结束事件为23：59：
         * @since 2021-04-01
         * @auther 王伟
         */
        if(projectHousePersonRelVo.getRentStopTime() != null){
            LocalDateTime time = projectHousePersonRelVo.getRentStopTime();
            projectHousePersonRelVo.setRentStopTime(LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 23, 59, 59));
        }

        projectHousePersonRelVo.setOrigin(DataOriginEnum.WEB.code);
        projectHousePersonRelVo.setOriginEx(DataOriginExEnum.WY.code);
        projectHousePersonRelVo.setCheckInTime(LocalDateTime.now());
        ProjectHousePersonRel projectHousePersonRel = projectHousePersonRelService.saveRel(projectHousePersonRelVo);
        /**
         * 对接远端增值服务
         *
         * @author 顾煌龙
         */
        /*List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = projectHouseServiceService.getHouseServiceByHouseId(projectHousePersonRel.getHouseId());
        projectHouseServiceInfoVos.forEach(e -> {
            IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().addPerson(projectHousePersonRel, ProjectContextHolder.getProjectId());
        });*/
        return R.ok(projectHousePersonRel);
    }

    /**
     * 新增住户微服务调用
     *
     * @param projectHousePersonRelVo 住户
     * @return R
     */
    @ApiOperation(value = "新增住户微服务调用", notes = "新增住户微服务调用")
    @SysLog("新增住户微服务调用")
    @PostMapping("/saveRel")
    public R saveRel(@RequestBody ProjectHousePersonRelVo projectHousePersonRelVo) {


        /**
         * 设置租赁结束事件为23：59：
         * @since 2021-04-01
         * @auther 王伟
         */
        if(projectHousePersonRelVo.getRentStopTime() != null){
            LocalDateTime time = projectHousePersonRelVo.getRentStopTime();
            projectHousePersonRelVo.setRentStopTime(LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 23, 59, 59));
        }

        ProjectHousePersonRel projectHousePersonRel = projectHousePersonRelService.wechatSaveRel(projectHousePersonRelVo);
        /**
         * 对接远端增值服务
         *
         * @author 顾煌龙
         */
        /*List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = projectHouseServiceService.getHouseServiceByHouseId(projectHousePersonRel.getHouseId());
        projectHouseServiceInfoVos.forEach(e -> {
            IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().addPerson(projectHousePersonRel, ProjectContextHolder.getProjectId());
        });*/

        WebSocketNotifyUtil.sendMessgae(ProjectContextHolder.getProjectId().toString(), JSONObject.toJSONString(projectWebSocketService.findNumByProjectId()));

        return R.ok(projectHousePersonRel);
    }

    /**
     * 修改住户
     *
     * @param projectHousePersonRel 住户
     * @return R
     */
    @ApiOperation(value = "修改住户", notes = "修改住户")
    @SysLog("修改住户")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('estate_housepersonrel_edit')")
    public R updateById(@RequestBody ProjectHousePersonRelVo projectHousePersonRel) {

        /**
         * 设置租赁结束事件为23：59：
         * @since 2021-04-01
         * @auther 王伟
         */
        if(projectHousePersonRel.getRentStopTime() != null){
            LocalDateTime time = projectHousePersonRel.getRentStopTime();
            projectHousePersonRel.setRentStopTime(LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 23, 59, 59));
        }

        return R.ok(projectHousePersonRelService.updateById(projectHousePersonRel));
    }

    /**
     * 通过id迁出住户
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id删除住户", notes = "通过id删除住户")
    @SysLog("通过id删除住户")
    @DeleteMapping("/{id}")
//    @PreAuthorize("@pms.hasPermission('estate_housepersonrel_del')")
    public R removeById(@PathVariable String id) {
        ProjectHousePersonRel housePersonRel = projectHousePersonRelService.getById(id);
        boolean result = projectHousePersonRelService.removeHousePersonRelById(id);
        if (housePersonRel != null) {
            projectPersonInfoService.checkPersonAssets(housePersonRel.getPersonId());
        }
        return R.ok(result);
    }

    /**
     * 取消申请
     *
     * @param id
     * @return
     */
    @DeleteMapping("/rel/{id}")
    public R removeRelById(@PathVariable("id") String id) {
        projectHousePersonRelService.removeByRelId(id);
        return R.ok();
    }

    /**
     * 通过id批量迁出住户
     *
     * @param ids
     * @return R
     */
    @ApiOperation(value = "通过id批量删除住户", notes = "通过id批量删除住户")
    @SysLog("通过id批量删除住户")
    @DeleteMapping("/removeAll")
//    @PreAuthorize("@pms.hasPermission('estate_housepersonrel_del')")
    public R removeById(@RequestBody List<String> ids) {
        if (ids.size() > 0) {
            List<ProjectHousePersonRel> list = projectHousePersonRelService.list(new LambdaQueryWrapper<ProjectHousePersonRel>().in(ProjectHousePersonRel::getRelaId, ids));
            boolean b = projectHousePersonRelService.removeAll(ids);
            if (CollUtil.isNotEmpty(list)) {
                List<String> personIdList = list.stream().map(ProjectHousePersonRel::getPersonId).collect(Collectors.toList());
                projectPersonInfoService.checkPersonAssets(personIdList);
            }
            return R.ok(b);
        } else {
            return R.ok(false);
        }
    }

    /**
     * 使用Excel批量导入住户
     *
     * @param file Excel文件
     * @return R
     */
    @ApiOperation(value = "使用Excel批量导入住户", notes = "使用Excel批量导入住户")
    @SysLog("使用Excel批量导入住户")
    @PostMapping("/importExcel/{type}")
    public R importExcel(@PathVariable("type") String type, @RequestParam("file") MultipartFile file) {
        return projectHousePersonRelService.importExcel(file, type);
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
        projectHousePersonRelService.errorExcel(name, httpServletResponse);
    }

    /**
     * <p>
     * 获取导入模板
     * </p>
     *
     * @author: 许亮亮
     */
    @GetMapping("/modelExcel/{type}")
    @ApiModelProperty(value = "获取导入模板", notes = "获取导入模板")
    @Inner(false)
    public void modelExcel(@PathVariable("type") String type, HttpServletResponse httpServletResponse) throws IOException {
        projectHousePersonRelService.modelExcel(type, httpServletResponse);
    }

    /**
     * <p>
     * 身份认证审核
     * </p>
     *
     * @author: 顾煌龙
     */
    @PutMapping("/verify")
    @ApiModelProperty(value = "身份认证审核", notes = "身份认证审核")
    @SysLog("身份认证审核")
    public R verify(@RequestBody ProjectHousePersonRelVo projectHousePersonRel) {

        /**
         * 设置租赁结束事件为23：59：
         * @since 2021-04-01
         * @auther 王伟
         */
        if(projectHousePersonRel.getRentStopTime() != null){
            LocalDateTime time = projectHousePersonRel.getRentStopTime();
            projectHousePersonRel.setRentStopTime(LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), 23, 59, 59));
        }

//        ProjectHousePersonRel housePersonRel = projectHousePersonRelService.verify(projectHousePersonRel);
        /*ProjectHousePersonRel housePersonRel = adapterProjectHousePersonRelServiceImpl.verify(projectHousePersonRel);
        if (projectHousePersonRel.getAuditStatus().equals(AuditStatusEnum.pass.code)) {
            List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = projectHouseServiceService.getHouseServiceByHouseId(housePersonRel.getHouseId());
            projectHouseServiceInfoVos.forEach(e -> {
                //添加住户增值服务权限
                IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().addPerson(housePersonRel, ProjectContextHolder.getProjectId());
            });
        }*/
        return R.ok();
    }

    /**
     * <p>
     * 发送生成人脸消息
     * </p>
     *
     * @author: 顾煌龙
     */
    @GetMapping("/sendGeneratedFaceMessage")
    @ApiModelProperty(value = "发送生成人脸消息", notes = "sendGeneratedFaceMessage")
    @SysLog("发送生成人脸消息")
    public R sendGeneratedFaceMessage(KafkaSaveFaceVo map) {

        map.setProjectId(ProjectContextHolder.getProjectId());
        map.setTenantId(1);
        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(KafkaConstant.HUMANFACE_ISSUED,JSONObject.toJSONString(map));
        send.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                log.info(stringStringSendResult.getProducerRecord().toString());
            }
            @Override
            public void onFailure(Throwable throwable) {
                //发送失败的处理
                log.error("发送消息失败");
            }
        });

        return R.ok();
    }


    /**
     * 通过id查询住户
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "身份认证详情", notes = "通过id查询")
    @GetMapping("/verify/{id}")
    public R getVerifyById(@PathVariable("id") String id) {
        return R.ok(projectHousePersonRelService.getHousePersonVerifyInfo(id));
    }

    /**
     * 查询用户人脸状态
     *
     * @param  relaId
     * @return R
     */
    @ApiOperation(value = "身份认证详情", notes = "通过id查询")
    @GetMapping("/findSaveFace/{relaId}")
    public R<String> findSaveFace(@PathVariable("relaId") String relaId) {
        String saveFace = projectHousePersonRelService.findSaveFace(relaId);
        if (saveFace==null){
            return R.failed();
        }
        return R.ok();
    }
    /**
     * <p>
     * 批量审核通过
     * </p>
     *
     * @author: 顾煌龙
     */
    @PutMapping("/passAll")
    @ApiModelProperty(value = "批量审核通过", notes = "批量审核通过")
    @SysLog("批量审核通过")
    public R passAll(@RequestBody List<String> ids) {
        return R.ok(projectHousePersonRelService.passAll(ids));
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
    @GetMapping("/checkHouseRel/{houseId}/{personName}/{houseHoldType}/{personId}")
    @ApiModelProperty(value = "查看是否存在业主或家属", notes = "查看是否存在业主或家属")
    public R checkHouseRel(@PathVariable("houseId") String houseId,
                           @PathVariable("personName") String personName,
                           @PathVariable("houseHoldType") String houseHoldType,
                           @PathVariable("personId") String personId) {
        return R.ok(projectHousePersonRelService.checkHouseRel(houseId, personName, houseHoldType, personId));
    }

    /**
     * 身份认证分页
     *
     * @param page            分页对象
     * @param searchCondition 查询条件
     * @return
     * @author: 顾煌龙
     */
    @ApiOperation(value = "身份认证分页", notes = "身份认证分页")
    @GetMapping("/pageIdentity")
    public R pageIdentity(Page page, ProjectHousePersonRelSearchConditionVo searchCondition) {
        return R.ok(projectHousePersonRelService.pageIdentity(page, searchCondition));
    }

    @GetMapping("/list-house-by-person-id/{id}")
    R<List<ProjectHouseDTO>> listHouseByPersonId(@PathVariable("id") String id) {
        return R.ok(projectHousePersonRelService.listHouseByPersonId(id));
    }

    /**
     * 按住户类型统计当前项目下的入住数量
     * 1 业主（产权人） 2 家属 3 租客
     * @param houseHoldType
     * @return
     */
    @GetMapping("/countByHouseHoldType/{houseHoldType}")
    R<Integer> countByHouseHoldType(@PathVariable("houseHoldType") String houseHoldType) {
        return R.ok(projectHousePersonRelService.countHousePersonRel(houseHoldType));
    }

    /**
     * 烤猫说:
     *     有啥用:
     *         根据给定的电话号码找到personId,然后在房屋用户表表中,查询对应的房屋ID是否已经预注册(一个房屋可以有个住户,hhhhh)
     *     缺点:
     *         前端最好在电话和房屋都存在的情况下，在向后端发送请求,只是需要用户自己的机子处理而已
     * */
    @PostMapping("/checkHasPreRegister")
    public R checkHasPreRegister(@RequestBody ProjectHousePersonRelVo projectHousePersonRel) {

        return R.ok(projectHousePersonRelService.checkHasPreRegister(projectHousePersonRel.getTelephone(), projectHousePersonRel.getHouseId()));
    }

    /**
     * 重新下发
     * @param personId
     * @return
     */
    @GetMapping("/reissue/{personId}")
    public R reissue(@PathVariable String personId) {
        return R.ok(adapterProjectHousePersonRelServiceImpl.reSaveRel(personId));
    }
}
