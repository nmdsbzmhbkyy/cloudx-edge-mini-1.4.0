package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectInspectCheckinDetail;
import com.aurine.cloudx.estate.vo.ProjectInspectCheckinDetailVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 巡检点签到明细(ProjectInspectCheckinDetail)表服务接口
 *
 * @author 王良俊
 * @since 2020-08-04 10:08:52
 */
public interface ProjectInspectCheckinDetailService extends IService<ProjectInspectCheckinDetail> {

    /**
     * <p>
     * 根据任务明细id列表删除其签到详情记录
     * </p>
     *
     * @param detailIdList 任务明细id列表
     * @return 删除结果
     */
    boolean removeByDetailIdList(List<String> detailIdList);

    /**
     * <p>
     * 巡检点签到
     * </p>
     *
     * @param checkinDetailVo 任务巡检点签到明细vo对象
     * @return 是否签到成功
     */
    boolean checkin(ProjectInspectCheckinDetailVo checkinDetailVo);

}