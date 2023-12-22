package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.EdgeCascadeRequestSlave;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;


/**
 * <p>级联申请管理（从）服务</p>
 * @author : 王良俊
 * @date : 2021-12-17 09:37:40
 */
public interface EdgeCascadeRequestSlaveAspectService extends IService<EdgeCascadeRequestSlave> {

    R<EdgeCascadeRequestSlave> revokeRequestUpdate(EdgeCascadeRequestSlave edgeCascadeRequestSlave);

    R<EdgeCascadeRequestSlave> requestCascadeSaveOrUpdate(EdgeCascadeRequestSlave edgeCascadeRequestSlave);

    R<EdgeCascadeRequestSlave> requestUnbindUpdate(EdgeCascadeRequestSlave requestSlave);
}
