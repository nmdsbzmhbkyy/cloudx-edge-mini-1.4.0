package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.controller.EdgeCascadeRequestMasterController;
import com.aurine.cloudx.estate.entity.EdgeCascadeRequestMaster;
import com.aurine.cloudx.estate.vo.CascadeManageQuery;
import com.aurine.cloudx.estate.vo.CascadeProjectInfoVo;
import com.aurine.cloudx.estate.vo.ProjectBuildingBatchAddTemplateRecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EdgeCascadeRequestMasterMapper extends BaseMapper<EdgeCascadeRequestMaster> {

    /**
     * <p>分页查询级联管理项目信息</p>
     *
     * @param cascadeManageQuery 查询条件
     * @return 分页数据
     */
    Page<CascadeProjectInfoVo> pageCascadeManage(Page<?> page, @Param("query") CascadeManageQuery cascadeManageQuery, @Param("masterProjectId") Integer masterProjectId);

    /**
     * <p>根据项目ID获取项目管理员角色ID</p>
     *
     * @param projectId 项目ID
     * @return 项目 管理员角色ID
     * @author: 王良俊
     */
    Integer getAdminRoleId(@Param("projectId") Integer projectId);

    /**
     * <p>获取边缘网关自带项目的项目ID</p>
     *
     * @return 边缘网关自带非级联生成的项目ID
     */
    Integer getOriginProjectId();
}
