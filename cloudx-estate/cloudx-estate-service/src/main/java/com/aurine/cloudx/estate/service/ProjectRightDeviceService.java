package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.vo.CertVo;
import com.aurine.cloudx.estate.vo.ProjectRightDeviceOptsAccessSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectRightDeviceOptsAccessVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 权限设备关系表，记录权限（认证介质）的下发状态
 *
 * @author 王良俊
 * @date 2020-05-21 09:52:28
 */
public interface ProjectRightDeviceService extends IService<ProjectRightDevice> {


    /**
     * 根据凭证id列表、设备id和状态 修改凭证的下载状态
     * 与项目id不关联，
     *
     * @param certIdList
     * @param statusEnum
     * @return
     */
    boolean updateStateByIds(List<String> certIdList, String deviceId, PassRightCertDownloadStatusEnum statusEnum);


    List<CertVo> listCertmediaVoByPersonId(String personId);

    /**
     * <p>
     * 根据设备id插叙你设备权限记录
     * </p>
     *
     * @param deviceId 设备id
     * @param query    查询条件
     * @param page     分页对象
     * @return 分页数据
     * @author: 王良俊
     */
    Page<ProjectRightDeviceOptsAccessVo> pageByDeviceId(Page page, ProjectRightDeviceOptsAccessSearchConditionVo query, String deviceId);


}
