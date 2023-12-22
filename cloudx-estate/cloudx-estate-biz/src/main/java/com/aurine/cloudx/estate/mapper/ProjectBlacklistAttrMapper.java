package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectBlacklistAttr;
import com.aurine.cloudx.estate.vo.BlacklistAttrSearchCondition;
import com.aurine.cloudx.estate.vo.CarPreRegisterSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectBlacklistAttrVo;
import com.aurine.cloudx.estate.vo.ProjectCarPreRegisterVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 黑名单属性(ProjectBlacklistAttr)表数据库访问层
 *
 * @author 顾文豪
 * @since 2023-11-9 10:40:48
 */
@Mapper
public interface ProjectBlacklistAttrMapper extends BaseMapper<ProjectBlacklistAttr> {

    /**
     * <p>
     * 分页查询黑名单属性
     * </p>
     *
     * @param page 分页信息
     * @param query 查询条件
     * @author: 顾文豪
     * @return 分页查询结果
     */
    Page<ProjectBlacklistAttrVo> fetchList(Page page, @Param("query") BlacklistAttrSearchCondition query);
}
