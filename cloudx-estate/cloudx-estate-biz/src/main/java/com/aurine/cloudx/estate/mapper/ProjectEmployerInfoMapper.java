package com.aurine.cloudx.estate.mapper;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.vo.ProjectEmployerInfoPageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectEmployerInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 项目实有单位信息(ProjectEmployerInfo)表数据库访问层
 *
 * @author guhl@aurine.cn
 * @since 2020-08-25 14:58:44
 */
@Mapper
public interface ProjectEmployerInfoMapper extends BaseMapper<ProjectEmployerInfo> {

    /**
     * 分页查询实有单位
     *
     * @param page
     * @param projectEmployerInfoPageVo
     * @return
     */
    Page<ProjectEmployerInfoPageVo> pageEmployer(Page page, @Param("query") ProjectEmployerInfoPageVo projectEmployerInfoPageVo);
}