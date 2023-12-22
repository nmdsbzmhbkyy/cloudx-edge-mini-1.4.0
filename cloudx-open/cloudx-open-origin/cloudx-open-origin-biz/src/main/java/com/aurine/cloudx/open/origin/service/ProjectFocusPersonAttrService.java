package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.entity.ProjectFocusPersonAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 重点人员拓展信息表(ProjectFocusPersonAttr)表服务接口
 *
 * @author 王良俊
 * @since 2020-08-18 09:06:39
 */
public interface ProjectFocusPersonAttrService extends IService<ProjectFocusPersonAttr> {

    /**
     * <p>
     *  保存或更新更新重点人员信息（重点人员信息表和人员信息表都是用personId作为主键）
     * </p>
     *
     * @param focusPersonAttr 重点人员信息po对象
     * @return 更新/保存是否成功
    */
    boolean saveOrUpdateFocusPersonAttrByPersonId(ProjectFocusPersonAttr focusPersonAttr);


    /**
     * <p>
     *  批量保存或更新更新重点人员信息（重点人员信息表和人员信息表都是用personId作为主键）
     * </p>
     *
     * @param focusPersonAttrList 重点人员信息po对象列表
     * @return 更新/保存是否成功
    */
    boolean saveOrUpdateFocusPersonAttrByPersonId(List<ProjectFocusPersonAttr> focusPersonAttrList);

    /**
     * <p>
     *  删除重点人员信息通过personId
     * </p>
     *
     * @param personId 人员id
     * @return 删除是否成功
    */
    boolean removeFocusPersonAttrByPersonId(String personId);

    /**
     * <p>
     *  批量删除重点人员信息通过personId
     * </p>
     *
     * @param personIdList 人员id列表
     * @return 删除是否成功
    */
    boolean removeFocusPersonAttrByPersonId(List<String> personIdList);

    /**
     * <p>
     *  通过人阿玉nid获取到重点人员信息
     * </p>
     *
     * @param personId 人员id
     * @return 重点人员信息(没有返回空对象)
    */
    ProjectFocusPersonAttr getFocusPersonAttrByPersonId(String personId);

}