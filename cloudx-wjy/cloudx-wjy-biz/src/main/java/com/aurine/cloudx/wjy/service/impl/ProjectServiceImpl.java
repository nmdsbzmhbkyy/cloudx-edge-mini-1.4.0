package com.aurine.cloudx.wjy.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.feign.*;
import com.aurine.cloudx.estate.vo.ProjectPersonInfoVo;
import com.aurine.cloudx.wjy.constant.*;
import com.aurine.cloudx.wjy.mapper.ProjectMapper;
import com.aurine.cloudx.wjy.pojo.PushData;
import com.aurine.cloudx.wjy.service.ProjectService;
import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.utils.RedisUtil;
import com.aurine.cloudx.wjy.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/4/22
 * @description： 项目配置服务实现类
 */
@Service
@Slf4j
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Resource
    ProjectMapper projectMapper;
    @Resource
    RemoteBuildingInfoService remoteBuildingInfoService;
    @Resource
    RemoteUnitInfoService remoteUnitInfoService;
    @Resource
    RemoteHouseInfoService remoteHouseInfoService;
    @Resource
    RemoteHousePersonRelService remoteHousePersonRelService;
    @Resource
    RemotePersonInfoService remotePersonInfoService;
    @Resource
    RemoteStaffService remoteStaffService;

    @Override
    public Project getByPid(String pid) {
        return projectMapper.selectOne(new QueryWrapper<Project>().lambda().eq(Project::getPid,pid));
    }

    @Override
    public Project getByProjectId(Integer projectId) {
        return projectMapper.selectOne(new QueryWrapper<Project>().lambda().eq(Project::getProjectId,projectId));
    }

    @Override
    public List<Project> getList() {
        return this.list();
    }

    @Override
    @Async
    public void initProjectToWjy(Integer projectId) {

        Project project = getByProjectId(projectId);
        if(project == null){
            log.info("未找到项目配置数据");
            return;
        }

        System.out.println("小区数据读取");
        //读取楼宇信息
        R<List<ProjectBuildingInfo>> rBuilding = remoteBuildingInfoService.innerListByProjectId(projectId, SecurityConstants.FROM_IN);
        JSONArray builingArr = (JSONArray) JSONArray.toJSON(rBuilding.getData());
        List<ProjectBuildingInfo> buildingInfoList = builingArr.toJavaList(ProjectBuildingInfo.class);
        System.out.println(JSONObject.toJSONString(buildingInfoList));
        if(buildingInfoList != null && buildingInfoList.size() > 0){
            for (ProjectBuildingInfo buildingInfo:
            buildingInfoList) {

                //读取单元信息
                R<List<ProjectUnitInfo>> rUnit = remoteUnitInfoService.innerListUnit(buildingInfo.getBuildingId(),SecurityConstants.FROM_IN);
                JSONArray unitArr = (JSONArray) JSONArray.toJSON(rUnit.getData());
                if(unitArr == null){
                    continue;
                }
                List<ProjectUnitInfo> unitInfoList = unitArr.toJavaList(ProjectUnitInfo.class);
                System.out.println(JSONObject.toJSONString(unitInfoList));

                //构造楼栋数据，存入缓存中，待发送
                saveBuildingToRedis(projectId,buildingInfo,unitInfoList);

                if(unitInfoList != null && unitInfoList.size() > 0){
                    for (ProjectUnitInfo unitInfo:
                    unitInfoList) {
                        //读取房间信息
                        R<List<ProjectHouseInfo>> rRoom = remoteHouseInfoService.innerListByUnit(projectId,unitInfo.getUnitId(),SecurityConstants.FROM_IN);
                        JSONArray roomArr = (JSONArray) JSONArray.toJSON(rRoom.getData());
                        if(roomArr == null){
                            continue;
                        }
                        List<ProjectHouseInfo> houseInfoList = roomArr.toJavaList(ProjectHouseInfo.class);
                        System.out.println(JSONObject.toJSONString(houseInfoList));
                        if(houseInfoList != null && houseInfoList.size() > 0){
                            for (ProjectHouseInfo houseInfo:
                            houseInfoList) {

                                //构造房间数据，存入缓存中，待发送
                                saveRoomToRedis(projectId,buildingInfo,unitInfo,houseInfo);

                                //房客关系
                                R<List<ProjectHousePersonRel>> rRel = remoteHousePersonRelService.innerGetPersonRelByHouseId(houseInfo.getHouseId(),SecurityConstants.FROM_IN);
                                JSONArray relArr = (JSONArray) JSONArray.toJSON(rRel.getData());
                                if(relArr == null){
                                   continue;
                                }
                                List<ProjectHousePersonRel> relList = relArr.toJavaList(ProjectHousePersonRel.class);
                                System.out.println(JSONObject.toJSONString(relList));
                                if(relList != null && relList.size() > 0){
                                    for (ProjectHousePersonRel rel:
                                    relList) {
                                        //读取住户信息
                                        R<ProjectPersonInfoVo> rCustomer = remotePersonInfoService.innerGetById(rel.getPersonId(),SecurityConstants.FROM_IN);
                                        JSONObject customeObj = (JSONObject) JSONObject.toJSON(rCustomer.getData());
                                        if(customeObj == null){
                                            continue;
                                        }
                                        ProjectPersonInfoVo personInfoVo = customeObj.toJavaObject(ProjectPersonInfoVo.class);
                                        System.out.println(JSONObject.toJSONString(personInfoVo));
                                        //构造客户数据，存入缓存中，待发送
                                        saveCustomerToRedis(projectId,personInfoVo);
                                        //构造房间客户绑定数据，存入缓存中，待发送
                                        saveRoomCustomerBindToRedis(projectId,buildingInfo,unitInfo,houseInfo,personInfoVo,rel);
                                    }
                                }


                            }
                        }
                    }
                }
            }

        }
        //读取工程人员
        if(StringUtils.isNotBlank(project.getOrgName())){
            R<ProjectStaff> rStaff = remoteStaffService.innerListByProjectId(projectId,SecurityConstants.FROM_IN);
            JSONArray staffArr = (JSONArray) JSONArray.toJSON(rStaff.getData());
            System.out.println(staffArr.toJSONString());
            if(staffArr != null){
                List<ProjectStaff> relList = staffArr.toJavaList(ProjectStaff.class);
                if(relList != null && relList.size() > 0){
                    for (ProjectStaff projectStaff:
                    relList) {
                        saveWorkerToRedis(project,projectStaff);
                    }
                }
            }
        }
    }

    private void saveBuildingToRedis(Integer projectId,ProjectBuildingInfo buildingInfo,List<ProjectUnitInfo> unitInfoList){
        //封装楼栋数据
        BuildingVo buildingVo = new BuildingVo();
        buildingVo.setName(buildingInfo.getBuildingName());
        buildingVo.setNumber(buildingInfo.getBuildingCode()==null?buildingInfo.getBuildingName():buildingInfo.getBuildingCode());
        buildingVo.setSourceID(buildingInfo.getBuildingId());
        buildingVo.setSourceSystem(Constant.source);
        //判断楼层数，默认为1
        Integer overgroundCount = 99;
        Integer undergroundCount = 9;
        if(buildingInfo.getFloorGround() == null || buildingInfo.getFloorGround() == 0){
            if(buildingInfo.getFloorTotal() != null && buildingInfo.getFloorTotal() >= 0){
                overgroundCount = buildingInfo.getFloorTotal();
            }
        }else{
            overgroundCount = buildingInfo.getFloorGround();
        }
        if(buildingInfo.getFloorUnderground() != null){
            undergroundCount = buildingInfo.getFloorUnderground();
        }
        buildingVo.setOvergroundCount(overgroundCount);
        buildingVo.setUndergroundCount(undergroundCount);
        buildingVo.setProjectId(projectId);
        buildingVo.setCatalog(CatalogType.House.getType());

        List<UnitVo> unitVos = new ArrayList<>();
        if(unitInfoList != null && unitInfoList.size() > 0){
            for (ProjectUnitInfo unitInfo:
                    unitInfoList) {
                UnitVo unitVo = new UnitVo();
                unitVo.setName(unitInfo.getUnitName());
                unitVo.setNumber(unitInfo.getUnitCode()==null?unitInfo.getUnitName():unitInfo.getUnitCode());
                unitVo.setSeq(unitInfo.getSeq());
                unitVo.setSourceID(unitInfo.getUnitId());
                unitVo.setSourceSystem(Constant.source);
                unitVos.add(unitVo);
            }
        }




        buildingVo.setBuildingUnitVoList(unitVos);
        //R r = remoteBuildingService.addBuilding(buildingVo);
        //构造缓存数据
        PushData pushData = new PushData();
        pushData.setDataType(PushData.DataType.Building.getCode());
        pushData.setOperateType(PushData.OperateType.Insert.getCode());
        pushData.setTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
        pushData.setData(JSONObject.toJSONString(buildingVo));
        log.info("楼栋数据存入缓存："+JSONObject.toJSONString(pushData));
        RedisUtil.lSet("wjy_project"+projectId,JSONObject.toJSONString(pushData));
    }

    private void saveRoomToRedis(Integer projectId,
                                 ProjectBuildingInfo buildingInfo,
                                 ProjectUnitInfo unitInfo,
                                 ProjectHouseInfo houseInfo){
        //封装房间数据
        RoomStandardVo roomVo = new RoomStandardVo();
        roomVo.setRoomName(buildingInfo.getBuildingName()+unitInfo.getUnitName()+houseInfo.getHouseName());
        roomVo.setRoomNumber(houseInfo.getHouseCode()==null?houseInfo.getHouseName():houseInfo.getHouseCode());
        roomVo.setFloor(houseInfo.getFloor()==null?null:houseInfo.getFloor().toString());

        //String unitId = dataJson.getString("buildingUnit");
        roomVo.setBuildingName(buildingInfo.getBuildingName());
        roomVo.setBuildUnitName(unitInfo.getUnitName());

        roomVo.setProperty(RoomProperty.House.getType());//住宅
        //roomVo.setBuildingArea(128);
        Integer roomArea = 88;
        if(houseInfo.getHouseArea() != null && houseInfo.getHouseArea().intValue() > 0){
            roomArea = houseInfo.getHouseArea().intValue();
        }
        roomVo.setRoomArea(roomArea);
        roomVo.setBuildingArea(roomArea);
        roomVo.setProductName(Constant.productName);//
        //roomVo.setAttributeName("A户型-3房1室1厅");//

        roomVo.setSourceID(houseInfo.getHouseId());
        roomVo.setSourceSystem(Constant.source);

        roomVo.setFeeStatus(4);
        String usageType = houseInfo.getUsageType();
        if("1".equals(usageType)){//自用
            roomVo.setStatus(1);
        }else if("2".equals(usageType)){//出租
            roomVo.setStatus(2);
        }else if("3".equals(usageType)){//闲置
            roomVo.setStatus(0);
        }
        roomVo.setProjectId(projectId);

        roomVo.setUnitId(unitInfo.getUnitId());
        //R r = remoteRoomService.addRoom(roomVo);
        //System.out.println(JSONObject.toJSONString(r));
        //构造缓存数据
        PushData pushData = new PushData();
        pushData.setDataType(PushData.DataType.Room.getCode());
        pushData.setOperateType(PushData.OperateType.Insert.getCode());
        pushData.setTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
        pushData.setData(JSONObject.toJSONString(roomVo));
        log.info("房间数据存入缓存："+JSONObject.toJSONString(pushData));
        RedisUtil.lSet("wjy_project"+projectId,JSONObject.toJSONString(pushData));
    }

    public void saveCustomerToRedis(Integer projectId,ProjectPersonInfoVo personInfoVo){
        //封装客户数据
        CustomerStandardVo cus = new CustomerStandardVo();
        cus.setName(personInfoVo.getPersonName());
        cus.setPhone(personInfoVo.getTelephone());
        cus.setTelephone(personInfoVo.getTelephone());
        cus.setType(CustomerType.CustomerTypePersonal.getType());
        cus.setCertType(CertType.MainlandIdentityCard.getType());
        cus.setCertNo(personInfoVo.getCredentialNo());
        if("1".equals(personInfoVo.getGender())){//男
            cus.setSex("0");
        }else if("2".equals(personInfoVo.getGender())){//女
            cus.setSex("1");
        }

        //cus.setBirthday("1999-01-09");
        cus.setSourceID(personInfoVo.getPersonId());
        cus.setSourceSystem(Constant.source);
        cus.setProjectId(projectId);
        //R r = remoteCustomerService.addStandardCus(cus);
        //System.out.println(JSONObject.toJSONString(r));
        //构造缓存数据
        PushData pushData = new PushData();
        pushData.setDataType(PushData.DataType.Customer.getCode());
        pushData.setOperateType(PushData.OperateType.Insert.getCode());
        pushData.setTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
        pushData.setData(JSONObject.toJSONString(cus));
        log.info("客户数据存入缓存："+JSONObject.toJSONString(pushData));
        RedisUtil.lSet("wjy_project"+projectId,JSONObject.toJSONString(pushData));
    }

    public void saveRoomCustomerBindToRedis(Integer projectId,
                                            ProjectBuildingInfo buildingInfo,
                                            ProjectUnitInfo unitInfo,
                                            ProjectHouseInfo houseInfo,
                                            ProjectPersonInfoVo personInfoVo,
                                            ProjectHousePersonRel rel){

        BindCustomer2Vo bindCustomer2Vo = new BindCustomer2Vo();

        //封装客户数据
        CustomerStandardVo cus = new CustomerStandardVo();
        cus.setName(personInfoVo.getPersonName());
        cus.setPhone(personInfoVo.getTelephone());
        cus.setType(CustomerType.CustomerTypePersonal.getType());
        cus.setCertType(CertType.MainlandIdentityCard.getType());
        cus.setCertNo(personInfoVo.getCredentialNo());
        if("1".equals(personInfoVo.getGender())){//男
            cus.setSex("0");
        }else if("2".equals(personInfoVo.getGender())){//女
            cus.setSex("1");
        }
        cus.setSourceID(personInfoVo.getPersonId());
        cus.setSourceSystem(Constant.source);

        //cus.setBirthday("1999-01-09");
        cus.setProjectId(projectId);

        BindCustomerVo bindCustomerVo = new BindCustomerVo();
        bindCustomerVo.setBuildName(buildingInfo.getBuildingName());
        bindCustomerVo.setUnitName(unitInfo.getUnitName());
        bindCustomerVo.setRoomName(buildingInfo.getBuildingName()+unitInfo.getUnitName()+houseInfo.getHouseName());
        bindCustomerVo.setCustomerName(personInfoVo.getPersonName());
        if(rel.getHouseholdType().equals("1")){
            bindCustomerVo.setType("0");
        }else{
            bindCustomerVo.setType("2");
        }

        bindCustomerVo.setCustomerPhone(personInfoVo.getTelephone());
        bindCustomerVo.setProjectId(projectId);
        bindCustomerVo.setCustomerType(CustomerType.CustomerTypePersonal.getType());
        bindCustomerVo.setIsCharge("1");

        bindCustomer2Vo.setCustomerStandardVo(cus);
        bindCustomer2Vo.setBindCustomerVo(bindCustomerVo);

        //构造缓存数据
        PushData pushData = new PushData();
        pushData.setDataType(PushData.DataType.Relationship.getCode());
        pushData.setOperateType(PushData.OperateType.Insert.getCode());
        pushData.setTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
        pushData.setData(JSONObject.toJSONString(bindCustomer2Vo));
        log.info("房客关系数据存入缓存："+JSONObject.toJSONString(pushData));
        RedisUtil.lSet("wjy_project"+projectId,JSONObject.toJSONString(pushData));
    }

    public void saveWorkerToRedis(Project project, ProjectStaff projectStaff){
        //封装客户数据
        WorkerVo workerVo = new WorkerVo();
        //workerVo
        //cus.setBirthday("1999-01-09");
        workerVo.setNumber(projectStaff.getMobile());
        workerVo.setName(projectStaff.getStaffName());
        workerVo.setPhone(projectStaff.getMobile());
        workerVo.setSourceID(projectStaff.getStaffId());
        workerVo.setSourceSystem(Constant.source);
        workerVo.setOrgUnitNames(project.getOrgName());
        workerVo.setProjectId(project.getProjectId());
        //R r = remoteCustomerService.addStandardCus(cus);
        //System.out.println(JSONObject.toJSONString(r));
        //构造缓存数据
        PushData pushData = new PushData();
        pushData.setDataType(PushData.DataType.Worker.getCode());
        pushData.setOperateType(PushData.OperateType.Insert.getCode());
        pushData.setTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
        pushData.setData(JSONObject.toJSONString(workerVo));
        log.info("员工数据存入缓存："+JSONObject.toJSONString(pushData));
        RedisUtil.lSet("wjy_project"+project.getProjectId(),JSONObject.toJSONString(pushData));
    }
}