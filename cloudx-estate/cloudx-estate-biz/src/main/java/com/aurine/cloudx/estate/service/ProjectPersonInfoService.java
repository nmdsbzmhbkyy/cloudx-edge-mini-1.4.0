

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>人员信息服务</p>
 * @ClassName: ProjectPersonInfoService
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/11 14:33
 * @Copyright:
 */
public interface ProjectPersonInfoService extends IService<ProjectPersonInfo> {
    /**
     * 通过门禁系统保存
     * @param personInfo
     * @return
     */
    boolean saveFromSystem(ProjectPersonInfo personInfo);

    /**
     * 通过门禁系统保存(批量)
     * @param personInfoList
     * @return
     */
    boolean saveFromSystemBatch(List<ProjectPersonInfo> personInfoList);

    /**
     * 通过电话获取人员信息
     * @param telephone
     * @return
     */
    ProjectPersonInfo getByTelephone(String telephone);


    /**
     * 通过电话获取人员信息
     * @param redisKey redis缓存的键
     * @param telephone 人员手机号
     * @return 人员信息对象，如果未找到则返回null
     */
    ProjectPersonInfo getByTelephone(String redisKey, String telephone);

    /**
     * 生成最新的人员信息缓存（用来根据人员手机号获取人员信息）
     * @param redisKey redis缓存的键
     */
    void initPersonInfoTmpCache(String redisKey);


    /**
     * 如果没有这个手机号的人员信息就会添加否则不处理
     * @param redisKey redis缓存的键
     * @param personInfo 所要添加的人员信息
     */
    void addNewPersonInfoToTheCache(String redisKey, String telephone, ProjectPersonInfo personInfo);

    /**
     * 删除人员信息临时缓存
     * @param redisKey redis缓存的键
     */
    void deletePersonInfoTmpCache(String redisKey);

    /**
     * <p>
     * 对当前人员进行处理-判断是否需要删除该人员
     * </p>
     *
     * @param personId 人员ID
     * @author: 王良俊
    */
    Integer checkPersonAssets(String personId);
    /**
     * <p>
     * 对当前人员进行处理-判断是否需要删除该人员
     * </p>
     *
     * @param personId 人员ID
     * @author: 王良俊
     */
    Integer checkPersonAssetsParking(String personId);
    /**
     * <p>
     * 对当前人员进行处理-判断是否需要删除该人员
     * </p>
     *
     * @param personIdList 人员ID集合
     * @author: 王良俊
    */
    List<PersonAssetsNumVo> checkPersonAssets(List<String> personIdList);

    /**
     * <p>
     * 根据人员ID获取到这个人在系统中的资产（房屋、车辆、车位）数
     * </p>
     *
     * @param personId 人员ID
     * @author: 王良俊
    */
    Integer getAssetsNum(String personId);
    /**
     * <p>
     * 根据人员ID获取到这个人在系统中的资产（房屋、车辆、车位）数
     * </p>
     *
     * @param personId 人员ID
     * @author: 王良俊
     */
    Integer getAssetsNumParking(String personId);
    /**
     * <p>
     * 根据人员ID集合获取到这些人在系统中的资产（房屋、车辆、车位）数
     * </p>
     *
     * @param personIdList 人员ID集合
     * @author: 王良俊
    */
    List<PersonAssetsNumVo> getAssetsNum(List<String> personIdList);

    /**
     * 通过传入的任务关系对象列表中存储的住户手机号对住户进行查询操作如果未根据手机号查询出指定住户则添加该住户
     * @param projectHousePersonRelVoList 人屋关系vo对象列表
     * @return 已经添加了住户信息的vo对象列表
     * @author: 王良俊
     */
    List<ProjectHousePersonRelVo> initHousePersonRelPersonId(List<ProjectHousePersonRelVo> projectHousePersonRelVoList);


    Map<String, Object> groupByPersonType();

    /**
     * 通过传入的c车辆登记对象列表中存储的住户手机号对住户进行查询操作如果未根据手机号查询出指定住户则添加该住户
     * @param parCarRegisterVoList 车辆登记vo对象列表
     * @return 已经添加了住户信息的vo对象列表
     * @author: 王良俊
     */
    List<ProjectParCarRegisterVo> initParCarRegisterPersonId(List<ProjectParCarRegisterVo> parCarRegisterVoList);

    /**
     * 查询业主信息
     * @param name
     * @return
     */
    List<ProjectPersonInfo> findByName(String name);

    /**
     * 根据userId更新手机号
     * @param phone
     * @param userId
     */
    @SqlParser(filter=true)
    void updatePhoneByUserId(String phone,Integer userId);

    /**
     * 根据手机号更新userId
     * @param phone
     * @param userId
     */
    @SqlParser(filter=true)
    void updateUserIdByPhone(String phone,Integer userId);

    /**
     * 获取人员信息
     * @param relaId
     * @return
     */
    ProjectPersonInfo getPersonById(String relaId);

    /**
     *
     * @return
     */
    ProjectPersonInfo getPersonByOwner();
    /**
     * 获取小区和房屋状态（app相关接口）
     * @return
     */
    List<ProjectHouseStatusVo> getListPerson();

    List<ProjectUserHouseInfoVo> getUserHouseInfo(Integer userId);

    /**
     * 根据用户信息获取personId,手机号不存在则创建PersonId
     * @param projectHousePersonRel
     * @return PersonId
     */
    String getPersonId(ProjectHousePersonRelVo projectHousePersonRel);
}
