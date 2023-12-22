package com.aurine.cloudx.wjy.canal.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.aurine.cloudx.estate.entity.ProjectBuildingInfo;
import com.aurine.cloudx.estate.entity.ProjectHouseInfo;
import com.aurine.cloudx.estate.entity.ProjectUnitInfo;
import com.aurine.cloudx.estate.feign.RemoteBuildingInfoService;
import com.aurine.cloudx.estate.feign.RemoteHouseInfoService;
import com.aurine.cloudx.estate.feign.RemotePersonInfoService;
import com.aurine.cloudx.estate.feign.RemoteUnitInfoService;
import com.aurine.cloudx.estate.vo.ProjectPersonInfoVo;
import com.aurine.cloudx.wjy.canal.utils.CanalDataUtil;
import com.aurine.cloudx.wjy.constant.CertType;
import com.aurine.cloudx.wjy.constant.Constant;
import com.aurine.cloudx.wjy.constant.CustomerType;
import com.aurine.cloudx.wjy.pojo.PushData;
import com.aurine.cloudx.wjy.utils.RedisUtil;
import com.aurine.cloudx.wjy.vo.BindCustomer2Vo;
import com.aurine.cloudx.wjy.vo.BindCustomerVo;
import com.aurine.cloudx.wjy.vo.CustomerStandardVo;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/5/12
 * @description： 房间数据变化处理
 */
@Component
@Slf4j
public class RelCanalHandle extends AbstractCanalHandler {

    private final String TABLE_NAME = "project_house_person_rel";

    @Resource
    RemoteBuildingInfoService remoteBuildingInfoService;
    @Resource
    RemoteUnitInfoService remoteUnitInfoService;
    @Resource
    RemoteHouseInfoService remoteHouseInfoService;
    @Resource
    RemotePersonInfoService remotePersonInfoService;

    /*<resultMap id="projectHousePersonRelMap" type="com.aurine.cloudx.estate.entity.ProjectHousePersonRel">
        <id property="relaId" column="relaId"/>
        <result property="relaCode" column="relaCode"/>
        <result property="houseId" column="houseId"/>
        <result property="personId" column="personId"/>
        <result property="housePeopleRel" column="housePeopleRel"/>
        <result property="isOwner" column="isOwner"/>
        <result property="status" column="status"/>
        <result property="operator" column="operator"/>
        <result property="createTime" column="createTime"/>
        <result property="updateTime" column="updateTime"/>
    </resultMap>*/

