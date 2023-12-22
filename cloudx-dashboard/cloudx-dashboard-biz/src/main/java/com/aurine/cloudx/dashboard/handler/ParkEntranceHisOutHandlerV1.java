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
 * @description: 车行出场数据处理器
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-29
 * @Copyright:
 */

@Component
@Slf4j
public class ParkEntranceHisOutHandlerV1 extends AbstractCanalHandler {

    private final String eventName = "parkEntranceHisOut";
    private final String version = VersionEnum.V1.number;
    private final String TABLE_NAME = "project_park_entrance_his";

    /**
     * 处理事件方法
     * <p>
     * `seq` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
     * `parkOrderNo` varchar(32) NOT NULL COMMENT '停车订单号',
     * `parkOrderCode` varchar(32) DEFAULT NULL COMMENT '第三方订单号',
     * `plateNumber` varchar(15) NOT NULL COMMENT '车牌号',
     * `parkId` varchar(32) NOT NULL COMMENT '停车场ID',
     * `enterTime` datetime NOT NULL COMMENT '入场时间',
     * `enterGateName` varchar(64) DEFAULT NULL COMMENT '入口名称',
     * `enterOperatorName` varchar(64) DEFAULT NULL COMMENT '入口操作员',
     * `enterPicUrl` varchar(256) DEFAULT NULL COMMENT '入口车辆图片',
     * `outTime` datetime DEFAULT NULL COMMENT '出厂时间',
     * `outGateName` varchar(64) DEFAULT NULL COMMENT '出口名称',
     * `outOperatorName` varchar(64) DEFAULT NULL COMMENT '出口操作员',
     * `outPicUrl` varchar(256) DEFAULT NULL COMMENT '出口车辆图片',
     * `projectId` int(11) NOT NULL COMMENT '项目ID',
     * `tenant_id` int(11) NOT NULL,
     * `operator` int(11) DEFAULT NULL COMMENT '操作人',
     * `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
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

        if (eventType != EventType.UPDATE) {
            return next();
        }
        //----------------条件筛选 结束----------------

        super.logCanalEntry(canalEntry, eventType);

        //获取指定记录列，并转换为JSON数据
        for (RowData rowData : rowChange.getRowDatasList()) {
            Column picColumn = this.getByName(rowData.getAfterColumnsList(), "outPicUrl");
            if (picColumn == null || !picColumn.getUpdated()){
                //出场图片为未更新或无字段，跳过事件（出场、出场抓拍均会触发该事件）
                return next();
            }

            List<Column> columnList = this.getListByName(rowData.getAfterColumnsList(), "projectId", "plateNumber", "enterTime", "enterGateName", "enterPicUrl","outTime","outGateName","outPicUrl");

            if (CollUtil.isNotEmpty(columnList)) {
                JSONObject dataJson = new JSONObject();
                for (Column column : columnList) {
                    dataJson.put(column.getName(), column.getValue());
                }

                dataJson.put("outDeviceId", "bfbe1cd3903c5dd5e053be00a8c04b3c");

                String projectId = getProjectId(rowData.getAfterColumnsList());

                //推送数据给客户端
                WebSocketUtil.sendMessgae(Result.ok(dataJson, projectId, eventName, version), projectId, eventName, version);
            }

        }


        return done();
    }


}
