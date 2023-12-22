package com.aurine.cloudx.edge.sync.biz.service.handler;

import com.aurine.cloudx.edge.sync.common.componments.chain.AbstractHandleChain;
import com.aurine.cloudx.edge.sync.common.componments.chain.annotation.Chain;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021/09/17
 * @Copyright: BASE64码转图片并存在MinIo中
 */
@Chain
@Component
@Slf4j
public class Base64ToImageChain extends AbstractHandleChain<TaskInfoDto> {
    @Override
    public AbstractHandleChain done() {
        //所有处理器遍历后，事件未被执行的处理方法
        return super.done();
    }
}
