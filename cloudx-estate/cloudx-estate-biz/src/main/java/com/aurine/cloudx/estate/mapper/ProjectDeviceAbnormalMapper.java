package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectDeviceAbnormal;
import com.aurine.cloudx.estate.vo.ProjectDeviceAbnormalSearchCondition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * <p>异常设备管理</p>
 * @author : 王良俊
 * @date : 2021-09-26 18:41:00
 */
@Mapper
public interface ProjectDeviceAbnormalMapper extends BaseMapper<ProjectDeviceAbnormal> {

    /**
     * <p>分页查询异常设备管理</p>
     *
     * @param page 分页参数
     * @param condition 查询条件
     * @return 分页数据
     * @author: 王良俊
     */
    Page<ProjectDeviceAbnormal> fetchList(Page page, @Param("query") ProjectDeviceAbnormalSearchCondition condition);

}
