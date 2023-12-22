package com.aurine.cloudx.wjy.service;

import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.pojo.R;
import com.aurine.cloudx.wjy.vo.Organization;
import com.aurine.cloudx.wjy.vo.Worker;
import com.aurine.cloudx.wjy.vo.WorkerVo;

import java.util.List;

public interface WjyOrgService {
    boolean orgSyncSave(Project project, List<Organization> orgs);
    boolean workerAdd(Project project, List<WorkerVo> workerVos);
    boolean workerDelete(Project project, String phones);
}
