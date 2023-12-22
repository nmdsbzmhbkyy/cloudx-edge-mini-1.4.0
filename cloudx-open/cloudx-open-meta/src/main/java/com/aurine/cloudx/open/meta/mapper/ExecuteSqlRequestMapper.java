package com.aurine.cloudx.open.meta.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author Administrator
 */
@Mapper
public interface ExecuteSqlRequestMapper {


    /**
     * 新增sql
     * @param statement
     * @return
     */
    boolean addSql(@Param("statement") String statement);

    /**
     * 更新sql
     * @param statement
     * @return
     */
    boolean updateSql(@Param("statement") String statement);

    /**
     * 删除sql
     * @param statement
     * @return
     */
    boolean deleteSql(@Param("statement") String statement);
}
