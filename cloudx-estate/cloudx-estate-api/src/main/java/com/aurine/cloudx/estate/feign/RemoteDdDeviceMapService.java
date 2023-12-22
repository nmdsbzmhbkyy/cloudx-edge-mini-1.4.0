package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(contextId = "remoteDdDeviceMapService", value = "cloudx-estate-biz")
public interface RemoteDdDeviceMapService {

    /**
     * 获取咚咚设备列表
     *
     * @return
     */
    @GetMapping("/projectDeviceInfo/dd/list")
    R<List<ProjectDeviceInfo>> getDdDeviceList();
}
