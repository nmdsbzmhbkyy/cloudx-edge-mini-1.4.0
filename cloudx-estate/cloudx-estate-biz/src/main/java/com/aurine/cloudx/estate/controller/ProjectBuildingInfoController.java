

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.BuildingPublicFloorVo;
import com.aurine.cloudx.estate.vo.ProjectBuildingBatchVo;
import com.aurine.cloudx.estate.vo.ProjectBuildingInfoVo;
import com.aurine.cloudx.estate.vo.ProjectUnitFileVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 楼栋
 *
 * @author 王伟
 * @date 2020-05-07 16:52:22
 */
@RestController
@RequestMapping("/baseBuilding")
@Api(value = "baseBuilding", tags = "楼栋管理")
public class ProjectBuildingInfoController {
    @Resource
    private ProjectBuildingInfoService buildingInfoService;
    @Resource
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;
    @Resource
    private ProjectConfigService projectConfigService;

    /**
     * 分页查询
     *
     * @param page         分页对象
     * @param buildingName 楼栋名
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getBuildingInfoPage(Page page, String buildingName) {
        Page page1 = buildingInfoService.page(page, new QueryWrapper<ProjectBuildingInfo>().lambda()
                .like(ProjectBuildingInfo::getBuildingName, buildingName));
        for (ProjectBuildingInfo projectBuildingInfo : (List<ProjectBuildingInfo>) page1.getRecords()) {
            String frameNameById = projectFrameInfoService.getFrameNameById(projectBuildingInfo.getGroup4());
            if (StrUtil.isNotBlank(frameNameById)) {
                String[] split = frameNameById.split(",");
                StringBuilder framName = new StringBuilder();
                for (int i = split.length - 1; i >= 0; i--) {
                    framName.append(split[i] + "-");
                }
                projectBuildingInfo.setBuildingName(framName + projectBuildingInfo.getBuildingName());
                //projectBuildingInfo.setHasStiltFloor(1);

            }
        }
        return R.ok(page1);
    }

    /**
     * 获取项目下的楼栋列表
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    public R list() {
        return R.ok(buildingInfoService.list());
//        return R.ok(buildingInfoService.listBuildingWithGroup());
    }

    /**
     * 根据项目ID获取楼栋集合
     *
     * @return
     */
    @ApiOperation(value = "根据项目ID获取楼栋集合", notes = "根据项目ID获取楼栋集合")
    @GetMapping("/inner/list/{projectId}")
    @Inner(value = false)
    public R innerListByProjectId(@PathVariable Integer projectId) {
        ProjectContextHolder.setProjectId(projectId);
        return R.ok(buildingInfoService.list());
    }

    /**
     * 查询楼栋信息
     *
     * @return
     */
    @ApiOperation(value = "查询楼栋信息", notes = "查询楼栋信息")
    @GetMapping("/inner/info/{buildingId}")
    @Inner(value = false)
    public R innerBuildingInfo(@PathVariable("buildingId") String buildingId) {
        return R.ok(buildingInfoService.getById(buildingId));
    }


