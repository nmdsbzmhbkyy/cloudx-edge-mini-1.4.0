package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * (RemoteFaceRecources)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 10:26
 */
@FeignClient(contextId = "RemoteProjectPersonDeviceService", value = "cloudx-estate-biz")
public interface RemoteProjectPersonDeviceService {
    /**
     * 根据personId,重载该用户的权限，用于用户房屋归属变化等情况
     *
     * @param personId
     * @return
     */
    @GetMapping("/projectPersonDevice/refreshByPersonId")
    boolean refreshByPersonId(@RequestParam("personId")String personId, @RequestParam("personTypeEnum")PersonTypeEnum personTypeEnum);

}
