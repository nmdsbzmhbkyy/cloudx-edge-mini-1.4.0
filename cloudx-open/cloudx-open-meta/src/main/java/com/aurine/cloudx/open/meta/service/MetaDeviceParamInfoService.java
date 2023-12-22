package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceParamInfo;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-设备参数
 *
 * @author : zouyu
 * @date : 2022-07-28 10:19:13
 */
public interface MetaDeviceParamInfoService {

    /**
     * 新增设备参数
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectDeviceParamInfo> save(ProjectDeviceParamInfo po);

    /**
     * 修改设备参数
     *
     * @param po
     * @return
     */
    R<ProjectDeviceParamInfo> update(ProjectDeviceParamInfo po);


    /**
     * 删除设备参数
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);
}
