package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.ProjectFingerprints;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 记录项目辖区内允许通行的指纹信息，供辖区内已开放通行权限的指纹识别设备下载
 *
 * @author 王良俊
 * @date 2020-05-22 11:20:22
 */
public interface ProjectFingerprintsService extends IService<ProjectFingerprints> {

    /**
     * <p>
     * 根据用户id获取该用户的所有指纹列表
     * </p>
     *
     * @param personId 人员id
     * @return 指纹对象列表
     * @author: 王良俊
     */
    List<ProjectFingerprints> listByPersonId(String personId);


    /**
     * <p>
     * 根据用户id列表获取该列表用户的所有指纹
     * </p>
     *
     * @param personIdList 人员id列表
     * @return 指纹对象列表
     * @author: 王良俊
     */
    List<ProjectFingerprints> listByPersonId(List<String> personIdList);

    /**
     * 根据第三方id和项目编号，获取对象
     *
     * @param code
     * @param projectId
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    ProjectFingerprints getByCode(String code, int projectId);

}
