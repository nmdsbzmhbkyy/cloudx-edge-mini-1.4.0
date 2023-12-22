package com.aurine.cloudx.wjy.service;

import com.aurine.cloudx.wjy.vo.WorkerVo;
import com.pig4cloud.pigx.common.core.util.R;

public interface OrgService {
    R addWorker(WorkerVo workerVo);
    R delWorker(Integer projectId, String phone);
}
