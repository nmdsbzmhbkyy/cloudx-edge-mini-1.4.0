package com.aurine.cloudx.estate.controller;


import com.aurine.cloudx.estate.service.ProjectMediaAdDevCfgService;
import com.aurine.cloudx.estate.vo.ProjectMediaAdDevCfgVo;
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
 * Title: ProjectMediaAdDevCfgController
 * Description: 媒体广告设备发布
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/6/5 9:55
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectMediaAdDevCfg")
@Api(value = "projectMediaAdDevCfg", tags = "媒体广告设备发布")
public class ProjectMediaAdDevCfgController {
    private final ProjectMediaAdDevCfgService projectMediaAdDevCfgService;

    /**
     * 分页查询
     *
     * @param page
     *         分页对象
     * @param projectMediaAdDevCfgVo
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
    public R<Page<ProjectMediaAdDevCfgVo>> getProjectNoticeDevicePage(Page page, ProjectMediaAdDevCfgVo projectMediaAdDevCfgVo) {
        return R.ok(projectMediaAdDevCfgService.pageMediaAdDevCfg(page, projectMediaAdDevCfgVo));
    }
}
