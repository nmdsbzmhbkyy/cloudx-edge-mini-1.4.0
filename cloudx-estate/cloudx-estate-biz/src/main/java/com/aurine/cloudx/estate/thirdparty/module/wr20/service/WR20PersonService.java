

package com.aurine.cloudx.estate.thirdparty.module.wr20.service;


import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;

import java.util.List;

/**
 * WR20 住户接口
 *
 * @ClassName: WR20FrameService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04 10:53
 * @Copyright:
 */
public interface WR20PersonService extends BaseService {

    /**
     * 同步住户信息
     *
     * @param projectId 项目编号
     * @return
     */
    boolean syncPerson(int projectId);

    /**
     * 新增住户
     *
     * @param housePersonRel 人屋关系
     * @return
     */
    boolean addPerson( int projectId,ProjectHousePersonRel housePersonRel);


    /**
     * 批量新增住户
     *
     * @param housePersonRelList 人屋关系对象列表
     * @return
     */
    boolean addPersonBatch(int projectId, List<ProjectHousePersonRel> housePersonRelList);

    /**
     * 删除住户
     *
     * @param housePersonRel 人屋关系
     * @return
     */
    boolean delPerson( int projectId,ProjectHousePersonRel housePersonRel);

    /**
     * 获取最早的一个住户对象
     * @param personId
     * @return
     */
    ProjectHousePersonRel getFitstHouseRel(String personId);

    /**
     * 一户多房用户迁入
     * @param housePersonRel
     * @return
     */
    boolean checkIn(ProjectHousePersonRel housePersonRel);

    /**
     * 一户多房用户迁出
     * @param housePersonRel
     * @return
     */
    boolean checkIOut(ProjectHousePersonRel housePersonRel);


}
