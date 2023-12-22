package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.entity.ProjectCarPreRegister;
import com.aurine.cloudx.open.origin.mapper.ProjectCarPreRegisterMapper;
import com.aurine.cloudx.open.origin.service.ProjectCarPreRegisterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 车辆登记表
 */
@Service
public class ProjectCarPreRegisterServiceImpl extends ServiceImpl<ProjectCarPreRegisterMapper, ProjectCarPreRegister> implements ProjectCarPreRegisterService {

    @Override
    public Integer countByOff() {
        return baseMapper.countByOff();
    }
}
