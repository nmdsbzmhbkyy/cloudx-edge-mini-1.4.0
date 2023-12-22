

package com.aurine.cloudx.estate.controller;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.entity.ProjectAlarmHandle;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.util.WebSocketNotifyUtil;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;


@RestController
@RequestMapping("/projectwebsocket")
@Api(value = "projectwebsocket", tags = "工单条数查询")
public class ProjectWebSocketController {


    @Resource
    private ProjectWebSocketService projectWebSocketService;

    @GetMapping("/findNumByProjectId")
    public R findNumByProjectId() {
     return   R.ok(projectWebSocketService.findNumByProjectId());
    }

    @GetMapping("/transferSocket/{projectId}")
    public R transferSocket(@PathVariable("projectId") String projectId) {
        WebSocketNotifyUtil.sendMessgae(projectId, JSONObject.toJSONString(projectWebSocketService.findNumByProjectId()));

         return  R.ok();
    }
}
