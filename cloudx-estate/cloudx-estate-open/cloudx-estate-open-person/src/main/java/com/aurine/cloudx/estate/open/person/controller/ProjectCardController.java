package com.aurine.cloudx.estate.open.person.controller;

import com.aurine.cloudx.estate.constant.enums.PassRightTokenStateEnum;
import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.open.person.fegin.RemoteProjectCardService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/card")
@Api(value = "projectCard", tags = "卡片管理")
public class ProjectCardController {


    @Resource
    private RemoteProjectCardService projectCardService;


    /**
     * 根据人员id查询所有卡片
     *
     * @param personId 人员id
     * @return
     */
    @ApiOperation(value = "根据人员id查询所有卡片", notes = "根据人员id查询所有卡片")
    @GetMapping("/list/{personId}")
    @PreAuthorize("@pms.hasPermission('card:get:list')")
    public R listProjectCardByPersonId(@PathVariable("personId") String personId) {
        return projectCardService.listProjectCardByPersonId(personId);
    }

    /**
     * 新增记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载
     *
     * @param projectCard 记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载
     * @return R
     */
    @ApiOperation(value = "新增记录项目卡资源信息", notes = "新增记录项目卡资源信息")
    @SysLog("新增卡片资源信息")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('card:post:save')")
    public R save(@RequestBody ProjectCard projectCard) {
      return projectCardService.save(projectCard);
    }

    /**
     * 通过卡片id删除
     *
     * @param cardId 卡片id
     * @return R
     */
    @ApiOperation(value = "通过卡片id删除", notes = "通过卡片id删除")
    @SysLog("通过seq序列删除某张通行卡片（设置为未使用)")
    @DeleteMapping("/{cardId}")
    @PreAuthorize("@pms.hasPermission('card:delete:removeById')")
    public R removeById(@PathVariable String cardId) {
        return projectCardService.removeById(cardId);
    }
}
