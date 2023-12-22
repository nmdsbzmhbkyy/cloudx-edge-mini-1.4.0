package com.aurine.cloudx.open.origin.feign;

import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author ： huangjj
 * @date ： 2021/5/13
 * @description： 单元管理接口
 */
@FeignClient(contextId = "openRemoteUnitInfoService", value = "cloudx-estate-biz")
public interface RemoteUnitInfoService {
    /**
     * 功能描述: 根据楼栋ID获取单元集合
     *
     * @author huangjj
     * @date 2021/5/13
     * @param  buildingId
     * @return  
    */
    @GetMapping("/baseUnitInfo/inner/list/{buildingId}" )
    public R innerListUnit(@PathVariable("buildingId") String buildingId,
                           @RequestHeader(SecurityConstants.FROM) String from);

    @GetMapping("/baseUnitInfo/inner/info/{unitId}" )
    public R innerUnitInfo(@PathVariable("unitId") String unitId, @RequestHeader(SecurityConstants.FROM) String from);

    @GetMapping("/baseUnitInfo/inner/getBuildingId/{unitId}" )
    public R innerGetBuildingId(@PathVariable("unitId") String unitId, @RequestHeader(SecurityConstants.FROM) String from);
}
