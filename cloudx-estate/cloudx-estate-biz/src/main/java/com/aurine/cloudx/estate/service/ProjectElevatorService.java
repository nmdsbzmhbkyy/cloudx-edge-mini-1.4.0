package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * 电梯设备
 *
 * @author 王良俊
 * @since 2022/2/11 10:13
 */
public interface ProjectElevatorService extends IService<ProjectDeviceInfo> {

    /**
     * <p>分页查询电梯</p>
     *
     * @param page 分页信息
     * @param elevatorPageFormVo 查询条件
     * @return 分页数据
     */
    Page<ProjectElevatorDeviceVo> pageElevator(Page page, ElevatorPageFormVo elevatorPageFormVo);

    /**
     * <p>分页查询电梯分层控制器</p>
     *
     * @param page 分页信息
     * @param formVo 查询条件
     * @return 分页数据
     */
    Page<ProjectLayerDeviceInfoVo> pageLayerDevice(Page page, String unitId, ProjectDeviceInfoPageFormVo formVo);

    /**
     * <p>分页查询乘梯识别终端</p>
     *
     * @param page 分页参数
     * @param unitId 单元ID
     * @param formVo 前端查询条件
     * @return 分页数据
     */
    Page<ProjectDeviceInfoVo> pageRecognizerControlDevice(Page page, String unitId, ProjectDeviceInfoPageFormVo formVo);

    /**
     * <p>创建电梯</p>
     *
     * @param formVo 电梯
     * @return 创建结果
     */
    R creatElevator(ProjectElevatorFormVo formVo);

    /**
     * <p>获取电梯信息</p>
     *
     * @param deviceId 电梯ID
     * @return 电梯信息对象
     */
    ProjectElevatorFormVo getElevatorById(String deviceId);

    /**
     * <p>更新电梯信息</p>
     *
     * @param data 电梯数据
     */
    R updateElevatorById(ProjectElevatorFormVo data);

    /**
     * <p>删除电梯</p>
     *
     * @param deviceId 设备ID
     */
    R removeElevatorById(String deviceId);

    /**
     * <p>根据电梯ID获取分层控制器列表</p>
     *
     * @param deviceId 电梯ID
     * @return 分层控制器列表
     */
    List<ProjectDeviceInfoVo> getLayerControlDeviceByElevatorId(String deviceId);

    /**
     * <p>根据电梯ID获取乘梯识别终端列表</p>
     *
     * @param deviceId 电梯ID
     * @return 乘梯识别终端列表
     */
    List<ProjectDeviceInfoVo> getRecognizerDeviceByElevatorId(String deviceId);

    /**
     * 获取电梯的分层控制器配置
     * @param deviceId 电梯id
     * @return 电梯的分层控制器的所有配置参数
     */
    DeviceParamDataVo getRecognizerDeviceParamByElevatorId(String deviceId);
}
