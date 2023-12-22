package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.vo.ProjectFaceResourceAppPageVo;
import com.aurine.cloudx.estate.vo.ProjectFaceResourcesAppVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * 项目人脸库，用于项目辖区内的人脸识别设备下载
 *
 * @author 王良俊
 * @date 2020-05-22 11:21:06
 */
public interface ProjectFaceResourcesService extends IService<ProjectFaceResources> {

    /**
     * <p>
     * 根据用户id获取该用户的所有人脸列表
     * </p>
     *
     * @param personId 人员ID
     * @return 人脸对象列表
     * @author: 王良俊
     */
    List<ProjectFaceResources> listByPersonId(String personId);

    /**
     * <p>
     * 根据用户id列表获取该列表用户的所有人脸数据
     * </p>
     *
     * @param personIdList 人员ID列表
     * @return 人脸对象列表
     * @author: 王良俊
     */
    List<ProjectFaceResources> listByPersonId(List<String> personIdList);

    /**
     * <p>
     * 根据用户id更新人脸数据
     * </p>
     *
     * @param faceIdList 人脸ID
     * @param personId   用户ID
     * @return 处理结果
     */
    boolean updateFacesBatch(String personId, List<String> faceIdList);

    /**
     * <p>
     * 根据用户id删除人脸数据
     * </p>
     *
     * @param personId 用户ID
     * @return 处理结果
     */
    boolean removeByPersonId(String personId);

    /**
     * <p>
     * 根据传入的人脸列表和访客id判断哪些被删除或添加并进行对应操作
     * </p>
     *
     * @param faceList  人脸对象列表
     * @param visitorId 访客ID
     * @author: 王良俊
     */
    void operateFace(List<ProjectFaceResources> faceList, String visitorId);

    /**
     * 根据第三方id和项目编号，获取对象
     *
     * @param code
     * @param projectId
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    ProjectFaceResources getByCode(String code, int projectId);
    /**
     * 根据FaceId和项目编号，获取对象
     *
     * @param faceId
     * @param projectId
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    ProjectFaceResources getByFaceId(String faceId, int projectId);

    /**
     * 根据用户id，获取用户的第一张面部识别照片
     * <p>
     * 当用户不存在人脸，返回empty对象
     * 当用户存在多张人脸，且不存在微信上传的人脸时，返回最新上传的头像
     * 当用户存在多张人脸，存在微信上传的人脸时，返回微信上传的人脸
     * 当用户正在下发人脸, 根据来源获取人脸的下载状态，并返回正在下载人脸的url地址
     * <p>
     * 必填：url\personId\personType
     *
     * @param personId
     * @return
     * @author: 王伟
     * @since 2020-09-09
     */
    ProjectFaceResourcesAppVo getByPersonIdForApp(String personId, String personType);



    /**
     * 业主人脸分页查询
     * @param page
     * @return
     */
    Page<ProjectFaceResourceAppPageVo> pagePersonFace(Page page, String dlStatus);
}