    /**
     * 获取项目下的楼栋列表(楼栋信息表包括了项目id.另外,更详细的说法是:获取不同项目下的基本楼栋名称列表)
     * 烤猫说：
     *     功能:
     *       根据楼栋信息表的楼栋ID结合递归查询,替换原表中的buildingName,即查寻所需的楼栋名称,
     *       如果实参name!=null,则被替换的匹配楼栋名称(buildingName)还需与name匹配.
     *       (楼栋名称的查询是多余的,因为查询出来的和原来的一样).
     *     有啥用:
     *       用于给住户登记页面的房屋地址的楼栋选择,以及给前端提供buildingId(和所谓的框架表下的entityId相等)
     *     缺点:
     *       返回的信息还有很多不需要的信息,实际需要的信息只有buildingName(如果有组团,则包括了组团信息在buildingName中)
     * @param name 要匹配的楼栋名称(为空则不匹配)
     * @return 所查出来的楼栋信息
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/listWithGroup")
    public R listWithGroup(@RequestParam(value = "name", required = false) String name) {
        // return R.ok(buildingInfoService.list());
        return R.ok(buildingInfoService.listBuildingWithGroup(name));
    }

    /**
     * 通过楼栋ID获取到楼栋所属区域的信息
     *
     * @return
     */
    @ApiOperation(value = "通过楼栋ID获取到楼栋所属区域的信息", notes = "通过楼栋ID获取到楼栋所属区域的信息")
    @GetMapping("/regionByBuildingId")
    public R regionByBuildingId(@RequestParam("buildingId") String buildingId) {
        ProjectBuildingInfo buildingInfo = buildingInfoService.getOne(new QueryWrapper<ProjectBuildingInfo>().lambda()
                .eq(ProjectBuildingInfo::getBuildingId, buildingId));
        if (buildingInfo != null) {
            if (StrUtil.isEmpty(buildingInfo.getRegionId())) {
                List<ProjectDeviceRegion> deviceRegionList = projectDeviceRegionService.list(new QueryWrapper<ProjectDeviceRegion>()
                        .lambda().eq(ProjectDeviceRegion::getRegionName, "公共区域"));
                if (CollUtil.isNotEmpty(deviceRegionList)) {
                    return R.ok(deviceRegionList.get(0).getRegionId());
                }
                throw new RuntimeException("本项目未配置公共区域，且楼栋未分配区域");
            }
            return R.ok(buildingInfo.getRegionId());
        }
        return R.ok();
    }


    /**
     * 通过id查询楼栋
     *
     * @param buildingid id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{buildingid}")
    public R getById(@PathVariable("buildingid") String buildingid) {
        List<ProjectBuildingInfo> buildingInfoList = buildingInfoService.list(new QueryWrapper<ProjectBuildingInfo>().lambda()
                .eq(ProjectBuildingInfo::getBuildingId, buildingid));
        if (CollUtil.isNotEmpty(buildingInfoList)) {
            ProjectBuildingInfo projectBuildingInfo = buildingInfoList.get(0);
            projectBuildingInfo.setBuildingCode(projectBuildingInfo.getFrameNo());
            ProjectBuildingInfoVo projectBuildingInfoVo = new ProjectBuildingInfoVo();
            BeanUtils.copyProperties(projectBuildingInfo, projectBuildingInfoVo);
            if (StrUtil.isNotBlank(projectBuildingInfo.getGroup4())) {
                String group5 = getFramePuid(projectBuildingInfoVo.getGroup4());
                String group6 = getFramePuid(group5);
                String group7 = getFramePuid(group6);
                projectBuildingInfoVo.setGroup5(group5);
                projectBuildingInfoVo.setGroup6(group6);
                projectBuildingInfoVo.setGroup7(group7);
                List<ProjectUnitFileVo> fileList = new ArrayList<>();
                ProjectUnitFileVo fileVo;
                // 前端回显图片列表
                if (StrUtil.isNotBlank(projectBuildingInfoVo.getPicUrl1())) {
                    fileVo = new ProjectUnitFileVo();
                    fileVo.setUrl(projectBuildingInfoVo.getPicUrl1());
                    fileList.add(fileVo);
                    if (StrUtil.isNotBlank(projectBuildingInfoVo.getPicUrl2())) {
                        fileVo = new ProjectUnitFileVo();
                        fileVo.setUrl(projectBuildingInfoVo.getPicUrl2());
                        fileList.add(fileVo);
                        if (StrUtil.isNotBlank(projectBuildingInfoVo.getPicUrl3())) {
                            fileVo = new ProjectUnitFileVo();
                            fileVo.setUrl(projectBuildingInfoVo.getPicUrl3());
                            fileList.add(fileVo);
                        }
                    }
                }
                projectBuildingInfoVo.setFileList(fileList);
            }
            //projectBuildingInfoVo.setHasStiltFloor(0);
            return R.ok(projectBuildingInfoVo);
        } else {
            return R.ok();
        }
    }

    /**
     * 查询当前项目所有组团列表
     *
     * @return R
     * @author:王良俊
     */
    @ApiOperation(value = "查询组团列表")
    @GetMapping("/frameList")
    public R getFrameList() {
        return R.ok(projectEntityLevelCfgService.getFrameList());
    }

