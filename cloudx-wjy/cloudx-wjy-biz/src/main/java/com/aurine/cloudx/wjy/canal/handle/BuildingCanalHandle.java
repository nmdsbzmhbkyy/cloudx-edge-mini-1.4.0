package com.aurine.cloudx.wjy.canal.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.aurine.cloudx.wjy.canal.utils.CanalDataUtil;
import com.aurine.cloudx.wjy.constant.CatalogType;
import com.aurine.cloudx.wjy.vo.BuildingVo;
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
 * @description： 楼栋数据变化处理
 */
@Component
@Slf4j
public class BuildingCanalHandle extends AbstractCanalHandler {

    private final String TABLE_NAME = "project_building_info";

    /*<resultMap id="buildingInfoMap" type="com.aurine.cloudx.estate.entity.ProjectBuildingInfo">
        <id property="buildingId" column="buildingId"/>
        <result property="buildingCode" column="buildingCode"/>
        <result property="buildingName" column="buildingName"/>
        <result property="floorTotal" column="floorTotal"/>
        <result property="houseTotal" column="houseTotal"/>
        <result property="unitTotal" column="unitTotal"/>
        <result property="unitNameType" column="unitNameType"/>
        <result property="buildingArea" column="buildingArea"/>
        <result property="buildingEra" column="buildingEra"/>
        <result property="lon" column="lon"/>
        <result property="lat" column="lat"/>
        <result property="alt" column="alt"/>
        <result property="gisArea" column="gisArea"/>
        <result property="gisType" column="gisType"/>
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
            List<CanalEntry.Column> columnList = this.getListByName(rowData.getAfterColumnsList(), "buildingId", "buildingCode", "buildingName", "floorGround", "floorUnderground", "buildingArea");

            if (CollUtil.isNotEmpty(columnList)) {
                JSONObject dataJson = new JSONObject();
                for (CanalEntry.Column column : columnList) {
                    dataJson.put(column.getName(), column.getValue());
                }
                String projectId = getProjectId(rowData.getAfterColumnsList());
                if(!RedisUtil.sExistValue("wjy_project_set",projectId)){//判断该项目是否需要同步我家云
                    return next();
                }
                //封装楼栋数据
                BuildingVo buildingVo = new BuildingVo();
                buildingVo.setName(dataJson.getString("buildingName"));
                buildingVo.setNumber(dataJson.getString("buildingCode"));
                buildingVo.setSourceID(dataJson.getString("buildingId"));
                buildingVo.setSourceSystem("aurine");
                buildingVo.setOvergroundCount(dataJson.getInteger("floorGround"));
                buildingVo.setUndergroundCount(dataJson.getInteger("floorUnderground"));
                buildingVo.setProjectId(Integer.valueOf(projectId));
                buildingVo.setCatalog(CatalogType.House.getType());
               /* List<UnitVo> unitVos = new ArrayList<>();
                UnitVo unitVo = new UnitVo();
                unitVo.setName("A栋1单元");
                unitVo.setNumber("0101");
                unitVo.setSeq(101);
                unitVo.setSourceID("1");
                unitVo.setSourceSystem("冠林源");
                unitVos.add(unitVo);
                buildingVo.setBuildingUnitVoList(unitVos);*/
                //R r = remoteBuildingService.addBuilding(buildingVo);
                //构造缓存数据
                PushData pushData = new PushData();
                pushData.setDataType(PushData.DataType.Building.getCode());
                pushData.setOperateType(PushData.OperateType.Insert.getCode());
                pushData.setTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
                pushData.setData(JSONObject.toJSONString(buildingVo));
                log.info("楼栋数据存入缓存："+JSONObject.toJSONString(pushData));
                //RedisUtil.lSet("wjy_project"+projectId,JSONObject.toJSONString(pushData));
            }

        }


        return done();
    }
}