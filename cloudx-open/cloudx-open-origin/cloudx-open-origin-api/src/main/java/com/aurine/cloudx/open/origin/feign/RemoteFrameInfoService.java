package com.aurine.cloudx.open.origin.feign;

import com.aurine.cloudx.open.origin.vo.ProjectFrameInfoTreeVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/5/13
 * @description： 楼栋管理接口
 */
@FeignClient(contextId = "openRemoteFrameInfoService", value = "cloudx-estate-biz")
public interface RemoteFrameInfoService {

    /**
     * 获取子系统树，带根节点
     *
     * @return
     */
    @GetMapping("/baseBuildingFrame/findTreeWithRoot")
    R<List<ProjectFrameInfoTreeVo>> findTreeWithRoot();
}