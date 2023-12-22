package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceAbnormal;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceAbnormalSearchCondition;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>设备异常事件记录服务</p>
 *
 * @author : 王良俊
 * @date : 2021-09-26 13:50:28
 */
public interface ProjectDeviceAbnormalService extends IService<ProjectDeviceAbnormal> {

    /**
     * <p>分页查询</p>
     *
     * @param page      分页参数
     * @param condition 查询条件
     * @return 分页数据
     * @author: 王良俊
     */
    Page<ProjectDeviceAbnormal> fetchList(Page page, ProjectDeviceAbnormalSearchCondition condition);

//    /**
//     * <p>
//     * 设备异常处理方法
//     * 如果设备存在异常则新增或是更新异常记录，如果设备不存在异常则删除异常记录
//     * </p>
//     *
//     * @param abnormalHandleInfo 带有设备异常判断所需要的数据
//     * @author: 王良俊
//     */
//    void checkAbnormal(DeviceAbnormalHandleInfo abnormalHandleInfo);

    /**
     * <p>根据设备ID删除异常记录</p>
     *
     * @param deviceId 设备ID
     * @author: 王良俊
     */
    void removeAbnormalByDeviceId(String deviceId);

}
