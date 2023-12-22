

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectLiftPlan;
import com.aurine.cloudx.estate.service.ProjectLiftPlanService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param.EdgeSysParamObj;
import com.aurine.cloudx.estate.vo.ProjectLiftPlanVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * <p>电梯通行方案视图控制器</p>
 *
 * @ClassName: ProjectEdgeSettingController
 * @author: 陈喆 <chenz@aurine.cn>
 * @date: 2022/2/22 14:51
 * @Copyright:
 */
@RestController
@RequestMapping("/serviceEdgeSetting")
@Api(value = "serviceEdgeSetting", tags = "边缘网关设置")
public class ProjectEdgeSettingController {

    /**
     * 保存
     *
     * @param edgeSysParamObj            边缘网关参数
     * @return
     */
    @ApiOperation(value = "保存边缘网关配置", notes = "保存边缘网关配置")
    @PostMapping
    public R updateEdgeParams( EdgeSysParamObj edgeSysParamObj) {

        return R.ok();
    }

    /**
     * 查询
     *
     * @return
     */
    @ApiOperation(value = "初始化通行方案", notes = "初始化通行方案")
    @GetMapping
    public R queryEdgeParams() {

        return R.ok();
    }



}
