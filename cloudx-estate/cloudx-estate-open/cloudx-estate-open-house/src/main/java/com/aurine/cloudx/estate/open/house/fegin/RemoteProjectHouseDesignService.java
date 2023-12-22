package com.aurine.cloudx.estate.open.house.fegin;

import com.aurine.cloudx.estate.entity.ProjectHouseDesign;
import com.aurine.cloudx.estate.open.house.bean.HouseDesignPage;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoteProjectHouseDesignService", value = "cloudx-estate-biz")
public interface RemoteProjectHouseDesignService {

    /**
     * 分页查询
     */
    @GetMapping("/baseHousedesign/page")
    R getHouseDesignPage(@SpringQueryMap HouseDesignPage page);

    /**
     * 通过id查询户型
     */
    @GetMapping("/baseHousedesign/{id}")
    R getById(@PathVariable("id") String id);

    /**
     * 新增户型
     */
    @PostMapping("/baseHousedesign")
    R save(@RequestBody ProjectHouseDesign projectHouseDesign);

    /**
     * 修改户型
     */
    @PutMapping("/baseHousedesign")
    R updateById(@RequestBody ProjectHouseDesign projectHouseDesign);

    /**
     * 删除户型
     */
    @DeleteMapping("/baseHousedesign/{designId}")
    R removeById(@PathVariable("designId") String designId);
}