    /**
     * 新增楼栋
     *
     * @param building 楼栋
     * @return R
     */
    @ApiOperation(value = "新增楼栋", notes = "新增楼栋")
    @SysLog("新增楼栋")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_buildinginfo_add')")
    public R save(@RequestBody ProjectBuildingInfoVo building) {
        return R.ok(buildingInfoService.saveBuildingAndUnit(building));
    }

    /**
     * 批量新增楼栋
     *
     * @param vo 楼栋批量添加VO
     * @return R
     */
    @ApiOperation(value = "批量新增楼栋", notes = "批量新增楼栋")
    @SysLog("批量新增楼栋")
    @PostMapping("/batch")
    @PreAuthorize("@pms.hasPermission('estate_buildinginfo_add')")
    public R saveBatch(@RequestBody ProjectBuildingBatchVo vo) {
        boolean result = buildingInfoService.saveBatch(vo);
        if (result) {
            return R.ok();
        } else {
            return R.failed("添加失败");
        }
    }

    /**
     * 修改楼栋
     *
     * @param building 楼栋
     * @return R
     */
    @ApiOperation(value = "修改楼栋", notes = "修改楼栋")
    @SysLog("修改楼栋")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_buildinginfo_edit')")
    public R updateById(@RequestBody ProjectBuildingInfoVo building) {
        ProjectBuildingInfoVo infoVo = buildingInfoService.getById(building.getBuildingId());
        //判断楼层数是否变化
        boolean floorChange =
                !infoVo.getFloorGround().equals(building.getFloorGround()) ||
                        !infoVo.getFloorUnderground().equals(building.getFloorUnderground()) ||
                        !infoVo.getHasStiltFloor().equals(building.getHasStiltFloor());
        if (floorChange) {
            int count = buildingInfoService.countHouseInBuilding(building.getBuildingId());
            if (count >= 1) {
                throw new RuntimeException("已存在房屋，无法修改楼层数量");
            } else {
                building.setPublicFloors("");
                building.setFloorNumber("");
                return R.ok(buildingInfoService.updateById(building),"楼层数发生变化，请至“公共楼层配置”重新设置楼层编号");
            }
        } else {
            return R.ok(buildingInfoService.updateById(building));
        }
    }

    /**
     * 通过id删除楼栋
     *
     * @param buildingid id
     * @return R
     */
    @ApiOperation(value = "通过id删除楼栋", notes = "通过id删除楼栋")
    @SysLog("删除楼栋")
    @DeleteMapping("/{buildingid}")
    @PreAuthorize("@pms.hasPermission('estate_buildinginfo_del')")
    public R removeById(@PathVariable String buildingid) {
//        List<ProjectFrameInfo> list = projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().lambda()
//                .eq(ProjectFrameInfo::getPuid, buildingid));
//        for (ProjectFrameInfo p:
//             list) {
//            int num = projectHouseInfoService.count(new QueryWrapper<ProjectHouseInfo>().lambda()
//                    .eq(ProjectHouseInfo::getBuildingUnit, p.getEntityId()));
//            if (num > 0){
//                return R.failed("楼栋已有房屋，禁止删除");
//            }
//        }
//
//        projectFrameInfoService.remove(new QueryWrapper<ProjectFrameInfo>().lambda()
//                .eq(ProjectFrameInfo::getEntityId,buildingid));
//        return R.ok(buildingInfoService.removeById(buildingid));

        return buildingInfoService.deleteBuilding(buildingid);
    }

    /**
     * 统计楼栋总数
     *
     * @return
     */
    @ApiOperation(value = "统计楼栋总数", notes = "统计楼栋总数")
    @GetMapping("/countBuilding")
    public R countBuilding() {
        return R.ok(buildingInfoService.countBuilding());
    }

