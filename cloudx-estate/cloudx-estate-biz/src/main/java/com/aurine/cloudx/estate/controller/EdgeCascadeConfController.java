package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.EdgeCascadeConf;
import com.aurine.cloudx.estate.service.EdgeCascadeConfService;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>边缘网关入云申请控制器</p>
 * @author : 王良俊
 * @date : 2021-12-02 17:34:59
 */
@RestController
@RequestMapping("/edgeCascadeConf")
public class EdgeCascadeConfController {

    @Resource
    EdgeCascadeConfService edgeCascadeConfService;

    @GetMapping("/{projectId}")
    public R getEdgeCascadeConf(@PathVariable Integer projectId) {
        return R.ok(edgeCascadeConfService.getConf(projectId));
    }

    @GetMapping("/switchNetwork/{projectId}/{status}")
    public R switchNetwork(@PathVariable Integer projectId, @PathVariable Character status) {
        if (status == '1') {
            return edgeCascadeConfService.enableNetwork(projectId);
        } else {
            return edgeCascadeConfService.disableNetwork(projectId);
        }
    }

    @GetMapping("/switchCascade/{projectId}/{status}")
    public R switchCascade(@PathVariable Integer projectId, @PathVariable Character status) {
        if (status == '1') {
            return edgeCascadeConfService.enableCascade(projectId);
        } else {
            return edgeCascadeConfService.disableCascade(projectId);
        }
    }

    @PostMapping("/updateCascadeConfInfo")
    public R switchCascade(@RequestBody EdgeCascadeConf conf) {
        return edgeCascadeConfService.updateCascadeConfInfo(conf);
    }


}
