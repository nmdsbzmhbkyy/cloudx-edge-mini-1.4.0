package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.FaceInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 人脸信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenFaceInfoService", value = "cloudx-open-biz")
public interface RemoteOpenFaceInfoService {

    /**
     * 通过id查询人脸信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回人脸信息
     */
    @GetMapping("/v1/open/face-info/{id}")
    R<FaceInfoVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询人脸信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回人脸信息分页数据
     */
    @GetMapping("/v1/open/face-info/page")
    R<Page<FaceInfoVo>> page(@RequestBody OpenApiPageModel<FaceInfoVo> pageModel);

    /**
     * 新增人脸信息
     *
     * @param model 人脸信息
     * @return R 返回新增后的人脸信息
     */
    @PostMapping("/v1/open/face-info")
    R<FaceInfoVo> save(@RequestBody OpenApiModel<FaceInfoVo> model);

    /**
     * 修改人脸信息
     *
     * @param model 人脸信息
     * @return R 返回修改后的人脸信息
     */
    @PutMapping("/v1/open/face-info")
    R<FaceInfoVo> update(@RequestBody OpenApiModel<FaceInfoVo> model);

    /**
     * 通过id删除人脸信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/face-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
