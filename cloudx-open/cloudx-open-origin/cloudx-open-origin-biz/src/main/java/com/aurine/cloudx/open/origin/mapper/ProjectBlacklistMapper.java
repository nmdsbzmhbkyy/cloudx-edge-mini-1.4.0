package com.aurine.cloudx.open.origin.mapper;



import com.aurine.cloudx.open.origin.entity.ProjectBlacklist;
import com.aurine.cloudx.open.origin.vo.ProjectBlacklistVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 黑名单管理(ProjectBlackList)表数据库访问层
 *
 * @author guwh
 * @since 2022-06-09 15:31:25
 */
@Mapper
public interface ProjectBlacklistMapper extends BaseMapper<ProjectBlacklist> {

    /**
     * 分页查询黑名单信息
     *
     * @param page
     * @param projectBlackListVo
     * @return
     */
    Page<ProjectBlacklistVo> getBlacklistPage(Page page, @Param("query") ProjectBlacklistVo projectBlackListVo);
}
