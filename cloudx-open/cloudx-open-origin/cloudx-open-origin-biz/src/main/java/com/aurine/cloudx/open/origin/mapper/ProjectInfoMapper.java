

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectInfoByAdminFormVo;
import com.aurine.cloudx.open.origin.vo.ProjectInfoSimplePageVo;
import com.aurine.cloudx.open.origin.entity.ProjectInfo;
import com.aurine.cloudx.open.origin.vo.ProjectAddressParamVo;
import com.aurine.cloudx.open.origin.vo.ProjectInfoPageVo;
import com.aurine.cloudx.open.common.entity.vo.ProjectInfoVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目
 *
 * @author xull@aurine.cn
 * @date 2020-05-06 19:14:05
 */
@Mapper
public interface ProjectInfoMapper extends BaseMapper<ProjectInfo> {

    /**
     * 项目组管理员分页查询项目
     *
     * @param page
     * @param projectInfo
     * @return
     */
    Page<ProjectInfoPageVo> pageProject(Page page, @Param("query") ProjectInfo projectInfo);


    /**
     * 平台管理员分页查询审核项目
     *
     * @param page
     * @param projectInfoByAdminFormVo
     * @return
     */
    Page<ProjectInfoPageVo> pageByAdmin(Page page, @Param("query") ProjectInfoByAdminFormVo projectInfoByAdminFormVo);

    /**
     * 根据项目id获取项目信息
     *
     * @param projectId
     * @return
     */
    ProjectInfoPageVo getProjectInfoVoById(@Param("projectId") Integer projectId);

    /**
     * 分页查询项目配置
     *
     * @param page
     * @param projectInfoByAdminFormVo
     * @return
     */
    Page<ProjectInfoPageVo> pageByConfig(Page page, @Param("query") ProjectInfoByAdminFormVo projectInfoByAdminFormVo);

    /**
     * 根据员工Id查询当前员工下的所有项目(微信相关)
     *
     * @param page
     * @param userId
     * @return
     */
    Page<ProjectInfoPageVo> pageProjectByStaff(Page page, @Param("userId") Integer userId);

    /**
     * 根据业主手机号查询当前用户下的所有项目(微信相关)
     *
     * @param page
     * @param userId
     * @return
     */
    Page<ProjectInfoPageVo> pageProjectByPerson(Page page, @Param("userId") Integer userId);

    /**
     * 根据游客手机号查询当前用户下的所有项目(微信相关)
     *
     * @param page
     * @param userId
     * @return
     */
    Page<ProjectInfoPageVo> pageProjectByVisitor(Page page, @Param("userId") Integer userId);

    /**
     * 分页查询所有项目（微信接口相关）
     *
     * @param page
     * @param address
     * @return
     */
    Page<ProjectInfoSimplePageVo> pageAll(Page page, @Param("query") ProjectAddressParamVo address, @Param("userId") Integer userId, @Param("type") String type);

    /**
     * 查询所有项目（微信接口相关）
     *
     * @return
     */
    List<ProjectInfoSimplePageVo> listAll(@Param("query") ProjectAddressParamVo addressParam, @Param("userId") Integer userId, @Param("type") String type);

    Integer getIntervals(@Param("projectId") Integer projectId);

//    /**
//     * <p>获取管理员人员信息</p>
//     *
//     * @param userId 用户表ID
//     * @return 管理员信息
//     * @author: 王良俊
//     */
//    AdminUserInfo getAdminUserInfo(@Param("userId") Integer userId);

    /**
     * <p>获取当前项目信息（级联需要提供的）</p>
     *
     * @param projectId 所要获取的项目ID
     * @return 项目信息级联用
     * @author: 王良俊
     */
    ProjectInfoVo getCascadeProjectInfoVo(@Param("projectId") Integer projectId);

    /**
     * 分页查询项目信息
     *
     * @param page
     * @param po
     * @return
     */
    Page<ProjectInfoVo> page(Page page, @Param("query") ProjectInfo po);

    /**
     * 平台侧获取已入云的项目
     *  （平台侧调用）
     *
     * @return
     */
    List<ProjectInfoVo> listCascadeByCloud();

    /**
     * 边缘侧获取已入云的项目
     *  （边缘侧调用）
     *
     * @return
     */
    List<ProjectInfoVo> listCascadeByEdge();

    /**
     * 主边缘侧获取已级联的项目
     *  （主边缘侧调用）
     *
     * @return
     */
    List<ProjectInfoVo> listCascadeByMaster();

    /**
     * 从边缘侧获取已级联的项目
     *  （从边缘侧调用）
     *
     * @return
     */
    List<ProjectInfoVo> listCascadeBySlave();
}
