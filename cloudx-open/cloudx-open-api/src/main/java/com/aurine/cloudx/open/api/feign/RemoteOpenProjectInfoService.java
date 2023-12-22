package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.ProjectInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 项目信息
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenProjectInfoService", value = "cloudx-open-biz")
public interface RemoteOpenProjectInfoService {

    /**
     * 通过id查询项目
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回项目信息
     */
    @GetMapping("/v1/open/project-info/{id}")
    R<ProjectInfoVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询项目
     *
     * @param pageModel 分页查询条件
     * @return R 返回项目分页数据
     */
    @GetMapping("/v1/open/project-info/page")
    R<Page<ProjectInfoVo>> page(@RequestBody OpenApiPageModel<ProjectInfoVo> pageModel);

    /**
     * 平台侧获取正在入云的项目
     * （平台侧调用）
     *
     * @param appId 应用ID
     * @return R 返回正在入云的项目列表
     */
    @GetMapping("/v1/open/project-info/list-cascade-by-cloud")
    R<List<ProjectInfoVo>> listCascadeByCloud(@RequestParam("appId") String appId);

    /**
     * 边缘侧获取正在入云的项目
     * （边缘侧调用）
     *
     * @param appId 应用ID
     * @return R 返回正在入云的项目列表
     */
    @GetMapping("/v1/open/project-info/list-cascade-by-edge")
    R<List<ProjectInfoVo>> listCascadeByEdge(@RequestParam("appId") String appId);

    /**
     * 主边缘侧获取正在级联的项目
     * （主边缘侧调用）
     *
     * @param appId 应用ID
     * @return R 返回正在级联的项目列表
     */
    @GetMapping("/v1/open/project-info/list-cascade-by-master")
    R<List<ProjectInfoVo>> listCascadeByMaster(@RequestParam("appId") String appId);

    /**
     * 从边缘侧获取正在级联的项目
     * （从边缘侧调用）
     *
     * @param appId 应用ID
     * @return R 返回正在级联的项目列表
     */
    @GetMapping("/v1/open/project-info/list-cascade-by-slave")
    R<List<ProjectInfoVo>> listCascadeBySlave(@RequestParam("appId") String appId);
}
