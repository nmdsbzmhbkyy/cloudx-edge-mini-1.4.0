package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectFeeConfVo;
import com.aurine.cloudx.open.origin.entity.ProjectFeeConf;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 费用设置(ProjectFeeConf)表数据库访问层
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Mapper
public interface ProjectFeeConfMapper extends BaseMapper<ProjectFeeConf> {
    /**
     * 分页差费用配置信息
     * @param page
     * @param projectFeeConf
     * @return
     */
    IPage<ProjectFeeConfVo> pageFee(Page<ProjectFeeConfVo> page, @Param("query") ProjectFeeConf projectFeeConf);

    /**
     * 查询所有费用列表
     * @param type  是否预存
     * @return
     */
    List<ProjectFeeConfVo> liseFee(@Param("type") String type, @Param("status") String status);

    List <ProjectFeeConf> getActiveConfig(@Param("query") ProjectFeeConf projectFeeConf);


}