package com.aurine.cloudx.estate.open.project.fegin;

import com.aurine.cloudx.estate.entity.SysDecided;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @version 1.0
 * @author： 林功鑫
 * @date： 2021-08-26 13:41
 */
@FeignClient(contextId = "remoteSysDecideService", value = "cloudx-estate-biz")
public interface RemoteSysDecideService {
    /**
     * 分页查询
     * @param pageMap
     * @return
     */
    @GetMapping("/sysDecided/page")
    R getSysDecidedPage(@SpringQueryMap Map<String, Object> pageMap);

    /**
     * 订阅
     * @param sysDecided
     * @return
     */
    @PostMapping("/sysDecided/subscription")
    R subscription(@RequestBody SysDecided sysDecided);

    /**
     * 通过id删除订阅
     * @param id
     * @return
     */
    @DeleteMapping("/sysDecided/{id}")
    R removeById(@PathVariable("id") Integer id);
}
