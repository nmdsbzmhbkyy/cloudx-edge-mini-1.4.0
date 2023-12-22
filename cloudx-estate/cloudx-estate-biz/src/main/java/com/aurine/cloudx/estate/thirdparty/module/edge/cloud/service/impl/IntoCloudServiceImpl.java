package com.aurine.cloudx.estate.thirdparty.module.edge.cloud.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.IntoCloudStatusEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.BridgingConfInfo;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.DriverManagerReq;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.constant.BridgingSwitchConstants;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.IntoCloudRequestInfoDTO;
import com.aurine.cloudx.estate.entity.IntoCloudResponse;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.SubCfgInfo;
import com.aurine.cloudx.estate.thirdparty.module.edge.cloud.enums.IntoCloudEventCodeEnum;
import com.aurine.cloudx.estate.thirdparty.module.edge.cloud.remote.IntoCloudRemoteService;
import com.aurine.cloudx.estate.thirdparty.module.edge.cloud.service.IntoCloudService;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.bean.BeanPropertyUtil;
import com.aurine.cloudx.estate.util.bean.FieldMapping;
import com.aurine.cloudx.estate.vo.ProjectInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>边缘侧入云服务</p>
 *
 * @author : 王良俊
 * @date : 2021-12-17 11:04:43
 */
@Slf4j
@Service
public class IntoCloudServiceImpl implements IntoCloudService {

    /**
     * 未入云
     */
    private static final char NOT_INTO_CLOUD = IntoCloudStatusEnum.NOT_INTO_CLOUD.code;
    /**
     * 已入云
     */
    private static final char INTO_CLOUD = IntoCloudStatusEnum.INTO_CLOUD.code;
    /**
     * 解绑中
     */
    private static final char UNBINDING = IntoCloudStatusEnum.UNBINDING.code;
    /**
     * 被拒绝
     */
    private static final char REJECT = IntoCloudStatusEnum.REJECT.code;
    /**
     * 审核中
     */
    private static final char PENDING_AUDIT = IntoCloudStatusEnum.PENDING_AUDIT.code;

    @Value("${server.cloud-uri}")
    String cloudUrl;
    @Value("${server.edge-center-uri}")
    String edgeCenterUrl;
    @Value("${server.cloud-ip}")
    String cloudIp;

    @Resource
    IntoCloudRemoteService intoCloudRemoteService;
    @Resource
    EdgeCascadeRequestSlaveService edgeCascadeRequestSlaveService;
    @Resource
    EdgeCascadeRequestMasterService edgeCascadeRequestMasterService;
    @Resource
    EdgeCloudRequestService edgeCloudRequestService;
    @Resource
    EdgeCloudRequestAspectService edgeCloudRequestAspectService;
    @Resource
    ProjectInfoService projectInfoService;
    @Resource
    ProjectThirdpartyInfoService projectThirdpartyInfoService;

