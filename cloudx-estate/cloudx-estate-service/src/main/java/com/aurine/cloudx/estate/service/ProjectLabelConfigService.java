

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectLabelConfig;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 标签管理
 *
 * @author 王伟
 * @date 2020-05-07 08:09:35
 */
public interface ProjectLabelConfigService extends IService<ProjectLabelConfig> {
    /**
     * 查询标签分页
     * @param page
     * @param labelName
     * @return
     */
    IPage<ProjectLabelConfig> findPage(IPage<ProjectLabelConfig> page, String labelName);

    /**
     * <p>
     *  保存标签
     * </p>
     *
     * @param
     * @return
     * @throws
     */
    boolean saveLabel(ProjectLabelConfig projectLabelConfig);

    /**
     * 系统预设标签 【欠费业主，孤寡老人，困难人员】
     * @return
     */
    boolean initLabel(Integer projectId,Integer tenantId);
}
