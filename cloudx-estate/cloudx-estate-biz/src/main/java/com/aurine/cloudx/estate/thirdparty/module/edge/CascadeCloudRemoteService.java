package com.aurine.cloudx.estate.thirdparty.module.edge;

import com.aurine.cloudx.estate.thirdparty.module.edge.entity.DriverManagerReq;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.BridgingConfInfo;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.SubCfgInfo;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>入云级联remote服务</p>
 * @author : 王良俊
 * @date : 2021-12-27 14:00:12
 */
public interface CascadeCloudRemoteService {

    ObjectMapper objectMapper = ObjectMapperUtil.instance();

    /**
     * <p>获取设备SN</p>
     *
     * @return 设备SN
     */
    String getDeviceSN();


    /**
     * <p>配置桥接</p>
     *
     * @param bridgingConfInfo 桥接配置信息
     * @return 是否配置桥接成功
     */
    boolean configBridging(BridgingConfInfo bridgingConfInfo);

    /**
     * <p>发布订阅配置-就是中台的订阅主题</p>
     *
     * @param subCfgInfo 订阅配置信息
     * @return 是否发布订阅信息成功
     */
    boolean pubSubCfg(SubCfgInfo subCfgInfo);

    /**
     * <p>设置主项目UUID（非级联项目）</p>
     *
     * @param req 中台驱动主社区配置对象
     * @return 是否配置成功
     */
    boolean confDriverCommunityId(DriverManagerReq req);
}
