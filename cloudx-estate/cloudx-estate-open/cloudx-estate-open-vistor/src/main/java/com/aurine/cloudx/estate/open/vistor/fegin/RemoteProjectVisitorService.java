package com.aurine.cloudx.estate.open.vistor.fegin;

import com.aurine.cloudx.estate.entity.ProjectVisitorHis;
import com.aurine.cloudx.estate.open.vistor.bean.ProjectVisitorSearchConditionPage;
import com.aurine.cloudx.estate.vo.ProjectVisitorVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(contextId = "remoteProjectVisitorService", value = "cloudx-estate-biz")
public interface RemoteProjectVisitorService {
    @GetMapping("/serviceVisitor/page")
    R fetchList(@SpringQueryMap ProjectVisitorSearchConditionPage projectVisitorSearchConditionPage);

    @PostMapping("/serviceVisitor/register")
    R register(@RequestBody ProjectVisitorVo projectVisitorVo);

    @PostMapping("/serviceVisitor/registerVo")
    R registerVo(@RequestBody ProjectVisitorVo projectVisitorVo);

    @PostMapping("/serviceVisitor/delay")
    R delay(@RequestBody ProjectVisitorVo projectVisitorVo);

    @PutMapping("/serviceVisitor/rejectAudit")
    R rejectAudit(@RequestBody ProjectVisitorVo visitorVo);

    @GetMapping("/serviceVisitor/getData/{visitId}")
    R<ProjectVisitorVo> getData(@PathVariable("visitId") String visitId);

    @GetMapping("/serviceVisitor/signOff/{visitId}")
    R signOff(@PathVariable("visitId") String visitId);

    @GetMapping("/serviceVisitor/signOffAll")
    R signOffAll();

    @PostMapping("/serviceVisitor/homeowersPassAudit")
    R homeowersPassAudit(@RequestBody ProjectVisitorVo projectVisitorVo);

    @PostMapping("/serviceVisitor/passAudit")
    R passAudit(@RequestBody ProjectVisitorVo projectVisitorVo);

    @PostMapping("/serviceVisitor/passAuditBatch")
    R passAuditBatch(@RequestBody List<ProjectVisitorVo> visitorVoList);

    @GetMapping("/serviceVisitor/getByMobileNo/{mobileNo}")
    R getByMobileNo(@PathVariable("mobileNo") String mobileNo);

    @GetMapping("/serviceVisitor/isAllLeave/{mobileNo}/{visitId}")
    R isAllLeave(@PathVariable("mobileNo") String mobileNo, @PathVariable("visitId") String visitId);

    @PostMapping("/serviceVisitor/saveVisitTime")
    R updateVisitTime(@RequestBody ProjectVisitorHis visitorHisVo);
}
