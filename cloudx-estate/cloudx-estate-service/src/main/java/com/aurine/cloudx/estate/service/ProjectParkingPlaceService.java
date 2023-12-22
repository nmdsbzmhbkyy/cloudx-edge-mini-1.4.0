

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.vo.ProjectParkingInfoVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceAssignmentVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceHisVo;
import com.aurine.cloudx.estate.vo.ProjectParkingPlaceVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 车位
 *
 * @author 王伟
 * @date 2020-05-08 10:24:42
 */
public interface ProjectParkingPlaceService extends IService<ProjectParkingPlace> {

    IPage<ProjectParkingPlaceVo> findPage(IPage<ProjectParkingPlace> page, ProjectParkingPlaceVo projectParkingPlace);

    boolean add(ProjectParkingPlace projectParkingPlace);

    /**
     * <p>
     * 获取车位归属信息
     * </p>
     *
     * @param placeId 车位ID
     * @return 车位归属记录列表
     * @author: 王良俊
     */
    List<ProjectParkingPlaceAssignmentVo> getParkingAttribution(String placeId);

    /**
     * @param placeId 车位ID
     * @return 车位使用记录列表
     * @author: 王良俊
     */
    List<ProjectParkingPlaceHisVo> getParkUseHis(String placeId);

    /**
     * 核对同一车库下是否存在同名车位
     *
     * @param placeName
     * @param parkId
     * @return
     */
    boolean checkPlaceNameExist(String placeName, String parkId);

    /**
     * 核对同一车库下是否存在同名车位
     *
     * @param placeName
     * @param parkId
     * @return
     */
    boolean checkPlaceNameExist(String placeName, String parkId, String placeId);

    /**
     * 核对同一车库下是否存在同名车位
     *
     * @param placeNameList
     * @param parkId
     * @return
     */
    boolean checkPlaceNameExist(List<String> placeNameList, String parkId);


    /**
     * 核对同一车库下是否存在同名车位
     *
     * @param parkingPlaceVo
     * @return
     */
    boolean savePlaceBatch(ProjectParkingPlaceVo parkingPlaceVo);

    /**
     * 更新车库信息
     *
     * @param projectParkingPlace
     * @return
     */
    boolean update(ProjectParkingPlace projectParkingPlace);

    /**
     * <p>
     * 获取包含同区域所有车位列表和所属区域parkId的map
     * </p>
     *
     * @param placeId 车位ID
     * @return 包含所属区域ID和车位列表的map数据
     * @author: 王良俊
     */
    Map<String, Object> getCurrentListAndPuidMap(String placeId);

    /**
     * <p>
     * 空置车位
     * </p>
     *
     * @param placeId
     */
    boolean cleanParkingPlace(String placeId);

    /**
     * <p>
     * 空置公共车位数
     * </p>
     *
     * @param parkId 车场ID
     */
    int getFreePublicParkingSpaceNum(String parkId);

    /**
     * <p>
     * 车位登记管理注销时调用
     * </p>
     *
     * @param placeId 车位地址
     * @author: 王良俊
     */
    boolean cancelParkingPlace(String placeId);

    /**
     * <p>
     * 获取这个人拥有多车位区域ID和车位ID
     * </p>
     *
     * @param personId 人员ID
     * @return 车位区域ID列表
     * @author: 王良俊
     */
    List<String> listParkRegionIdByPersonId(String parkId, String personId, String relType, String placeId);

    /**
     * <p>
     * 获取这个用户所拥有的车位类型（有未被登记的车位的车位类型）
     * </p>
     *
     * @param removePlaceIdList 已被登记的车位id列表用于排除所有车位都被登记的车位类型的车位区域
     * @param parkId            车场ID
     * @param personId          人员ID
     * @return 车位类型列表
     * @author: 王良俊
     */
    List<String> getPlaceRelTypeByPersonId(String parkId, String personId, List<String> removePlaceIdList);

    /**
     * <p>
     * 通过车位区域id和人员id获取到这个人在这个区域拥有的车位列表
     * </p>
     *
     * @param parkRegionId 车位区域ID
     * @param personId     人员ID
     * @param relType      车位类型
     * @param placeId      要被排除的车位id（最后返回的列表要显示这个id对应的车位）
     * @return 车位列表
     * @author: 王良俊
     */
    List<ProjectParkingPlace> listParkPlaceByRelType(String parkRegionId, String personId, String relType, String placeId);

    /**
     * <p>
     * 检查收费规则是否正在被使用中
     * </p>
     *
     * @param ruleId 收费规则ID
     * @return 是否被使用
     * @author: 王良俊
     */
    boolean checkRuleUseStatus(String ruleId);

    List<ProjectParkingPlace> getByPersonId(String personId);

    /**
     * 车位统计
     * @return
     */
    int countPlace();

    List<ProjectParkingPlace> listByParkRegionId(String parkRegionId);
}
