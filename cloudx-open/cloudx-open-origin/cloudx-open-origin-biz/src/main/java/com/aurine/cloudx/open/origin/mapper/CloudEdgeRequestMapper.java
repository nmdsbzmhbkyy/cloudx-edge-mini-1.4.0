

package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.CloudEdgeRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>入云申请mapper</p>
 * @author : 王良俊
 * @date : 2021-12-08 11:08:02
 */
@Mapper
public interface CloudEdgeRequestMapper extends BaseMapper<CloudEdgeRequest> {

    /**
     * <p>用来判断是否重复提交申请</p>
     *
     * @param projectCode 第三方项目ID
     * @param targetProjectId 入云申请对应系统项目ID
     * @return 有可能重复的申请或是需要更新的申请
     * @author: 王良俊
     */
    List<CloudEdgeRequest> listExistRequest(@Param("projectCode") String projectCode, @Param("targetProjectId") Integer targetProjectId);

    /**
     * <p>根据项目ID获取项目管理员角色ID</p>
     *
     * @param projectId 项目ID
     * @return 项目 管理员角色ID
     * @author: 王良俊
     */
    Integer getAdminRoleId(@Param("projectId") Integer projectId);
}
