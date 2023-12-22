package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectRightDeviceCache;
import com.aurine.cloudx.estate.mapper.ProjectRightDeviceCacheMapper;
import com.aurine.cloudx.estate.service.ProjectRightDeviceCacheService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 凭证信息缓存
 * @author:zy
 * @data:2023/4/26 9:22 上午
 */
@Service
@Slf4j
public class ProjectRightDeviceCacheServiceImpl  extends ServiceImpl<ProjectRightDeviceCacheMapper, ProjectRightDeviceCache> implements ProjectRightDeviceCacheService {


    @Override
    public void saveRightDeviceCache(ProjectDeviceInfo deviceInfo, String passNo, String state,String type) {
        if(StrUtil.isEmpty(passNo) || StrUtil.isEmpty(state)){
            return;
        }
        ProjectRightDeviceCache cache = new ProjectRightDeviceCache();
        cache.setDeviceId(deviceInfo.getDeviceId());
        cache.setDeviceThirdCode(deviceInfo.getThirdpartyCode());
        cache.setPassNo(passNo);
        cache.setState(state);
        cache.setType(type);
        this.save(cache);
    }

    @Override
    public List<ProjectRightDeviceCache> getRightDeviceCache() {
        return baseMapper.getRightDeviceCache();
    }
}
