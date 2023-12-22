package com.aurine.cloudx.edge.sync.biz.service.handler;

import com.aurine.cloudx.edge.sync.common.componments.chain.AbstractHandleChain;
import com.aurine.cloudx.edge.sync.common.componments.chain.annotation.Chain;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: wrm
 * @Date: 2022/03/02 10:34
 * @Package: com.aurine.cloudx.edge.sync.handler
 * @Version: 1.0
 * @Remarks: 图片转BASE64码
 **/
@Chain
@Component
@Slf4j
public class ImageToBase64Chain extends AbstractHandleChain<TaskInfoDto> {
    @Override
    public AbstractHandleChain done() {
        //所有处理器遍历后，事件未被执行的处理方法
        return super.done();
    }
}