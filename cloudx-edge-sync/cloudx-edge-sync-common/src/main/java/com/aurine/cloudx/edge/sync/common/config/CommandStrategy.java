package com.aurine.cloudx.edge.sync.common.config;

import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author: wrm
 * @Date: 2022/03/03 8:38
 * @Package: com.aurine.cloudx.edge.sync.biz.config
 * @Version: 1.0
 * @Remarks:
 **/
public interface CommandStrategy extends InitializingBean {
    /**
     * 处理命令
     * @param taskInfoDto
     * @return
     */
    OpenRespVo doHandler(TaskInfoDto taskInfoDto);
}