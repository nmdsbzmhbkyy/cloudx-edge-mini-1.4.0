

package com.aurine.cloudx.estate.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.service.ProjectSnapRecordService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.EventSubTypeEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.EventTypeEnum;
import com.aurine.cloudx.estate.vo.ProjectSnapRecordVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 抓拍记录(结构化事件记录)
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectSnapRecord")
@Api(value = "projectSnapRecord", tags = "抓拍记录(结构化事件记录)")
public class ProjectSnapRecordController {

    private final ProjectSnapRecordService projectSnapRecordService;

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param vo   查询对象
     * @return
     */
    @ApiOperation(value = "分页查询警情记录", notes = "分页查询警情记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/page")
    public R getProjectEntranceAlarmEventPage(Page page, ProjectSnapRecordVo vo) {
        vo.setProjectId(ProjectContextHolder.getProjectId());
        vo.setTenantId(TenantContextHolder.getTenantId());
        return R.ok(projectSnapRecordService.page(page, vo));
    }

    /**
     * 获取监控设备的事件类型和事件子类型
     */
    @GetMapping("/eventTypeList")
    public JSONObject eventTypeList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("eventTypeList", new JSONArray());
        jsonObject.put("eventSubTypeList", new JSONArray());

        for (EventTypeEnum value : EventTypeEnum.values()) {
            if (value.deviceTypeEnum != null && DeviceTypeConstants.MONITOR_DEVICE.equals(value.deviceTypeEnum.getCode())) {
                JSONObject object = new JSONObject();
                object.put("eventTypeId", value.eventTypeId);
                object.put("eventTypeName", value.eventTypeName);
                jsonObject.getJSONArray("eventTypeList").add(object);
            }
        }

        for (EventSubTypeEnum value : EventSubTypeEnum.values()) {
            if (EventSubTypeEnum.OTHER.equals(value)) {
                continue;
            }
            JSONObject object = new JSONObject();
            object.put("eventSubType", value.typeString);
            object.put("eventSubTypeName", value.typeName);
            object.put("eventTypeId", value.eventTypeEnum.eventTypeId);
            jsonObject.getJSONArray("eventSubTypeList").add(object);
        }

        return jsonObject;
    }

}
