package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectRightDeviceCache;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 凭证信息缓存
 * @author:zy
 * @data:2023/4/26 9:22 上午
 */
public interface ProjectRightDeviceCacheService extends IService<ProjectRightDeviceCache> {

    /**
     * 保存凭证信息缓存
     * *
     * @param deviceInfo
     * @param passNo
     * @param state
     * @param type
     */
    void saveRightDeviceCache(ProjectDeviceInfo deviceInfo, String passNo, String state,String type);


    /**
     * 查询凭证信息缓存 (在两个表都存在的数据并更新)
     * *
     * */
    List<ProjectRightDeviceCache> getRightDeviceCache();
}
