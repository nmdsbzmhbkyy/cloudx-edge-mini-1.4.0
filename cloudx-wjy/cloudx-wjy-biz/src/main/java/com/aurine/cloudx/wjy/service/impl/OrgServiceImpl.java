package com.aurine.cloudx.wjy.service.impl;

import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.service.OrgService;
import com.aurine.cloudx.wjy.service.ProjectService;
import com.aurine.cloudx.wjy.service.WjyOrgService;
import com.aurine.cloudx.wjy.vo.WorkerVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrgServiceImpl implements OrgService {
    @Resource
    ProjectService projectService;
    @Resource
    WjyOrgService wjyOrgService;
    @Override
    public R addWorker(WorkerVo workerVo) {
        Project project = projectService.getByProjectId(workerVo.getProjectId());
        if(project == null){
            return R.failed("未找到项目信息");
        }
        List<WorkerVo> workerVos = new ArrayList<>();
        workerVos.add(workerVo);
        Boolean isSave = wjyOrgService.workerAdd(project,workerVos);
        if (!isSave){
            return R.failed("添加员工失败");
        }
        return R.ok();
    }

    @Override
    public R delWorker(Integer projectId, String phone) {
        Project project = projectService.getByProjectId(projectId);
        if(project == null){
            return R.failed("未找到项目信息");
        }
        Boolean isSave = wjyOrgService.workerDelete(project,phone);
        if (!isSave){
            return R.failed("删除员工失败");
        }
        return R.ok();
    }
}
