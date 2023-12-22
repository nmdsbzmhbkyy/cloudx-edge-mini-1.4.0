

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目
 *
 * @author xull@aurine.cn
 * @date 2020-05-06 19:14:05
 */
public interface ProjectInfoService extends IService<ProjectInfo> {

    /**
     * 保存项目并放回id
     *
     * @param entity
     * @return
     */
    Integer saveReturnId(ProjectInfoFormVo entity);

    /**
     * 获取审核通过的项目列表
     *
     * @param page
     * @param projectInfoByAdminFormVo
     * @return
     */
    Page<ProjectInfoPageVo> pageByVisible(Page page, ProjectInfoByAdminFormVo projectInfoByAdminFormVo);

    /**
     * 分页查询项目
     *
     * @param page
     * @param projectInfo
     * @return
     */
    Page<ProjectInfoPageVo> pageProject(Page page, ProjectInfo projectInfo);

    /**
     * 获取所有啓用中且过审核的项目列表
     *
     * @return
     * @author: 王伟
     * @since :2020-09-16
     */
    List<ProjectInfo> listProject();


    /**
     * 更新项目及用户信息
     *
     * @param projectInfo
     * @return
     */
    boolean updateProjectAndUser(ProjectInfoFormVo projectInfo);

    /**
     * 根据角色分页查询项目
     *
     * @param page
     * @param projectInfoByAdminFormVo
     * @return
     */
    Page<ProjectInfoPageVo> pageByAdmin(Page page, @Param("query") ProjectInfoByAdminFormVo projectInfoByAdminFormVo);

    /**
     * 根据项目id获取项目信息
     *
     * @param id
     * @return
     */
    ProjectInfoPageVo getProjectInfoVoById(Integer id);

    /**
     * 分页查询项目配置
     *
     * @param page
     * @param projectInfoByAdminFormVo
     * @return
     */
    Page<ProjectInfoPageVo> pageByConfig(Page page, ProjectInfoByAdminFormVo projectInfoByAdminFormVo);


    /**
     * 根据员工手机号查询当前员工下的所有项目(微信接口相关)
     *
     * @param page
     * @return
     */
    Page<ProjectInfoPageVo> pageProjectByStaff(Page page, String projectId);

    /**
     * 根据业主或游客手机号查询当前用户下的所有项目(微信接口相关)
     *
     * @param page
     * @return
     */
    Page<ProjectInfoPageVo> pageProjectByPerson(Page page);

    /**
     * 分页查询所有项目（微信接口相关）
     *
     * @param page
     * @param
     * @return
     */
    Page<ProjectInfoSimplePageVo> pageAll(Page page, ProjectAddressParamVo address);

    /**
     * 审核通过
     *
     * @param projectId 项目id
     * @return
     */
    boolean pass(Integer projectId);
    /**
     * 审核通过
     *
     * @param projectInfoApprovalVo 项目id
     * @return
     */
    boolean passByVo(ProjectInfoApprovalVo projectInfoApprovalVo);

    /**
     * 查询所有项目（微信接口相关）
     *
     * @param
     * @return
     */
    List<ProjectInfoSimplePageVo> listAll(ProjectAddressParamVo address );
}
