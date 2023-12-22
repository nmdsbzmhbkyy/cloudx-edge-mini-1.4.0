package com.aurine.cloudx.wjy.canal.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.aurine.cloudx.wjy.canal.utils.CanalDataUtil;
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
public class RoomCanalHandle extends AbstractCanalHandler {

    private final String TABLE_NAME = "project_room_info";

    /*<resultMap id="projectHouseInfoMap" type="com.aurine.cloudx.estate.entity.ProjectHouseInfo">
        <result property="houseId" column="houseId"/>
        <result property="houseCode" column="houseCode"/>
        <result property="buildingUnit" column="buildingUnit"/>
        <result property="floor" column="floor"/>
        <result property="houseDesginId" column="houseDesginId"/>
        <result property="houseName" column="houseName"/>
        <result property="houseLabelCode" column="houseLabelCode"/>
        <result property="houseLabel" column="houseLabel"/>
        <result property="housePurposeCode" column="housePurposeCode"/>
        <result property="housePurpose" column="housePurpose"/>
        <result property="houseArea" column="houseArea"/>
        <result property="personNumber" column="personNumber"/>
        <result property="note" column="note"/>
        <result property="lon" column="lon"/>
        <result property="lat" column="lat"/>
        <result property="alt" column="alt"/>
        <result property="gisArea" column="gisArea"/>
        <result property="gisType" column="gisType"/>
        <result property="usageType" column="usageType"/>
        <result property="propertyType" column="propertyType"/>
        <result property="locationCode" column="locationCode"/>
        <result property="locationName" column="locationName"/>
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
            List<CanalEntry.Column> columnList = this.getListByName(rowData.getAfterColumnsList(), "houseId", "houseCode", "houseName", "floor", "buildingUnit", "houseArea","usageType");

            if (CollUtil.isNotEmpty(columnList)) {
                JSONObject dataJson = new JSONObject();
                for (CanalEntry.Column column : columnList) {
                    dataJson.put(column.getName(), column.getValue());
                }
                String projectId = getProjectId(rowData.getAfterColumnsList());
                if(!RedisUtil.sExistValue("wjy_project_set",projectId)){//判断该项目是否需要同步我家云
                    return next();
                }
                //封装房间数据
                RoomStandardVo roomVo = new RoomStandardVo();
                roomVo.setRoomName(dataJson.getString("houseName"));
                roomVo.setRoomNumber(dataJson.getString("houseCode"));
                roomVo.setFloor(dataJson.getString("floor"));

                String unitId = dataJson.getString("buildingUnit");

                roomVo.setBuildingName("A栋");
                roomVo.setBuildUnitName("A栋1单元");

                roomVo.setProperty(0);//住宅
                //roomVo.setBuildingArea(128);
                roomVo.setRoomArea(dataJson.getInteger("houseArea"));

                roomVo.setProductName("门禁系统");//
                roomVo.setAttributeName("A户型-3房1室1厅");//

                roomVo.setSourceID(dataJson.getString("houseId"));
                roomVo.setSourceSystem("aurine");

                roomVo.setFeeStatus(4);
                String usageType = dataJson.getString("usageType");
                if("1".equals(usageType)){//自用
                    roomVo.setStatus(1);
                }else if("2".equals(usageType)){//出租
                    roomVo.setStatus(2);
                }else if("3".equals(usageType)){//闲置
                    roomVo.setStatus(0);
                }
                roomVo.setProjectId(Integer.valueOf(projectId));

                roomVo.setUnitId(unitId);
                //R r = remoteRoomService.addRoom(roomVo);
                //System.out.println(JSONObject.toJSONString(r));
                //构造缓存数据
                PushData pushData = new PushData();
                pushData.setDataType(PushData.DataType.Room.getCode());
                pushData.setOperateType(PushData.OperateType.Insert.getCode());
                pushData.setTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
                pushData.setData(JSONObject.toJSONString(roomVo));
                log.info("房间数据存入缓存："+JSONObject.toJSONString(pushData));
                //RedisUtil.lSet("wjy_project"+projectId,JSONObject.toJSONString(pushData));
            }

        }


        return done();
    }
}