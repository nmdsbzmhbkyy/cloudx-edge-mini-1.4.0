

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.aurine.cloudx.estate.entity.ProjectFrameInfo;
import com.aurine.cloudx.estate.service.ProjectFrameInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * <p>楼栋框架</p>
 *
 * @ClassName: BuildingFrameController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/7 14:17
 * @Copyright:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/baseBuildingFrame")
@Api(value = "baseBuildingFrame", tags = "框架管理")
public class ProjectFrameInfoController {

    private final ProjectFrameInfoService projectFrameInfoService;

    /**
     * 分页查询
     *
     * @param page             分页对象
     * @param projectFrameInfo 框架
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getFrameInfoPage(Page page, ProjectFrameInfo projectFrameInfo) {

        return R.ok(projectFrameInfoService.page(page, Wrappers.query(projectFrameInfo)));
    }


    /**
     * 通过id查询框架
     *
     * @param entityId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{entityId}")
    public R getById(@PathVariable("entityId") String entityId) {
        return R.ok(projectFrameInfoService.getById(entityId));
    }

    /**
     * 通过level和projectId查询所有组团
     *
     * @param level
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/list/{level}")
    public R getList(@PathVariable("level") Integer level) {
        return R.ok(projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getLevel, level)));
    }
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/list/{level}/{code}")
    public R getByCode(@PathVariable("level") Integer level,@PathVariable("code")String code) {
        return R.ok(projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getLevel, level)
        .eq(ProjectFrameInfo::getFrameNo,code)));
    }
    /**
     * 通过level和projectId查询框架的数量
     *
     * @param level
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/countByLevel/{level}")
    public R countByLevel(@PathVariable("level") Integer level) {
        return R.ok(projectFrameInfoService.count(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getLevel, level)));
    }

    /**
     * 通过puid查询puid相等的楼宇信息(能找单元号,如01单元,02单元,03单元;还能查找房间号,如0101,0102,0201,0202等)
     *
     * @param puid
     * @return R
     */
    @ApiOperation(value = "通过puid查询框架", notes = "通过puid查询框架")
    @GetMapping("/listPuid/{puid}")
    public R getListByPuid(@PathVariable("puid") String puid) {
        return R.ok(projectFrameInfoService.listByPuid(puid));
    }

    /**
     * 新增框架
     *
     * @param projectFrameInfo 框架
     * @return R
     * @author: 王良俊
     */
    @ApiOperation(value = "新增框架", notes = "新增框架")
    @SysLog("新增框架")
    @PostMapping
    public R save(@RequestBody ProjectFrameInfo projectFrameInfo) {
        projectFrameInfo.setIsUnit("0");
        projectFrameInfo.setIsBuilding("0");
        projectFrameInfo.setIsHouse("0");
        if (projectFrameInfoService.checkExists(projectFrameInfo)) {
            return R.failed("组团名重复");
        }
        if (projectFrameInfoService.checkExistsCode(projectFrameInfo)) {
            return R.failed("组团编号重复");
        }
        String uid = UUID.randomUUID().toString().replaceAll("-", "");
        projectFrameInfo.setEntityId(uid);
        projectFrameInfo.setEntityCode(projectFrameInfo.getFrameNo());
        Boolean bool = projectFrameInfoService.saveFrameInfo(projectFrameInfo);
        if(!bool){
            return R.failed("添加组团失败");
        }

        return R.ok(projectFrameInfo);
    }

    /**
     * 修改框架
     *
     * @param projectFrameInfo 框架
     * @return R
     */
    @ApiOperation(value = "修改框架", notes = "修改框架")
    @SysLog("修改框架")
    @PutMapping
    public R updateById(@RequestBody ProjectFrameInfo projectFrameInfo) {

        int count = projectFrameInfoService.count(new QueryWrapper<ProjectFrameInfo>().lambda()
                .eq(ProjectFrameInfo::getEntityName, projectFrameInfo.getEntityName())
                .eq(StringUtils.isNotEmpty(projectFrameInfo.getPuid()), ProjectFrameInfo::getPuid, projectFrameInfo.getPuid())
                .ne(ProjectFrameInfo::getEntityId, projectFrameInfo.getEntityId()));

        int count1 = projectFrameInfoService.count(new QueryWrapper<ProjectFrameInfo>().lambda()
                .eq(ProjectFrameInfo::getEntityCode, projectFrameInfo.getEntityCode())
                .eq(StringUtils.isNotEmpty(projectFrameInfo.getPuid()), ProjectFrameInfo::getPuid, projectFrameInfo.getPuid())
                .ne(ProjectFrameInfo::getEntityId, projectFrameInfo.getEntityId()));

        if (count >= 1) {
            return R.failed("组团名重复");
        }
        if (count1 >= 1) {
            return R.failed("组团编号重复");
        }
        Boolean flag = projectFrameInfoService.updateFrameNoByCode(projectFrameInfo);
        return R.ok(flag);
    }

