package com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.callback;


import com.alibaba.fastjson.JSONObject;


import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.service.ProjectDeviceAttrService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectSnapRecordService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.DeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.YushiCallBackObj;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 和数字政通对接用的数据接口
 */
@RestController
@AllArgsConstructor
@RequestMapping("/V1.0" )
@Api(value = "/V1.0", tags = "宇视回调地址")
public class YushiCallbackController {

  @Resource
  private ProjectSnapRecordService projectSnapRecordService;
  @Resource
  private ProjectDeviceInfoService projectDeviceInfoService;
  @Resource
  private ProjectDeviceAttrService projectDeviceAttrService;
  @Resource
  private DeviceService deviceServiceImplByYushiV1;

  @ApiOperation(value = "宇视结构化数据回调接口", notes = "宇视结构化数据回调接口")
  @PostMapping("/System/Event/Notification/Structure")
  @Inner(false)
  public R handle(@RequestBody String jsonObject) {
    YushiCallBackObj obj = JSONObject.parseObject(jsonObject, YushiCallBackObj.class);
    String ip = obj.getReference().split("/")[0].split(":")[0];
    ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getIpv4, ip));
    projectSnapRecordService.saveRecord(obj, deviceInfo);
    return R.ok();
  }


}
