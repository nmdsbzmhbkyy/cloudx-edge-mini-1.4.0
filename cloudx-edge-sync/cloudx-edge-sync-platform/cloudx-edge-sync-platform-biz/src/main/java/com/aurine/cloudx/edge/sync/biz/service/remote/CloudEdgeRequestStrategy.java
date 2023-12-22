package com.aurine.cloudx.edge.sync.biz.service.remote;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.service.UuidRelationService;
import com.aurine.cloudx.edge.sync.common.config.CommandStrategy;
import com.aurine.cloudx.edge.sync.common.config.CommandStrategyFactory;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.entity.po.UuidRelation;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.api.inner.feign.RemoteCascadeCloudEdgeRequestService;
import com.aurine.cloudx.open.common.core.constant.enums.CommandTypeEnum;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.CloudEdgeRequest;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 边缘网关入云申请
 *
 * @author：zouyu
 * @data: 2022/3/24 10:00
 */

@Slf4j
@Component
public class CloudEdgeRequestStrategy implements CommandStrategy {

    @Resource
    private RemoteCascadeCloudEdgeRequestService remoteCascadeCloudEdgeRequestService;

    @Resource
    private UuidRelationService uuidRelationService;


    @Override
    public OpenRespVo doHandler(TaskInfoDto taskInfoDto) {
        log.info("清空指令 -> openApi send ：{}", JSONObject.toJSONString(taskInfoDto));
        CloudEdgeRequest cloudEdgeRequest = JSONObject.parseObject(taskInfoDto.getData(), CloudEdgeRequest.class);
        OpenApiHeader openApiHeader = getOpenApiHeader(taskInfoDto, cloudEdgeRequest);
        log.info("清空指令 -> openApiHeader:{}", JSONObject.toJSONString(openApiHeader));
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(cloudEdgeRequest);
        openApiModel.setHeader(openApiHeader);
        R r = remoteCascadeCloudEdgeRequestService.update(openApiModel);
        log.info("清空指令 -> openApi result ：{}", JSONObject.toJSONString(r));
        if (r.getCode() != 0) {
            return new OpenRespVo(null, r);
        }
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(r.getData()));
        String id = object.getString("requestId");

        //清空uuid关系
        uuidRelationService.remove(Wrappers.lambdaQuery(UuidRelation.class).eq(UuidRelation::getProjectUUID,taskInfoDto.getProjectUUID()));
        return new OpenRespVo(id, r);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CommandStrategyFactory.registerStrategy(CommandTypeEnum.EMPTY.name, this);
    }

    private OpenApiHeader getOpenApiHeader(TaskInfoDto taskInfoDto, CloudEdgeRequest cloudEdgeRequest) {
        OpenApiHeader openApiHeader = new OpenApiHeader();
        openApiHeader.setAppId(Constants.appId);
        openApiHeader.setTenantId(1);
        openApiHeader.setProjectId(cloudEdgeRequest.getProjectId());
        openApiHeader.setProjectUUID(taskInfoDto.getProjectUUID());
        return openApiHeader;
    }
}
