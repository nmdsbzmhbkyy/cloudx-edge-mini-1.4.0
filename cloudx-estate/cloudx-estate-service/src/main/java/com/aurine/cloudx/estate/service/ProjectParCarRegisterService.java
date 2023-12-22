package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectParCarRegister;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterRecordVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterSeachConditionVo;
import com.aurine.cloudx.estate.vo.ProjectParCarRegisterVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 车辆登记
 *
 * @author 王伟
 * @date 2020-07-08 14:10:32
 */
public interface ProjectParCarRegisterService extends IService<ProjectParCarRegister> {

    /**
     * 分页查询信息
     *
     * @param searchConditionVo
     * @return
     */
    Page<ProjectParCarRegisterRecordVo> pageCarRegister(Page<ProjectParCarRegisterRecordVo> page, @Param("query") ProjectParCarRegisterSeachConditionVo searchConditionVo);


    /**
     * 更新、车辆、以及新的人员信息
     *
     * @param parCarRegisterVo
     * @return
     */
    boolean updateCarRegister(ProjectParCarRegisterVo parCarRegisterVo);


    /**
     * 通过id获取注册信息VO,包含车辆信息、人员信息、车位信息、缴费信息等
     *
     * @param id
     * @return
     */
    ProjectParCarRegisterVo getVo(String id);

    /**
     * <p>
     * 判断车位是否已被登记
     * </p>
     *
     * @param placeId 车位id
     * @return 处理结果
     */
    boolean checkHasRegister(String placeId);


}
