package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config;

import com.aurine.cloudx.estate.thirdparty.main.entity.dto.BaseConfigDTO;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-14
 * @Copyright:
 */
@Data
public class AurineConfigDTO extends BaseConfigDTO {

    /**
     * URL地址
     */
    private String apiEndPoint;

    /**
     * appKey
     */
    private String appKey;
    /**
     * appSecret
     */
    private String appSecret;
    /**
     * appType
     */
    private String appType;
    /**
     * userName
     */
    private String userName;
    /**
     * 接口是否为项目级
     */
    private boolean useByProject;

    /**
     * 散列主题
     */
    private boolean hashTopic;

    /**
     * 散列数量
     */
    private int hashCount;

    /**
     * 一次性传输的数据大小，默认为100
     */
    private String transferBufferSize;

    private List<AurineSubscribeDTO> subscribe;

    public boolean getUseByProject() {
        return this.useByProject;
    }

    public boolean getHashTopic() {
        return this.hashTopic;
    }


}
