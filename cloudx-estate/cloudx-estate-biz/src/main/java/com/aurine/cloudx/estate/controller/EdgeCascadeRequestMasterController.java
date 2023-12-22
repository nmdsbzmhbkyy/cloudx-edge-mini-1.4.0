package com.aurine.cloudx.estate.controller;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.estate.entity.CascadeRequestInfoDTO;
import com.aurine.cloudx.estate.entity.EdgeCascadeRequestMaster;
import com.aurine.cloudx.estate.service.EdgeCascadeRequestMasterService;
import com.aurine.cloudx.estate.vo.CascadeManageQuery;
import com.aurine.cloudx.estate.vo.CascadeProjectInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>边缘网关入云申请控制器</p>
 *
 * @author : 王良俊
 * @date : 2021-12-02 17:34:59
 */
@RestController
@RequestMapping("/edgeCascadeRequestMaster")
public class EdgeCascadeRequestMasterController {

    @Resource
    EdgeCascadeRequestMasterService edgeCascadeRequestMasterService;

    /**
     * <p>申请级联接口</p>
     *
     * @param requestInfo 级联申请信息
     * @return 提交级联申请结果
     */
    @PostMapping("/requestCascade")
    @Inner(value = false)
    public R requestCascade(@RequestBody CascadeRequestInfoDTO requestInfo) {
        return edgeCascadeRequestMasterService.requestCascade(requestInfo);
    }

    /**
     * <p>批量通过级联申请</p>
     *
     * @param requestIdList 级联申请ID列表
     * @return 是否操作成功
     */
    @PostMapping("/passRequestBatch")
    public R passRequestBatch(@RequestBody List<String> requestIdList) {
        return edgeCascadeRequestMasterService.passRequestBatch(requestIdList);
    }

    /**
     * <p>通过级联申请</p>
     *
     * @param requestId 级联申请ID
     * @return 是否操作成功
     */
    @GetMapping("/passRequest/{requestId}")
    public R passRequest(@PathVariable("requestId") String requestId) {
        return edgeCascadeRequestMasterService.passRequest(requestId);
    }

    /**
     * <p>拒绝级联申请接口</p>
     *
     * @param requestId 级联申请ID
     * @return 是否操作成功
     */
    @GetMapping("/rejectRequest/{requestId}")
    public R rejectRequest(@PathVariable("requestId") String requestId) {
        return edgeCascadeRequestMasterService.rejectRequest(requestId);
    }

    /**
     * <p>分页查询级联项目</p>
     *
     * @param page 分页信息
     * @param cascadeManageQuery 查询条件
     * @return 分页数据
     */
    @GetMapping("/page")
    public R<Page<CascadeProjectInfoVo>> pageCascadeManage(Page page, CascadeManageQuery cascadeManageQuery) {
        return edgeCascadeRequestMasterService.pageCascadeManage(page, cascadeManageQuery);
    }

    /**
     * <p>获取级联申请列表</p>
     *
     * @param projectId 要查询的项目ID（一般是主边缘网关自有项目ID）
     * @return 级联申请列表
     */
    @PostMapping("/pageRequest/{projectId}")
    public R<Page<EdgeCascadeRequestMaster>> listRequest(Page page, @PathVariable("projectId") Integer projectId) {
        return edgeCascadeRequestMasterService.listRequest(page, projectId);
    }

    /**
     * <p>删除边缘网关</p>
     *
     * @param projectId 要查询的项目ID（一般是主边缘网关自有项目ID）
     * @return 删除结果
     */
    @DeleteMapping("/removeEdge/{projectId}")
    public R<String> removeEdge(@PathVariable("projectId") Integer projectId) {
        return edgeCascadeRequestMasterService.removeEdge(projectId);
    }


    /**
     * <p>取消级联申请</p>
     *
     * @param projectCode 第三方项目ID（从边缘网关的项目UUID）
     * @return 操作结果
     */
    @Inner(value = false)
    @GetMapping("/cancelRequest/{projectCode}")
    public String cancelRequest(@PathVariable("projectCode") String projectCode) {
        return JSON.toJSONString(edgeCascadeRequestMasterService.cancelRequest(projectCode).getData());
    }


    /**
     * <p>获取边缘网关初始项目的ID</p>
     *
     * @return 初始项目的项目ID
     */
    @GetMapping("/getOriginProjectId")
    public R getOriginProjectId() {
        return R.ok(edgeCascadeRequestMasterService.getOriginProjectId());
    }

}
