package com.aurine.cloudx.estate.open.szzt.fegin;

import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(contextId = "remoteSzztService", value = "cloudx-estate-biz")
public interface RemoteSzztService {
    /**
     * 通过id查询
     * @param projectId projectId
     * @return R
     */
    @ApiOperation(value = "通过项目编号查询", notes = "通过项目编号查询")
    @GetMapping("/szzt/table/{tableId}/{projectId}")
    R getById(@PathVariable("tableId") String tableId, @PathVariable("projectId") String projectId);

    /**
     * 通过id查询
     * @param projectId projectId
     * @return R
     */
    @ApiOperation(value = "通过项目编号查询", notes = "通过项目编号查询")
    @PostMapping("/szzt/table/{tableId}/{projectId}")
    R pushData(@PathVariable("tableId") String tableId, @PathVariable("projectId") String projectId);
}
