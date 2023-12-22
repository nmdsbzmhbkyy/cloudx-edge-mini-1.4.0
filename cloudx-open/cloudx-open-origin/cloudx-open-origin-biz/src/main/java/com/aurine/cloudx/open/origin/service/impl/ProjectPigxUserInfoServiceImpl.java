package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.entity.PigxxUserInfo;
import com.aurine.cloudx.open.origin.mapper.ProjectPigxUserMapper;
import com.aurine.cloudx.open.origin.service.ProjectPigxUserInfoService;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProjectPigxUserInfoServiceImpl implements ProjectPigxUserInfoService {

    @Resource
    ProjectPigxUserMapper projectPigxUserMapper;

    @Override
    public PigxxUserInfo getCurrentUserInfo() {
        PigxUser user = SecurityUtils.getUser();
        return projectPigxUserMapper.getUserInfo(user.getId());
    }
}
