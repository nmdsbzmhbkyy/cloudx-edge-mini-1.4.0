package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.common.entity.vo.PasswordInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectPasswd;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 项目密码库
 *
 * @author 王良俊
 * @date 2020-06-04 18:16:17
 */
public interface ProjectPasswdService extends IService<ProjectPasswd> {


    /**
     * <p>
     * 根据传入的密码列表和访客id判断哪些被删除或添加并进行对应操作
     * </p>
     *
     * @param passwordList 密码对象列表
     * @param visitorId    访客id
     * @author: 王良俊
     */
    void operatePassword(List<ProjectPasswd> passwordList, String visitorId);

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

    /**
     * 分页查询
     *
     * @param page
     * @param vo
     * @return
     */
    Page<PasswordInfoVo> page(Page page, PasswordInfoVo vo);

}
