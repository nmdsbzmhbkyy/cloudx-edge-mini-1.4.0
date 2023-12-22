

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectLabelConfig;
import com.aurine.cloudx.estate.vo.ProjectLabelConfigVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
    IPage<ProjectLabelConfig> findPage(IPage<ProjectLabelConfig> page, ProjectLabelConfigVo projectLabelConfigVo);

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

    
    /** 
     * @description: 根据ID获取标签
     * @param:  labelId
     * @return:  
     * @author cjw
     * @date: 2021/3/19 11:10
     */
    ProjectLabelConfigVo selectByLabelId(String labelId);

    /***
     * 查询项目层级的模板
     * @return
     */
    List<ProjectLabelConfigVo> selectProjectTemplate(String LabelName,Integer projectId);
}
