package com.aurine.cloudx.dashboard.canal;//package com.aurine.dashboard.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.aurine.cloudx.dashboard.canal.handle.AbstractCanalHandleChain;
import com.aurine.cloudx.dashboard.config.CanalConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.List;


/**
 * Canal客户端
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-23
 * @Copyright:
 */
@Component
@Slf4j
public class CanalClient {

    @Resource
    private CanalConfig canalConfig;

    private CanalConnector canalConnector = null;



    /**
     * 连接到服务器，
     */
    public void connection(AbstractCanalHandleChain canalHandleChain) {
        if (canalConnector == null) {
            // 创建链接
            this.canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress(canalConfig.getHost(), canalConfig.getPort()), canalConfig.getDestination(), canalConfig.getUsername(), canalConfig.getPassword());
            try {
                canalConnector.connect();
//            connector.subscribe(".*\\..*"); //匹配所有库，所有表
                canalConnector.subscribe(canalConfig.getSubscribeRegex()); //规则匹配
                canalConnector.rollback();
                log.info("[canal] 客户端已连接：host:{}, destination:{}", canalConfig.getHost() + ":" + canalConfig.getPort(), canalConfig.getDestination());
                this.listen(canalHandleChain);
            } finally {
                canalConnector.disconnect();
                canalConnector = null;
            }
        } else {
            log.info("[canal] 连接已创建");
        }
    }

    /**
     * 监听binLog
     *
     * @param canalHandleChain
     */
    private void listen(AbstractCanalHandleChain canalHandleChain) {
        if (this.canalConnector == null) throw new NullPointerException("canal未连接");

        while (true) {
            Message message = canalConnector.getWithoutAck(canalConfig.getBatchSize()); // 获取指定数量的数据
            long batchId = message.getId();
            int size = message.getEntries().size();
            if (batchId == -1 || size == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            } else {
                handelEntry(message.getEntries(), canalHandleChain);
            }

            canalConnector.ack(batchId); // 提交确认
            // connector.rollback(batchId); // 处理失败, 回滚数据
        }
    }

    /**
     * 过滤并处理信息实例
     *
     * @param entrys
     * @param canalHandleChain
     */
    private void handelEntry(List<Entry> entrys, AbstractCanalHandleChain canalHandleChain) {
        for (Entry entry : entrys) {

            if (entry.getEntryType() != EntryType.ROWDATA) {
                continue;
            }

            canalHandleChain.doChain(entry);


        }
    }

    private void printColumn(List<Column> columns) {
        for (Column column : columns) {
            log.info(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }

}
