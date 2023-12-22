package com.aurine.cloudx.open.push.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.annotation.SkipCheck;
import com.aurine.cloudx.open.common.core.constant.enums.OpenFieldEnum;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.OpenPushSubscribeCallbackVo;
import com.aurine.cloudx.open.common.validate.group.AppGroup;
import com.aurine.cloudx.open.common.validate.group.InsertOnlyGroup;
import com.aurine.cloudx.open.push.service.PushSubscribeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 推送订阅管理
 * 注：该接口是应用层级的接口，所以需要在接口处定义较多的注解且在校验规则上使用了AppGroup（应用分组）
 * 注：应用层级的接口新增时需要使用InsertOnlyGroup（只新增分组），防止校验到默认规则，修改则使用UpdateOnlyGroup（只修改分组）
 * 注：应用层级接口不需要校验projectUUID、tenantId、projectId，所以在注解处排除
 *
 * @author : Qiu
 * @date : 2021 12 07 10:31
 */

@RestController
@RequestMapping("/v1/push/subscribe")
@Api(value = "openPushSubscribe", tags = {"v1", "推送相关", "推送订阅管理"})
@Inner
@Slf4j
public class PushSubscribeController {

    @Resource
    private PushSubscribeService pushSubscribeService;


    /**
     * 通过id查询推送订阅信息
     *
     * @param header 请求头信息
     * @param id     id
     * @return R 返回推送订阅信息
     */
    @SkipCheck(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID})
    @ApiOperation(value = "通过id查询推送订阅信息", notes = "通过id查询推送订阅信息")
    @SysLog("通过id查询推送订阅信息")
    @GetMapping("/{id}")
    public R<OpenPushSubscribeCallbackVo> getById(@Validated({AppGroup.class}) OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[PushSubscribeController - getById]: 通过id查询推送订阅信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return pushSubscribeService.getById(id);
    }

    /**
     * 分页查询推送订阅
     *
     * @param pageModel 分页查询条件
     * @return R 返回推送订阅分页数据
     */
    @AutoInject(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID, OpenFieldEnum.PROJECT_ID})
    @SkipCheck(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID})
    @ApiOperation(value = "分页查询推送订阅", notes = "分页查询推送订阅")
    @SysLog("分页查询推送订阅")
    @PostMapping("/page")
    public R<Page<OpenPushSubscribeCallbackVo>> page(@Validated({AppGroup.class}) @RequestBody OpenApiPageModel<OpenPushSubscribeCallbackVo> pageModel) {
        log.info("[PushSubscribeController - page]: 分页查询推送订阅, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return pushSubscribeService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 根据查询条件获取订阅列表
     *
     * @param model 查询条件
     * @return R 返回推送订阅列表
     */
    @AutoInject(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID, OpenFieldEnum.PROJECT_ID})
    @SkipCheck(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID})
    @ApiOperation(value = "根据查询条件获取订阅列表", notes = "根据查询条件获取订阅列表")
    @SysLog("根据查询条件获取订阅列表")
    @PostMapping("/list")
    public R<List<OpenPushSubscribeCallbackVo>> list(@Validated({AppGroup.class}) @RequestBody OpenApiModel<OpenPushSubscribeCallbackVo> model) {
        log.info("[PushSubscribeController - list]: 根据查询条件获取列表, model={}", JSONConvertUtils.objectToString(model));
        return pushSubscribeService.getList(model.getData());
    }

    /**
     * 新增订阅信息
     *
     * @param model 订阅信息对象
     * @return R 返回新增后的订阅信息对象
     */
    @AutoInject(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID, OpenFieldEnum.PROJECT_ID})
    @SkipCheck(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID})
    @ApiOperation(value = "新增订阅信息", notes = "新增订阅信息")
    @SysLog("新增订阅信息")
    @PostMapping
    public R<OpenPushSubscribeCallbackVo> save(@Validated({AppGroup.class, InsertOnlyGroup.class}) @RequestBody OpenApiModel<OpenPushSubscribeCallbackVo> model) {
        log.info("[PushSubscribeController - save]: 新增订阅, model={}", JSONConvertUtils.objectToString(model));
        return pushSubscribeService.save(model.getData());
    }

    /**
     * 批量新增订阅信息
     *
     * @param model 订阅信息对象列表
     * @return R 返回新增后的订阅信息对象列表
     */
    @AutoInject(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID, OpenFieldEnum.PROJECT_ID})
    @SkipCheck(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID})
    @ApiOperation(value = "批量新增订阅信息", notes = "批量新增订阅信息")
    @SysLog("批量新增订阅信息")
    @PostMapping("/batch")
    public R<List<OpenPushSubscribeCallbackVo>> saveBatch(@Validated({AppGroup.class, InsertOnlyGroup.class}) @RequestBody OpenApiModel<List<OpenPushSubscribeCallbackVo>> model) {
        log.info("[PushSubscribeController - saveBatch]: 批量新增订阅信息, model={}", JSONConvertUtils.objectToString(model));
        return pushSubscribeService.saveBatch(model.getData());
    }


    // 修改接口暂不开放，想要修改订阅信息需要要删除之后再新增才行
