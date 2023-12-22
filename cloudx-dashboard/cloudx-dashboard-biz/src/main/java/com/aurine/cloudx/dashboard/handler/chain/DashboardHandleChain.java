package com.aurine.cloudx.dashboard.handler.chain;

import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.aurine.cloudx.dashboard.canal.handle.AbstractCanalHandleChain;
import com.aurine.cloudx.dashboard.canal.handle.AbstractCanalHandler;
import com.aurine.cloudx.dashboard.handler.EntranceAlarmEventHandlerV1;
import com.aurine.cloudx.dashboard.handler.EntranceEventHandlerV1;
import com.aurine.cloudx.dashboard.handler.ParkEntranceHisInHandlerV1;
import com.aurine.cloudx.dashboard.handler.ParkEntranceHisOutHandlerV1;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: DashBoard Mysql binlog事件监听处理链
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-23
 * @Copyright:
 */
@Component
public class DashboardHandleChain extends AbstractCanalHandleChain {

    @Resource
    private EntranceEventHandlerV1 entranceEventHandlerV1;
    @Resource
    private EntranceAlarmEventHandlerV1 entranceAlarmEventHandlerV1;
    @Resource
    private ParkEntranceHisInHandlerV1 parkEntranceHisInHandlerV1;
    @Resource
    private ParkEntranceHisOutHandlerV1 parkEntranceHisOutHandlerV1;

    /**
     * 执行处理链
     *
     * @param canalEvent 事件信息
     */
    @Override
    public AbstractCanalHandleChain doChain(Entry canalEvent) {
        this.canalEntry = canalEvent;
        return this
                .doHandle(entranceEventHandlerV1)
                .doHandle(entranceAlarmEventHandlerV1)
                .doHandle(parkEntranceHisInHandlerV1)
                .doHandle(parkEntranceHisOutHandlerV1)
                .done();
    }

    /**
     * 执行处理
     *
     * @param handler 处理器
     * @return
     */
    @Override
    public AbstractCanalHandleChain doHandle(AbstractCanalHandler handler) {
        if (nextFlag) {
            this.nextFlag = handler.handle(this.canalEntry);
        }
        return this;
    }

}
