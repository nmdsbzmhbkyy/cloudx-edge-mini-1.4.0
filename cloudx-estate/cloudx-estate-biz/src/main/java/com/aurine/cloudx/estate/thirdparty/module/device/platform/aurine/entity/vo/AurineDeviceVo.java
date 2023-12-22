package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.vo;

import lombok.Data;

/**
 * <p>设备VO</p>
 *
 * @ClassName: AurineDeviceVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-17 10:37
 * @Copyright:
 */
@Data
public class AurineDeviceVo {

    /**
     * sn
     */
    private String devsn;

    /**
     * mac
     */
    private String mac;

    /**
     * 产品型号，门禁固定020302
     */
    private String modelid;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 社区ID,既projectId
     */
    private String communityId;

    public void setCommunityId(Integer communityId) {
        this.communityId = "S"+communityId.toString();
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

}
