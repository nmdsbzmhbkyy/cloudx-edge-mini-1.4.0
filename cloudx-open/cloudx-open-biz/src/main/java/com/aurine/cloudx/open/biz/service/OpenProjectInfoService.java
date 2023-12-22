package com.aurine.cloudx.open.biz.service;

import com.aurine.cloudx.open.common.entity.vo.ProjectInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * open平台-项目信息
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface OpenProjectInfoService {

    /**
     * 通过id查询项目
     *
     * @param id
     * @return
     */
    R<ProjectInfoVo> getById(String id);

    /**
     * 通过projectUUID（项目UUID）查询项目
     *
     * @param projectUUID
     * @return
     */
    R<ProjectInfoVo> getByProjectUUID(String projectUUID);

    /**
     * 分页查询项目
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<ProjectInfoVo>> page(Page page, ProjectInfoVo vo);

    /**
     * 平台侧获取已入云的项目
     * （平台侧调用）
     *
     * @return
     */
    R<List<ProjectInfoVo>> listCascadeByCloud();

    /**
     * 边缘侧获取已入云的项目
     * （边缘侧调用）
     *
     * @return
     */
    R<List<ProjectInfoVo>> listCascadeByEdge();

    /**
     * 主边缘侧获取已级联的项目
     * （主边缘侧调用）
     *
     * @return
     */
    R<List<ProjectInfoVo>> listCascadeByMaster();

    /**
     * 从边缘侧获取已级联的项目
     * （从边缘侧调用）
     *
     * @return
     */
    R<List<ProjectInfoVo>> listCascadeBySlave();

}
