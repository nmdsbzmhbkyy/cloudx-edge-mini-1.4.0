package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.vo.ProjectFrameInfoTreeVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 * 项目区域层级
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/16 17:32
 */
@FeignClient(contextId = "remoteEntityLevelCfgService", value = "cloudx-estate-biz")
public interface RemoteEntityLevelCfgService {

    @GetMapping("/baseBuildingFrameCfg/checkIsEnable")
    R checkIsEnable();

}