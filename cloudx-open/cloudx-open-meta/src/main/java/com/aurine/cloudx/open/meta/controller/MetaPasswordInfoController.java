package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaPasswordInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectPasswd;
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
 * 密码信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/password-info")
@Api(value = "metaPasswordInfo", tags = {"v1", "密码信息管理"}, hidden = true)
@Inner
@Slf4j
public class MetaPasswordInfoController {

    @Resource
    private MetaPasswordInfoService metaPasswordInfoService;


    /**
     * 新增密码信息
     *
     * @param model 密码信息
     * @return R 返回新增后的密码信息
     */
    @AutoInject
    @ApiOperation(value = "新增密码信息", notes = "新增密码信息", hidden = true)
    @SysLog("新增密码信息")
    @PostMapping
    public R<ProjectPasswd> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectPasswd> model) {
        log.info("[MetaPasswordInfoController - save]: 新增密码信息, model={}", JSONConvertUtils.objectToString(model));
        return metaPasswordInfoService.save(model.getData());
    }

    /**
     * 修改密码信息
     *
     * @param model 密码信息
     * @return R 返回修改后的密码信息
     */
    @AutoInject
    @ApiOperation(value = "修改密码信息", notes = "修改密码信息", hidden = true)
    @SysLog("通过id修改密码信息")
    @PutMapping
    public R<ProjectPasswd> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectPasswd> model) {
        log.info("[MetaPasswordInfoController - update]: 修改密码信息, model={}", JSONConvertUtils.objectToString(model));
        return metaPasswordInfoService.update(model.getData());
    }

    /**
     * 通过id删除密码信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除密码信息", notes = "通过id删除密码信息", hidden = true)
    @SysLog("通过id删除密码信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaPasswordInfoController - delete]: 通过id删除密码信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaPasswordInfoService.delete(id);
    }
}
