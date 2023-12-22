package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectEpidemicEvent;
import com.aurine.cloudx.estate.vo.ProjectEpidemicEventVo;
import com.aurine.cloudx.estate.vo.ProjectEventVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 疫情记录
 *
 * @author 邹宇
 * @date 2021-6-7 11:08:18
 */
public interface ProjectEpidemicEventService extends IService<ProjectEpidemicEvent> {

    /**
     * 统计健康码红码的数量
     *
     * @author: 邹宇
     */
    Integer countRedCode();


    /**
     * 分页查询
     *
     * @param projectEpidemicEventVo
     * @return
     */
    Page<ProjectEpidemicEvent> pageEpidemicEvent(ProjectEpidemicEventVo projectEpidemicEventVo);

    void saveEpidemicEvent(ProjectEventVo eventVo, String temperature);
}
