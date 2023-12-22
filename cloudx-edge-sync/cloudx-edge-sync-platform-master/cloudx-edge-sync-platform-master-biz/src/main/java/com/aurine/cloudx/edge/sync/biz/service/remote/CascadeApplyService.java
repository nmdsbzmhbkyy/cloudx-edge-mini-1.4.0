package com.aurine.cloudx.edge.sync.biz.service.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.biz.service.ProjectRelationService;
import com.aurine.cloudx.edge.sync.common.entity.po.ProjectRelation;
import com.aurine.cloudx.open.api.inner.feign.RemoteCascadeCloudApplyService;
import com.aurine.cloudx.open.api.inner.feign.RemoteCascadeEdgeApplyService;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/01/20 13:57
 * @Package: com.aurine.cloudx.edge.sync.biz.service.remote
 * @Version: 1.0
 * @Remarks: 级联绑定
 **/
@Slf4j
@Component
public class CascadeApplyService {

    @Resource
    private RemoteCascadeEdgeApplyService remoteCascadeEdgeApplyService;
    @Resource
    private RemoteCascadeCloudApplyService remoteCascadeCloudApplyService;

    @Resource
    private ProjectRelationService projectRelationService;

    /**
     * 同意申请
     *
     * @param projectCode
     * @param openApiHeader
     * @return
     */
    public R accept(String projectCode, OpenApiHeader openApiHeader) {
        log.info("intoCloud -> openApi acceptApply req= {}", JSON.toJSONString(openApiHeader));
        R r = remoteCascadeCloudApplyService.accept(openApiHeader, projectCode);
        log.info("intoCloud -> openApi acceptApply res = {}", JSON.toJSONString(r));
        // 同意申请成功时将返回的data改为对象值改为projectRelation对象
        if (r.getCode() == 0) {
            ProjectRelation byProjectCode = projectRelationService.getByProjectCode(projectCode);
            ProjectRelation respData = new ProjectRelation();
            respData.setProjectUUID(byProjectCode.getProjectCode());
            respData.setProjectCode(byProjectCode.getProjectUUID());
            r.setData(JSONObject.parseObject(JSONObject.toJSONString(respData)));
        }
        return r;
    }

    /**
     * 拒绝申请
     *
     * @param projectCode
     * @param openApiHeader
     * @return
     */
    public R reject(String projectCode, OpenApiHeader openApiHeader) {
        log.info("intoCloud -> openApi rejectApply projectCode={}, req= {}", projectCode, JSON.toJSONString(openApiHeader));
        R r = remoteCascadeCloudApplyService.reject(openApiHeader, projectCode);
        log.info("intoCloud -> openApi rejectApply res = {}", JSON.toJSONString(r));
        return r;
    }

    /**
     * 撤销申请
     *
     * @param projectCode
     * @param openApiHeader
     * @return
     */
    public R revoke(String projectCode, OpenApiHeader openApiHeader) {
        log.info("intoCloud -> openApi deleteApply req= {}", JSON.toJSONString(openApiHeader));
        ProjectRelation byProjectCode = projectRelationService.getByProjectCode(projectCode);
        R r = remoteCascadeCloudApplyService.revoke(openApiHeader, byProjectCode.getProjectUUID(), projectCode);
        log.info("intoCloud -> openApi deleteApply res =" + JSON.toJSONString(r));
        return r;
    }
}
