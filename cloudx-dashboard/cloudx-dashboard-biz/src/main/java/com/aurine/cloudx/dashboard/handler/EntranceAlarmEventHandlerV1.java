package com.aurine.cloudx.dashboard.handler;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.aurine.cloudx.dashboard.canal.handle.AbstractCanalHandler;
import com.aurine.cloudx.dashboard.canal.util.CanalDataUtil;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import com.aurine.cloudx.dashboard.vo.Result;
import com.aurine.cloudx.dashboard.websocket.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 门禁告警记录处理器
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-26
 * @Copyright:
 */

@Component
@Slf4j
public class EntranceAlarmEventHandlerV1 extends AbstractCanalHandler {

    private final String eventName = "entranceAlarmEvent";
    private final String version = VersionEnum.V1.number;
    private final String TABLE_NAME = "project_entrance_alarm_event";

    /**
     * <p>
     * `seq` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '序列，自增',
     * `eventId` varchar(32) NOT NULL COMMENT '事件id，uuid',
     * `personType` varchar(5) DEFAULT NULL COMMENT '人员类型',
     * `personName` varchar(64) DEFAULT NULL COMMENT '姓名',
     * `deviceId` varchar(32) DEFAULT NULL COMMENT '设备id',
     * `deviceName` varchar(64) DEFAULT NULL COMMENT '设备名称',
     * `deviceRegionName` varchar(64) DEFAULT NULL COMMENT '设备区域名称',
     * `eventDesc` varchar(128) DEFAULT NULL COMMENT '事件描述',
     * `eventTime` datetime DEFAULT NULL COMMENT '事件时间',
     * `picUrl` varchar(255) DEFAULT NULL COMMENT '抓拍图片',
     * `status` varchar(5) DEFAULT NULL COMMENT '状态',
     * `projectId` int(11) NOT NULL COMMENT '项目id',
     * `tenant_id` int(11) NOT NULL,
     * `operator` int(11) DEFAULT NULL COMMENT '创建人',
     * `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     * `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
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

        //记录行
        for (RowData rowData : rowChange.getRowDatasList()) {
            //获取指定记录列，并转换为JSON数据
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
