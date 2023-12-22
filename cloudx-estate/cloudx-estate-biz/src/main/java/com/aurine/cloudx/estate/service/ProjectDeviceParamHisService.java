package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.vo.ProjectDeviceParamHisPageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamHis;

import java.util.List;

/**
 * 记录项目设备参数配置的历史记录(ProjectDeviceParamHis)表服务接口
 *
 * @author makejava
 * @since 2020-12-23 09:31:51
 */
public interface ProjectDeviceParamHisService extends IService<ProjectDeviceParamHis> {

    /**
     * 设备参数信息分页查询
     *
     * @param page
     * @param projectDeviceParamHisPageVo
     * @return
     */
    Page<ProjectDeviceParamHisPageVo> pageHis(Page page, ProjectDeviceParamHisPageVo projectDeviceParamHisPageVo);

    /**
     * <p>
     * 添加设备参数设置失败的记录
     * </p>
     *
     * @param deviceInfoList 设备信息对象集合
     * @author: 王良俊
     */
    void addFailedParamHis(List<ProjectDeviceInfo> deviceInfoList, Integer projectId);

    /**
     * <p>
     * 添加设备参数设置失败的记录（这些设备必须是同产品的）
     * </p>
     *
     * @param deviceInfoList 设备信息对象集合
     * @author: 王良俊
     */
    void addSuccessParamHis(List<ProjectDeviceInfo> deviceInfoList, Integer projectId);

    /**
     * <p>
     * 这里要更新设备参数配置历史（重配成功）
     * </p>
     *
     * @param deviceIdList 设备ID集合
     * @author: 王良俊
     */
    void updateSuccessParamHis(List<String> deviceIdList, Integer projectId);
}