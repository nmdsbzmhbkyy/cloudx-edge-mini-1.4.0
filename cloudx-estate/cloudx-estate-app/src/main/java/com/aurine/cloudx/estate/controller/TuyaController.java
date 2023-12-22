package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.estate.service.TuyaService;
import com.aurine.cloudx.estate.vo.AppTuyaUserVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.tuya.api.client.user.Models.SyncUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@RestController
@RequestMapping("tuya")
@Api(value = "/tuya", tags = "涂鸦接口")
public class TuyaController {
    @Resource
    private TuyaService tuyaServiceImpl;

    @PostMapping("/syncUser")
    @ApiOperation(value = "同步涂鸦用户", notes = "如果涂鸦用户存在则返回uid，不存在则注册涂鸦账号并返回uid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    public R<String> register (@RequestBody AppTuyaUserVo appTuyaUserVo) {
        SyncUserVO syncUserVO = new SyncUserVO();
        BeanUtil.copyProperties(appTuyaUserVo, syncUserVO);
        syncUserVO.setNickName(appTuyaUserVo.getUsername());
        String uid = tuyaServiceImpl.syncUser(syncUserVO, appTuyaUserVo.getSCHEMA());

        if (uid == null) {
            return R.failed("涂鸦接口调用异常");
        } else {
            return R.ok(uid);
        }
    }
}
