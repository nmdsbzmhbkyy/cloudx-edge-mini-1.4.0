package com.aurine.cloudx.estate.thirdparty.module.edge.cascade.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.CascadeStatusConstants;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.entity.EdgeCascadeResponse;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.DriverManagerReq;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.MQTTAccount;
import com.aurine.cloudx.estate.constant.EdgeCascadeEventCodeEnum;
import com.aurine.cloudx.estate.thirdparty.module.edge.cascade.remote.CascadeRemoteService;
import com.aurine.cloudx.estate.thirdparty.module.edge.cascade.service.EdgeCascadeService;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.BridgingConfInfo;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.constant.BridgingSwitchConstants;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.SubCfgInfo;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.bean.BeanPropertyUtil;
import com.aurine.cloudx.estate.util.bean.FieldMapping;
import com.aurine.cloudx.estate.vo.ProjectInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>级联第三方服务类</p>
 *
 * @author : 王良俊
 * @date : 2021-12-21 17:47:31
 */
@Service
@Slf4j
public class EdgeCascadeServiceImpl implements EdgeCascadeService {

    @Resource
    CascadeRemoteService cascadeRemoteService;
    @Resource
    ProjectInfoService projectInfoService;
    @Resource
    EdgeCascadeRequestSlaveService edgeCascadeRequestSlaveService;
    @Resource
    EdgeCascadeRequestSlaveAspectService edgeCascadeRequestSlaveAspectService;
    @Resource
    ProjectThirdpartyInfoService projectThirdpartyInfoService;
    @Resource
    EdgeCascadeRequestMasterService edgeCascadeRequestMasterService;
    @Resource
    EdgeCascadeConfService edgeCascadeConfService;

    @Override
    public String getDeviceSN(Integer projectId) {
        return cascadeRemoteService.getDeviceSN();
    }

    @Override
    public boolean createAccount(EdgeCascadeRequestMaster edgeCascadeRequestMaster) {
        log.info("[MQTT账号创建] 准备创建MQTT账号 {}", edgeCascadeRequestMaster);
        MQTTAccount mqttAccount = new MQTTAccount();
        mqttAccount.setMachineSN(edgeCascadeRequestMaster.getSlaveEdgeDeviceId());
        mqttAccount.setCommunityId(edgeCascadeRequestMaster.getProjectCode());
        mqttAccount.setCascadeSwitch("0");
        // 应该在外面就已经校验过了
        String connectCode = edgeCascadeConfService.getConnectCode();
        if (StrUtil.isEmpty(connectCode)) {
            log.info("[MQTT账号创建] 未获取到边缘网关级联码 {}", edgeCascadeRequestMaster);
            return false;
        }
        mqttAccount.setCascadeCode(connectCode);
        return cascadeRemoteService.createAccount(mqttAccount);
    }

