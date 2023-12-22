package com.aurine.cloudx.open.biz.service.impl;

import com.aurine.cloudx.open.biz.service.OpenProjectInfoService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.ProjectInfoVo;
import com.aurine.cloudx.open.origin.entity.ProjectInfo;
import com.aurine.cloudx.open.origin.service.ProjectInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * open平台-项目信息实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class OpenProjectInfoServiceImpl implements OpenProjectInfoService {

    @Resource
    private ProjectInfoService projectInfoService;


    @Override
    public R<ProjectInfoVo> getById(String id) {
        ProjectInfo po = projectInfoService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        ProjectInfoVo vo = new ProjectInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<ProjectInfoVo> getByProjectUUID(String projectUUID) {
        ProjectInfo po = projectInfoService.getByProjectUUID(projectUUID);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        ProjectInfoVo vo = new ProjectInfoVo();
        BeanUtils.copyProperties(po, vo);

        return R.ok(vo);
    }

    @Override
    public R<Page<ProjectInfoVo>> page(Page page, ProjectInfoVo vo) {
        return R.ok(projectInfoService.page(page, vo));
    }

    @Override
    public R<List<ProjectInfoVo>> listCascadeByCloud() {
        return R.ok(projectInfoService.listCascadeByCloud());
    }

    @Override
    public R<List<ProjectInfoVo>> listCascadeByEdge() {
        return R.ok(projectInfoService.listCascadeByEdge());
    }

    @Override
    public R<List<ProjectInfoVo>> listCascadeByMaster() {
        return R.ok(projectInfoService.listCascadeByMaster());
    }

    @Override
    public R<List<ProjectInfoVo>> listCascadeBySlave() {
        return R.ok(projectInfoService.listCascadeBySlave());
    }
}
