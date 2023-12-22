package com.aurine.cloudx.wjy.feign;

import com.aurine.cloudx.wjy.vo.BuildingVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 楼栋管理
 * @author ：huangjj
 * @date ：2021/4/15
 * @description：楼栋管理路由
 */
@FeignClient(contextId = "remoteBuildingService2", value = "cloudx-wjy-biz")
public interface RemoteBuildingService {
    /**
     * 功能描述: 添加楼栋单元信息
     *
     * @author huangjj
     * @date 2021/4/15
     * @param buildingVo 楼栋数据对象
     * @return 返回添加结果
     */
    @PostMapping("/building/add")
    public R addBuilding(@RequestBody BuildingVo buildingVo);
}