    @Override
    public R<String> requestCascade(EdgeCascadeRequestSlave request) {
        AdminUserInfo adminInfo = projectInfoService.getCurrentAdminUserInfo();
        Integer projectId = request.getProjectId();
        ProjectInfo projectInfo = projectInfoService.getOne(new LambdaQueryWrapper<ProjectInfo>().eq(ProjectInfo::getProjectId, projectId));
        CascadeRequestInfoDTO cascadeRequest = new CascadeRequestInfoDTO();

        int count = edgeCascadeConfService.count(new LambdaQueryWrapper<EdgeCascadeConf>().eq(EdgeCascadeConf::getConnectCode, request.getConnectionCode()));
        if (count > 0) {
            return R.failed("级联连接码不允许和本机级联码相同");
        }
        String projectCode = projectInfoService.getProjectUUID(projectId);
        cascadeRequest.setProjectCode(projectCode);
        BeanPropertyUtil.copyProperty(cascadeRequest, adminInfo, new FieldMapping<CascadeRequestInfoDTO, AdminUserInfo>()
                        .add(CascadeRequestInfoDTO::getContactGender, AdminUserInfo::getSex)
                        .add(CascadeRequestInfoDTO::getContactPersonName, AdminUserInfo::getTrue_name)
                        .add(CascadeRequestInfoDTO::getContactPhone, AdminUserInfo::getPhone)
                // 头像先不管
//                .add(CascadeRequestInfoDTO::getContactPicBase64, AdminUserInfo::getAvatar)
        );
        String deviceSN = this.getDeviceSN(projectId);
        cascadeRequest.setDeviceSn(deviceSN);
        cascadeRequest.setRequestTime(System.currentTimeMillis() / 1000);
        cascadeRequest.setProjectName(projectInfo.getProjectName());
        cascadeRequest.setConnectCode(request.getConnectionCode());
        // 申请级联
        EdgeCascadeResponse edgeCascadeResponse = cascadeRemoteService.requestCascade(request.getParentEdgeIp(), cascadeRequest);
        String resultCode = edgeCascadeResponse.getResultCode();
        if (EdgeCascadeEventCodeEnum.REQUEST_SUCCESS.code.equals(resultCode)) {
            request.setCascadeStatus(CascadeStatusConstants.PENDING_AUDIT);
        } else if (EdgeCascadeEventCodeEnum.REQUEST_SUCCESS_HAS_CASCADE.code.equals(resultCode)) {
            request.setCascadeStatus(CascadeStatusConstants.CASCADE);
        } else if (EdgeCascadeEventCodeEnum.REQUEST_FAILED_CODE_ERROR.code.equals(resultCode)) {
            return R.failed("提交级联申请失败，级联码错误");
        } else if (EdgeCascadeEventCodeEnum.REQUEST_FAILED_MQTT.code.equals(resultCode)) {
            return R.failed("提交级联申请失败，MQTT账号创建失败");
        } else if (EdgeCascadeEventCodeEnum.REQUEST_FAILED_MISSING_PARAMETERS.code.equals(resultCode)) {
            return R.failed("提交级联申请失败，缺少必要参数");
        }
        this.pubSubCfg(new SubCfgInfo("0"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 进行桥接配置
        this.configBridging(request);
        // 级联要清空驱动社区ID配置（可能会在入云的时候配置）
        this.confDriverCommunityId(new DriverManagerReq(""));

        ObjectMapper objectMapper = ObjectMapperUtil.instance();
        if (StrUtil.isNotEmpty(edgeCascadeResponse.getData())) {
            try {
                ObjectNode dataNode = objectMapper.readValue(edgeCascadeResponse.getData(), ObjectNode.class);
                JsonNode thirdpartyProjectId = dataNode.findPath("projectCode");
                JsonNode projectInfoNode = dataNode.findPath("projectInfo");
                if (!projectInfoNode.isMissingNode()) {
                    log.warn("获取到级联主边缘网关项目信息：{}", projectInfoNode);
                    ProjectInfoVo projectInfoVo = objectMapper.readValue(projectInfoNode.asText(), ProjectInfoVo.class);
                    projectThirdpartyInfoService.saveOrUpdateThirdpartyInfo(projectInfoVo, projectId, request.getRequestId());
                }
                if (!thirdpartyProjectId.isMissingNode()) {
                    // 这里将主边缘网关的项目UUID存到项目信息表的第三方编号字段中
                    projectInfoService.updateProjectCode(projectId, thirdpartyProjectId.asText());
                } else {
                    log.warn("未获取到级联主边缘网关项目UUID(第三方项目ID)：{}", edgeCascadeResponse);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                request.setCascadeStatus(CascadeStatusConstants.NOT_CASCADE);
                return R.failed("级联申请失败");
            }
        }
        edgeCascadeRequestSlaveAspectService.requestCascadeSaveOrUpdate(request);
//        if (StrUtil.isNotEmpty(request.getRequestId())) {
//            edgeCascadeRequestSlaveService.updatePo(request);
//        } else {
//            edgeCascadeRequestSlaveService.savePo(request);
//        }
        return R.ok("级联申请成功");
    }

    @Override
    public boolean configBridging(EdgeCascadeRequestSlave request) {
        String projectCode = projectInfoService.getProjectUUID(request.getProjectId());
        // 这里配置桥接信息
        BridgingConfInfo bridgingConfInfo = new BridgingConfInfo();
        bridgingConfInfo.setCascadeCode(request.getConnectionCode());
        bridgingConfInfo.setCascadeSwitch(BridgingSwitchConstants.SWITCH_CASCADE);
        bridgingConfInfo.setAddress(request.getParentEdgeIp());
        bridgingConfInfo.setCommunityId(projectCode);
        return cascadeRemoteService.configBridging(bridgingConfInfo);
    }

    @Override
    public R<String> cancelRequest(String requestId) {
        EdgeCascadeRequestSlave requestSlave = edgeCascadeRequestSlaveService.getOne(new LambdaQueryWrapper<EdgeCascadeRequestSlave>().eq(EdgeCascadeRequestSlave::getRequestId, requestId).last("limit 1"));
        Integer projectId = requestSlave.getProjectId();
        ProjectInfo projectInfo = projectInfoService.getById(projectId);
        EdgeCascadeResponse edgeCascadeResponse = null;
        try {
            edgeCascadeResponse = cascadeRemoteService.cancelRequest(projectInfo.getProjectCode(), requestSlave.getParentEdgeIp());
            String resultCode = edgeCascadeResponse.getResultCode();
            if (EdgeCascadeEventCodeEnum.CANCEL_CASCADE_REQUEST_FAILED.code.equals(resultCode)) {
                return R.ok("撤销级联申请失败");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return R.failed("撤销级联申请失败");
        }
        return R.ok("撤销级联申请成功");
    }

    @Override
    public boolean pubSubCfg(SubCfgInfo subCfgInfo) {
        return cascadeRemoteService.pubSubCfg(subCfgInfo);
    }

    @Override
    public boolean confDriverCommunityId(DriverManagerReq req) {
        return cascadeRemoteService.confDriverCommunityId(req);
    }
}
