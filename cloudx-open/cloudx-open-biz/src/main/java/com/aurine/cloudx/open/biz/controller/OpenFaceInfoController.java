package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenFaceInfoService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.FaceInfoVo;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
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

/**
 * 人脸信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/face-info")
@Api(value = "openFaceInfo", tags = {"v1", "人脸信息管理"})
@Inner
@Slf4j
public class OpenFaceInfoController {

    @Resource
    private OpenFaceInfoService openFaceInfoService;


    /**
     * 通过id查询人脸信息
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回人脸信息
     */
    @ApiOperation(value = "通过id查询人脸信息", notes = "通过id查询人脸信息")
    @SysLog("通过id查询人脸信息")
    @GetMapping("/{id}")
    public R<FaceInfoVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenFaceInfoController - getById]: 通过id查询人脸信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openFaceInfoService.getById(id);
    }

    /**
     * 分页查询人脸信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回人脸信息分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询人脸信息", notes = "分页查询人脸信息")
    @SysLog("分页查询人脸信息")
    @PostMapping("/page")
    public R<Page<FaceInfoVo>> page(@Validated @RequestBody OpenApiPageModel<FaceInfoVo> pageModel) {
        log.info("[OpenFaceInfoController - page]: 分页查询人脸信息, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openFaceInfoService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增人脸信息
     *
     * @param model 人脸信息
     * @return R 返回新增后的人脸信息
     */
    @AutoInject
    @ApiOperation(value = "新增人脸信息", notes = "新增人脸信息")
    @SysLog("新增人脸信息")
    @PostMapping
    public R<FaceInfoVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<FaceInfoVo> model) {
        log.info("[OpenFaceInfoController - save]: 新增人脸信息, model={}", JSONConvertUtils.objectToString(model));
        return openFaceInfoService.save(model.getData());
    }

    /**
     * 修改人脸信息
     *
     * @param model 人脸信息
     * @return R 返回修改后的人脸信息
     */
    @AutoInject
    @ApiOperation(value = "修改人脸信息", notes = "修改人脸信息")
    @SysLog("通过id修改人脸信息")
    @PutMapping
    public R<FaceInfoVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<FaceInfoVo> model) {
        log.info("[OpenFaceInfoController - update]: 修改人脸信息, model={}", JSONConvertUtils.objectToString(model));
        return openFaceInfoService.update(model.getData());
    }

    /**
     * 通过id删除人脸信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除人脸信息", notes = "通过id删除人脸信息")
    @SysLog("通过id删除人脸信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenFaceInfoController - delete]: 通过id删除人脸信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openFaceInfoService.delete(id);
    }
}
