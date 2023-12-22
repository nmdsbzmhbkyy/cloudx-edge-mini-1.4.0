

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.SysEventExpertPlanConf;
import com.aurine.cloudx.estate.feign.RemoteEntranceAlarmEventService;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 报警事件
 *
 * @author lgx
 * @date 2021-8-11 08:30:07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectEntranceAlarmEvent")
@Api(value = "projectEntranceAlarmEvent", tags = "警情记录")
public class ProjectEntranceAlarmEventController {

    private final RemoteEntranceAlarmEventService remoteEntranceAlarmEventService;

    /**
     * 分页查询警情记录
     *
     * @param page
     * @param handleOperator
     * @param status
     * @return
     */
    @ApiOperation(value = "分页查询警情记录", notes = "分页查询警情记录 返回值 status  0 未确认；1 处理中；2 完成；  输入只要 传 1 就代表未处理的（未确认和处理中） 2 代表已处理" +
            "handleOperator 用户id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "handleOperator", value = "用户id", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/page")
    public R getProjectEntranceAlarmEventPage(Page page,
                                              @RequestParam(value = "handleOperator", required = false) String handleOperator,
                                              @RequestParam(value = "status", required = false) String status) {
        Map<String, Object> map = new HashMap<>(5);
        map.put("size", page.getSize());
        map.put("current", page.getCurrent());
        map.put("handleOperator", handleOperator);
        map.put("status", status);
        return remoteEntranceAlarmEventService.getProjectEntranceAlarmEventPage(map);
    }


    /**
     * 通过id查询报警事件
     *
     * @param eventId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{eventId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "eventId", value = "通行事件id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getById(@PathVariable("eventId") String eventId) {
        return remoteEntranceAlarmEventService.getById(eventId);
    }


    /**
     * 修改处理状态为处理中并储存开始处理时间
     *
     * @param vo
     * @return
     */
    @ApiOperation(value = "修改处理状态为处理中并储存开始处理时间,确认类的状态修改为已处理"
            , notes = "修改处理状态为处理中并储存开始处理时间,确认类的状态修改为已处理")
    @PutMapping("/confirm")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R confirmByStatus(@RequestBody ProjectEntranceAlarmEventVo vo) {
        return remoteEntranceAlarmEventService.setStatus(vo);
    }

    /**
     * 修改处理状态为已处理并储存事件信息
     *
     * @param vo
     * @return
     */
    @ApiOperation(value = "修改处理状态为已处理并储存事件信息", notes = "修改处理状态为已处理并储存事件信息   result 处理结果； livePic  照片  多张图片逗号（,）隔开" +
            "类似   /admin/sys-file/saas-res-project/7e450f63f37e47b489ea3787c3e51af7.jpg,/admin/sys-file/saas-res-project/7e450f63f37e47b489ea3787c3e51af7.jpg")
    @PutMapping("/modify")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R modifyByStatus(@RequestBody ProjectEntranceAlarmEventVo vo) {
        if (vo.getResult() == null || "".equals(vo.getResult())) {
            throw new RuntimeException("请输入处理情况信息");
        }
        return remoteEntranceAlarmEventService.putStatus(vo);
    }


    @ApiOperation(value = "通过事件类型id(eventTypeId)获取关联的预案", notes = "通过事件类型id(eventTypeId)获取关联的预案")
    @GetMapping("/planInfoList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "eventTypeId", value = "事件类型id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<SysEventExpertPlanConf>> getExpertPlanListByEventTypeId(String eventTypeId) {
        return remoteEntranceAlarmEventService.getPlanInfoList(eventTypeId);
    }

    /**
     * <p>
     * 分页检索列表数据
     * </p>
     *
     * @param
     * @param page
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @ApiImplicitParam(name = "picName", value = "平面图名称", paramType = "query"),
     */
    @GetMapping("findList")
    @ApiOperation(value = "获取平面图列表", notes = "获取平面图列表  regionId 对应 deviceRegionId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "regionId", value = "设备区域id", paramType = "query",required = true),
            @ApiImplicitParam(name = "picName", value = "平面图名称", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R findList(Page page,
                      @RequestParam(value = "picName", required = false) String picName,
                      @RequestParam(value = "regionId") String regionId) {
        Map<String, Object> map = new HashMap<>(5);
        map.put("size", page.getSize());
        map.put("current", page.getCurrent());
        map.put("picName", picName);
        map.put("regionId", regionId);
        return remoteEntranceAlarmEventService.findPage(map);
    }

    @GetMapping("getMonitoring")
    @ApiOperation(value = "查询关联的设备", notes = "查询关联的设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "设备id", paramType = "query", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getMonitoring(String deviceId) {
        return remoteEntranceAlarmEventService.getMonitoring(deviceId);
    }

    /**
     * 获取视频直播流的地址
     *
     * @param deviceId
     * @return
     */
    @ApiOperation(value = "获取视频直播流的地址", notes = "获取视频直播流的地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "设备id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/getLiveUrl/{deviceId}")
    public R<String> getLiveUrl(@PathVariable("deviceId") String deviceId) {
        R<String> liveUrl = remoteEntranceAlarmEventService.getLiveUrl(deviceId);
        return liveUrl;
    }

}
