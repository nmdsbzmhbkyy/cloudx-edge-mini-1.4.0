package com.aurine.cloudx.estate.mapper;
import com.aurine.cloudx.estate.vo.ProjectPromotionConfFormVo;
import com.aurine.cloudx.estate.vo.ProjectPromotionConfOnFeeIdVo;
import com.aurine.cloudx.estate.vo.ProjectPromotionConfPageVo;
import com.aurine.cloudx.estate.vo.ProjectPromotionConfVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectPromotionConf;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 优惠活动设置(ProjectPromotionConf)表数据库访问层
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Mapper
public interface ProjectPromotionConfMapper extends BaseMapper<ProjectPromotionConf> {

    IPage<ProjectPromotionConfPageVo> pageByForm(Page<ProjectPromotionConfPageVo> page, @Param("query") ProjectPromotionConfFormVo projectPromotionConf);

    /**
     * 按类型查询优惠
     *
     * @return 优惠信息
     */
    List<ProjectPromotionConfOnFeeIdVo> listConfByType(@Param("types")List<String > types);

    /**
     * 查询是否存在普通优惠和设置时间相交的数据
     * @param effTime 开始时间
     * @param expTime 结束时间
     * @param id 优惠id(编辑时候使用)
     * @return 数据个数
     */
    Integer selectPromotionNormalCount(@Param("effTime") LocalDate effTime,@Param("expTime") LocalDate expTime,@Param("id") String id);

    /**
     * 根据id批量查询优惠
     * @param ids
     * @return
     */
    List<ProjectPromotionConfOnFeeIdVo> listConfById(@Param("ids")List<String> ids);
}