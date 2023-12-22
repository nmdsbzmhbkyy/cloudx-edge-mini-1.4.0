package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.ProjectOpenLaneHis;
import com.aurine.cloudx.open.origin.vo.OpenLaneHisQueryVo;
import com.aurine.cloudx.open.origin.vo.ProjectOpenLaneHisVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * <p>
 * 车道开闸记录service
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/27 10:45
 */
public interface ProjectOpenLaneHisService extends IService<ProjectOpenLaneHis> {

    /**
     * <p>
     * 分页查询车辆开闸记录
     * </p>
     *
     * @param query 查询条件
     * @author: 王良俊
     * @return 开闸记录分页数据
     */
    R<IPage<ProjectOpenLaneHisVo>> fetchList(Page page, OpenLaneHisQueryVo query);

    /**
     * <p>
     * 根据车场ID、车道ID更新对应手动开闸记录的抓拍图片
     * </p>
     *
     * @param
     * @author: 王良俊
     * @return
     */
    void updateImage(String parkId, String laneId, String localImageUrl);
}
