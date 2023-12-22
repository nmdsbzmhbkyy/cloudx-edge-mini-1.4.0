

package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.ProjectPersonLabel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 人员标签
 *
 * @author 王伟
 * @date 2020-05-13 10:42:38
 */
public interface ProjectPersonLabelService extends IService<ProjectPersonLabel> {

    /**
     * 添加人员标签
     * @param labelUidArray
     * @param personId
     * @return
     */
    boolean addLabel(String[] labelUidArray, String personId);

    /**
     * 通过人员ID获取人员标签列表
     * @param personId
     * @return
     */
    List<ProjectPersonLabel> listByPersonId(String personId);

}
