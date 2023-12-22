package com.aurine.cloudx.wjy.canal.handle;

import com.alibaba.otter.canal.protocol.CanalEntry;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ： huangjj
 * @date ： 2021/5/12
 * @description： canal事件处理
 */
@Component
public class DealCanalHandleChain extends AbstractCanalHandleChain{

    @Resource
    StaffCanalHandle staffCanalHandle;
    @Resource
    CustomerCanalHandle customerCanalHandle;
    @Resource
    RelCanalHandle relCanalHandle;

    @Override
    public AbstractCanalHandleChain doChain(CanalEntry.Entry canalEntry) {
        this.canalEntry = canalEntry;
        return this
                .doHandle(staffCanalHandle)
                .doHandle(customerCanalHandle)
                .doHandle(relCanalHandle)
                .done();
    }

    @Override
    public AbstractCanalHandleChain doHandle(AbstractCanalHandler handler) {
        if (nextFlag) {
            try {
                this.nextFlag = handler.handle(this.canalEntry);
            }catch (Exception e){
                return this;
            }
        }
        return this;
    }
}