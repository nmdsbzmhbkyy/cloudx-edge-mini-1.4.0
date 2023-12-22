package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.ProjectCardHis;
import com.aurine.cloudx.open.origin.entity.ProjectRightDevice;
import com.aurine.cloudx.open.origin.vo.ProjectCardHisVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 卡操作记录
 *
 * @author zy
 * @date 2022-10-18 08:40:49
 */
public interface ProjectCardHisService extends IService<ProjectCardHis> {

    Page<ProjectCardHisVo> pageVo(Page page, ProjectCardHisVo projectCardVo);

    void updateState(ProjectRightDevice projectRightDevice, boolean bool);
}
