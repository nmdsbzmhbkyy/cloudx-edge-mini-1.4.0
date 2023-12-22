package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报警事件处理
 *
 * @author 黄阳光
 * @date 2020-06-04 08:31:21
 */
@Mapper
public interface SzztMapper extends Mapper {
    /**
     * 社区信息
     * @param projectId
     * @return
     */
    Szzt6193 findTable6193(@Param("projectId") String projectId);

    /**
     * 楼栋信息
     * @param projectId
     * @return
     */
    List<Szzt6194> findTable6194(@Param("projectId") String projectId);

    /**
     * 单元信息
     * @param projectId
     * @return
     */
    List<Szzt6195> findTable6195(@Param("projectId") String projectId);

    /**
     * 房屋信息
     * @param projectId
     * @return
     */
    List<Szzt6196> findTable6196(@Param("projectId") String projectId);


    /**
     * 居民信息
     * @param projectId
     * @return
     */
    List<Szzt6197> findTable6197(@Param("projectId") String projectId);

    /**
     * 车辆信息
     * @param projectId
     * @return
     */
    List<Szzt6198> findTable6198(@Param("projectId") String projectId);

    /**
     * 出入信息
     * @param projectId
     * @return
     */
    List<Szzt6199> findTable6199(@Param("projectId") String projectId);
}
