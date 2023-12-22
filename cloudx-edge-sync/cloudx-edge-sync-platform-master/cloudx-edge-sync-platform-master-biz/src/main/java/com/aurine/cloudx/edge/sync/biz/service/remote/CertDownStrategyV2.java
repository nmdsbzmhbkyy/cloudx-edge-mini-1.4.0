package com.aurine.cloudx.edge.sync.biz.service.remote;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategy;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategyFactory;
import com.aurine.cloudx.edge.sync.common.constant.PublicConstants;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.estate.cert.remote.RemoteCertAdownService;
import com.aurine.cloudx.estate.cert.vo.CertAdownRequestVO;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zy
 * @Date: 2023-10-23 10:49:04
 * @Package: com.aurine.cloudx.edge.sync.biz.service.remote
 * @Version: 2.0
 * @Remarks: 凭证下发 之前是command指令，数据量过大会有时序的问题，改为operate同步
 **/
@Slf4j
@Component
public class CertDownStrategyV2 implements BaseStrategy {

    @Resource
    private RemoteCertAdownService remoteCertAdownService;


    @Override
    public OpenRespVo doAdd(OpenApiModel<JSONObject> requestObj) throws Exception {
        // 调用下发服务
        List<CertAdownRequestVO> certAdownRequestVOList = JSONObject.parseArray(requestObj.getData().getString("certData"), CertAdownRequestVO.class);
        try {
            Object r = remoteCertAdownService.requestList(certAdownRequestVOList, PublicConstants.FROM_IN);
            R res = new R();
            BeanUtil.copyProperties(r, res);
            return new OpenRespVo(null, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public OpenRespVo doUpdate(OpenApiModel<JSONObject> requestObj, String uuid) throws Exception {
        return null;
    }

    @Override
    public OpenRespVo doDelete(String appId, String projectUUID, Integer tenantId, String id) throws Exception {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BaseStrategyFactory.registerStrategy(ServiceNameEnum.CERT_DOWN.name, this);
    }
}
