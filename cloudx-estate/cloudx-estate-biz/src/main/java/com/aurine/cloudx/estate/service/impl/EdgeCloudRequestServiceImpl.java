package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.IntoCloudStatusEnum;
import com.aurine.cloudx.estate.entity.EdgeCascadeConf;
import com.aurine.cloudx.estate.entity.EdgeCloudRequest;
import com.aurine.cloudx.estate.mapper.EdgeCloudRequestMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.module.edge.cloud.service.IntoCloudService;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.vo.EdgeCascadeConfVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
    @Resource
    IntoCloudService intoCloudService;
    @Resource
    ProjectInfoService projectInfoService;
    @Resource
    EdgeCascadeConfService edgeCascadeConfService;
    @Resource
    EdgeCascadeRequestMasterService edgeCascadeRequestMasterService;
    @Resource
    EdgeCloudRequestAspectService edgeCloudRequestAspectService;

    @Override
//    @Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = Exception.class)
    public R requestIntoCloud(Character syncType, String connectCode, Integer projectId) {
        // 主边缘网关的级联项目使用的UUID是从边缘网关的UUID
        String projectCode = projectInfoService.getProjectUUID(projectId);
        EdgeCloudRequest edgeCloudRequest = this.getOne(new LambdaUpdateWrapper<EdgeCloudRequest>()
                .eq(EdgeCloudRequest::getProjectId, projectId));
        // 判断是否已有申请记录
        if (edgeCloudRequest == null) {
            edgeCloudRequest = new EdgeCloudRequest();
            edgeCloudRequest.setRequestId(UUID.randomUUID().toString().replaceAll("-", ""));
            edgeCloudRequest.setRequestTime(LocalDateTime.now());
            edgeCloudRequest.setCloudSyncProcess(new BigDecimal(0));
            edgeCloudRequest.setProjectId(projectId);
            edgeCloudRequest.setConfigJson(ObjectMapperUtil.instance().createObjectNode().toString());
            edgeCloudRequest.setProjectCode(projectCode);
        } else {
            Character cloudStatus = edgeCloudRequest.getCloudStatus();
            if (IntoCloudStatusEnum.INTO_CLOUD.code == cloudStatus || IntoCloudStatusEnum.UNBINDING.code == cloudStatus) {
                return R.failed("项目已入云无法再次申请");
            } else if (IntoCloudStatusEnum.PENDING_AUDIT.code == cloudStatus || IntoCloudStatusEnum.REVOKE_REQUEST.code == cloudStatus) {
                return R.failed("已有入云申请无法再次申请");
            }
        }
        // 这两个是每次申请都可能变的
        edgeCloudRequest.setSyncType(syncType);
        edgeCloudRequest.setConnectionCode(connectCode);
        // 是否申请成功是同步返回的
        R<String> result = intoCloudService.requestIntoCloud(edgeCloudRequest);
        if (result.getCode() == CommonConstants.SUCCESS) {
            // 如果是主边缘网关项目就不要去用这种方式初始化入云级联配置
            boolean master = edgeCascadeRequestMasterService.isMaster(projectId);
            if (!master) {
                edgeCascadeConfService.initCascadeConf(projectId);
            }
        }
        return result;
        // 就不在这里进行保存了
        /*if (requestResult) {
            this.save(edgeCloudRequest);
        }*/
    }

    @Override
    public boolean canSyncData(String projectCode) {
        //TODO: 暂时不清楚云端同步数据靠什么判断这个数据能不能存储
        int count = this.count(new LambdaUpdateWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getProjectCode, projectCode)
                .in(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code, IntoCloudStatusEnum.UNBINDING.code));
        return count > 0;
    }

    @Override
    public boolean canSyncData(Integer projectId) {
        //TODO: 暂时不清楚云端同步数据靠什么判断这个数据能不能存储
        int count = this.count(new LambdaUpdateWrapper<EdgeCloudRequest>()
                .eq(EdgeCloudRequest::getProjectId, projectId)
                .in(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code, IntoCloudStatusEnum.UNBINDING.code));
        return count > 0;
    }

    @Override
    public R requestUnbind(String requestId) {
        EdgeCloudRequest cloudRequest = this.getOne(new LambdaUpdateWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getRequestId, requestId).last("limit 1"));
        if (cloudRequest != null) {
            Character cloudStatus = cloudRequest.getCloudStatus();
            if (IntoCloudStatusEnum.INTO_CLOUD.code != cloudStatus && IntoCloudStatusEnum.UNBINDING.code != cloudStatus) {
                return R.failed("当前入云状态已变无法申请解绑，请刷新页面");
            }
        }
        this.edgeCloudRequestAspectService.requestUnbindUpdate(requestId, IntoCloudStatusEnum.UNBINDING);
        return R.ok(true);
    }

    @Override
    public R revokeUnbindRequest(String requestId) {
        EdgeCloudRequest cloudRequest = this.getOne(new LambdaUpdateWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getRequestId, requestId).last("limit 1"));
        if (cloudRequest != null) {
            Character cloudStatus = cloudRequest.getCloudStatus();
            if (IntoCloudStatusEnum.INTO_CLOUD.code != cloudStatus && IntoCloudStatusEnum.UNBINDING.code != cloudStatus) {
                return R.failed("当前入云状态已变无法申请解绑，请刷新页面");
            }
        }
        this.edgeCloudRequestAspectService.revokeUnbindRequestUpdate(requestId, IntoCloudStatusEnum.INTO_CLOUD);
        return R.ok(true);
    }

    @Override
    public boolean updateCloudStatus(Integer projectId, IntoCloudStatusEnum statusEnum) {
        String projectCode = projectInfoService.getProjectUUID(projectId);
        log.info("更新入云状态 projectId：{} code：{}", projectId, statusEnum.code);
        return this.update(new LambdaUpdateWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getProjectCode, projectCode)
                .set(EdgeCloudRequest::getCloudStatus, statusEnum.code));
    }

    @Override
    public boolean updateCloudStatus(String requestId, IntoCloudStatusEnum statusEnum) {
        log.info("更新入云状态 requestId：{} code：{}", requestId, statusEnum.code);
        EdgeCloudRequest edgeCloudRequest = new EdgeCloudRequest();
        edgeCloudRequest.setRequestId(requestId);
        edgeCloudRequest.setCloudStatus(statusEnum.code);
        return this.updateById(edgeCloudRequest);
    }

    @Override
    public R revokeIntoCloudRequest(String requestId) {
        return intoCloudService.cancelRequest(requestId);
    }

    @Override
    public Boolean canDisableNetwork(Integer projectId) {
        int count = this.count(new LambdaUpdateWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getProjectId, projectId)
                .notIn(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.NOT_INTO_CLOUD.code, IntoCloudStatusEnum.REJECT.code));
        return count == 0;
    }

    @Override
    public void passRequest(String projectCode) {
        EdgeCloudRequest request = getPendingAuditRequest(projectCode);
        if (request != null) {
            request.setCloudStatus(IntoCloudStatusEnum.INTO_CLOUD.code);
            this.updateById(request);
        }
    }

    @Override
    public void rejectRequest(String projectCode) {
        EdgeCloudRequest request = getPendingAuditRequest(projectCode);
        if (request != null) {
            request.setCloudStatus(IntoCloudStatusEnum.REJECT.code);
            this.updateById(request);
        }
    }

    @Override
    public void removeEdge(String projectCode) {
        EdgeCloudRequest request = this.getOne(new LambdaQueryWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getProjectCode, projectCode)
                .in(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code, IntoCloudStatusEnum.UNBINDING.code));
        if (request != null) {
            request.setCloudStatus(IntoCloudStatusEnum.NOT_INTO_CLOUD.code);
            this.updateById(request);
        }
    }

    private EdgeCloudRequest getPendingAuditRequest(String projectCode) {
        return this.getOne(new LambdaQueryWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getProjectCode, projectCode)
                .eq(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.PENDING_AUDIT.code).last("limit 1"));

    }
}
