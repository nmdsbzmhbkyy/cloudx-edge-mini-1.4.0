package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.constant.enums.IntoCloudStatusEnum;
import com.aurine.cloudx.estate.entity.EdgeCloudRequest;
import com.aurine.cloudx.estate.vo.EdgeCloudRequestVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * <p>边缘网关入云申请服务</p>
 * @author : 王良俊
 * @date : 2021-12-02 17:31:32
 */
public interface EdgeCloudRequestAspectService extends IService<EdgeCloudRequest> {

    R<EdgeCloudRequestVo> requestIntoCloudSaveOrUpdate(EdgeCloudRequest request, String sn);

    R<EdgeCloudRequest> cancelRequestUpdate(EdgeCloudRequest request);

    R<EdgeCloudRequest> requestUnbindUpdate(String requestId, IntoCloudStatusEnum unbinding);

    R<EdgeCloudRequest> revokeUnbindRequestUpdate(String requestId, IntoCloudStatusEnum intoCloud);
}
