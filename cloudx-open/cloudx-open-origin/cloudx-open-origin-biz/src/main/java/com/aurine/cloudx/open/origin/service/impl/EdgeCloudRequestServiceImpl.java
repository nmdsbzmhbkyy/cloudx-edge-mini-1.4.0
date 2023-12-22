package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.mapper.EdgeCloudRequestMapper;
import com.aurine.cloudx.open.origin.constant.enums.IntoCloudStatusEnum;
import com.aurine.cloudx.open.origin.entity.EdgeCloudRequest;
import com.aurine.cloudx.open.origin.service.EdgeCascadeConfService;
import com.aurine.cloudx.open.origin.service.EdgeCloudRequestService;
import com.aurine.cloudx.open.origin.service.ProjectInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>边缘网关入云申请表</p>
 *
 * @author : 王良俊
 * @date : 2021-12-17 09:21:16
 */
@Slf4j
@Service
public class EdgeCloudRequestServiceImpl extends ServiceImpl<EdgeCloudRequestMapper, EdgeCloudRequest> implements EdgeCloudRequestService {

    @Resource
    EdgeCloudRequestMapper edgeCloudRequestMapper;
    //    @Resource
//    IntoCloudService intoCloudService;
    @Resource
    ProjectInfoService projectInfoService;
    @Resource
    EdgeCascadeConfService edgeCascadeConfService;

    @Override
    public R requestUnbind(String requestId) {
        EdgeCloudRequest cloudRequest = this.getOne(new LambdaUpdateWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getRequestId, requestId).last("limit 1"));
        if (cloudRequest != null) {
            Character cloudStatus = cloudRequest.getCloudStatus();
            if (IntoCloudStatusEnum.INTO_CLOUD.code != cloudStatus && IntoCloudStatusEnum.UNBINDING.code != cloudStatus) {
                return R.failed("当前入云状态已变无法申请解绑，请刷新页面");
            }
        }
        return R.ok(this.updateCloudStatus(requestId, IntoCloudStatusEnum.UNBINDING));
    }

    @Override
    public R<Boolean> revokeUnbindRequest(String requestId) {
        EdgeCloudRequest cloudRequest = this.getOne(new LambdaUpdateWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getRequestId, requestId).last("limit 1"));
        if (cloudRequest != null) {
            Character cloudStatus = cloudRequest.getCloudStatus();
            if (IntoCloudStatusEnum.INTO_CLOUD.code != cloudStatus && IntoCloudStatusEnum.UNBINDING.code != cloudStatus) {
                return R.failed("当前入云状态已变无法申请解绑，请刷新页面");
            }
        }
        return R.ok(this.updateCloudStatus(requestId, IntoCloudStatusEnum.INTO_CLOUD));
    }

    @Override
    public boolean updateCloudStatus(String requestId, IntoCloudStatusEnum statusEnum) {
        log.info("更新入云状态 requestId：{} code：{}", requestId, statusEnum.code);
        EdgeCloudRequest edgeCloudRequest = new EdgeCloudRequest();
        edgeCloudRequest.setRequestId(requestId);
        edgeCloudRequest.setCloudStatus(statusEnum.code);
        return this.updateById(edgeCloudRequest);
    }

//    @Override
//    public R<Boolean> revokeIntoCloudRequest(String requestId) {
//        return intoCloudService.cancelRequest(requestId);
//    }

    @Override
    public Boolean canDisableNetwork(Integer projectId) {
        int count = this.count(new LambdaUpdateWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getProjectId, projectId)
                .notIn(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.NOT_INTO_CLOUD.code, IntoCloudStatusEnum.REJECT.code));
        return count == 0;
    }

    @Override
    public Boolean passRequest(String projectCode) {
        EdgeCloudRequest edgeCloudRequest = this.getOne(new LambdaQueryWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getCloudProjectUid, projectCode));
        if (edgeCloudRequest == null || (!edgeCloudRequest.getCloudStatus().equals(IntoCloudStatusEnum.PENDING_AUDIT.code) && !edgeCloudRequest.getCloudStatus().equals(IntoCloudStatusEnum.INTO_CLOUD.code))) {
            return false;
        }
        edgeCloudRequest.setCloudStatus(IntoCloudStatusEnum.INTO_CLOUD.code);
        return this.updateById(edgeCloudRequest);
    }

    @Override
    public Boolean rejectRequest(String projectCode) {
        EdgeCloudRequest edgeCloudRequest = this.getOne(new LambdaQueryWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getCloudProjectUid, projectCode));

        if (edgeCloudRequest == null ||
                (!edgeCloudRequest.getCloudStatus().equals(IntoCloudStatusEnum.PENDING_AUDIT.code) &&
                        !edgeCloudRequest.getCloudStatus().equals(IntoCloudStatusEnum.REJECT.code)
                )) {
            return false;
        }
        edgeCloudRequest.setCloudStatus(IntoCloudStatusEnum.REJECT.code);
        return this.updateById(edgeCloudRequest);
    }

    @Override
    public Boolean removeEdge(String projectCode) {
        EdgeCloudRequest request = this.getOne(new LambdaQueryWrapper<EdgeCloudRequest>()
                .eq(EdgeCloudRequest::getCloudProjectUid, projectCode)
                .in(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code, IntoCloudStatusEnum.UNBINDING.code));
        if (request != null) {
            request.setCloudStatus(IntoCloudStatusEnum.NOT_INTO_CLOUD.code);
            return this.updateById(request);
        }
        return false;
    }

    @Override
    public Boolean rejectUnbindRequest(String projectCode) {
        EdgeCloudRequest request = this.getOne(new LambdaQueryWrapper<EdgeCloudRequest>()
                .eq(EdgeCloudRequest::getCloudProjectUid, projectCode)
                .in(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.UNBINDING.code, IntoCloudStatusEnum.INTO_CLOUD.code).last("limit 1"));
        if (request != null) {
            request.setCloudStatus(IntoCloudStatusEnum.INTO_CLOUD.code);
            return this.updateById(request);
        }
        return false;
    }

}
