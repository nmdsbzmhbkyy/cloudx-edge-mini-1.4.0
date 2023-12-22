package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.constant.CascadeStatusConstants;
import com.aurine.cloudx.estate.entity.EdgeCascadeRequestSlave;
import com.aurine.cloudx.estate.mapper.EdgeCascadeRequestSlaveMapper;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCascadeTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.service.EdgeCascadeRequestMasterService;
import com.aurine.cloudx.estate.service.EdgeCascadeRequestSlaveAspectService;
import com.aurine.cloudx.estate.service.EdgeCascadeRequestSlaveService;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.thirdparty.module.edge.cascade.service.EdgeCascadeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * <p>边缘网关级联申请管理（主）</p>
 *
 * @author : 王良俊
 * @date : 2021-12-17 10:03:20
 */
@Slf4j
@Service
public class EdgeCascadeRequestSlaveServiceImpl extends ServiceImpl<EdgeCascadeRequestSlaveMapper, EdgeCascadeRequestSlave> implements EdgeCascadeRequestSlaveService {

    @Resource
    EdgeCascadeService edgeCascadeService;
    @Resource
    EdgeCascadeRequestMasterService edgeCascadeRequestMasterService;
    @Resource
    EdgeCascadeRequestSlaveAspectService edgeCascadeRequestSlaveAspectService;
    @Resource
    ProjectInfoService projectInfoService;

    @Override
    public R requestCascade(EdgeCascadeRequestSlave request) {
        Integer projectId = request.getProjectId();

        boolean canRequestCascade = edgeCascadeRequestMasterService.canRequestCascade(projectId);
        if (!canRequestCascade) {
            return R.failed("当前边缘网关不允许提交级联申请，存在待审核或是已级联的从边缘网关");
        }
        if (StringUtil.isEmpty(request.getParentEdgeIp()) || StringUtil.isEmpty(request.getConnectionCode())) {
            return R.failed("提交申请失败，缺少必要参数");
        }
        if (request.getProjectId() == null) {
            log.error("缺少项目ID：{}", request);
        }
        if (request.getConnectionCode().length() != 8) {
            return R.failed("级联码长度不符合要求，应该为8位");
        }
        EdgeCascadeRequestSlave requestSlave = this.getOne(new LambdaUpdateWrapper<EdgeCascadeRequestSlave>()
                .eq(EdgeCascadeRequestSlave::getProjectId, request.getProjectId())
                .last("limit 1"));
        if (requestSlave != null) {
            if (requestSlave.getCascadeStatus() != CascadeStatusConstants.NOT_CASCADE && requestSlave.getCascadeStatus() != CascadeStatusConstants.REJECT) {
                return R.failed("提交申请失败，已有级联申请");
            }
        } else {
            requestSlave = request;
            requestSlave.setRequestId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        requestSlave.setParentEdgeIp(request.getParentEdgeIp());
        requestSlave.setConnectionCode(request.getConnectionCode());
        // 提交级联申请
        return edgeCascadeService.requestCascade(requestSlave);
    }

    @Override
    public boolean isCascade() {
        int count = this.count(new LambdaUpdateWrapper<EdgeCascadeRequestSlave>().eq(EdgeCascadeRequestSlave::getCascadeStatus, CascadeStatusConstants.CASCADE));
        return count > 0;
    }

    @Override
    public R<String> revokeRequest(String requestId) {
        EdgeCascadeRequestSlave requestSlave = this.getOne(new LambdaQueryWrapper<EdgeCascadeRequestSlave>().eq(EdgeCascadeRequestSlave::getRequestId, requestId).last("limit 1"));
        if (requestSlave == null) {
            return R.failed("操作失败，无法找到对应的级联申请");
        }
        if (CascadeStatusConstants.CASCADE == requestSlave.getCascadeStatus()) {
            return R.failed("撤销申请失败，项目已级联");
        }
        requestSlave.setCascadeStatus(CascadeStatusConstants.NOT_CASCADE);
//        this.updateById(requestSlave);
        edgeCascadeRequestSlaveAspectService.revokeRequestUpdate(requestSlave);
//        return edgeCascadeService.cancelRequest(requestId);
        return R.ok("撤销申请成功");
    }

    @Override
    public R<String> requestUnbind(String requestId) {
        EdgeCascadeRequestSlave requestSlave = this.getOne(new LambdaQueryWrapper<EdgeCascadeRequestSlave>().eq(EdgeCascadeRequestSlave::getRequestId, requestId).last("limit 1"));
        if (requestSlave == null) {
            return R.failed("操作失败，无法找到对应的级联申请");
        }
        requestSlave.setCascadeStatus(CascadeStatusConstants.NOT_CASCADE);
        edgeCascadeRequestSlaveAspectService.requestUnbindUpdate(requestSlave);
        this.updateById(requestSlave);
        return R.ok("解绑成功");
    }

    @Override
    public boolean removeSlave(String projectCode) {
        return this.updateStatus(projectCode, CascadeStatusConstants.NOT_CASCADE);
    }

    @Override
    public boolean passRequest(String projectCode) {
        return this.updateStatus(projectCode, CascadeStatusConstants.CASCADE);
    }

    @Override
    public boolean rejectRequest(String projectCode) {
        return this.updateStatus(projectCode, CascadeStatusConstants.REJECT);
    }

    boolean updateStatus(String projectCode, char cascadeStatus) {

        Integer projectId = projectInfoService.getProjectId(projectCode);
        if (projectId != null) {
            EdgeCascadeRequestSlave requestSlave = this.getOne(new LambdaQueryWrapper<EdgeCascadeRequestSlave>()
                    .eq(EdgeCascadeRequestSlave::getProjectId, projectId));
            if (requestSlave != null) {
                requestSlave.setCascadeStatus(cascadeStatus);
                return this.updateById(requestSlave);
            }
        }
        return false;
    }
}
