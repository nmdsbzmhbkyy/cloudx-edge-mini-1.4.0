package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import com.aurine.cloudx.estate.component.chain.AbstractHandleChain;
import com.aurine.cloudx.estate.component.chain.annotation.Chain;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/17
 * @Copyright:
 */
@Chain
@Component
@Slf4j
public class AurineEdgeObjectManageChain extends AbstractHandleChain<CallBackData> {
    @Override
    public AbstractHandleChain done() {
        //所有处理器遍历后，事件未被执行的处理方法
        String objName = handleEntity.getOnNotifyData().getObjManagerData().getObjName();
        String action = handleEntity.getOnNotifyData().getObjManagerData().getAction();
        log.error("[冠林边缘网关] 未定义的上报事件 objName :{}, action:{},{}", objName, action, handleEntity);

        return super.done();
    }
}