    @Override
    public R<String> requestIntoCloud(EdgeCloudRequest request) {
        Integer projectId = request.getProjectId();

        String deviceSN = this.getDeviceSN(projectId);
        if (StrUtil.isEmpty(deviceSN)) {
            throw new RuntimeException("[边缘侧入云] 未获取到设备SN无法申请入云");
        }
        boolean master = edgeCascadeRequestMasterService.isMaster(projectId);

        try {

            IntoCloudRequestInfoDTO intoCloudRequestInfo = new IntoCloudRequestInfoDTO();
            intoCloudRequestInfo.setRequestTime(System.currentTimeMillis() / 1000);
            intoCloudRequestInfo.setConnectCode(request.getConnectionCode());
            intoCloudRequestInfo.setDeviceSn(deviceSN);
            intoCloudRequestInfo.setProjectCode(request.getProjectCode());
            intoCloudRequestInfo.setSyncType(request.getSyncType());
            ProjectInfo projectInfo = projectInfoService.getById(projectId);
            if (master) {
                AdminUserInfo currentAdminUserInfo = projectInfoService.getCurrentAdminUserInfo();
                if (currentAdminUserInfo == null) {
                    throw new RuntimeException("[边缘侧入云] 无法获取到管理员信息，申请失败");
                }

                intoCloudRequestInfo.setContactPersonName(currentAdminUserInfo.getTrue_name());
                intoCloudRequestInfo.setContactGender(currentAdminUserInfo.getSex());
                intoCloudRequestInfo.setContactPhone(currentAdminUserInfo.getPhone());
                intoCloudRequestInfo.setProjectName(projectInfo.getProjectName());
                intoCloudRequestInfo.setContactIdNumber(currentAdminUserInfo.getCredential_no());
                //TODO: 头像先不管
                String picPath = currentAdminUserInfo.getAvatar();
                if (StrUtil.isEmpty(picPath)) {

                }
            } else {
                EdgeCascadeRequestMaster requestMaster = edgeCascadeRequestMasterService.getOne(new LambdaUpdateWrapper<EdgeCascadeRequestMaster>()
                        .eq(EdgeCascadeRequestMaster::getProjectCode, projectInfo.getProjectCode()));
                intoCloudRequestInfo.setContactGender(requestMaster.getSlaveGender());
                intoCloudRequestInfo.setContactPersonName(requestMaster.getSlaveContactPerson());
                intoCloudRequestInfo.setContactPhone(requestMaster.getSlaveContactPhone());
                intoCloudRequestInfo.setProjectName(projectInfo.getProjectName());
            }
            IntoCloudResponse intoCloudResponse = intoCloudRemoteService.requestIntoCloud(intoCloudRequestInfo);

            String resultCode = intoCloudResponse.getResultCode();
            if (IntoCloudEventCodeEnum.REQUEST_SUCCESS.code.equals(resultCode)) {
                // 申请提交成功，更新本地申请状态为待审核
                request.setCloudStatus(PENDING_AUDIT);
                String uuid = projectInfoService.getProjectUUID(projectId);
                // 发布订阅配置
                intoCloudRemoteService.pubSubCfg(new SubCfgInfo("0", uuid));
                Thread.sleep(1000);
                this.configCloudBridging(request);
                // 配置驱动主社区ID（驱动topic前缀）
                Integer originProjectId = edgeCascadeRequestMasterService.getOriginProjectId();
                String masterUUID = projectInfoService.getProjectUUID(originProjectId);
                intoCloudRemoteService.confDriverCommunityId(new DriverManagerReq(masterUUID));

            } else if (IntoCloudEventCodeEnum.REQUEST_SUCCESS_HAS_INTO_CLOUD.code.equals(resultCode)) {
                // 项目已经入云但是又提交了申请？ 这里相当于已经入云了要判断项目入云状态
                // 判断是否已存在入云申请记录
                int count = edgeCloudRequestService.count(new LambdaUpdateWrapper<EdgeCloudRequest>()
                        .eq(EdgeCloudRequest::getProjectCode, request.getProjectCode())
                        .eq(EdgeCloudRequest::getProjectId, projectId)
                        .in(EdgeCloudRequest::getCloudStatus, INTO_CLOUD, UNBINDING)
                );
                if (count != 0) {
                    return R.failed("申请入云失败，当前项目已入云");
                } else {
                    // 这里设置为已入云
                    request.setCloudStatus(INTO_CLOUD);
                    this.configCloudBridging(request);
                    //TODO: 这里做入云申请通过要做的事情 等待数据获取请求或是接收数据 批注：这些都不需要在这里做了，全部都在Open API处理
                }
            } else if (IntoCloudEventCodeEnum.REQUEST_FAILED_CODE_ERROR.code.equals(resultCode)) {
                return R.failed("申请入云失败，请输入正确的入云码");
            } else if (IntoCloudEventCodeEnum.REQUEST_FAILED_EXIST_CLOUD.code.equals(resultCode)) {
                return R.failed("申请入云失败，云端项目已绑定其他边缘网关");
            } else if (IntoCloudEventCodeEnum.REQUEST_FAILED_MQTT.code.equals(resultCode)) {
                // 入云申请提交失败，云端为边缘侧注册MQTT账号失败，提示前端需要重新尝试
                return R.failed("申请入云失败，云端MQTT账号创建失败");
            } else if (IntoCloudEventCodeEnum.REQUEST_FAILED_BOUND_OTHER.code.equals(resultCode)) {
                // 入云申请提交失败，当前边缘网关已经连接云端项目？
                return R.failed("申请入云失败，当前项目已入云");
            } else if (IntoCloudEventCodeEnum.REQUEST_FAILED_MISSING_PARAMETERS.code.equals(resultCode)) {
                // 入云申请提交失败，缺少申请参数
                return R.failed("申请入云失败，缺少必要参数");
            } else if (IntoCloudEventCodeEnum.REQUEST_FAILED_CLOUD_DATA_NOT_CLEARED.code.equals(resultCode)) {
                return R.failed("入云申请失败，存在云端项目未清空数据");
            } else if (IntoCloudEventCodeEnum.REQUEST_FAILED_REQUEST_EXIST.code.equals(resultCode)) {
                return R.failed("入云申请失败，请勿重复申请同个云端项目");
            }
            String data = intoCloudResponse.getData();
            if (StrUtil.isNotEmpty(data)) {
                //TODO: 这里保存云端项目信息
                ProjectInfoVo projectInfoVo = ObjectMapperUtil.instance().readValue(data, ProjectInfoVo.class);
                request.setCloudProjectUid(projectInfoVo.getProjectUUID());
                projectThirdpartyInfoService.saveOrUpdateThirdpartyInfo(projectInfoVo, projectId, request.getRequestId());
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            request.setCloudStatus(NOT_INTO_CLOUD);
        }

        if (request.getSyncType() =='2') {
            request.setDelStatus('0');
        } else {
            request.setDelStatus(null);
            if (StrUtil.isNotEmpty(request.getRequestId())) {
                int count = edgeCloudRequestService.count(new LambdaQueryWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getRequestId, request.getRequestId()));
                if (count > 0) {
                    edgeCloudRequestService.update(new LambdaUpdateWrapper<EdgeCloudRequest>().eq(EdgeCloudRequest::getRequestId, request.getRequestId()).set(EdgeCloudRequest::getDelStatus, null));
                }
            }
        }
        request.setIsSync('0');
        edgeCloudRequestAspectService.requestIntoCloudSaveOrUpdate(request, deviceSN);
        if (IntoCloudStatusEnum.NOT_INTO_CLOUD.code == request.getCloudStatus()) {
            return R.failed("入云申请提交失败");
        }
        return R.ok("入云申请成功");
    }