    private String getFramePuid(String frameId) {
        if (StrUtil.isBlank(frameId)) {
            return "";
        }
        List<ProjectFrameInfo> list = projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getEntityId, frameId));
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0).getPuid();
        } else {
            return "";
        }
    }

    /**
     * 根据楼栋id获取其下的房屋名称
     *
     * @return
     */
    @ApiOperation(value = "根据楼栋id获取其下的房屋名称", notes = "根据楼栋id获取其下的房屋名称")
    @GetMapping("/list/house-name/{buildingId}")
    public R listHouseNameByBuildingId(@PathVariable("buildingId") String buildingId) {
        return R.ok(projectFrameInfoService.listHouseNameByBuildingId(buildingId));
    }

    /**
     * 根据项目id获取楼层号长度
     *
     * @return
     */
    @ApiOperation(value = "根据项目id获取楼层号长度", notes = "根据项目id获取楼层号长度")
    @GetMapping("/floorNoLen/{projectId}")
    public R floorNoLenByProjectId(@PathVariable("projectId") String projectId) {
        Integer floorNoLen = projectConfigService.getOne(new LambdaQueryWrapper<ProjectConfig>().eq(ProjectConfig::getProjectId, projectId)).getFloorNoLen();
        int maxFloorNoLen = 1;
        if (floorNoLen == null || floorNoLen == 0) {
            maxFloorNoLen = 99;
        } else {
            for (int i = 1; i <= floorNoLen; i++) {
                maxFloorNoLen *= 10;
            }
        }
        maxFloorNoLen -= 1;
        return R.ok(maxFloorNoLen);
    }

    /**
     * 根据项目id单元每层房数相加最大值
     *
     * @return
     */
    @ApiOperation(value = "根据项目id单元每层房数相加最大值", notes = "根据项目id单元每层房数相加最大值")
    @GetMapping("/maxNumberOfUnits/{projectId}")
    public R maxNumberOfUnits(@PathVariable("projectId") String projectId) {
        Integer floorNoLen = projectConfigService.getOne(new LambdaQueryWrapper<ProjectConfig>()
                .eq(ProjectConfig::getProjectId, projectId)).getFloorNoLen();
        Integer codeRule = projectEntityLevelCfgService.getOne(new LambdaQueryWrapper<ProjectEntityLevelCfg>()
                .eq(ProjectEntityLevelCfg::getProjectId, projectId)
                .eq(ProjectEntityLevelCfg::getLevel, 1)).getCodeRule();
        int maxNumber = 1;
        if (floorNoLen == null) {
            return R.ok(99);
        } else {
            for (int i = 1; i <= codeRule - floorNoLen; i++) {
                maxNumber *= 10;
            }
        }
        maxNumber -= 1;
        return R.ok(maxNumber);
    }

    /**
     * 批量修改楼栋公共楼层
     *
     * @param list 楼栋公共楼层批量添加VO
     * @return R
     */
    @ApiOperation(value = "批量新增楼栋", notes = "批量新增楼栋")
    @SysLog("批量修改楼栋公共楼层")
    @PutMapping("/public-floor/batch")
    @PreAuthorize("@pms.hasPermission('estate_buildinginfo_edit')")
    public R savePublicFloorsBatch(@RequestBody BuildingPublicFloorVo list) {
        List<ProjectBuildingInfo> lstBuildingInfo = new ArrayList<>();
        for (ProjectBuildingInfoVo vo : list.getList()) {
            ProjectBuildingInfo projectBuildingInfo = new ProjectBuildingInfo();
            BeanUtils.copyProperties(vo, projectBuildingInfo);
            projectBuildingInfo.setPublicFloors(StringUtils.removeEnd(projectBuildingInfo.getPublicFloors(), ","));
            lstBuildingInfo.add(projectBuildingInfo);
        }
        boolean result = buildingInfoService.updateBatchById(lstBuildingInfo);
        if (result) {
            return R.ok();
        } else {
            return R.failed("修改失败");
        }
    }

    /**
     * 修改楼栋公共楼层
     *
     * @param buildingInfoVo 楼栋信息VO
     * @return R
     */
    @ApiOperation(value = "修改楼栋公共楼层", notes = "修改楼栋公共楼层")
    @SysLog("修改楼栋公共楼层")
    @PutMapping("/public-floor")
    @PreAuthorize("@pms.hasPermission('estate_buildinginfo_edit')")
    public R savePublicFloorsById(@RequestBody ProjectBuildingInfoVo buildingInfoVo) {
        ProjectBuildingInfo buildingInfo = new ProjectBuildingInfo();
        BeanUtils.copyProperties(buildingInfoVo, buildingInfo);
        boolean result = buildingInfoService.updateById(buildingInfo);
        if (result) {
            return R.ok();
        } else {
            return R.failed("修改失败");
        }
    }


}
