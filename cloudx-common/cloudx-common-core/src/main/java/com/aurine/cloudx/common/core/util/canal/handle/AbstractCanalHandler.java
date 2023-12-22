package com.aurine.cloudx.common.core.util.canal.handle;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Canal事件处理器
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-23
 * @Copyright:
 */
@Slf4j
public abstract class AbstractCanalHandler {

    /**
     * 处理事件方法
     *
     * @param canalEntry 事件信息
     */
    public abstract boolean handle(Entry canalEntry);

    /**
     * 执行完成
     *
     * @return
     */
    public boolean done() {
        return false;
    }

    /**
     * 不予执行,跳转到下一个执行方法
     *
     * @return
     */
    public boolean next() {
        return true;
    }

    /**
     * 打印列信息
     *
     * @param columns
     */
    protected void printColumn(List<Column> columns) {
        for (Column column : columns) {
            log.info(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }

    /**
     * 获取主键对象
     *
     * @param columnList
     * @return
     */
    protected Column getPrimeColumn(List<Column> columnList) {
        return this.getByName(columnList, "seq");
    }

    /**
     * 获取指定字段数据
     *
     * @param columName
     * @param columnList
     * @return
     */
    protected Column getByName(List<Column> columnList, String columName) {
        for (Column column : columnList) {
            if (StringUtils.equalsIgnoreCase(column.getName(), columName)) return column;
        }

        return null;
    }


    /**
     * 获取指定字段数据
     *
     * @param columNameArray
     * @param columnList
     * @return
     */
    protected List<Column> getListByName(List<Column> columnList, String... columNameArray) {
        List<Column> resultList = new ArrayList<>();
        for (Column column : columnList) {
            for (int i = 0; i < columNameArray.length; i++) {
                if (column.getName().equals(columNameArray[i])) {
                    resultList.add(column);
                    break;
                }
            }
        }

        return resultList;
    }

    /**
     * 字段是否更新
     * @param column
     * @return
     */
    protected boolean isUpdated(Column column){
        return column.getUpdated();
    }

    /**
     * 返回projectId
     *
     * @param columnList
     * @return
     */
    protected String getProjectId(List<Column> columnList) {
        String projectId = null;
        Column column = getByName(columnList, "projectId");

        if (column != null) {//如果处在projectId字段信息
            if (StringUtils.isNotEmpty(column.getValue()) && StringUtils.isNumeric(column.getValue())) {//如果数据存储信息正常
                projectId = column.getValue();
            }
        }

        return projectId;
    }

    /**
     * 打印当前事件日志
     * @param canalEntry
     * @param eventType
     */
    protected void logCanalEntry(Entry canalEntry, EventType eventType) {
        log.info("[canal] 通行数据信息 binlog {} {},name {} {}, 事件类型：{}",
                canalEntry.getHeader().getLogfileName(),
                canalEntry.getHeader().getLogfileOffset(),
                canalEntry.getHeader().getSchemaName(),
                canalEntry.getHeader().getTableName(),
                eventType);
    }

}
