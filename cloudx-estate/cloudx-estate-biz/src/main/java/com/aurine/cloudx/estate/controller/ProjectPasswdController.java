package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectPasswd;
import com.aurine.cloudx.estate.service.ProjectPasswdService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 项目密码库
 *
 * @author 王良俊
 * @date 2020-06-04 18:16:17
 */
@RestController
@RequestMapping("/projectPasswd")
@Api(value = "projectPasswd", tags = "项目密码库管理")
public class ProjectPasswdController {
    @Resource
    private ProjectPasswdService projectPasswdService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;


    /**
     * 分页查询
     *
     * @param page          分页对象
     * @param projectPasswd 项目密码库
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R fetchList(Page page, ProjectPasswd projectPasswd) {
        return R.ok(projectPasswdService.page(page, Wrappers.query(projectPasswd)));
    }

    /**
     * 根据人员id查询所有密码
     *
     * @param personId 人员id
     * @return
     */
    @ApiOperation(value = "根据人员id查询所有密码", notes = "根据人员id查询所有密码")
    @GetMapping("/list/{personId}")
    public R listProjectPasswdByPersonId(@PathVariable("personId") String personId) {
        return R.ok(projectPasswdService.list(new QueryWrapper<ProjectPasswd>().lambda().eq(ProjectPasswd::getPersonId, personId)));
    }

    /**
     * 通过id查询项目密码库
     *
     * @param passId 密码id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{passId}")
    public R getById(@PathVariable("passId") String passId) {
        return R.ok(projectPasswdService.getById(passId));
    }

    /**
     * 新增项目密码库
     *
     * @param projectPasswd 项目密码库
     * @return R
     */
    @ApiOperation(value = "保存或更新密码", notes = "保存或更新密码")
    @SysLog("保存或更新密码")
    @PostMapping
    public R save(@RequestBody ProjectPasswd projectPasswd) {
        return R.ok(projectPasswdService.saveOrUpdatePassword(projectPasswd));
    }

    /**
     * 通过id删除项目密码库
     *
     * @param passId 密码id
     * @return R
     */
    @ApiOperation(value = "通过id删除项目密码库", notes = "通过id删除项目密码库")
    @SysLog("通过id删除项目密码库")
    @DeleteMapping("/{passId}")
    public R removeById(@PathVariable String passId) {
        //删除密码前删除密码与设备的权限
        projectRightDeviceService.removeCertmedia(passId);
        return R.ok(projectPasswdService.removeById(passId));
    }

}
