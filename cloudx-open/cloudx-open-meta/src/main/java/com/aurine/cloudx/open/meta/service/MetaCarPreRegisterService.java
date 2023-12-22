package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectCarPreRegister;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-车辆登记记录管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaCarPreRegisterService {

    /**
     * 新增车辆登记记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectCarPreRegister> save(ProjectCarPreRegister po);

    /**
     * 修改车辆登记记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectCarPreRegister> update(ProjectCarPreRegister po);

    /**
     * 删除车辆登记记录
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
