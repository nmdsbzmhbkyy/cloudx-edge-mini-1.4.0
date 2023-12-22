package com.aurine.cloudx.edge.sync.biz.service.biz;

import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;

/**
 * @Author: wrm
 * @Date: 2022/01/20 9:03
 * @Package: com.aurine.cloudx.edge.sync.biz.service
 * @Version: 1.0
 * @Remarks:
 **/
public interface DealRequestService {

    /**
     * 处理业务
     * @param requestObj 请求taskInfo对象
     */
    void dealRequest(TaskInfoDto requestObj);

}