    @Override
    public R<String> cancelRequest(String requestId) {
        EdgeCloudRequest request = edgeCloudRequestService.getOne(new LambdaUpdateWrapper<EdgeCloudRequest>()
                .eq(EdgeCloudRequest::getRequestId, requestId).last("limit 1"));
        if (request == null) {
            return R.ok("撤销入云申请成功");
        }
        String projectCode = request.getProjectCode();
        String connectionCode = request.getConnectionCode();
//        try {
//            IntoCloudResponse intoCloudResponse = intoCloudRemoteServiceAbstract.cancelRequest(projectCode, connectionCode);
//            String resultCode = intoCloudResponse.getResultCode();
//            if (IntoCloudEventCodeEnum.CANCEL_INTO_CLOUD_REQUEST_FAILED.code.equals(resultCode)) {
//                return R.failed("撤销入云申请失败");
//            }
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            log.error("JSON解析异常");
//            return R.failed("撤销入云申请失败");
//        }

        EdgeCloudRequest edgeCloudRequest = new EdgeCloudRequest();
        edgeCloudRequest.setRequestId(requestId);
        edgeCloudRequest.setCloudStatus(IntoCloudStatusEnum.NOT_INTO_CLOUD.code);
        edgeCloudRequestAspectService.cancelRequestUpdate(edgeCloudRequest);
//        edgeCloudRequestService.updateCloudStatus(requestId, IntoCloudStatusEnum.NOT_INTO_CLOUD);
        return R.ok("撤销入云申请成功");
    }

