

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

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
     *
     * @return
     */
    ProjectPersonInfo getPersonByOwner();

    /**
     * 查询是否有业主角色，没有则添加,并返回业主信息
     * @return
     */
    ProjectPersonInfoVo getPersoninfo();
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

    ProjectPersonCarInfoVo getCarInfo(String personId);

    R<Boolean> updatePhoneById(String phone,String newPhone);

}
