package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenProjectInfoService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.annotation.SkipCheck;
import com.aurine.cloudx.open.common.core.constant.enums.OpenFieldEnum;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.ProjectInfoVo;
import com.aurine.cloudx.open.common.validate.group.AppGroup;
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
 * 项目信息
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/project-info")
@Api(value = "openProjectInfo", tags = {"v1", "项目信息"})
@Inner
@Slf4j
public class OpenProjectInfoController {

    @Resource
    private OpenProjectInfoService openProjectInfoService;


    /**
     * 通过id查询项目
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回项目信息
     */
    @ApiOperation(value = "通过id查询项目", notes = "通过id查询项目")
    @SysLog("通过id查询项目")
    @GetMapping("/{id}")
    public R<ProjectInfoVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenProjectInfoController - getById]: 通过id查询项目, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openProjectInfoService.getById(id);
    }

    /**
     * 分页查询项目
     *
     * @param pageModel 分页查询条件
     * @return R 返回项目分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询项目", notes = "分页查询项目")
    @SysLog("分页查询项目")
    @PostMapping("/page")
    public R<Page<ProjectInfoVo>> page(@Validated @RequestBody OpenApiPageModel<ProjectInfoVo> pageModel) {
        log.info("[OpenProjectInfoController - page]: 分页查询项目, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openProjectInfoService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 平台侧获取正在入云的项目
     * （平台侧调用）
     * 注：应用层级接口不需要校验projectUUID、tenantId，所以在注解处排除，且校验分组为AppGroup
     *
     * @param header 请求头信息
     * @return R 返回正在入云的项目列表
     */
    @SkipCheck(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID})
    @ApiOperation(value = "平台侧获取正在入云的项目", notes = "平台侧获取正在入云的项目（平台侧调用）")
    @SysLog("平台侧获取正在入云的项目")
    @GetMapping("/list-cascade-by-cloud")
    public R<List<ProjectInfoVo>> listCascadeByCloud(@Validated({AppGroup.class}) OpenApiHeader header) {
        log.info("[OpenProjectInfoController - listCascadeByCloud]: 平台侧获取正在入云的项目, header={}", JSONConvertUtils.objectToString(header));
        return openProjectInfoService.listCascadeByCloud();
    }

    /**
     * 边缘侧获取正在入云的项目
     * （边缘侧调用）
     * 注：应用层级接口不需要校验projectUUID、tenantId，所以在注解处排除，且校验分组为AppGroup
     *
     * @param header 请求头信息
     * @return R 返回正在入云的项目列表
     */
    @SkipCheck(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID})
    @ApiOperation(value = "边缘侧获取正在入云的项目", notes = "边缘侧获取正在入云的项目（边缘侧调用）")
    @SysLog("边缘侧获取正在入云的项目")
    @GetMapping("/list-cascade-by-edge")
    public R<List<ProjectInfoVo>> listCascadeByEdge(@Validated({AppGroup.class}) OpenApiHeader header) {
        log.info("[OpenProjectInfoController - listCascadeByEdge]: 边缘侧获取正在入云的项目, header={}", JSONConvertUtils.objectToString(header));
        return openProjectInfoService.listCascadeByEdge();
    }

    /**
     * 主边缘侧获取正在级联的项目
     * （主边缘侧调用）
     * 注：应用层级接口不需要校验projectUUID、tenantId，所以在注解处排除，且校验分组为AppGroup
     *
     * @param header 请求头信息
     * @return R 返回正在级联的项目列表
     */
    @SkipCheck(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID})
    @ApiOperation(value = "主边缘侧获取正在级联的项目", notes = "主边缘侧获取正在级联的项目（主边缘侧调用）")
    @SysLog("主边缘侧获取正在级联的项目")
    @GetMapping("/list-cascade-by-master")
    public R<List<ProjectInfoVo>> listCascadeByMaster(@Validated({AppGroup.class}) OpenApiHeader header) {
        log.info("[OpenProjectInfoController - listCascadeByMaster]: 主边缘侧获取正在级联的项目, header={}", JSONConvertUtils.objectToString(header));
        return openProjectInfoService.listCascadeByMaster();
    }

    /**
     * 从边缘侧获取正在级联的项目
     * （从边缘侧调用）
     * 注：应用层级接口不需要校验projectUUID、tenantId，所以在注解处排除，且校验分组为AppGroup
     *
     * @param header 请求头信息
     * @return R 返回正在级联的项目列表
     */
    @SkipCheck(exclude = {OpenFieldEnum.PROJECT_UUID, OpenFieldEnum.TENANT_ID})
    @ApiOperation(value = "从边缘侧获取正在级联的项目", notes = "从边缘侧获取正在级联的项目（从边缘侧调用）")
    @SysLog("从边缘侧获取正在级联的项目")
    @GetMapping("/list-cascade-by-slave")
    public R<List<ProjectInfoVo>> listCascadeBySlave(@Validated({AppGroup.class}) OpenApiHeader header) {
        log.info("[OpenProjectInfoController - listCascadeBySlave]: 从边缘侧获取正在级联的项目, header={}", JSONConvertUtils.objectToString(header));
        return openProjectInfoService.listCascadeBySlave();
    }
}
