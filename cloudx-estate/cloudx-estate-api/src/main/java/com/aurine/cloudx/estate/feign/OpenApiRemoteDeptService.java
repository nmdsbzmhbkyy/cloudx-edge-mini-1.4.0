package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.constant.ServiceNameConstant;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: wrm
 * @Date: 2022/06/06 15:11
 * @Package: com.aurine.openv2.api.feign
 * @Version: 1.0
 * @Remarks:
 **/
@Api(hidden = true)
@RequestMapping("/dept")
@FeignClient(contextId = "openApiRemoteDeptService", value = ServiceNameConstant.UPMS_SERVICE)
public interface OpenApiRemoteDeptService {
    /**
     * 添加并返回部门主键
     *
     * @param sysDept 实体
     * @return 主键
     */
    @PostMapping("/inner/saveRetId")
    R<Integer> innerSaveRetId(@RequestBody SysDept sysDept, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 编辑
     *
     * @param sysDept 实体
     * @return success/false
     */
    @PutMapping("/inner")
    public R<Boolean> innerUpdate(@RequestBody SysDept sysDept, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/inner/{id}")
    public R<Boolean> innerRemoveById(@PathVariable(value = "id") Integer id, @RequestHeader(SecurityConstants.FROM) String from);
}