    @Override
    public String getDeviceSN(Integer projectId) {

        /*String projectCode = projectInfoService.getProjectUUID(projectId);
        boolean isMaster = edgeCascadeRequestMasterService.isMaster(projectId);
        if (isMaster) {*/
            // 如果是主边缘网关
            return intoCloudRemoteService.getDeviceSN();
        /*} else {
            // 否则拿的是级联设备级联时提供的设备SN
            EdgeCascadeRequestMaster requestMaster = edgeCascadeRequestMasterService.getOne(new LambdaUpdateWrapper<EdgeCascadeRequestMaster>()
                    .eq(EdgeCascadeRequestMaster::getProjectCode, projectCode));
            if (requestMaster != null) {
                return requestMaster.getSlaveEdgeDeviceId();
            } else {
                log.error("未获取到从边缘网关设备SN");
                return null;
            }
        }*/
    }

    @Override
    public boolean configCloudBridging(EdgeCloudRequest request) {
        log.info("配置桥接：{}", request);
        String uuid = projectInfoService.getProjectUUID(request.getProjectId());

        BridgingConfInfo bridgingConfInfo = new BridgingConfInfo();
        BeanPropertyUtil.copyProperty(bridgingConfInfo, request, new FieldMapping<BridgingConfInfo, EdgeCloudRequest>()
                .add(BridgingConfInfo::getCascadeCode, EdgeCloudRequest::getConnectionCode)
        );
        bridgingConfInfo.setAddress(cloudIp);
        bridgingConfInfo.setCascadeSwitch(BridgingSwitchConstants.SWITCH_CLOUD);
        bridgingConfInfo.setCommunityId(uuid);
        return intoCloudRemoteService.configBridging(bridgingConfInfo);
    }

/*
    @Override
    public boolean requestUnbind(String requestId) {
        return false;
    }
*/

/*    @Override
    public void requestUnbindResponseHandler(String response) {
        if (StrUtil.isNotEmpty(response)) {
            try {
                IntoCloudResponse intoCloudResponse = ObjectMapperUtil.instance().readValue(response, IntoCloudResponse.class);
                // 带有projectCode和connectCode信息
                ObjectNode data = intoCloudResponse.getData();
                String projectCode = data.findPath("projectCode").asText();
                String connectCode = data.findPath("connectCode").asText();

                switch (intoCloudResponse.getResultCode()) {
                    case REQUEST_UNBIND_SUCCESS:
                        // 申请成功
                        // 更新入云状态为解绑中
                        log.info("解绑申请提交成功：projectCode：{} connectCode：{}", projectCode, connectCode);
                        edgeCloudRequestService.updateIntoCloudStatus(projectCode, connectCode, IntoCloudStatusEnum.UNBINDING.code);
                        break;
                    case UNBIND_SUCCESS:
                        // 解绑成功（已经解绑了，可能某些原因边缘侧还是没解绑状态）
                        // 更新入云状态为未入云，因为云端已经是解绑状态
                        edgeCloudRequestService.unbind(projectCode, connectCode);
                        edgeCloudRequestService.updateIntoCloudStatus(projectCode, connectCode, IntoCloudStatusEnum.NOT_INTO_CLOUD.code);
                        break;
                    default:
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                log.error("处理解绑申请回调失败");
            }

        }

    }*/
}
