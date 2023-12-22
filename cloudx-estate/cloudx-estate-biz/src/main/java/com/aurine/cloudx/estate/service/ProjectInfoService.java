

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.AdminUserInfo;
import com.aurine.cloudx.estate.entity.EdgeCascadeRequestMaster;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.pig4cloud.pigx.common.core.util.R;
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
    Page<ProjectInfoPageVo> pageProjectByStaff(Page page);

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


    Integer getIntervals(Integer projectId);

    /**
     * <p>获取当前项目的UUID</p>
     *
     * @param projectId 项目ID
     * @return 项目的UUID（主边缘网关上从边缘网关项目则是从边缘网关的第三方项目ID，而不是主边缘网关上生成的UUID）
     * @author: 王良俊
     */
    String getProjectUUID(Integer projectId);


    /**
     * <p>获取当前登录管理员信息</p>
     *
     * @return 管理员信息
     */
    AdminUserInfo getCurrentAdminUserInfo();

    /**
     * <p>获取当前项目信息（级联需要提供的）</p>
     *
     * @param projectId 所要获取的项目ID
     * @return 项目信息级联用
     * @author: 王良俊
     */
    ProjectInfoVo getCascadeProjectInfoVo(Integer projectId);

    /**
     * <p>创建级联项目并审核通过项目（如果没有项目的话）</p>
     *
     * @param projectId 主边缘网关项目ID（用作模板项目）
     * @return 级联项目ID
     * @author: 王良俊
     */
    Integer createCascadeProject(Integer projectId, EdgeCascadeRequestMaster requestMaster);

    /**
     * <p>更新项目信息的第三方ID</p>
     *
     * @param projectId 项目ID
     * @param projectCode 第三方项目ID
     * @author: 王良俊
     */
    void updateProjectCode(Integer projectId, String projectCode);

    /**
     * <p>更新项目信息的第三方ID</p>
     *
     * @param projectId 项目ID
     * @param UUID 项目UUID
     * @author: 王良俊
     */
    void updateProjectUUID(Integer projectId, String UUID);

    /**
     * <p>使用项目UUID获取项目ID</p>
     *
     * @param projectUUID 项目UUID
     * @return 项目ID
     */
    Integer getProjectId(String projectUUID);

    /**
     * <p>通过项目审核（原本都写在controller现在挪到service）</p>
     *
     * @param
     * @return
     */
    void passProject(ProjectInfoApprovalVo projectInfoApprovalVo);

    /**
     * <p>检查社区ID是否是当前边缘网关的自带项目</p>
     *
     * @param communityId 社区ID
     * @author 王良俊
     */
    boolean checkCommunityIdIsOriginProject(String communityId);

    /**
     * <p>检查社区ID是否是当前边缘网关的自带项目</p>
     *
     * @param projectId 项目
     * @return 
     * @author 王良俊
     */
    boolean checkProjectIdIsOriginProject(Integer projectId);

    /**
     * <p>检查社区ID是否是当前边缘网关的自带项目</p>
     *
     * @param json 带有社区ID的json数据
     * @param communityIdFieldName 社区ID在json里面的字段名
     * @author 王良俊
     */
    boolean checkCommunityIdIsOriginProject(String json, String communityIdFieldName);

    /**
     * 初次入云以云端数据为准，要删除边缘侧数据
     * @param projectId
     * @return
     */
    R delAllObj(Integer projectId);

    /**
     * 同步数据
     *
     * @param projectId
     * @return
     */
    R sync(Integer projectId);

    /**
     * 初始化minio桶*
     * @return
     */
    R initMinio();

}
