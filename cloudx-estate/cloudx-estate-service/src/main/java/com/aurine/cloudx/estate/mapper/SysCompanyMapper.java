package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.SysCompany;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 集团管理
 *
 * @author xull@aurine.cn
 * @date 2020-04-29 16:23:11
 */
@Mapper
public interface SysCompanyMapper extends BaseMapper<SysCompany> {

    /**
     * 平台管理员分页查询集团
     * @param page
     * @param sysCompany
     * @return
     */
    Page<SysCompany> pageCompany(Page page,@Param("query") SysCompany sysCompany);

    /**
     * 根据项目或项目组获取所属集团列表
     *
     * @param id
     *
     * @return
     */
    List<SysCompany> findByGroupOrProjectId(@Param("id") Integer id);
}
