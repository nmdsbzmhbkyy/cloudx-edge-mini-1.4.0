package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectCardHis;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.vo.ProjectCardHisVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 卡操作记录
 *
 * @author zy
 *
 * @date 2022-10-18 08:40:49
 */
public interface ProjectCardHisService extends IService<ProjectCardHis> {

    Page<ProjectCardHisVo> pageVo(Page page, ProjectCardHisVo projectCardVo);

    void updateState(ProjectRightDevice projectRightDevice, boolean bool);

    void handleCardHis();

}

