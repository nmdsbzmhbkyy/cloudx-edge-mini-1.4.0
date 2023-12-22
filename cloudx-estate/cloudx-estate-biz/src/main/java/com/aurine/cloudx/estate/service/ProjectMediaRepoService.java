

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectMediaRepo;

import com.aurine.cloudx.estate.vo.ProjectMediaRepoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * 项目媒体资源库
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:35:57
 */
public interface ProjectMediaRepoService extends IService<ProjectMediaRepo> {

    final String BUCKET_NAME = "saas-res-project";

    Page getList(Page page, ProjectMediaRepoVo projectMediaRepo);

    List<ProjectMediaRepo> queryObj(String id);


    /**
     * <p>根据广告ID获取所有广告资源列表</p>
     *
     * @param adSeq 广告自增序列
     * @return 广告资源列表
     * @author: 王良俊
     */
    List<ProjectMediaRepo> listMediaRepoByAdSeq(Long adSeq);

    /**
     * <p>根据ID删除媒体资源</p>
     *
     * @param repoId 资源ID
     * @return 是否删除成功
     * @author: 王良俊
     */
    boolean removeResource(String repoId);

    /**
     * 根据资源id获取资源信息
     * @param projectBroadcastTaskVo
     * @return
     */


}
