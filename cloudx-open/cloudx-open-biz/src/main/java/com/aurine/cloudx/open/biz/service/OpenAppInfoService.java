package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.entity.OpenAppInfo;
import com.aurine.cloudx.open.common.entity.vo.OpenAppInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * 开放平台应用信息
 *
 * @author : Qiu
 * @date : 2022 01 20 11:01
 */

public interface OpenAppInfoService extends IService<OpenAppInfo> {

    /**
     * 通过id查询应用信息
     *
     * @param id
     * @return
     */
    R<OpenAppInfoVo> getById(String id);

    /**
     * 通过appId（应用id）查询应用信息
     *
     * @param appId
     * @return
     */
    R<OpenAppInfoVo> getByAppId(String appId);

}
