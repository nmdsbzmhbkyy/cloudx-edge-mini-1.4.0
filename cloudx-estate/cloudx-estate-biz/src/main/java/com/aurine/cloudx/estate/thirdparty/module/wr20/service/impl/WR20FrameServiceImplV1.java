package com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.entity.ProjectHouseDesign;
import com.aurine.cloudx.estate.service.ProjectHouseDesignService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.WR20FrameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04
 * @Copyright:
 */
@Service
@Slf4j
public class WR20FrameServiceImplV1 implements WR20FrameService {

    @Resource
    private WR20RemoteService wr20RemoteService;
    @Resource
    private ProjectHouseDesignService projectHouseDesignService;

    /**
     * 同步组团、楼栋、单元等信息
     *
     * @param projectId WR20的ID号
     * @return
     */
    @Override
    public boolean syncFrame(int projectId) {
        //检查是否存在户型
        ProjectHouseDesign projectHouseDesign = projectHouseDesignService.getTopOne(projectId);
        if (projectHouseDesign == null) {
            throw new RuntimeException("请先添加户型");
        }

        HuaweiRespondDTO respondDTO = wr20RemoteService.syncFrame(projectId, new JSONObject());
        log.info("[WR20] 同步楼栋信息：{}", respondDTO);

        if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
            return true;
        } else {
            log.error("[WR20] 同步楼栋信息失败：{}", respondDTO.getErrorMsg());
            throw new RuntimeException("同步楼栋信息失败:" + respondDTO.getErrorMsg());
        }
    }

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }

}