    /**
     * 通过id删除框架
     *
     * @param entityid id
     * @return R
     */
    @ApiOperation(value = "通过id删除框架", notes = "通过id删除框架")
    @SysLog("通过id删除框架")
    @DeleteMapping("/{entityid}")
    public R removeById(@PathVariable String entityid) {

        List<ProjectFrameInfo> list = projectFrameInfoService.list(new QueryWrapper<ProjectFrameInfo>().lambda().eq(ProjectFrameInfo::getPuid, entityid).eq(ProjectFrameInfo::getLevel, "3"));
        if (list.size() != 0) {
            return R.failed("该组团下有楼栋，无法删除");
        }

        if (projectFrameInfoService.checkHaveChild(entityid)) {
            return R.failed("该组团下存在子节点，无法删除");
        }
        return R.ok(projectFrameInfoService.removeById(entityid));
    }

    /**
     * 通过当前单元的id查出本单元的其他单元的列表
     *
     * @param entityid id
     * @return R
     * @author: 王良俊
     */
    @ApiOperation(value = "通过当前单元的id查出本单元的其他单元的列表，并返回当前单元的父id", notes = "通过当前单元的id查出和本单元相同父uid的单元列表")
    @GetMapping("/getCurrentListAndPuid/{entityid}")
    public R getCurrentList(@PathVariable String entityid) {
        Map<String, Object> currentListAndPuid = projectFrameInfoService.getCurrentListAndPuid(entityid);
        return R.ok(currentListAndPuid);
    }

    /**
     * 获取子系统树
     *
     * @return
     */
    @ApiOperation("获取框架树-LV4-7")
    @GetMapping("/tree")
    public R findTree() {
        return R.ok(projectFrameInfoService.findTree(""));
    }

    /**
     * 获取楼栋-房屋树-LV3-1
     *
     * @return
     */
    @ApiOperation("获取楼栋-房屋树-LV3-1")
    @GetMapping("/buildingTree")
    public R findBuildingTree() {
        return R.ok(projectFrameInfoService.findBuildingTree());
    }

    /**
     * 获取子系统树，带根节点
     *
     * @return
     */
    @ApiOperation("获取框架树-LV4-7,带有社区根节点")
    @GetMapping("/findTreeWithRoot")
    public R findTreeWithRoot() {
        return R.ok(projectFrameInfoService.findTree("小区"));
    }

    /**
     * 根据节点id，获取其下各层级的节点个数以及住户数量
     *
     * @return
     */
    @ApiOperation("根据节点id，获取其下各层级的节点个数以及住户数量")
    @GetMapping("/frameCount")
    public R listFrameCountAndPersonCount(String entityId) {
        return R.ok(projectFrameInfoService.listFrameCountAndPersonCount(entityId));
    }

    @ApiOperation("根据组团id获取其所有上级组团名")
    @GetMapping("/getFrameNameById/{entityId}")
    public R getFrameNameById(@PathVariable("entityId") String entityId) {
        if (StrUtil.isBlank(entityId)) {
            return R.ok("");
        }
        return R.ok(projectFrameInfoService.getFrameNameById(entityId));
    }

    /**
     * <p>
     * 判断房屋地址是否正确
     * </p>
     *
     * @param address    房屋地址如：1区-1栋-01单元-1010
     * @throws ExcelAnalysisException Excel异常对象
     * @author: 王良俊
     */
    @Inner(value = false)
    @ApiOperation("判断房屋地址是否正确")
    @GetMapping("/excel/checkHouseIsCorrect/{address}/{isGroup}")
    public R checkHouseIsCorrect(@PathVariable("address") String address, @PathVariable("isGroup") boolean isGroup) {
        return projectFrameInfoService.checkHouseIsCorrect(address, isGroup);
    }

 /*   @ApiOperation("根据组团id获取其所有上级id数组")
    @GetMapping("/getFrameSuperiorIdArrById/{entityId}")
    public R getFrameSuperiorIdArrById(@PathVariable("entityId") String entityId){
        if (StrUtil.isBlank(entityId)){
            return R.ok("");
        }
        return R.ok(projectFrameInfoService.getFrameNameById(entityId));
    }*/

}
