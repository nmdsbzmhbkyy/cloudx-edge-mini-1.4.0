

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.SysProjectDept;
import com.aurine.cloudx.estate.vo.SysProjectDeptVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目内部门信息
 *
 * @author lingang
 * @date 2020-05-07 18:44:46
 */
@Mapper
public interface SysProjectDeptMapper extends BaseMapper<SysProjectDept> {

    @SqlParser(filter = true)
    List<SysProjectDept> selectByTemplate();

    /**
     * 查询默认部门是否存在
     * @param office 办公室
     * @param finance 财务部
     * @param comprehensive 综合管理部
     * @param securityStaff 保安部
     * @param repair 维修部
     * @param environment 环境管理部
     * @return
     */
    @SqlParser(filter = true)
    List<SysProjectDept> defaultDept(
            @Param("projectId") Integer projectId, @Param("office") String office, @Param("finance") String finance, @Param("comprehensive") String comprehensive,
            @Param("securityStaff") String securityStaff, @Param("repair") String repair, @Param("environment") String environment
    );


    @SqlParser(filter = true)
    IPage <SysProjectDeptVo> getPage(Page page, @Param("query") SysProjectDeptVo sysProjectDeptVo);
}
