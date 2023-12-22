package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.common.core.constant.ServiceNameConstants;
import com.aurine.cloudx.estate.entity.ProjectCarouselConf;
import com.aurine.cloudx.estate.vo.ProjectCarouselConfVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>设备第三方对接配置</p>
 * @ClassName: RemoteSysDevicetypeThirdpartyConfigService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-25 17:17
 * @Copyright:
 */
@FeignClient(contextId = "remoteSysDevicetypeThirdpartyConfigService", value = ServiceNameConstants.ESTATE_SERVICE)
public interface RemoteSysDevicetypeThirdpartyConfigService {


    /**
     * 根据项目编号，设备类型 获取配置信息
     *
     * @param deviceType 设备类型   1:室内终端 2:梯口终端 3:区口终端  4:中心机  5:编码设备 6: 监控设备
     * @param projectId  项目编号
     * @return
     */
    @ApiOperation(value = "根据项目编号，设备类型 获取配置信息", notes = "根据项目编号，设备类型 获取配置信息")
    @GetMapping("/devicetypeThirdpartyConfig/getConfig/{deviceType}/{projectId}")
    R<ProjectCarouselConf> getConfig(@PathVariable("deviceType") String deviceType, @PathVariable("projectId") Integer projectId);

}
