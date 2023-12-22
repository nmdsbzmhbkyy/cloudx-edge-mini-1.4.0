package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.config;

import com.aurine.cloudx.estate.thirdparty.main.entity.dto.BaseConfigDTO;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-10 9:52
 * @Copyright:
 */
@Data
public class AliEdgeConfigDTO extends BaseConfigDTO {

    /**
     * 服务器地址
     */
    private String apiEndPoint;
    /**
     * 版本
     */
    private String apiVersion;
    /**
     * 基础连接地址
     */
    private String apiResUrl;
    /**
     * appKey
     */
    private String appKey;
    /**
     * appSecret
     */
    private String appSecret;
    /**
     * faceUuidPre
     */
    private String faceUuidPre;

    /**
     * 一次性传输的数据大小，默认为100
     */
    private int transferBufferSize;

    private List<AliEdgeSubscribeDTO> subscribe;

}
