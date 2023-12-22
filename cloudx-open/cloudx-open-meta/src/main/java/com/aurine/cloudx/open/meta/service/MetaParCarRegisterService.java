package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectParCarRegister;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-车辆登记管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaParCarRegisterService {

    /**
     * 新增车辆登记
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParCarRegister> save(ProjectParCarRegister po);

    /**
     * 修改车辆登记
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParCarRegister> update(ProjectParCarRegister po);

    /**
     * 删除车辆登记
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
