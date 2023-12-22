package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.entity.ProjectPasswd;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 项目密码库
 *
 * @author 王良俊
 * @date 2020-06-04 18:16:17
 */
public interface ProjectPasswdService extends IService<ProjectPasswd> {

    boolean saveOrUpdatePassword(ProjectPasswd projectPasswd);

    /**
     * <p>
     * 根据id获取这个人的所有密码
     * </p>
     *
     * @param personId 人员id
     * @return 密码对象列表
     */
    List<ProjectPasswd> listByPersonId(String personId);

    /**
     * <p>
     * 根据人员id列表获取这个人的所有密码
     * </p>
     *
     * @param personIdList 人员id列表
     * @return 密码对象列表
     */
    List<ProjectPasswd> listByPersonId(List<String> personIdList);

    /**
     * <p>
     * 根据人员id删除其所有密码
     * </p>
     *
     * @param personId 人员id
     * @return 删除是否成功
     */
    boolean removeByPersonId(String personId);


    /**
     * 根据第三方id和项目编号，获取数据
     *
     * @param code
     * @param projectId
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    ProjectPasswd getByCode(String code, int projectId);

}
