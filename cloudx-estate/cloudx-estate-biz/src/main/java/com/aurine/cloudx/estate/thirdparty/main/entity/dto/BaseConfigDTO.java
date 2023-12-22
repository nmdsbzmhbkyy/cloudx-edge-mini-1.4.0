package com.aurine.cloudx.estate.thirdparty.main.entity.dto;

import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @description:接口配置信息基类
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-14
 * @Copyright:
 */
@Data
public class BaseConfigDTO implements Serializable {

    /**
     * 对接平台名称，基于枚举字典 PlatformEnum
     */
    private PlatformEnum platform;

    /**
     * 接口的版本枚举
     */
    private VersionEnum version;

    /**
     * 所属项目ID,用于项目级接口配置
     */
    private Integer projectId;


}
