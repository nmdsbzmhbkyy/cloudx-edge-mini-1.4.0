package com.aurine.cloudx.open.origin.feign;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(contextId = "openRemoteDdDeviceMapService", value = "cloudx-estate-biz")
public interface RemoteDdDeviceMapService {

    /**
     * 获取咚咚设备列表
     *
     * @return
     */
    @GetMapping("/projectDeviceInfo/dd/list")
    R<List<ProjectDeviceInfo>> getDdDeviceList();
}
