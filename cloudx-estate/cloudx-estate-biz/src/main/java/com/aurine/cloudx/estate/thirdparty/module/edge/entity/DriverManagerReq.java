package com.aurine.cloudx.estate.thirdparty.module.edge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>中台驱动主社区配置对象</p>
 *
 * @author 王良俊
 * @date "2022/4/12"
 */
@Data
@NoArgsConstructor
public class DriverManagerReq {

    /*
     * 社区ID 非级联项目的项目ID，每台边缘网关最多只有一个这样的项目
     **/
    private String communityId;

    /*
     * cascade 暂时无用
     **/
    private String cascade;

    public DriverManagerReq(String communityId) {
        this.communityId = communityId;
    }
}
