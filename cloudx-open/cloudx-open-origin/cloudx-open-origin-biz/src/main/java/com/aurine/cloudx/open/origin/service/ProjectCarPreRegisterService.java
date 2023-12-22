package com.aurine.cloudx.open.origin.service;


import com.aurine.cloudx.open.origin.entity.ProjectCarPreRegister;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 车辆登记表
 */
public interface ProjectCarPreRegisterService extends IService<ProjectCarPreRegister> {

    Integer countByOff();
}
