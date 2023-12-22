package com.aurine.cloudx.edge.sync.biz.service.biz;

import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;

/**
 * @Author: wrm
 * @Date: 2022/01/20 9:08
 * @Package: com.aurine.cloudx.edge.sync.biz.service
 * @Version: 1.0
 * @Remarks:
 **/
public interface DealResponseService {
    /**
     * 转换消息，
     * 判断消息成功失败,失败保存异常记录，中断操作
     * 新增操作更新关系表，删除操作删除关系表
     * 修改操作判断数据库对应数据发送状态是否已发送，不是则终止业务(从端被覆盖以及多次操作校验)
     * 更新数据库taskInfo数据
     * 删除redis消息队列消息
     *
     * @param taskInfoDto
     */
    void dealResponse(TaskInfoDto taskInfoDto);
}
