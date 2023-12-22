package com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.service.ProjectHouseInfoService;
import com.aurine.cloudx.estate.service.ProjectHousePersonRelService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.WR20PersonService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04
 * @Copyright:
 */
@Service
@Slf4j
public class WR20PersonServiceImplV1 implements WR20PersonService {
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private ProjectHouseInfoService projectHouseInfoService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private WR20RemoteService wr20RemoteService;

    /**
     * 同步住户信息
     *
     * @param projectId 项目ID
     * @return
     */
    @Override
    public boolean syncPerson(int projectId) {
        HuaweiRespondDTO respondDTO = null;
        respondDTO = wr20RemoteService.syncTenement(projectId, new JSONObject());
        if (respondDTO.getErrorEnum() != HuaweiErrorEnum.SUCCESS) {
            throw new RuntimeException("同步住户失败：" + respondDTO.getErrorMsg());
        }
        log.info("[WR20] 同步住户信息 {}", respondDTO);
        respondDTO = wr20RemoteService.syncStaff(projectId, new JSONObject());
        log.info("[WR20] 同步员工信息 {}", respondDTO);
        return true;
    }


    /**
     * 新增住户
     *
     * @param housePersonRel 人屋关系
     * @return
     */
    @Override
    public boolean addPerson(int projectId, ProjectHousePersonRel housePersonRel) {
        //1.init
//        ProjectHouseInfo house = projectHouseInfoService.getById(housePersonRel.getHouseId());
//        ProjectPersonInfo person = projectPersonInfoService.getById(housePersonRel.getPersonId());
//
//        String frameNo = house.getHouseCode();
//        String personName = person.getPersonName();
//        String personPhone = person.getTelephone();
//
//        if (StringUtils.isEmpty(frameNo)) {
//            return false;//房屋无第三方框架信息
//        }
//
//        JSONArray params = new JSONArray();
//        JSONObject param = new JSONObject();
//        param.put("id", "-1");
//        param.put("frameNo", frameNo);
//        param.put("name", personName);
//        param.put("Telephone", personPhone);
//        params.add(param);
//
//        Map<String, Object> otherParam = new HashMap<>();
//        otherParam.put("orgUid", housePersonRel.getRelaId());
//        JSONObject.toJSON(otherParam);
//
//        HuaweiRespondDTO huaweiRespondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(VersionEnum.V1).objectManage(wr20Id,
//                "TenementManager", "ADD", "", params, new JSONObject());


        return true;
    }

    @Override
    public boolean addPersonBatch(int projectId, List<ProjectHousePersonRel> housePersonRelList) {
//        List<HuaweiRespondDTO> result = new ArrayList<>();
//        for (ProjectHousePersonRel housePersonRel : housePersonRelList) {
//            //1.init
//            Map<String, Object> personMap = new HashMap<>();
//            ProjectHouseInfo house = projectHouseInfoService.getById(housePersonRel.getHouseId());
//            ProjectPersonInfo person = projectPersonInfoService.getById(housePersonRel.getPersonId());
//
//            String frameNo = house.getHouseCode();
//            String personName = person.getPersonName();
//            String personPhone = person.getTelephone();
//
//            if (StringUtils.isEmpty(frameNo)) {
//                return false;//房屋无第三方框架信息
//            }
//            JSONArray params = new JSONArray();
//            JSONObject param = new JSONObject();
//            param.put("id", "-1");
//            param.put("frameNo", frameNo);
//            param.put("name", personName);
//            param.put("Telephone", personPhone);
//            params.add(param);
//            /*JSONObject resultJson = HuaweiRemoteDeviceObjectServiceFactory.getInstance(VersionEnum.V1)
//                    .objectManage(wr20Id, "TenementManager", "ADD", "", personMap, housePersonRel.getRelaId());*/
//            Map<String, Object> otherParam = new HashMap<>();
//            otherParam.put("orgUid", housePersonRel.getRelaId());
//            HuaweiRespondDTO huaweiRespondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(VersionEnum.V1)
//                    .objectManage(wr20Id, "TenementManager", "ADD", "", params, new JSONObject());
//            result.add(huaweiRespondDTO);
//        }
        return false;
    }

    /**
     * 删除住户
     *
     * @param housePersonRel 人屋关系
     * @return
     */
    @Override
    public boolean delPerson(int projectId, ProjectHousePersonRel housePersonRel) {
//        //1.init
//        Map<String, Object> personMap = new HashMap<>();
//        ProjectHouseInfo house = projectHouseInfoService.getById(housePersonRel.getHouseId());
//        ProjectPersonInfo person = projectPersonInfoService.getById(housePersonRel.getPersonId());
//
//        String frameNo = house.getHouseCode();
//        String personName = person.getPersonName();
//        String personPhone = person.getTelephone();
//
//        if (StringUtils.isEmpty(frameNo)) {
//            return false;//房屋无第三方框架信息
//        }
//
//        personMap.put("userId", housePersonRel.getRelaCode());
//        JSONArray params = new JSONArray();
//        JSONObject param = new JSONObject();
//        param.put("userId", housePersonRel.getRelaCode());
//        boolean add = params.add(param);
//        HuaweiRespondDTO huaweiRespondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(VersionEnum.V1).objectManage(wr20Id,
//                "TenementManager", "DELETE", "", params, new JSONObject());
//

        return true;
    }

    /**
     * 获取最早的一个住户对象
     *
     * @param personId
     * @return
     */
    @Override
    public ProjectHousePersonRel getFitstHouseRel( String personId) {

        List<ProjectHousePersonRel> housePersonRelList = projectHousePersonRelService.list(new QueryWrapper<ProjectHousePersonRel>().lambda().eq(ProjectHousePersonRel::getPersonId, personId).orderByAsc(ProjectHousePersonRel::getCreateTime));

        if (CollUtil.isNotEmpty(housePersonRelList)) {
            return housePersonRelList.get(0);
        }

        return null;
    }

    /**
     * 一户多房用户迁入
     *
     * @param housePersonRel
     * @return
     */
    @Override
    public boolean checkIn(ProjectHousePersonRel housePersonRel) {
        return false;
    }

    /**
     * 一户多房用户迁出
     *
     * @param housePersonRel
     * @return
     */
    @Override
    public boolean checkIOut(ProjectHousePersonRel housePersonRel) {
        return false;
    }

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }
}
