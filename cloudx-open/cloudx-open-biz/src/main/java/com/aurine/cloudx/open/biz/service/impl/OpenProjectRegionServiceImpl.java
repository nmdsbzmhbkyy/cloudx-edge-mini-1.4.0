package com.aurine.cloudx.open.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.open.biz.service.OpenProjectRegionService;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.response.Result;
import com.aurine.cloudx.open.common.entity.vo.ProjectRegionVo;
import com.aurine.cloudx.open.origin.entity.ProjectBuildingInfo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceRegion;
import com.aurine.cloudx.open.origin.entity.ProjectFloorPic;
import com.aurine.cloudx.open.origin.service.ProjectBuildingInfoService;
import com.aurine.cloudx.open.origin.service.ProjectDeviceInfoService;
import com.aurine.cloudx.open.origin.service.ProjectDeviceRegionService;
import com.aurine.cloudx.open.origin.service.ProjectFloorPicService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * open平台-户型管理实现
 *
 * @author : Qiu
 * @date : 2021 12 28 9:55
 */

@Service
public class OpenProjectRegionServiceImpl implements OpenProjectRegionService {

    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;

    @Resource
    private ProjectFloorPicService projectFloorPicService;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;


    @Override
    public R<ProjectRegionVo> getById(String id) {
        ProjectDeviceRegion po = projectDeviceRegionService.getById(id);
        if (po == null) return Result.fail(null, CloudxOpenErrorEnum.EMPTY_RESULT);

        ProjectRegionVo resultVo = new ProjectRegionVo();
        BeanUtils.copyProperties(po, resultVo);

        return R.ok(resultVo);
    }

    @Override
    public R<Page<ProjectRegionVo>> page(Page page, ProjectRegionVo vo) {
        return R.ok(projectDeviceRegionService.page(page, vo));
    }

    @Override
    public R<ProjectRegionVo> save(ProjectRegionVo vo) {
//        ProjectDeviceRegion po = new ProjectDeviceRegion();
//        BeanUtils.copyProperties(vo, po);
//
//        ProjectDeviceRegion resultPo = projectDeviceRegionService.saveRegion(po);
//        if (resultPo == null) return Result.fail(vo, CloudxOpenErrorEnum.ARGUMENT_INVALID);
//
//        ProjectRegionVo resultVo = new ProjectRegionVo();
//        BeanUtils.copyProperties(resultPo, resultVo);
//
//        return R.ok(resultVo);

        return R.ok();
    }

    @Override
    public R<ProjectRegionVo> update(ProjectRegionVo vo) {
//        ProjectDeviceRegion po = new ProjectDeviceRegion();
//        BeanUtils.copyProperties(vo, po);
//
//        boolean result = projectDeviceRegionService.updateById(po);
//        if (!result) return Result.fail(vo, CloudxOpenErrorEnum.EMPTY_RESULT);
//
//        ProjectRegionVo resultVo = new ProjectRegionVo();
//        BeanUtils.copyProperties(po, resultVo);
//
//        return R.ok(resultVo);

        return R.ok();
    }

    @Override
    public R<Boolean> delete(String id) {
//        String childRegionIds = projectDeviceRegionService.getChildRegionIds(id);
//        if (StrUtil.isNotEmpty(childRegionIds)) {
//            String[] childRegionIdArr = childRegionIds.split(",");
//            List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new QueryWrapper<ProjectDeviceInfo>().lambda()
//                    .in(ProjectDeviceInfo::getDeviceRegionId, childRegionIdArr));
//            List<ProjectBuildingInfo> buildingList = projectBuildingInfoService.list(new QueryWrapper<ProjectBuildingInfo>().lambda()
//                    .in(ProjectBuildingInfo::getRegionId, childRegionIdArr));
//            if (deviceInfoList.size() > 0 || CollUtil.isNotEmpty(buildingList)) {
//                return Result.fail(false, CloudxOpenErrorEnum.SERVICE_ERROR.getCode(), "区域下已有楼栋或设备不可删除");
//            } else {
//                // 这里删除区域下所有的平面图
//                projectFloorPicService.remove(new QueryWrapper<ProjectFloorPic>().lambda().in(ProjectFloorPic::getRegionId, childRegionIdArr));
//                return R.ok(projectDeviceRegionService.remove(new QueryWrapper<ProjectDeviceRegion>().lambda().in(ProjectDeviceRegion::getRegionId, childRegionIdArr)));
//            }
//        }
//        return R.ok(true);

        return R.ok();
    }
}
