package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config;

import com.aurine.cloudx.estate.thirdparty.main.entity.dto.BaseConfigDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-14
 * @Copyright:
 */
@Data
public class AurineEdgeConfigDTO extends BaseConfigDTO {

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
     * visitorFaceUuidPre
     */
    private String visitorFaceUuidPre;

    /**
     * 一次性传输的数据大小，默认为100
     */
    private int transferBufferSize;

    private List<AurineEdgeSubscribeDTO> subscribe;

}
