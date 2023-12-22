package com.aurine.cloudx.dashboard.handler;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.aurine.cloudx.dashboard.canal.handle.AbstractCanalHandler;
import com.aurine.cloudx.dashboard.canal.util.CanalDataUtil;
import com.aurine.cloudx.dashboard.constant.ServiceNameEnum;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.vo.Result;
import com.aurine.cloudx.dashboard.websocket.WebSocketUtil;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 门禁通行记录处理器
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-23
 * @Copyright:
 */

@Component
@Slf4j
public class EntranceEventHandlerV1 extends AbstractCanalHandler {

    private final String eventName = ServiceNameEnum.ENTRANCE_EVENT.serviceName;
    private final String version = VersionEnum.V1.number;
    private final String TABLE_NAME = "project_entrance_event";

    /**
     * 处理事件方法
     * <p>
     * seq : 22901    update=true
     * projectId : 1000000292    update=true
     * personType : 1    update=true
     * personId : 205bd647e6a411ea9c28fa163eabdf31    update=true
     * personName : 伍球生    update=true
     * eventTime : 2021-03-24 16:29:25    update=true
     * deviceId : 8ee9d823ecd4efde2a701698ebcb9cec    update=true
     * deviceName : 大门区口机    update=true
     * deviceRegionName : A片区    update=true
     * entranceType :     update=true
     * eventType : 1    update=true
     * certMedia : 2    update=true
     * picUrl : https://icloudobs.aurine.cn:7003/eventserver/community/S1000000292/device/P1OQJ20M080285BZBP4K/P1OQJ20M080285BZBP4K_1616574565.jpg    update=true
     * eventDesc : 使用面部识别开门    update=true
     * eventStatus :     update=true
     * remark :     update=true
     * tenant_id : 1    update=true
     * operator :     update=true
     * createTime : 2021-03-24 16:29:25    update=true
     * updateTime : 2021-03-24 16:29:25    update=true
     *
     * @param canalEntry 事件信息
     */
    @Override
    public boolean handle(Entry canalEntry) {
        //init
        String tableName = canalEntry.getHeader().getTableName();
        RowChange rowChange = CanalDataUtil.getRowChange(canalEntry);
        EventType eventType = rowChange.getEventType();


        //----------------条件筛选 开始----------------
        if (!StringUtils.equalsIgnoreCase(tableName, TABLE_NAME)) {
            return next();
        }

        if (eventType != EventType.INSERT) {
            return next();
        }
        //----------------条件筛选 结束----------------

        super.logCanalEntry(canalEntry, eventType);

        //获取指定记录列，并转换为JSON数据
        for (RowData rowData : rowChange.getRowDatasList()) {
            List<Column> columnList = this.getListByName(rowData.getAfterColumnsList(), "projectId", "deviceId", "personType", "personName", "deviceName", "createTime", "eventDesc", "picUrl");

            if (CollUtil.isNotEmpty(columnList)) {
                JSONObject dataJson = new JSONObject();
                for (Column column : columnList) {
                    dataJson.put(column.getName(), column.getValue());
                }

                String projectId = getProjectId(rowData.getAfterColumnsList());

                //推送数据给客户端
                WebSocketUtil.sendMessgae(Result.ok(dataJson, projectId, eventName, version), projectId, eventName, version);
            }

        }


        return done();
    }


}
