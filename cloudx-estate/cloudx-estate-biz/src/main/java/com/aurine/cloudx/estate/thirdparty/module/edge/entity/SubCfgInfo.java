package com.aurine.cloudx.estate.thirdparty.module.edge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>中台桥接配置信息对象</p>
 * @author : 王良俊
 * @date : 2021-12-22 09:27:30
 */
@Data
public class SubCfgInfo {

    /**
     * 开关: 级联下级: 2, 级联上级：0，入云上级：1
     */
    private String cascadeSwitch;

    /**
     * 社区ID （UUID）下级边缘网关的 只有云端和上级边缘网关要配置这个
     */
    private String communityId;

    /**
     * 设备SN 只有云端要配置这个
     */
    private String machineSN;

    /**
     * <p>主边缘网关订阅构造器</p>
     *
     * @param cascadeSwitch 这里一般为0
     * @param communityId 从边缘网关的社区ID
     */
    public SubCfgInfo(String cascadeSwitch, String communityId) {
        this.cascadeSwitch = cascadeSwitch;
        this.communityId = communityId;
    }

    /**
     * <p>从边缘网关构造器</p>
     *
     * @param cascadeSwitch 一般为2
     * @return
     * @author 王良俊
     */
    public SubCfgInfo(String cascadeSwitch) {
        this.cascadeSwitch = cascadeSwitch;
    }
}
