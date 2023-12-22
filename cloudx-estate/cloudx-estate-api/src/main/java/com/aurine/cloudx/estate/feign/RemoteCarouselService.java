package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.entity.ProjectCarousel;
import com.aurine.cloudx.estate.vo.ProjectCarouselVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * RemoteCarouselService
 *
 * @author DW
 * @version 1.0.0
 * @date 2021/1/18 8:42
 */
@FeignClient(contextId = "remoteCarouselService", value = "cloudx-estate-biz")
public interface RemoteCarouselService {
    
    /**
     * 分页查询
     *
     * @return
     */
    @GetMapping("/page")
    R<Page<ProjectCarouselVo>> voPage(@SpringQueryMap Map<String, Object> map);
    
    /**
     * 新增轮播图信息
     * @param projectCarousel 新增轮播图信息
     * @return R
     */
    @PostMapping
    public R save(@RequestBody ProjectCarousel projectCarousel);
    
    /**
     * 修改轮播图信息
     *
     * @param projectCarousel
     * @return
     */
    @PostMapping("/updateCarousel")
    public R update(@RequestBody ProjectCarousel projectCarousel);
    
    /**
     * 删除轮播图信息
     *
     * @param carouselId 轮播图id
     * @return
     */
    @DeleteMapping("/remove/{carouselId}")
    public R deleteCarousel(@PathVariable("carouselId") String carouselId);

}
