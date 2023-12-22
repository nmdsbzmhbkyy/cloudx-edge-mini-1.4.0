
package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.ProjectNoticeDeviceService;
import com.aurine.cloudx.estate.vo.ProjectNoticeDeviceVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 设备配置信息发布
 *
 * @author guhl@aurine.cn
 * @date 2020-05-20 11:52:18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectNoticeDevice")
@Api(value = "projectNoticeDevice", tags = "设备配置信息发布管理")
public class ProjectNoticeDeviceController {

    private final ProjectNoticeDeviceService projectNoticeDeviceService;

    /**
     * 分页查询
     *
     * @param page
     *         分页对象
     * @param ProjectNoticeDeviceVo
     *         设备配置信息发布
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectNoticeDeviceVo>> getProjectNoticeDevicePage(Page page, ProjectNoticeDeviceVo ProjectNoticeDeviceVo) {
        return R.ok(projectNoticeDeviceService.pageNoticeDevice(page, ProjectNoticeDeviceVo));
    }


}
