package com.aurine.cloudx.estate.controller;


import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmArea;
import com.aurine.cloudx.estate.service.ProjectPerimeterAlarmAreaService;
import com.aurine.cloudx.estate.vo.ProjectDeviceLocationVo;
import com.aurine.cloudx.estate.vo.ProjectPerimeterAlarmAreaVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * 防区管理
 *
 * @author 邹宇
 * @date 2021-6-15 14:45:18
 */
@RestController
@RequestMapping("/perimeterAlarm")
@Api(value = "perimeterAlarm", tags = "周界报警防区信息表")
public class ProjectPerimeterAlarmAreaController {

    @Resource
    private ProjectPerimeterAlarmAreaService projectPerimeterAlarmAreaService;


    /**
     * 分页查询
     *
     * @param projectPerimeterAlarmAreaVo
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R findPage(Page page, ProjectPerimeterAlarmAreaVo projectPerimeterAlarmAreaVo) {
        return R.ok(projectPerimeterAlarmAreaService.findPage(page, projectPerimeterAlarmAreaVo));
    }


    /**
     * 修改状态
     *
     * @param infoUid
     * @param name
     * @return
     */
    @ApiOperation(value = "修改状态", notes = "修改状态")
    @PutMapping("/putById/{infoUid}/{name}")
    public R updateStatusById(@PathVariable String infoUid, @PathVariable String name) {
       return R.ok(projectPerimeterAlarmAreaService.updateStatusById(infoUid,name));
    }

    /**
     * 修改防区名
     * @param infoUid
     * @param name
     * @return
     */
    @ApiOperation(value = "修改防区名", notes = "修改防区名")
    @PutMapping("/putNameById/{infoUid}/{name}")
    public R putNameById(@PathVariable String infoUid, @PathVariable String name) {
        ProjectPerimeterAlarmArea perimeterAlarmAreaServiceOne = projectPerimeterAlarmAreaService.getOne(Wrappers.<ProjectPerimeterAlarmArea>query().eq("infoUid", infoUid));
        perimeterAlarmAreaServiceOne.setChannelName(name);
        Boolean bool = projectPerimeterAlarmAreaService.verifyDuplicateName(name, perimeterAlarmAreaServiceOne.getDeviceId());
        if(!bool){
            return R.failed(false);
        }
        return R.ok(projectPerimeterAlarmAreaService.updateById(perimeterAlarmAreaServiceOne));
    }

    /**
     * 重新获取防区
     * @param
     * @return
     */
    @ApiOperation(value = "重新获取防区")
    @PostMapping("/reacquireDefenseArea")
    public R reacquireDefenseArea(@RequestParam String deviceId) {
        return R.ok(this.projectPerimeterAlarmAreaService.reacquireDefenseArea(deviceId));
    }
    /**
     * 更改区域定位
     * @param projectDeviceLocationVo
     * @return
     */
    @ApiOperation(value = "更改区域定位")
    @PutMapping("/putPoint")
    public R putPoint(@RequestBody ProjectDeviceLocationVo projectDeviceLocationVo) {
        return R.ok(this.projectPerimeterAlarmAreaService.putPoint(projectDeviceLocationVo));
    }
    /**
     * 批量更改防区信息
     * @param lstProjectPerimeterAlarmArea
     * @return
     */
    @ApiOperation(value = "批量更改防区信息")
    @PutMapping("/putMultiDefenseAreaInfo/{deviceId}")
    //@RequestMapping({"lstProjectPerimeterAlarmArea"})
    public R putMultiDefenseAreaInfo(@PathVariable String deviceId,@RequestBody List<ProjectPerimeterAlarmAreaVo> lstProjectPerimeterAlarmArea) {
        return R.ok(this.projectPerimeterAlarmAreaService.putMultiDefenseAreaInfo(deviceId,lstProjectPerimeterAlarmArea));
    }

    /**
     * 更改防区信息
     * @param projectPerimeterAlarmArea
     * @return
     */
    @ApiOperation(value = "更改防区信息")
    @PutMapping("/putDefenseAreaInfo")
    //@RequestMapping({"lstProjectPerimeterAlarmArea"})
    public R putDefenseAreaInfo(@RequestBody ProjectPerimeterAlarmAreaVo projectPerimeterAlarmArea) {
        return R.ok(this.projectPerimeterAlarmAreaService.putDefenseAreaInfo(projectPerimeterAlarmArea));
    }

    /**
     * 更改防区的rtsp流地址
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "更改防区的rtsp流地址")
    @PutMapping("/updateRtspUrl")
    public R updateRtspUrl(@RequestBody Map<String,String> map) {
        String rtspUrl = map.get("rtspUrl");
        String userName = map.get("userName");
        String password = map.get("password");
        String deviceId = map.get("deviceId");
        return R.ok(this.projectPerimeterAlarmAreaService.update(Wrappers.lambdaUpdate(ProjectPerimeterAlarmArea.class)
                .set(ProjectPerimeterAlarmArea::getRtspUrl,rtspUrl)
                .set(ProjectPerimeterAlarmArea::getUserName,userName)
                .set(ProjectPerimeterAlarmArea::getPassword,password)
                .eq(ProjectPerimeterAlarmArea::getInfoUid,deviceId)));
    }

    /**
     * 查询防区的rtsp流地址
     *
     * @param deviceId
     * @return
     */
    @ApiOperation(value = "查询防区的rtsp流地址")
    @GetMapping("/getRtspUrlByDeviceId/{deviceId}")
    public R getRtspUrlByDeviceId(@PathVariable("deviceId") String deviceId) {
        ProjectPerimeterAlarmArea perimeterAlarmArea = this.projectPerimeterAlarmAreaService.getOne(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class)
                .eq(ProjectPerimeterAlarmArea::getInfoUid, deviceId));
        return R.ok(perimeterAlarmArea);
    }
}



