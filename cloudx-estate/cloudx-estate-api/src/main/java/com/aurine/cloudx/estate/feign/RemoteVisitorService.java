package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.vo.ProjectVisitorVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * (RemoteVisitorService)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/12/9 11:17
 */
@FeignClient(contextId = "remoteVisitorService", value = "cloudx-estate-biz")
public interface RemoteVisitorService {
    @PostMapping("/serviceVisitor/registerVo")
    R registerVo(@RequestBody ProjectVisitorVo projectVisitorVo);

    @PutMapping("/serviceVisitor/rejectAudit")
    R rejectAudit(@RequestBody ProjectVisitorVo visitorVo);

    @PostMapping("/serviceVisitor/passAudit")
    R passAudit(@RequestBody ProjectVisitorVo projectVisitorVo);

    @PostMapping("/serviceVisitor/homeowersPassAudit")
    R homeowersPassAudit(@RequestBody ProjectVisitorVo projectVisitorVo);

    @GetMapping("/serviceVisitor/getData/{visitId}")
    R<ProjectVisitorVo> getData(@PathVariable("visitId") String visitId);

}
