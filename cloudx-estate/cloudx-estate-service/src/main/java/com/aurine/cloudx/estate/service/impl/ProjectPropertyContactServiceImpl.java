package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectPropertyContact;
import com.aurine.cloudx.estate.mapper.ProjectPropertyContactMapper;
import com.aurine.cloudx.estate.service.ProjectPropertyContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 项目物业联系方式表(ProjectPropertyContact)表服务实现类
 *
 * @author xull
 * @version 1.0.0
 * @date 2020-10-27 15:38:49
 */
@Service("projectPropertyContactService")
public class ProjectPropertyContactServiceImpl extends ServiceImpl<ProjectPropertyContactMapper, ProjectPropertyContact> implements ProjectPropertyContactService {

}