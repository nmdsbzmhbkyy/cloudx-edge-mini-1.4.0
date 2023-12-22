package com.aurine.cloudx.wjy.canal.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.aurine.cloudx.wjy.canal.utils.CanalDataUtil;
import com.aurine.cloudx.wjy.constant.CertType;
import com.aurine.cloudx.wjy.constant.Constant;
import com.aurine.cloudx.wjy.constant.CustomerType;
import com.aurine.cloudx.wjy.vo.CustomerStandardVo;
import com.aurine.cloudx.wjy.vo.RoomStandardVo;
import com.aurine.cloudx.wjy.pojo.PushData;
import com.aurine.cloudx.wjy.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/5/12
 * @description： 房间数据变化处理
 */
@Component
@Slf4j
public class CustomerCanalHandle extends AbstractCanalHandler {

    private final String TABLE_NAME = "project_person_info";

    /*<resultMap id="projectPersonInfoMap" type="com.aurine.cloudx.estate.entity.ProjectPersonInfo">
        <id property="personId" column="personId"/>
        <result property="personCode" column="personCode"/>
        <result property="personName" column="personName"/>
        <result property="credentialType" column="credentialType"/>
        <result property="credentialNo" column="credentialNo"/>
        <result property="source" column="source"/>
        <result property="domiciile" column="domiciile"/>
        <result property="nationCode" column="nationCode"/>
        <result property="birth" column="birth"/>
        <result property="gender" column="gender"/>
        <result property="provinceCode" column="provinceCode"/>
        <result property="cityCode" column="cityCode"/>
        <result property="countyCode" column="countyCode"/>
        <result property="streetCode" column="streetCode"/>
        <result property="address" column="address"/>
        <result property="picUrl" column="picUrl"/>
        <result property="origin" column="origin"/>
        <result property="peopleTypeCode" column="peopleTypeCode"/>
        <result property="residence" column="residence"/>
        <result property="educationCode" column="educationCode"/>
        <result property="maritalStatusCode" column="maritalStatusCode"/>
        <result property="spouseName" column="spouseName"/>
        <result property="spouseIdType" column="spouseIdType"/>
        <result property="spouseIdNo" column="spouseIdNo"/>
        <result property="nationalityCode" column="nationalityCode"/>
        <result property="entryTime" column="entryTime"/>
        <result property="surnameEng" column="surnameEng"/>
        <result property="nameEng" column="nameEng"/>
        <result property="telephone" column="telephone"/>
        <result property="ownName" column="ownName"/>
        <result property="ownIdType" column="ownIdType"/>
        <result property="ownIdNo" column="ownIdNo"/>
        <result property="entranceTypeCode" column="entranceTypeCode"/>
        <result property="tag" column="tag"/>
        <result property="passBeginTime" column="passBeginTime"/>
        <result property="passEndTime" column="passEndTime"/>
        <result property="operator" column="operator"/>
        <result property="createTime" column="createTime"/>
        <result property="updateTime" column="updateTime"/>
        <result property="remark" column="remark"/>
    </resultMap>*/

    @Override
    public boolean handle(CanalEntry.Entry canalEntry) {
        //init
        String tableName = canalEntry.getHeader().getTableName();
        CanalEntry.RowChange rowChange = CanalDataUtil.getRowChange(canalEntry);
        CanalEntry.EventType eventType = rowChange.getEventType();

        /*log.info("canal接收到数据："+tableName);
        log.info("canal接收到数据："+JSONObject.toJSONString(eventType));*/

        //----------------条件筛选 开始----------------
        if (!StringUtils.equalsIgnoreCase(tableName, TABLE_NAME)) {
            return next();
        }

        if (eventType != CanalEntry.EventType.INSERT) {
            return next();
        }
        //----------------条件筛选 结束----------------

        super.logCanalEntry(canalEntry, eventType);

        //记录行
        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
            //获取指定记录列，并转换为JSON数据
            List<CanalEntry.Column> columnList = this.getListByName(rowData.getAfterColumnsList(), "personId", "personName", "telephone", "credentialNo", "credentialType","gender");

            if (CollUtil.isNotEmpty(columnList)) {
                JSONObject dataJson = new JSONObject();
                for (CanalEntry.Column column : columnList) {
                    dataJson.put(column.getName(), column.getValue());
                }
                String projectId = getProjectId(rowData.getAfterColumnsList());
                if(!RedisUtil.sExistValue("wjy_project_set",projectId)){//判断该项目是否需要同步我家云
                    return next();
                }
                //封装客户数据
                CustomerStandardVo cus = new CustomerStandardVo();
                cus.setName(dataJson.getString("personName"));
                cus.setPhone(dataJson.getString("telephone"));
                cus.setTelephone(dataJson.getString("telephone"));
                cus.setType(CustomerType.CustomerTypePersonal.getType());
                cus.setCertType(CertType.MainlandIdentityCard.getType());
                cus.setCertNo(dataJson.getString("credentialNo"));
                Integer gender = StringUtils.isNotBlank(dataJson.getString("gender"))?Integer.valueOf(dataJson.getString("gender")):1;
                if(gender == 1){
                    cus.setSex("0");
                }else if(gender == 2){
                    cus.setSex("1");
                }
                cus.setSourceID(dataJson.getString("personId"));
                cus.setSourceSystem(Constant.source);
                //cus.setBirthday("1999-01-09");
                cus.setProjectId(Integer.valueOf(projectId));
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

        }


        return done();
    }
}