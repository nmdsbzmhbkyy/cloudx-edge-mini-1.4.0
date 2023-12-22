
package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectOpenLaneHis;
import com.aurine.cloudx.open.origin.vo.OpenLaneHisQueryVo;
import com.aurine.cloudx.open.origin.vo.ProjectOpenLaneHisVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 车道开闸记录mapper
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/27 10:40
 */
@Mapper
public interface ProjectOpenLaneHisMapper extends BaseMapper<ProjectOpenLaneHis> {

    /**
     * <p>
     * 分页查询车辆开闸记录
     * </p>
     *
     * @param query 查询条件
     * @author: 王良俊
     * @return 开闸记录分页数据
     */
    IPage<ProjectOpenLaneHisVo> fetchList(Page page, @Param("query") OpenLaneHisQueryVo query);
}
