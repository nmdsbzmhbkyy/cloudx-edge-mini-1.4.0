

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectEntranceEvent;
import com.aurine.cloudx.estate.service.ProjectEntranceEventService;
import com.aurine.cloudx.estate.service.ProjectLiftEventService;
import com.aurine.cloudx.estate.vo.ProjectEventSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectEventVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 乘梯事件记录
 *
 * @author pigx code generator
 * @date 2020-05-20 13:27:59
 */
@RestController
@AllArgsConstructor
@RequestMapping("/liftEvent" )
@Api(value = "liftEvent", tags = "乘梯事件记录管理")
public class ProjectLiftEventController {

    private final ProjectLiftEventService projectLiftEventService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param projectEventSearchCondition 通行事件记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getProjectEventPage(Page page,@ModelAttribute ProjectEventSearchCondition projectEventSearchCondition) {
        projectEventSearchCondition.setProjectId(ProjectContextHolder.getProjectId());
        projectEventSearchCondition.setTenantId(TenantContextHolder.getTenantId());
        return R.ok(projectLiftEventService.page(page, projectEventSearchCondition));
    }



}
