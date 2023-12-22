package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.*;

import java.util.List;

public interface SzztService {
    /**
     * 社区信息
     * @param projectId
     * @return
     */
    Szzt6193 findTable6193(String projectId);

    /**
     * 楼栋信息
     * @param projectId
     * @return
     */
    List<Szzt6194> findTable6194(String projectId);

    /**
     * 单元信息
     * @param projectId
     * @return
     */
    List<Szzt6195> findTable6195(String projectId);

    /**
     * 房屋信息
     * @param projectId
     * @return
     */
    List<Szzt6196> findTable6196(String projectId);

    /**
     * 居民信息
     * @param projectId
     * @return
     */
    List<Szzt6197> findTable6197(String projectId);

    /**
     * 车辆信息
     * @param projectId
     * @return
     */
    List<Szzt6198> findTable6198(String projectId);

    /**
     * 出入信息
     * @param projectId
     * @return
     */
    List<Szzt6199> findTable6199(String projectId);

    /**
     * 推送数据
     * @param tableId 表格ID
     * @param projectId 项目ID
     * @return
     */
    String pushData(String tableId, String projectId);
}
