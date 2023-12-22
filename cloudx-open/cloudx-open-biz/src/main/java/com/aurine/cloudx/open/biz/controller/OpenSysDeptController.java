package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenSysDeptService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.vo.SysDeptVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统部门
 *
 * @author : Qiu
 * @date : 2021 04 18 17:34
 */

@RestController
@RequestMapping("/v1/open/sys-dept")
@Api(value = "openSysDept", tags = {"v1", "系统数据相关", "系统部门"})
@Inner
@Slf4j
public class OpenSysDeptController {

    @Resource
    private OpenSysDeptService openSysDeptService;


    /**
     * 通过项目查询第一个系统部门
     *
     * @param header 请求头信息
     * @return R 返回第一个系统部门
     */
    @AutoInject
    @ApiOperation(value = "通过项目查询第一个系统部门", notes = "通过项目查询第一个系统部门")
    @SysLog("通过项目查询第一个系统部门")
    @GetMapping("/get-first-by-project")
    public R<SysDeptVo> getFirstByProject(@Validated OpenApiHeader header) {
        log.info("[OpenSysDeptController - getFirstByProject]: 通过项目查询第一个系统部门, header={}, projectId={}", JSONConvertUtils.objectToString(header), header.getProjectId());
        return openSysDeptService.getFirstByProjectId(header.getProjectId());
    }
}
