package com.aurine.cloudx.open.origin.feign;

import com.aurine.cloudx.open.origin.vo.ProjectStaffVo;
import com.aurine.cloudx.open.origin.vo.AppFaceHeadPortraitVo;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "openRemoteStaffService", value = "cloudx-estate-biz")
public interface RemoteStaffService {
    @GetMapping("/projectStaff/get-owner")
    R<ProjectStaffVo> getOwner();
    @GetMapping("/projectStaff/inner/list/{projectId}")
    R innerListByProjectId(@PathVariable("projectId") Integer projectId, @RequestHeader(SecurityConstants.FROM) String from);

    @PostMapping("/projectStaff/saveEmployeeAvatar")
    R<AppFaceHeadPortraitVo> saveEmployeeAvatar(@RequestBody AppFaceHeadPortraitVo appFaceHeadPortraitVo);




    @PutMapping("/projectStaff/updatePhoneById/{phone}")
     R<Boolean> updatePhoneById(@PathVariable("phone") String phone);

}