//    /**
//     * 修改订阅信息
//     *
//     * @param model 订阅信息对象
//     * @return R 返回修改后的订阅信息对象
//     */
//    @AutoInject(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID, OpenFieldEnum.PROJECT_ID})
//    @SkipCheck(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID})
//    @ApiOperation(value = "修改订阅信息", notes = "修改订阅信息")
//    @SysLog("修改订阅信息")
//    @PutMapping
//    public R<OpenPushSubscribeCallbackVo> update(@Validated({AppGroup.class, UpdateOnlyGroup.class}) @RequestBody OpenApiModel<OpenPushSubscribeCallbackVo> model) {
//        log.info("[PushSubscribeController - update]: 通过id修改订阅信息, model={}", JSONConvertUtils.objectToString(model));
//        return pushSubscribeService.update(model.getData());
//    }

    /**
     * 通过id删除订阅
     *
     * @param header 请求头信息
     * @param id     id
     * @return R 返回删除结果
     */
    @SkipCheck(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID})
    @ApiOperation(value = "通过id删除订阅", notes = "通过id删除订阅")
    @SysLog("通过id删除订阅")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated({AppGroup.class}) OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[PushSubscribeController - delete]: 通过id删除订阅, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return pushSubscribeService.delete(id);
    }

    /**
     * 通过应用ID删除订阅
     *
     * @param header 请求头信息
     * @return R 返回删除结果
     */
    @SkipCheck(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID})
    @ApiOperation(value = "通过应用ID删除订阅", notes = "通过应用ID删除订阅")
    @SysLog("通过应用ID删除订阅")
    @DeleteMapping("/deleteByAppId")
    public R<Boolean> deleteByAppId(@Validated({AppGroup.class}) OpenApiHeader header) {
        log.info("[PushSubscribeController - deleteByAppId]: 通过应用ID删除订阅, header={}", JSONConvertUtils.objectToString(header));
        return pushSubscribeService.delete(header.getAppId());
    }

    /**
     * 通过应用id、回调类型、项目UUID删除订阅
     *
     * @param header       请求头信息
     * @param callbackType 回调类型
     * @param projectUUID  项目UUID
     * @return R 返回删除结果
     */
    @SkipCheck(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID})
    @ApiOperation(value = "通过应用id、回调类型、项目UUID删除订阅", notes = "通过应用id、回调类型、项目UUID删除订阅")
    @SysLog("通过应用id、回调类型、项目UUID删除订阅")
    @DeleteMapping("/delete/{callbackType}/{projectUUID_}")
    public R<Boolean> delete(@Validated({AppGroup.class}) OpenApiHeader header, @PathVariable("callbackType") String callbackType, @PathVariable("projectUUID_") String projectUUID) {
        log.info("[PushSubscribeController - delete]: 通过应用id、回调类型、项目UUID删除订阅, header={}, callbackType={}, projectUUID={}", header, callbackType, projectUUID);
        return pushSubscribeService.delete(header.getAppId(), callbackType, projectUUID);
    }

    /**
     * 清空订阅缓存
     *
     * @param header 请求头信息
     * @return R 返回清空缓存的结果
     */
    @SkipCheck(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID})
    @ApiOperation(value = "清空订阅缓存", notes = "清空订阅缓存")
    @SysLog("清空订阅缓存")
    @GetMapping("/clearCache")
    public R<Boolean> clearCache(@Validated({AppGroup.class}) OpenApiHeader header) {
        log.info("[PushSubscribeController - clearCache]: 清空订阅缓存, header={}", JSONConvertUtils.objectToString(header));
        return pushSubscribeService.clearCache();
    }
}