    @Override
    public boolean handle(CanalEntry.Entry canalEntry) {
        //init
        String tableName = canalEntry.getHeader().getTableName();
        CanalEntry.RowChange rowChange = CanalDataUtil.getRowChange(canalEntry);
        CanalEntry.EventType eventType = rowChange.getEventType();

        //log.info("canal接收到数据："+tableName);
        //log.info("canal接收到数据："+JSONObject.toJSONString(eventType));

        //----------------条件筛选 开始----------------
        if (!StringUtils.equalsIgnoreCase(tableName, TABLE_NAME)) {
            return next();
        }

        if (!(eventType == CanalEntry.EventType.INSERT || eventType == CanalEntry.EventType.DELETE)) {
            return next();
        }
        //----------------条件筛选 结束----------------

        super.logCanalEntry(canalEntry, eventType);

        //记录行
        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
            //获取指定记录列，并转换为JSON数据
            List<CanalEntry.Column> columnList = null;

            String projectId = null;
            if (eventType == CanalEntry.EventType.INSERT){
                columnList = this.getListByName(rowData.getAfterColumnsList(), "personId",
                        "houseId","householdType");
                projectId = getProjectId(rowData.getAfterColumnsList());
            }else if(eventType == CanalEntry.EventType.DELETE){
                columnList = this.getListByName(rowData.getBeforeColumnsList(), "personId",
                        "houseId","householdType");
                projectId = getProjectId(rowData.getBeforeColumnsList());
            }
            System.out.println("列数据："+columnList.size());
            if (CollUtil.isNotEmpty(columnList)) {
                JSONObject dataJson = new JSONObject();
                for (CanalEntry.Column column : columnList) {
                    dataJson.put(column.getName(), column.getValue());
                }
                if(!RedisUtil.sExistValue("wjy_project_set",projectId)){//判断该项目是否需要同步我家云
                    return next();
                }

                String personId = dataJson.getString("personId");
                String houseId = dataJson.getString("houseId");
                String householdType = dataJson.getString("householdType");

                //构造缓存数据
                PushData pushData = new PushData();
                pushData.setDataType(PushData.DataType.Relationship.getCode());
                pushData.setTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
                if (eventType == CanalEntry.EventType.INSERT){
                    System.out.println("增加房客数据");
                    //读取住户信息
                    R<ProjectPersonInfoVo> rCustomer = remotePersonInfoService.innerGetById(personId, SecurityConstants.FROM_IN);
                    JSONObject customeObj = (JSONObject) JSONObject.toJSON(rCustomer.getData());
                    if(customeObj == null){
                        continue;
                    }
                    ProjectPersonInfoVo personInfoVo = customeObj.toJavaObject(ProjectPersonInfoVo.class);
                    System.out.println(JSONObject.toJSONString(personInfoVo));

                    //读取房间信息
                    R<ProjectHouseInfo> rRoom = remoteHouseInfoService.innerInfoById(houseId,SecurityConstants.FROM_IN);
                    JSONObject roomObj = (JSONObject) JSONObject.toJSON(rRoom.getData());
                    if(roomObj == null){
                        continue;
                    }
                    ProjectHouseInfo houseInfo = roomObj.toJavaObject(ProjectHouseInfo.class);
                    System.out.println(JSONObject.toJSONString(houseInfo));
                    //读取单元信息
                    R<ProjectUnitInfo> rUnit = remoteUnitInfoService.innerUnitInfo(houseInfo.getBuildingUnit(),SecurityConstants.FROM_IN);
                    JSONObject unitObj = (JSONObject) JSONObject.toJSON(rUnit.getData());
                    if(unitObj == null){
                        continue;
                    }
                    ProjectUnitInfo unitInfo = unitObj.toJavaObject(ProjectUnitInfo.class);
                    System.out.println(JSONObject.toJSONString(unitInfo));
                    //读取楼宇信息
                    R<String> rBuildingId = remoteUnitInfoService.innerGetBuildingId(unitInfo.getUnitId(),SecurityConstants.FROM_IN);
                    String buildingId = rBuildingId.getData();
                    System.out.println(buildingId);
                    R<ProjectBuildingInfo> rBuilding = remoteBuildingInfoService.innerBuildingInfo(buildingId, SecurityConstants.FROM_IN);
                    JSONObject builingObj = (JSONObject) JSONObject.toJSON(rBuilding.getData());
                    ProjectBuildingInfo buildingInfo = builingObj.toJavaObject(ProjectBuildingInfo.class);
                    System.out.println(JSONObject.toJSONString(buildingInfo));


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
                    cus.setProjectId(Integer.valueOf(projectId));

                    BindCustomerVo bindCustomerVo = new BindCustomerVo();
                    bindCustomerVo.setBuildName(buildingInfo.getBuildingName());
                    bindCustomerVo.setUnitName(unitInfo.getUnitName());
                    bindCustomerVo.setRoomName(buildingInfo.getBuildingName()+unitInfo.getUnitName()+houseInfo.getHouseName());
                    bindCustomerVo.setCustomerName(personInfoVo.getPersonName());
                    if(householdType.equals("1")){
                        bindCustomerVo.setType("0");
                    }else{
                        bindCustomerVo.setType("2");
                    }

                    bindCustomerVo.setCustomerPhone(personInfoVo.getTelephone());
                    bindCustomerVo.setProjectId(Integer.valueOf(projectId));
                    bindCustomerVo.setCustomerType(CustomerType.CustomerTypePersonal.getType());
                    bindCustomerVo.setIsCharge("1");

                    bindCustomer2Vo.setCustomerStandardVo(cus);
                    bindCustomer2Vo.setBindCustomerVo(bindCustomerVo);
                    pushData.setOperateType(PushData.OperateType.Insert.getCode());
                    pushData.setData(JSONObject.toJSONString(bindCustomer2Vo));
                }else if(eventType == CanalEntry.EventType.DELETE){
                    pushData.setOperateType(PushData.OperateType.Delete.getCode());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("personId",personId);
                    jsonObject.put("roomId",houseId);
                    pushData.setData(jsonObject.toJSONString());
                }

                log.info("房客关系数据存入缓存："+JSONObject.toJSONString(pushData));
                RedisUtil.lSet("wjy_project"+projectId,JSONObject.toJSONString(pushData));
            }

        }


        return done();
    }
}