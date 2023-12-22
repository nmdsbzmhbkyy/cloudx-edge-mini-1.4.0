package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.meta.service.MetaHousePersonChangeHisService;
import com.aurine.cloudx.open.origin.entity.ProjectHousePersonChangeHis;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * open平台-房屋人员变更日志
 *
 * @author zouyu
 */

@RestController
@RequestMapping("/v1/meta/house-person-change-his")
@Api(value = "metaHousePersonChangeHis", tags = {"v1", "房屋人员变更日志"}, hidden = true)
@Inner
@Slf4j
public class MetaHousePersonChangeHisController {

    @Resource
    private MetaHousePersonChangeHisService metaHousePersonChangeHisService;


    /**
     * 新增房屋人员变更日志
     *
     * @param model 房屋人员变更日志
     * @return R 返回新增后的房屋人员变更日志
     */
    @AutoInject
    @ApiOperation(value = "新增房屋人员变更日志", notes = "新增房屋人员变更日志", hidden = true)
    @SysLog("新增房屋人员变更日志")
    @PostMapping
    public R<ProjectHousePersonChangeHis> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectHousePersonChangeHis> model) {
        log.info("[MetaHousePersonChangeHisController - save]: 新增房屋人员变更日志, model={}", JSONConvertUtils.objectToString(model));
        return metaHousePersonChangeHisService.save(model.getData());
    }
}
