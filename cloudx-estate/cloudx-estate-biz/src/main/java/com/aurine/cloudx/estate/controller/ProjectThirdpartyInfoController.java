package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectThirdpartyInfo;
import com.aurine.cloudx.estate.service.ProjectThirdpartyInfoService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>边缘网关项目信息controller</p>
 *
 * @author : 王良俊
 * @date : 2021-12-08 11:12:52
 */
@RestController
@RequestMapping("/projectThirdpartyInfo")
public class ProjectThirdpartyInfoController {

    @Resource
    ProjectThirdpartyInfoService projectThirdpartyInfoService;

    @GetMapping("/getInfoByRequestId/{requestId}")
    public R getInfoByRequestId(@PathVariable String requestId) {
        return R.ok(projectThirdpartyInfoService.getOne(new LambdaUpdateWrapper<ProjectThirdpartyInfo>().eq(ProjectThirdpartyInfo::getRequestId, requestId).last("limit 1")));
    }
}
