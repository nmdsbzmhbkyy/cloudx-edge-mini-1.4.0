package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectPasswd;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-密码信息管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaPasswordInfoService {

    /**
     * 新增密码信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPasswd> save(ProjectPasswd po);

    /**
     * 修改密码信息
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectPasswd> update(ProjectPasswd po);

    /**
     * 删除密码信息
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
